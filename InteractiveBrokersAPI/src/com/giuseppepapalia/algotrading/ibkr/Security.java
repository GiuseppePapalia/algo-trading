package com.giuseppepapalia.algotrading.ibkr;

import com.ib.client.Contract;

public abstract class Security {

	protected final Quote quote;
	protected final Contract contract;

	public Security(Contract contract, Quote quote) {
		this.contract = contract;
		this.quote = quote;
	}

	public Quote getQuote() {
		return quote;
	}

	public Contract getContract() {
		return contract;
	}

}
