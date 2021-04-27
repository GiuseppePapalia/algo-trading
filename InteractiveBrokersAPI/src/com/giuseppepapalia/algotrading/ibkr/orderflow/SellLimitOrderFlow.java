package com.giuseppepapalia.algotrading.ibkr.orderflow;

import com.giuseppepapalia.algotrading.ibkr.OrderFlow;
import com.ib.client.Order;
import com.ib.client.OrderType;
import com.ib.client.Types.Action;

public class SellLimitOrderFlow extends OrderFlow {

	public SellLimitOrderFlow(int orderID, double quantity, double limitPrice) {
		Order o = new Order();
		o.orderId(orderID);
		o.action(Action.SELL);
		o.orderType(OrderType.LMT);
		o.totalQuantity(quantity);
		o.lmtPrice(limitPrice);
		o.parentId(orderID);
		o.transmit(false);
		appendOrder(o);
	}

}
