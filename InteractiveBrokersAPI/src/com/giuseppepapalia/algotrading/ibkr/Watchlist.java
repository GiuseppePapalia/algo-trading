package com.giuseppepapalia.algotrading.ibkr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.giuseppepapalia.algotrading.ibkr.constants.GFormatter;
import com.ib.client.Contract;

public class Watchlist implements Runnable {

	private Map<Integer, Contract> realTimeBarIDMap;
	private Map<Integer, Contract> tickByTickIDMap;
	private Map<Contract, Stock> watchlist;
	private Portfolio portfolio;
	private InteractiveBrokersClient client;

	public Watchlist(Portfolio portfolio, InteractiveBrokersClient client) {
		realTimeBarIDMap = new HashMap<Integer, Contract>();
		tickByTickIDMap = new HashMap<Integer, Contract>();
		watchlist = new HashMap<Contract, Stock>();
		this.portfolio = portfolio;
		this.client = client;
	}

	public int stopWatching(Contract contract) {
		for (Integer i : realTimeBarIDMap.keySet()) {
			if (contract.equals(realTimeBarIDMap.get(i))) {
				realTimeBarIDMap.remove(i);
				watchlist.remove(contract);
				return i;
			}
		}
		return -1;
	}

	public List<Contract> getList() {
		return new ArrayList<Contract>(watchlist.keySet());
	}

	public void watchStock(int reqId, Contract contract) {
		realTimeBarIDMap.put(reqId, contract);
		tickByTickIDMap.put(reqId + 1, contract);
		watchlist.put(contract, new Stock(contract, new Quote(), new Chart()));
	}

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

	public Map<Position, Quote> getQuoteMap() {
		Map<Position, Quote> quoteMap = new HashMap<Position, Quote>();
		for (Position position : portfolio.getPositions()) {
			Quote quote = watchlist.get(position.getContract()).getQuote();
			quoteMap.put(position, quote);
		}

		return quoteMap;
	}

	public void updateLiveChart(int reqId, long time, double open, double high, double low, double close, long volume, double wap, int count) {
		watchlist.get(realTimeBarIDMap.get(reqId)).getLiveChart().addBar(new BarData(GFormatter.parseLong(time), open, close, high, low, volume, wap));
		buy();
	}

	public Portfolio getPortfolio() {
		return portfolio;
	}

	private void buy() {
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
				client.placeLongBracketOrder(contract, 1, stock.getQuote().getAsk(), stock.getQuote().getAsk() * 1.03, stock.getQuote().getAskSize() * 0.99);
			}
		}
	}

	@Override
	public void run() {
		while (true) {

		}

	}

}
