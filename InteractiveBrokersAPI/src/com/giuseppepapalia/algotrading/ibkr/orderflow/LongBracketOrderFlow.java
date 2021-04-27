package com.giuseppepapalia.algotrading.ibkr.orderflow;

import com.giuseppepapalia.algotrading.ibkr.OrderFlow;

public class LongBracketOrderFlow extends OrderFlow {

	public LongBracketOrderFlow(int orderID, double quantity, double entryLimitPrice, double takeProfitLimitPrice, double stopLossPrice) {
		appendOrderFlow(new BuyLimitOrderFlow(orderID, quantity, entryLimitPrice));
		appendOrderFlow(new SellLimitOrderFlow(orderID + 1, quantity, takeProfitLimitPrice));
		appendOrderFlow(new StopLossOrderFlow(orderID + 2, quantity, stopLossPrice));
	}

}
