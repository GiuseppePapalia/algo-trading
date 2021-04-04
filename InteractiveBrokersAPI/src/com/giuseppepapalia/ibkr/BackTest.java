package com.giuseppepapalia.ibkr;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.tuple.Pair;

import com.giuseppepapalia.ibkr.constants.BarSize;
import com.ib.client.Contract;

public class BackTest {

	private Contract testStock;
	private double ownedShares;
	private double portfolioCash;
	private double entryPoint;
	private double startingCash;
	private InteractiveBrokersClient client;
	private int tradeCount;

	public BackTest(InteractiveBrokersClient client, String symbol, double startingCash) {
		testStock = InteractiveBrokersFactory.createStock(symbol);
		ownedShares = 0;
		tradeCount = 0;
		portfolioCash += startingCash;
		this.startingCash = startingCash;
		this.client = client;
	}

	public double test(Date endDate, String duration) {
		Chart chart = client.getHistoricalData(testStock, endDate, duration, BarSize.ONE_MIN);
		for (Pair<BarData, ChartData> data : chart.getChart()) {
			if (buyFlag(data)) {
				buy(data);
				tradeCount++;
			} else if (sellFlag(data)) {
				sell(data);
			}
		}
		if (ownedShares > 0) {
			sell(chart.getChart().get(chart.getChart().size() - 1));
		}
		System.out.println("FINISH after " + tradeCount + " trades: $" + portfolioCash + " FROM: $" + startingCash);
		return portfolioCash;
	}

	private boolean buyFlag(Pair<BarData, ChartData> data) {
		boolean vwapDeriv = data.getRight().getVWAPDeriv() == null ? false : data.getRight().getVWAPDeriv() > 0.2;
		return ownedShares == 0 && portfolioCash > 0 && vwapDeriv;
	}

	private boolean sellFlag(Pair<BarData, ChartData> data) {
		boolean vwapDeriv = data.getRight().getVWAPDeriv() == null ? false : data.getRight().getVWAPDeriv() < -0.2;
		return ownedShares > 0 && (data.getLeft().getClose() > entryPoint * 1.02 || data.getLeft().getClose() < entryPoint * 0.99 || vwapDeriv);
	}

	private void buy(Pair<BarData, ChartData> data) {
		double cashSpend = portfolioCash * 0.2;
		ownedShares = cashSpend / data.getLeft().getClose();
		portfolioCash -= cashSpend;
		entryPoint = data.getLeft().getClose();
		System.out.println("BUY: " + ownedShares + " @ " + entryPoint);
	}

	private void sell(Pair<BarData, ChartData> data) {
		double soldShares = ownedShares;
		portfolioCash += soldShares * data.getLeft().getClose();
		ownedShares = 0;
		System.out.println("SELL: " + soldShares + " @ " + data.getLeft().getClose());
	}

	public static void main(String args[]) {
		InteractiveBrokersClient client = new InteractiveBrokersClient(false, "DU1919358");
		BackTest backTest = new BackTest(client, "DAL", 5000);
		double finalCash = 5000;
		int period = 9;
		for (int i = 0; i < period; i++) {
			double diff = backTest.test(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(i * 10)), "10 D") - 5000;
			finalCash += diff;
		}
		System.out.println("% Change: " + (finalCash / 5000 * 100 - 100));
	}

}
