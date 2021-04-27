package com.giuseppepapalia.algotrading.ibkr;

import java.util.ArrayList;
import java.util.List;

import com.ib.client.Order;

public class OrderFlow {

	protected List<Order> orderFlow;

	public OrderFlow() {
		this.orderFlow = new ArrayList<Order>();
	}

	protected void appendOrder(Order order) {
		orderFlow.add(order);
	}

	protected void appendOrderFlow(OrderFlow orderFlow) {
		for (Order o : orderFlow.getOrderFlow()) {
			appendOrder(o);
		}
	}

	public List<Order> getOrderFlow() {
		return orderFlow;
	}

}
