package com.giuseppepapalia.ibkr.order;

import com.giuseppepapalia.ibkr.Order;
import com.giuseppepapalia.ibkr.Quote;
import com.giuseppepapalia.ibkr.constants.Action;

public class SellLimitOrder extends Order {

	private double limitPrice;

	public SellLimitOrder(double cost, double limitPrice, double quantity) {
		super(Action.SELL, cost, quantity);
		this.limitPrice = limitPrice;
	}

	@Override
	public boolean isConditionMet(Quote quote) {
		return quote.getBid() >= limitPrice;
	}

}
