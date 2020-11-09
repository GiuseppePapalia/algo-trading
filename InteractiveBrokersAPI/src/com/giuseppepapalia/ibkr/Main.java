package com.giuseppepapalia.ibkr;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.giuseppepapalia.ibkr.constants.BarSize;
import com.giuseppepapalia.ibkr.constants.GFormatter;

public class Main {

	public static void main(String args[]) throws InterruptedException {
		InteractiveBrokersClient client = new InteractiveBrokersClient(false, "DY1919358");
		// Thread.sleep(20000);
		// client.placeLongBracketOrder(InteractiveBrokersFactory.createStock("MSFT"),
		// 5, 165.14, 168.14, 162.14);
		// Chart chart =
		// client.getHistoricalData(InteractiveBrokersFactory.createStock("TSLA"),
		// new Date(System.currentTimeMillis()), "1 D", BarSize.TEN_MIN);
		// Graph.plotTimeSeries(chart);
		// Graph.toCSV("Tesla");
		// Graph.plot();
		getCSVDataRange(client, "20201001 16:30:00", "20201031 16:30:00");
		System.exit(0);

	}

	public static void getCSVDataRange(InteractiveBrokersClient client, String startDate, String endDate) {
		try {
			int date = 1;
			Calendar calendar = new GregorianCalendar();
			Date start = GFormatter.TIMESTAMP.parse(startDate);
			Date end = GFormatter.TIMESTAMP.parse(endDate);
			calendar.setTime(start);
			while (calendar.getTime().before(end)) {
				if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
						&& calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
					Date result = calendar.getTime();
					System.out.println(result);
					Chart chart = client.getHistoricalData(InteractiveBrokersFactory.createStock("TSLA"), result, "1 D",
							BarSize.TEN_MIN);
					Graph g = new Graph();
					g.plotTimeSeries(chart);
					g.toCSV("Tesla" + date);
				}
				calendar.add(Calendar.DATE, 1);
				date++;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

}
