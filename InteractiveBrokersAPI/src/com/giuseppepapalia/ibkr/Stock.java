package com.giuseppepapalia.ibkr;

import com.ib.client.Contract;

public class Stock {

	private final Quote quote;
	private final SmartChart liveChart;
	private final Contract contract;

	public Stock(Contract contract, Quote quote, SmartChart liveChart) {
		this.contract = contract;
		this.quote = quote;
		this.liveChart = liveChart;
	}

	public Quote getQuote() {
		return quote;
	}

	public SmartChart getLiveChart() {
		return liveChart;
	}

	public Contract getContract() {
		return contract;
	}

}
