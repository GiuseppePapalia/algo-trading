package com.giuseppepapalia.ibkr;

import java.util.List;

import com.ib.client.Contract;

public class SmartChart {

	private List<SmartBar> chart;
	private Contract underlying;

	public SmartChart(Contract underlying, List<SmartBar> chart) {
		this.underlying = underlying;
		this.chart = chart;
	}

	public void addBar(SmartBar bar) {
		chart.add(bar);
	}

	public List<SmartBar> getChart() {
		return chart;
	}

	public Contract getUnderlying() {
		return underlying;
	}

}
