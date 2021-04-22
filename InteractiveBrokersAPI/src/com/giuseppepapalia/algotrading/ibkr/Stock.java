package com.giuseppepapalia.algotrading.ibkr;

import com.ib.client.Contract;

public class Stock {

	private final Quote quote;
	private final Chart liveChart;
	private final Contract contract;

	public Stock(Contract contract, Quote quote, Chart liveChart) {
		this.contract = contract;
		this.quote = quote;
		this.liveChart = liveChart;
	}

	public Quote getQuote() {
		return quote;
	}

	public Chart getLiveChart() {
		return liveChart;
	}

	public Contract getContract() {
		return contract;
	}

}
