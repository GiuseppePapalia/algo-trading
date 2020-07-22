package com.giuseppepapalia.ibkr;

import java.util.Date;

import com.giuseppepapalia.ibkr.constants.BarSize;
import com.giuseppepapalia.ibkr.constants.GFormatter;
import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;
import com.ib.client.Order;

public class InteractiveBrokersClient {

	protected EClientSocket client;
	private InteractiveBrokersAPI api;
	private int id;

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
		id = api.getCurrentOrderId();
	}

	public void watchStock(Contract contract) {
		api.watchStock(id, contract);
		client.reqRealTimeBars(id, contract, 5, "TRADES", true, null);
		client.reqTickByTickData(id + 1, contract, "BidAsk", 0, false);
		id += 2;
	}

	public void placeLongBracketOrder(Contract contract, double quantity, double entryLimitPrice, double takeProfitLimitPrice, double stopLossPrice) {
		LongBracketOrder order = new LongBracketOrder(id, quantity, entryLimitPrice, takeProfitLimitPrice, stopLossPrice);

		for (Order o : order.getOrders()) {
			client.placeOrder(o.orderId(), contract, o);
		}

		id += 3;
	}

	public Chart getHistoricalData(Contract contract, Date endDate, String duration, BarSize barSize) {
		int reqId = id;
		id++;
		api.createLongRequest(reqId);
		client.reqHistoricalData(reqId, contract, GFormatter.TIMESTAMP.format(endDate), duration, barSize.toString(), "TRADES", 1, 1, false, null);
		return api.getHistoricalData(reqId, contract);
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
