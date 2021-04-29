package com.giuseppepapalia.algotrading.ibkr;

import com.ib.client.Contract;

public interface InteractiveBrokersCoreInterface {

	public void updateQuote(int reqId, long time, double bidPrice, double askPrice, int bidSize, int askSize);

	public void updateLiveChart(int reqId, long time, double open, double high, double low, double close, long volume, double wap, int count);

	public void updatePosition(Contract contract, double pos, double avgCost);

	public void updatePortfolioPnL(double unrealizedPnL, double realizedPnL);

	public void updateCashBalance(double cashBalance);

	public void updateOptionChain(Contract option);

}
