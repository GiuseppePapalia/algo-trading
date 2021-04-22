package com.giuseppepapalia.algotrading.ibkr.order;

import com.giuseppepapalia.algotrading.ibkr.Order;
import com.giuseppepapalia.algotrading.ibkr.Quote;
import com.giuseppepapalia.algotrading.ibkr.constants.Action;

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
