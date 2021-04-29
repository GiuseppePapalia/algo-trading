package com.giuseppepapalia.algotrading.ibkr;

import com.giuseppepapalia.algotrading.ibkr.constants.Currency;
import com.ib.client.Contract;
import com.ib.client.Types.Right;

public class InteractiveBrokersFactory {

	public static Contract createStock(String symbol) {
		Contract contract = new Contract();
		contract.symbol(symbol);
		contract.secType("STK");
		contract.currency(Currency.USD.toString());
		contract.primaryExch("ISLAND");
		return contract;
	}

	public static Contract createMockOption(String symbol) {
		Contract contract = new Contract();
		contract.symbol(symbol);
		contract.secType("OPT");
		contract.currency(Currency.USD.toString());
		contract.primaryExch("ISLAND");
		contract.exchange("ISLAND");
		return contract;
	}

	public static Contract createOption(String symbol, double strike, Right right, int expDay, int expMonth, int expYear) {
		Contract contract = new Contract();
		contract.symbol(symbol);
		contract.secType("OPT");
		contract.currency(Currency.USD.toString());
		contract.exchange("SMART");
		contract.strike(strike);
		contract.lastTradeDateOrContractMonth("" + expYear + expMonth + expDay);
		contract.multiplier("100");
		contract.right(right);
		return contract;
	}

}
