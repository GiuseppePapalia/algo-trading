package com.giuseppepapalia.ibkr;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import tech.tablesaw.api.DateTimeColumn;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.csv.CsvWriteOptions;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.TimeSeriesPlot;

public class Graph {
	private static List<LocalDateTime> dates = new ArrayList<LocalDateTime>();
	private static List<Double> openPrices = new ArrayList<Double>();
	private static List<Double> closePrices = new ArrayList<Double>();
	private static List<Double> highPrices = new ArrayList<Double>();
	private static List<Double> lowPrices = new ArrayList<Double>();
	private static List<Long> volumeTrades = new ArrayList<Long>();
	private static List<Double> waps = new ArrayList<Double>();
	private static Table timeSeries;

	public static void plotTimeSeries(Chart chart) {
		List<DetailedBar> candles = chart.getChart();
		for (DetailedBar candle : candles) {
			dates.add(LocalDateTime.ofInstant(candle.getStartTime().toInstant(), ZoneId.systemDefault()));
			openPrices.add(candle.getOpen());
			closePrices.add(candle.getClose());
			highPrices.add(candle.getHigh());
			lowPrices.add(candle.getLow());
			volumeTrades.add(candle.getVolume());
			waps.add(candle.getWap());
		}
		createTimeSeries();
	}

	private static void createTimeSeries() {
		timeSeries = Table.create("Prices").addColumns(DateTimeColumn.create("Dates", dates),
				DoubleColumn.create("First traded price", openPrices),
				DoubleColumn.create("Last traded price", closePrices),
				DoubleColumn.create("Highest traded price", highPrices),
				DoubleColumn.create("Lowest traded price", lowPrices));
	}

	public static void toCSV() {
		CsvWriteOptions.Builder builder;
		try {
			builder = CsvWriteOptions.builder("TSLA.csv").separator(',').header(true).quoteChar('"');
			CsvWriteOptions options = builder.build();
			timeSeries.write().usingOptions(options);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void plot() {
		Plot.show(TimeSeriesPlot.create("Prices", timeSeries, "Dates", "First traded price"));
	}

}
