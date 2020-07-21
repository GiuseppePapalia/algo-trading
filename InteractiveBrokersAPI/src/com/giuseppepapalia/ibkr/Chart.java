package com.giuseppepapalia.ibkr;

import java.util.List;

import com.ib.client.Contract;

public class Chart {

	private List<Bar> chart;
	private Contract underlying;

	public Chart(Contract underlying, List<Bar> chart) {
		this.underlying = underlying;
		this.chart = chart;
	}

	public void addBar(Bar bar) {
		chart.add(bar);
	}

	public List<Bar> getChart() {
		return chart;
	}

	public Contract getUnderlying() {
		return underlying;
	}

}
