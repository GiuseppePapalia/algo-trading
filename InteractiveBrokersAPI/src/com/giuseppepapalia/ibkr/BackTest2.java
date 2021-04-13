package com.giuseppepapalia.ibkr;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.giuseppepapalia.ibkr.constants.BarSize;
import com.ib.client.Contract;

public class BackTest2 {

	private Contract testStock;
	private double ownedShares;
	private double portfolioCash;
	private double entryPoint;
	private double startingCash;
	private InteractiveBrokersClient client;
	private int tradeCount;
	private double buyThreshold;
	private double sellThreshold;
	private double takeProfit;
	private double cutLoss;

	public BackTest2(InteractiveBrokersClient client, String symbol, double startingCash, double buyThreshold, double sellThreshold, double takeProfit, double cutLoss) {
		testStock = InteractiveBrokersFactory.createStock(symbol);
		ownedShares = 0;
		tradeCount = 0;
		portfolioCash += startingCash;
		this.startingCash = startingCash;
		this.client = client;
		this.buyThreshold = buyThreshold;
		this.sellThreshold = sellThreshold;
		this.takeProfit = takeProfit;
		this.cutLoss = cutLoss;
	}

	public double[] test(Date endDate, String duration) {
		double[] result = new double[4];
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
		result[0] = portfolioCash;
		result[1] = tradeCount;
		result[2] = chart.getChart().get(0).getLeft().getOpen();
		result[3] = chart.getChart().get(chart.getChart().size() - 1).getLeft().getClose();
		return result;
	}

	private boolean buyFlag(Pair<BarData, ChartData> data) {
		boolean vwapDeriv = data.getRight().getVWAPDeriv() == null ? false : data.getRight().getVWAPDeriv() > buyThreshold;
		return ownedShares == 0 && portfolioCash > 0 && vwapDeriv;
	}

	private boolean sellFlag(Pair<BarData, ChartData> data) {
		boolean vwapDeriv = data.getRight().getVWAPDeriv() == null ? false : data.getRight().getVWAPDeriv() < sellThreshold;
		return ownedShares > 0 && (data.getLeft().getClose() > entryPoint * takeProfit || data.getLeft().getClose() < entryPoint * cutLoss || vwapDeriv);
	}

	private void buy(Pair<BarData, ChartData> data) {
		double cashSpend = portfolioCash * 0.2;
		ownedShares = cashSpend / data.getLeft().getClose();
		portfolioCash -= cashSpend;
		entryPoint = data.getLeft().getClose();
		// System.out.println("BUY: " + ownedShares + " @ " + entryPoint);
	}

	private void sell(Pair<BarData, ChartData> data) {
		double soldShares = ownedShares;
		portfolioCash += soldShares * data.getLeft().getClose();
		ownedShares = 0;
		// System.out.println("SELL: " + soldShares + " @ " + data.getLeft().getClose());
	}

	class TestParam {
		double takeProfit;
		double cutLoss;
		double thresholdBuy;
		double thresholdSell;
	}

	static void generatePermutations(List<List<Double>> lists, List<String> result, int depth, String current) {
		if (depth == lists.size()) {
			result.add(current);
			return;
		}

		for (int i = 0; i < lists.get(depth).size(); i++) {
			generatePermutations(lists, result, depth + 1, current + lists.get(depth).get(i));
		}
	}

	@SuppressWarnings("deprecation")
	public static void main(String args[]) throws ParseException, IOException {
		InteractiveBrokersClient client = new InteractiveBrokersClient(false, "DU1919358");
		DecimalFormat df = new DecimalFormat("##0.00");
		String[] tickers = { "SPY", "VOO", "IVV" };
		String dates[] = { "2020/03/31 00:00:00", "2019/06/30 00:00:00", "2018/09/30 00:00:00", "2017/12/31 00:00:00" };

		File file = new File("C:/TEMP/results.csv");
		if (!file.exists()) {
			file.createNewFile();
		}

		String output = "End Date,Ticker,Profit %,Trade Count,Default Return\n";
		FileUtils.writeStringToFile(file, output);

		for (String ticker : tickers) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date startDates[] = { sdf.parse(dates[0]), sdf.parse(dates[1]), sdf.parse(dates[2]), sdf.parse(dates[3]) };

			for (Date date : startDates) {
				double finalCash = 5000;
				int totalTradeCount = 0;
				double openPrice = 0;
				double endPrice = 0;
				BackTest2 backTest = new BackTest2(client, ticker, 5000, 0.1, -0.1, 1.03, 0.99);
				int period = 9;
				for (int m = 0; m < period; m++) {
					double[] result = backTest.test(new Date(date.getTime() - TimeUnit.DAYS.toMillis(m * 10)), "10 D");
					finalCash += result[0] - 5000; // add pnl
					totalTradeCount += result[1]; // add trade count'
					if (m == 0) {
						openPrice = result[2];
					} else if (m == 8) {
						endPrice = result[3];
					}
				}

				double pctChange = (finalCash / 5000 * 100 - 100);
				double pctChangeDefault = (openPrice / endPrice - 1) * 100;
				output += date + "," + ticker + "," + df.format(pctChange) + "," + totalTradeCount + "," + df.format(pctChangeDefault) + "\n";
				FileUtils.writeStringToFile(file, output);
			}
		}

	}

//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//		Date startDates[] = { sdf.parse(dates[0]), sdf.parse(dates[1]), sdf.parse(dates[2]) };
//		Map<Double[], Double> results;
//		for (String ticker : tickers) {
//			for (Date date : startDates) {
//				System.out.println("Results for " + ticker + " starting from " + date);
//				BackTest backTest = new BackTest(client, ticker, 5000);
//				double finalCash = 5000;
//				int period = 9;
//				for (int i = 0; i < period; i++) {
//					double diff = backTest.test(new Date(date.getTime() - TimeUnit.DAYS.toMillis(i * 10)), "10 D") - 5000;
//					finalCash += diff;
//				}
//				System.out.println("% Change: " + (finalCash / 5000 * 100 - 100));
//			}
//		}
}
