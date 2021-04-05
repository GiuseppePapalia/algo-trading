package com.giuseppepapalia.ibkr;

import com.giuseppepapalia.ibkr.constants.Action;

public abstract class Order {

	private final Action action;
	private final double cost;
	private final double quantity;
	
	public Order(Action action, double cost, double quantity) {
		this.action = action;
		this.cost = cost;
		this.quantity = quantity;
	}
	
	public abstract boolean isConditionMet(Quote quote);
	
	
	public double getCost() {
		return cost;
	}
	
	public Action getAction() {
		return action;
	}
	
	public double quantity() {
		return quantity;
	}
	
}
