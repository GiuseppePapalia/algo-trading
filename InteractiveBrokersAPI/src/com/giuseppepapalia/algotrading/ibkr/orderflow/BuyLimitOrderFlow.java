package com.giuseppepapalia.algotrading.ibkr.orderflow;

import com.giuseppepapalia.algotrading.ibkr.OrderFlow;
import com.ib.client.Order;
import com.ib.client.OrderType;
import com.ib.client.Types.Action;

public class BuyLimitOrderFlow extends OrderFlow {

	public BuyLimitOrderFlow(int orderID, double quantity, double limitPrice) {
		Order o = new Order();
		o.orderId(orderID);
		o.action(Action.BUY);
		o.totalQuantity(quantity);
		o.orderType(OrderType.LMT);
		o.lmtPrice(limitPrice);
		o.transmit(false);
		appendOrder(o);
	}

}
