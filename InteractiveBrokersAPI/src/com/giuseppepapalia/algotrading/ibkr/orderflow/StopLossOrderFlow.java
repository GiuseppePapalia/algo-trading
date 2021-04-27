package com.giuseppepapalia.algotrading.ibkr.orderflow;

import com.giuseppepapalia.algotrading.ibkr.OrderFlow;
import com.ib.client.Order;
import com.ib.client.OrderType;
import com.ib.client.Types.Action;

public class StopLossOrderFlow extends OrderFlow {

	public StopLossOrderFlow(int orderID, double quantity, double stopPrice) {
		Order o = new Order();
		o.orderId(orderID + 2);
		o.action(Action.SELL);
		o.orderType(OrderType.STP);
		o.auxPrice(stopPrice);
		o.totalQuantity(quantity);
		o.parentId(orderID);
		o.transmit(true);
		appendOrder(o);
	}

}
