package com.giuseppepapalia.ibkr;

import com.giuseppepapalia.ibkr.constants.Currency;
import com.ib.client.Contract;

public class InteractiveBrokersFactory {

	public static Contract createStock(String symbol) {
		Contract contract = new Contract();
		contract.symbol(symbol);
		contract.secType("STK");
		contract.currency(Currency.USD.toString());
		contract.exchange("SMART");
		return contract;
	}

}
