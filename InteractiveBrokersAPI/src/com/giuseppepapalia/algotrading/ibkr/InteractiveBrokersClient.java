package com.giuseppepapalia.algotrading.ibkr;

import java.util.Date;

import com.giuseppepapalia.algotrading.ibkr.constants.BarSize;
import com.giuseppepapalia.algotrading.ibkr.constants.GFormatter;
import com.giuseppepapalia.algotrading.ibkr.orderflow.LongBracketOrderFlow;
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
		initWatchlist();
		id = api.getCurrentOrderId();
	}

	public void watchStock(Contract contract) {
		api.watchStock(id, contract);
		client.reqRealTimeBars(id, contract, 5, "TRADES", true, null);
		client.reqTickByTickData(id + 1, contract, "BidAsk", 0, false);
		id += 2;
	}

	public void stopWatchingStock(Contract contract) {
		int id = getWatchlist().stopWatching(contract);
		client.cancelRealTimeBars(id);
		client.cancelTickByTickData(id + 1);
	}

	public Watchlist getWatchlist() {
		return api.getWatchlist();
	}

	public void placeLongBracketOrder(Contract contract, double quantity, double entryLimitPrice, double takeProfitLimitPrice, double stopLossPrice) {
		LongBracketOrderFlow order = new LongBracketOrderFlow(id, quantity, entryLimitPrice, takeProfitLimitPrice, stopLossPrice);

		for (Order o : order.getOrderFlow()) {
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

	public Portfolio getPortfolio() {
		return api.getPortfolio();
	}

	private void initWatchlist() {
		api.initWatchlist(this);
		for (Position pos : getPortfolio().getPositions()) {
			watchStock(pos.getContract());
		}
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
