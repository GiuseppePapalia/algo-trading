package com.giuseppepapalia.ibkr;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class Chart {

	private List<Pair<BarData, ChartData>> chart;

	public Chart() {
		this.chart = new LinkedList<Pair<BarData, ChartData>>();
	}

	public void addBar(BarData bar) {
		chart.add(new ImmutablePair<BarData, ChartData>(bar, new ChartData(this)));
	}

	public Pair<BarData, ChartData> getMostRecentData() {
		return chart.get(chart.size() - 1);
	}

	public List<Pair<BarData, ChartData>> getChart() {
		return chart;
	}

}
