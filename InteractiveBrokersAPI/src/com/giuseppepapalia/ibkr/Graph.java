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
	private List<LocalDateTime> dates;
	private List<Double> openPrices;
	private List<Double> closePrices;
	private List<Double> highPrices;
	private List<Double> lowPrices;
	private List<Long> volumeTrades;
	private List<Double> waps;
	private Table timeSeries;

	public Graph() {
		dates = new ArrayList<LocalDateTime>();
		openPrices = new ArrayList<Double>();
		closePrices = new ArrayList<Double>();
		highPrices = new ArrayList<Double>();
		lowPrices = new ArrayList<Double>();
		volumeTrades = new ArrayList<Long>();
		waps = new ArrayList<Double>();
	}

	public void plotTimeSeries(Chart chart) {
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

	private void createTimeSeries() {
		timeSeries = Table.create("Prices").addColumns(DateTimeColumn.create("Dates", dates),
				DoubleColumn.create("First traded price", openPrices),
				DoubleColumn.create("Last traded price", closePrices),
				DoubleColumn.create("Highest traded price", highPrices),
				DoubleColumn.create("Lowest traded price", lowPrices));
	}

	public void toCSV(String name) {
		CsvWriteOptions.Builder builder;
		try {
			builder = CsvWriteOptions.builder(name + ".csv").separator(',').header(true).quoteChar('"');
			CsvWriteOptions options = builder.build();
			timeSeries.write().usingOptions(options);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void plot() {
		Plot.show(TimeSeriesPlot.create("Prices", timeSeries, "Dates", "First traded price"));
	}

}
