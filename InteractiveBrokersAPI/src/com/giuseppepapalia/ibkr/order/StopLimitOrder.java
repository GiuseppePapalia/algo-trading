package com.giuseppepapalia.ibkr.order;

import com.giuseppepapalia.ibkr.Order;
import com.giuseppepapalia.ibkr.Quote;
import com.giuseppepapalia.ibkr.constants.Action;

public class StopLimitOrder extends Order {

	private double stopLimitPrice;

	public StopLimitOrder(double cost, double stopLimitPrice, double quantity) {
		super(Action.SELL, cost, quantity);
		this.stopLimitPrice = stopLimitPrice;
	}

	@Override
	public boolean isConditionMet(Quote quote) {
		return quote.getBid() <= stopLimitPrice;
	}

}
