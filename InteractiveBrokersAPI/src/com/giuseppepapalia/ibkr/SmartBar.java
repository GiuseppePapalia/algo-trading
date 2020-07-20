package com.giuseppepapalia.ibkr;

import java.util.Date;

public final class SmartBar {

	private final Date startTime;
	private final double open;
	private final double close;
	private final double high;
	private final double low;
	private final long volume;
	private final double wap;

	public SmartBar(Date startTime, double open, double close, double high, double low, long volume, double wap) {
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

	public boolean isGreen() {
		return close > open;
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

}
