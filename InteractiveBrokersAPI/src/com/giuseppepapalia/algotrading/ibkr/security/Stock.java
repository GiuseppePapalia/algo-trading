package com.giuseppepapalia.algotrading.ibkr.security;

import com.giuseppepapalia.algotrading.ibkr.Chart;
import com.giuseppepapalia.algotrading.ibkr.Quote;
import com.giuseppepapalia.algotrading.ibkr.Security;
import com.ib.client.Contract;

public class Stock extends Security {

	private final Chart liveChart;

	public Stock(Contract contract, Quote quote, Chart liveChart) {
		super(contract, quote);
		this.liveChart = liveChart;
	}

	public Chart getChart() {
		return liveChart;
	}
}
