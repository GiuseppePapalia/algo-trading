package com.giuseppepapalia.algotrading.ibkr;

import java.util.HashMap;
import java.util.Map;

import com.ib.client.Contract;

public class HistoricalData {

	protected boolean doneQuery;
	private Map<Integer, Contract> idMap;
	private Map<Contract, Chart> historicalData;

	public HistoricalData() {
		idMap = new HashMap<Integer, Contract>();
		historicalData = new HashMap<Contract, Chart>();
	}

	public void createChart(int reqId, Contract c) {
		idMap.put(reqId, c);
		historicalData.put(c, new Chart());
	}

	public Chart getChart(int reqId) {
		return historicalData.get(idMap.get(reqId));
	}

}
