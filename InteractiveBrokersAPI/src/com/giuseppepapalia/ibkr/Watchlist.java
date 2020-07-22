package com.giuseppepapalia.ibkr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.giuseppepapalia.ibkr.constants.GFormatter;
import com.ib.client.Contract;

public class Watchlist implements Runnable {

	private Map<Integer, Contract> idMap;
	private Map<Contract, Stock> watchlist;

	public Watchlist() {
		idMap = new HashMap<Integer, Contract>();
		watchlist = new HashMap<Contract, Stock>();
	}

	public void watchStock(int reqId, Contract contract) {
		idMap.put(reqId, contract);
		watchlist.put(contract, new Stock(contract, new Quote(), new Chart(new ArrayList<DetailedBar>())));
	}

	public void updateQuote(int reqId, long time, double bidPrice, double askPrice, int bidSize, int askSize) {
		Quote quote = watchlist.get(idMap.get(reqId)).getQuote();
		quote.setAsk(askPrice);
		quote.setBid(bidPrice);
		quote.setBidSize(bidSize);
		quote.setAskSize(askSize);
	}

	public void updateLiveChart(int reqId, long time, double open, double high, double low, double close, long volume, double wap, int count) {
		watchlist.get(idMap.get(reqId)).getLiveChart().addBar(new DetailedBar(GFormatter.parseLong(time), open, close, high, low, volume, wap));
	}

	@Override
	public void run() {
		while (true) {
		}

	}

}
