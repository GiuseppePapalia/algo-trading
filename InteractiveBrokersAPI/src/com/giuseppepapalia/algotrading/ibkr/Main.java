package com.giuseppepapalia.algotrading.ibkr;

import java.util.Date;

import com.giuseppepapalia.algotrading.ibkr.constants.BarSize;

public class Main {

	public static void main(String args[]) throws InterruptedException {
		// InteractiveBrokersClient client = new InteractiveBrokersClient(false, "DY1919358");
		InteractiveBrokersClient client = new InteractiveBrokersClient(false, "DU1919358");

		Thread.sleep(20000);
		// client.placeLongBracketOrder(InteractiveBrokersFactory.createStock("MSFT"),
		// 5, 165.14, 168.14, 162.14);
		Chart chart = client.getHistoricalData(InteractiveBrokersFactory.createStock("TSLA"), new Date(System.currentTimeMillis()), "1 D", BarSize.ONE_MIN);
		System.out.println(chart.getChart());
		client.watchStock((InteractiveBrokersFactory.createStock("TSLA")));
	}

}
