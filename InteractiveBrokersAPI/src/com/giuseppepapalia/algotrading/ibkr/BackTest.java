package com.giuseppepapalia.algotrading.ibkr;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.giuseppepapalia.algotrading.ibkr.constants.BarSize;
import com.ib.client.Contract;

public class BackTest {

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

	public BackTest(InteractiveBrokersClient client, String symbol, double startingCash, double buyThreshold, double sellThreshold, double takeProfit, double cutLoss) {
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
		double[] result = new double[2];
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
		String ticker = "SPY";
//		String dates[] = { "2017/01/01 00:00:00", "2018/03/03 00:00:00", "2019/06/06 00:00:00" };
//		String dates[] = { "2017/06/14 00:00:00", "2018/08/25 00:00:00", "2019/11/02 00:00:00" };
		String dates[] = { "2020/04/14 00:00:00", "2020/04/14 00:00:00", "2020/04/14 00:00:00" };

		// double[] takeProfits = { 1.01, 1.02, 1.03, 1.04 };
		// double[] cutLoss = { 0.995, 0.99, 0.98, 0.97 };
		// double[] buyThreshold = { 0.05, 0.1, 0.15, 0.2, 0.3 };
		double[] takeProfits = { 1.03 };
		double[] cutLoss = { 0.99 };
		double[] buyThreshold = { 0.1 };
		// double[] sellThreshold = { -0.05, -0.1, -0.15, -0.2, -0.30 };
		File file = new File("C:/TEMP/results.csv");
		if (!file.exists()) {
			file.createNewFile();
		}

		String output = "Buy Threshold,Sell Threshold,Take Profit,Cut Losses,PnL %,Trades Executed,PnL after Comission\n";
		FileUtils.writeStringToFile(file, output);

		Map<Double[], Double[]> results = new HashMap<>();

		for (int i = 0; i < takeProfits.length; i++) {
			for (int j = 0; j < cutLoss.length; j++) {
				for (int k = 0; k < buyThreshold.length; k++) {
					// for (int l = 0; l < sellThreshold.length; l++) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					Date startDates[] = { sdf.parse(dates[0]), sdf.parse(dates[1]), sdf.parse(dates[2]) };
					double finalCash = 5000;
					int totalTradeCount = 0;
					for (Date date : startDates) {
						System.out.println("Results for " + ticker + " starting from " + date);
						BackTest backTest = new BackTest(client, ticker, 5000, buyThreshold[k], -buyThreshold[k], takeProfits[i], cutLoss[j]);
						int period = 9;
						for (int m = 0; m < period; m++) {
							double[] result = backTest.test(new Date(date.getTime() - TimeUnit.DAYS.toMillis(m * 10)), "10 D");
							finalCash += result[0] - 5000; // add pnl
							totalTradeCount += result[1]; // add trade count
						}
					}
					DecimalFormat df = new DecimalFormat("##0.00");
					double pctChange = (finalCash / 5000 * 100 - 100);
					double pctChangeAfterCom = ((finalCash - totalTradeCount) / 5000 * 100 - 100);
					results.put(new Double[] { buyThreshold[k], -buyThreshold[k], takeProfits[i], cutLoss[j] }, new Double[] { pctChange, (double) totalTradeCount });
					System.out.println("BT: " + buyThreshold[k] + "\tST: " + -buyThreshold[k] + "\tTP: " + takeProfits[i] + "\tCL: " + cutLoss[j] + "\n% Change: " + pctChange + "\n");
					output += buyThreshold[k] + "," + -buyThreshold[k] + "," + takeProfits[i] + "," + cutLoss[j] + "," + df.format(pctChange) + "," + totalTradeCount + "," + df.format(pctChangeAfterCom) + "\n";
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

}
