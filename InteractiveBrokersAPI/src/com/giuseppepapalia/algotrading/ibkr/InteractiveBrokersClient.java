package com.giuseppepapalia.algotrading.ibkr;

import java.util.Date;

import com.giuseppepapalia.algotrading.ibkr.constants.BarSize;
import com.giuseppepapalia.algotrading.ibkr.constants.GFormatter;
import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;
import com.ib.client.Order;

public class InteractiveBrokersClient {

	protected EClientSocket client;
	private InteractiveBrokersAPI api;
	private int id;

	public InteractiveBrokersClient(boolean liveTrading, InteractiveBrokersCoreInterface core) {
		api = new InteractiveBrokersAPI(core);

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

		// client.reqAccountUpdates(true, accID);
		// client.reqPnL(17001, portfolio.getAccID(), "");
	}

	public void init(String accID) {
		client.reqAccountUpdates(true, accID);
		client.reqPnL(17001, accID, "");
		id = api.getCurrentOrderId();
	}

	public int getOptionChain(Contract contract) {
		client.reqContractDetails(id, InteractiveBrokersFactory.createMockOption(contract.symbol()));
		id++;
		return id - 1;
	}

	public int watchStock(Contract contract) {
		client.reqRealTimeBars(id, contract, 5, "TRADES", true, null);
		client.reqTickByTickData(id + 1, contract, "BidAsk", 0, false);
		client.reqContractDetails(id + 2, InteractiveBrokersFactory.createMockOption(contract.symbol()));
		id += 3;
		return id - 3;
	}

	public void stopWatchingStock(int reqId) {
		client.cancelRealTimeBars(id);
		client.cancelTickByTickData(id + 1);
	}

	public int placeOrder(Contract contract, OrderFlow orderFlow) {
		for (Order o : orderFlow.getOrderFlow()) {
			client.placeOrder(o.orderId(), contract, o);
		}

		id += orderFlow.getSize();
		return id - orderFlow.getSize();
	}

	public Chart getHistoricalData(Contract contract, Date endDate, String duration, BarSize barSize) {
		int reqId = id;
		id++;
		api.createLongRequest(reqId);
		client.reqHistoricalData(reqId, contract, GFormatter.TIMESTAMP.format(endDate), duration, barSize.toString(), "TRADES", 1, 1, false, null);
		return api.getHistoricalData(reqId, contract);
	}

	public int getID() {
		return id;
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
