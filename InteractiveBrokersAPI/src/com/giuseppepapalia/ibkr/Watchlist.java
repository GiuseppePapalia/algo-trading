package com.giuseppepapalia.ibkr;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ib.client.Contract;

public class Watchlist implements Runnable {

	private Map<Integer, Contract> watchlist;
	private Map<Contract, List<SmartBar>> history;

	public Watchlist() {
		watchlist = new HashMap<Integer, Contract>();
		history = new HashMap<Contract, List<SmartBar>>();
	}

	public void watchStock(int reqId, Contract contract) {
		watchlist.put(reqId, contract);
		history.put(contract, new ArrayList<SmartBar>());
	}

	public void update(int reqId, long time, double open, double high, double low, double close, long volume, double wap, int count) {
		if (watchlist.containsKey(reqId)) {
			history.get(watchlist.get(reqId)).add(new SmartBar(new Date(time), open, close, high, low, volume, wap));
		}
	}

	@Override
	public void run() {
		// do quant analysis

	}

}
