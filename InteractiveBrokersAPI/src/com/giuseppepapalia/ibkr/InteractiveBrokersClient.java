package com.giuseppepapalia.ibkr;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;
import com.ib.client.Order;
import com.ib.client.OrderType;
import com.ib.client.Types.Action;

public class InteractiveBrokersClient {

	private EClientSocket client;
	private InteractiveBrokersAPI api;

	public InteractiveBrokersClient(boolean liveTrading, String accID) {
		api = new InteractiveBrokersAPI();

		client = api.getClient();
		final EReaderSignal m_signal = api.getSignal();
		int port = liveTrading ? 7496 : 7497;
		api.getClient().eConnect("127.0.0.1", port, 2);
		final EReader reader = new EReader(client, m_signal);

		reader.start();
		// An additional thread is created in this program design to empty the messaging queue
		new Thread(() -> {
			while (client.isConnected()) {
				m_signal.waitForSignal();
				try {
					reader.processMsgs();
				} catch (Exception e) {
					System.out.println("Exception: " + e.getMessage());
				}
			}
		}).start();

		addShutdownHook();

		api.initPorfolio(accID);
		client.reqPositions();
		// client.reqAccountUpdates(true, accID);
		client.reqPnL(17001, accID, "");

	}

	public int placeOrder(String symbol, int quantity, Action action) {
		client.reqIds(-1);
		int orderID = api.getCurrentOrderId() + 1;

		Contract contract = new Contract();
		contract.symbol(symbol);

		Order order = new Order();
		order.account(api.getPortfolio().getAccID());
		order.action(action);
		order.totalQuantity(quantity);
		order.orderId(orderID);
		order.orderType(OrderType.LMT);

		client.placeOrder(orderID, contract, order);

		return orderID;
	}

	public int placeLongBracketOrder(String symbol, double quantity, double entryLimitPrice, double takeProfitLimitPrice, double stopLossPrice) {
		Contract contract = InteractiveBrokersFactory.createStock(symbol);

		client.reqIds(-1);
		int orderID = api.getCurrentOrderId() + 1;

		LongBracketOrder order = new LongBracketOrder(orderID, quantity, entryLimitPrice, takeProfitLimitPrice, stopLossPrice);

		for (Order o : order.getOrders()) {
			client.placeOrder(o.orderId(), contract, o);
		}

		return orderID;
	}

	private void addShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				disconnect();
			}
		});
	}

	public void disconnect() {
		client.eDisconnect();
	}
}
