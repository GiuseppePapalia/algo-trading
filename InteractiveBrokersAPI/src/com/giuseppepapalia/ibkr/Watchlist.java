package com.giuseppepapalia.ibkr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.giuseppepapalia.ibkr.constants.GFormatter;
import com.ib.client.Contract;

public class Watchlist implements Runnable {

	private Map<Integer, Contract> idMap;
	private Map<Contract, Stock> watchlist;
	private Map<Contract, List<Bar>> history;

	public Watchlist() {
		idMap = new HashMap<Integer, Contract>();
		history = new HashMap<Contract, List<Bar>>();
	}

	public void watchStock(int reqId, Contract contract) {
		idMap.put(reqId, contract);
		history.put(contract, new ArrayList<Bar>());
	}

	public void updateQuote(int reqId, long time, double bidPrice, double askPrice, int bidSize, int askSize) {
		Quote quote = watchlist.get(idMap.get(reqId)).getQuote();
		quote.setAsk(askPrice);
		quote.setBid(bidPrice);
		quote.setBidSize(bidSize);
		quote.setAskSize(askSize);
	}

	public void updateLiveChart(int reqId, long time, double open, double high, double low, double close, long volume, double wap, int count) {
		watchlist.get(idMap.get(reqId)).getLiveChart().addBar(new Bar(GFormatter.parseLong(time), open, close, high, low, volume, wap));
	}

	@Override
	public void run() {
		while (true) {
		}

	}

}
