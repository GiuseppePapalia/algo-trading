package com.giuseppepapalia.algotrading.ibkr;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.giuseppepapalia.algotrading.ibkr.constants.GFormatter;
import com.giuseppepapalia.algotrading.ibkr.constants.OptionType;
import com.giuseppepapalia.algotrading.ibkr.orderflow.LongBracketOrderFlow;
import com.giuseppepapalia.algotrading.ibkr.security.Option;
import com.ib.client.Contract;

public class InteractiveBrokersCore implements InteractiveBrokersCoreInterface {

	private Map<Integer, Contract> realTimeBarIDMap;
	private Map<Integer, Contract> tickByTickIDMap;
	private Map<Contract, Stock> watchlist;
	private Map<Stock, List<Option>> optionChainMap;
	private Portfolio portfolio;
	private InteractiveBrokersClient client;

	public InteractiveBrokersCore(boolean liveTrading, Portfolio portfolio) {
		this.client = new InteractiveBrokersClient(liveTrading, this);
		this.portfolio = portfolio;
		realTimeBarIDMap = new HashMap<Integer, Contract>();
		tickByTickIDMap = new HashMap<Integer, Contract>();
		watchlist = new HashMap<Contract, Stock>();
		optionChainMap = new HashMap<Stock, List<Option>>();
		init();
	}

	private void init() {
		client.init(portfolio.getAccID());
	}

	public void stopWatching(Contract contract) {
		for (Integer i : realTimeBarIDMap.keySet()) {
			if (contract.equals(realTimeBarIDMap.get(i))) {
				realTimeBarIDMap.remove(i);
				Stock stock = watchlist.get(contract);
				watchlist.remove(contract);
				optionChainMap.remove(stock);
				client.stopWatchingStock(i);
			}
		}
	}

	public List<Contract> getList() {
		return new ArrayList<Contract>(watchlist.keySet());
	}

	public void watchStock(Contract contract) {
		int id = client.watchStock(contract);
		realTimeBarIDMap.put(id, contract);
		tickByTickIDMap.put(id + 1, contract);
		Stock stock = new Stock(contract, new Quote(), new Chart());
		watchlist.put(contract, stock);
		optionChainMap.put(stock, new ArrayList<Option>());
	}

	@Override
	public void updateQuote(int reqId, long time, double bidPrice, double askPrice, int bidSize, int askSize) {
		if (watchlist.get(tickByTickIDMap.get(reqId)) == null) {
			return;
		}
		Quote quote = watchlist.get(tickByTickIDMap.get(reqId)).getQuote();

		quote.setAsk(askPrice);
		quote.setBid(bidPrice);
		quote.setBidSize(bidSize);
		quote.setAskSize(askSize);
	}

	private Contract getStockContract(String symbol) {
		for (Contract c : watchlist.keySet()) {
			if (c.symbol().equals(symbol)) {
				return c;
			}
		}
		return null;
	}

	public Option getShortTermOption(Contract contract, OptionType optionType) {
		Stock stock = watchlist.get(getStockContract(contract.symbol()));
		OptionFetcher fetcher = new OptionFetcher(stock, optionChainMap.get(stock));
		return fetcher.getShortTermOption(optionType);
	}

	public Map<Position, Quote> getQuoteMap() {
		Map<Position, Quote> quoteMap = new HashMap<Position, Quote>();
		for (Position position : portfolio.getPositions()) {
			Quote quote = watchlist.get(position.getContract()).getQuote();
			quoteMap.put(position, quote);
		}

		return quoteMap;
	}

	private void checkBuyConditions() {
		for (Contract contract : watchlist.keySet()) {
			Stock stock = watchlist.get(contract);
			boolean alreadyOwned = false;
			for (Position position : portfolio.getPositions()) {
				if (position.getContract().equals(contract)) {
					alreadyOwned = true;
				}
			}
			Double vwapDeriv = stock.getLiveChart().getMostRecentData().getRight().getVWAPDeriv();
			if (!alreadyOwned && vwapDeriv != null && vwapDeriv > 0.0001) {
				LongBracketOrderFlow orderFlow = new LongBracketOrderFlow(client.getID(), 1, stock.getQuote().getAsk(), stock.getQuote().getAsk() * 1.03, stock.getQuote().getAskSize() * 0.99);
				client.placeOrder(contract, orderFlow);
			}
		}
	}

	@Override
	public void updateLiveChart(int reqId, long time, double open, double high, double low, double close, long volume, double wap, int count) {
		watchlist.get(realTimeBarIDMap.get(reqId)).getLiveChart().addBar(new BarData(GFormatter.parseLong(time), open, close, high, low, volume, wap));
		checkBuyConditions();
	}

	@Override
	public void updatePosition(Contract contract, double pos, double avgCost) {
		Position position = portfolio.updatePosition(contract, pos, avgCost);
		if (position != null && !watchlist.containsKey(contract)) {
			watchStock(contract);
		}
	}

	@Override
	public void updatePortfolioPnL(double unrealizedPnL, double realizedPnL) {
		portfolio.setUnrealizedPnL(unrealizedPnL);
		portfolio.setRealizedPnL(realizedPnL);
	}

	@Override
	public void updateCashBalance(double cashBalance) {
		portfolio.setCashBalance(cashBalance);
	}

	@Override
	public void updateOptionChain(Contract option) {
		Contract stock = getStockContract(option.symbol());

		try {
			optionChainMap.get(watchlist.get(stock)).add(new Option(option, new Quote(), GFormatter.IBKR_DATE.parse(option.lastTradeDateOrContractMonth()), option.strike(), OptionType.getOptionType(option)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
