package com.giuseppepapalia.algotrading.ibkr.order;

import java.util.ArrayList;
import java.util.List;

import com.ib.client.Order;
import com.ib.client.OrderType;
import com.ib.client.Types.Action;

public class LongBracketOrder {

	private Order entryLimit;
	private Order takeProfitLimit;
	private Order stopLoss;

	public LongBracketOrder(int orderID, double quantity, double entryLimitPrice, double takeProfitLimitPrice, double stopLossPrice) {
		configureOrders(orderID, quantity, entryLimitPrice, takeProfitLimitPrice, stopLossPrice);
	}

	private void configureOrders(int orderID, double quantity, double entryLimitPrice, double takeProfitLimitPrice, double stopLossPrice) {
		entryLimit = new Order();
		entryLimit.orderId(orderID);
		entryLimit.action(Action.BUY);
		entryLimit.totalQuantity(quantity);
		entryLimit.orderType(OrderType.LMT);
		entryLimit.lmtPrice(entryLimitPrice);
		entryLimit.transmit(false);

		takeProfitLimit = new Order();
		takeProfitLimit.orderId(orderID + 1);
		takeProfitLimit.action(Action.SELL);
		takeProfitLimit.orderType(OrderType.LMT);
		takeProfitLimit.totalQuantity(quantity);
		takeProfitLimit.lmtPrice(takeProfitLimitPrice);
		takeProfitLimit.parentId(orderID);
		takeProfitLimit.transmit(false);

		stopLoss = new Order();
		stopLoss.orderId(orderID + 2);
		stopLoss.action(Action.SELL);
		stopLoss.orderType(OrderType.STP);
		stopLoss.auxPrice(stopLossPrice);
		stopLoss.totalQuantity(quantity);
		stopLoss.parentId(orderID);
		stopLoss.transmit(true);
	}

	public List<Order> getOrders() {
		List<Order> bracketOrder = new ArrayList<Order>();
		bracketOrder.add(entryLimit);
		bracketOrder.add(takeProfitLimit);
		bracketOrder.add(stopLoss);
		return bracketOrder;
	}
}
