package com.giuseppepapalia.algotrading.ibkr;

import java.text.ParseException;
import java.util.Date;

import com.giuseppepapalia.algotrading.ibkr.constants.GFormatter;
import com.ib.client.Bar;

public final class BarData implements Comparable<BarData> {

	private final Date startTime;
	private final double open;
	private final double close;
	private final double high;
	private final double low;
	private final long volume;
	private final double wap;

	public BarData(Bar bar) throws ParseException {
		this(GFormatter.TIMESTAMP.parse(bar.time()), bar.open(), bar.close(), bar.high(), bar.low(), bar.volume(), bar.wap());
	}

	public BarData(Date startTime, double open, double close, double high, double low, long volume, double wap) {
		this.startTime = startTime;
		this.volume = volume;
		this.open = open;
		this.close = close;
		this.high = high;
		this.low = low;
		this.wap = wap;
	}

	public double getWap() {
		return wap;
	}

	public long getVolume() {
		return volume;
	}

	public double getPercentChange() {
		return ((close / open) * 100) - 100;
	}

	public Date getStartTime() {
		return startTime;
	}

	public double getOpen() {
		return open;
	}

	public double getClose() {
		return close;
	}

	public double getHigh() {
		return high;
	}

	public double getLow() {
		return low;
	}

	/*
	 * To sort charts by the time of the bar
	 */
	@Override
	public int compareTo(BarData bar) {
		return bar.getStartTime().compareTo(getStartTime());
	}

	@Override
	public String toString() {
		return startTime + "\n" + GFormatter.formatPercent(getPercentChange()) + "%\nO: " + open + "\tC: " + close + "\nH: " + high + "\tL: " + low + "\nVol: " + volume;

	}

}
