package com.giuseppepapalia.ibkr.order;

import com.giuseppepapalia.ibkr.Order;
import com.giuseppepapalia.ibkr.Quote;
import com.giuseppepapalia.ibkr.constants.Action;

public class BuyLimitOrder extends Order {

	private double limitPrice;

	public BuyLimitOrder(double cost, double limitPrice, double quantity) {
		super(Action.BUY, cost, quantity);
	}

	@Override
	public boolean isConditionMet(Quote quote) {
		return quote.getAsk() <= limitPrice;
	}

}
