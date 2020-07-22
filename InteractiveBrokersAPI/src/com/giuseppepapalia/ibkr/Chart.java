package com.giuseppepapalia.ibkr;

import java.util.List;

public class Chart {

	private List<DetailedBar> chart;

	public Chart(List<DetailedBar> chart) {
		this.chart = chart;
	}

	public void addBar(DetailedBar bar) {
		chart.add(bar);
	}

	public List<DetailedBar> getChart() {
		return chart;
	}

}
