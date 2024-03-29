package com.giuseppepapalia.ibkr;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;

public final class ChartData {

	private final Double rsi;
	private final Double vwap;
	private final Double rsiDeriv;
	private final Double vwapDeriv;

	private final int RSI_PERIODS = 14;

	public ChartData(Chart chart) {
		vwap = computeVWAP(chart);
		vwapDeriv = computeVWAPDeriv(chart);
		rsi = computeRSI();
		rsiDeriv = computeRSIDeriv();
	}

	private Double computeRSI() {
		return null;
	}

	private Double computeRSIDeriv() {
		return null;
	}

	private Double computeVWAP(Chart chart) {
		int chartSize = chart.getChart().size();
		if (chartSize > 13) {
			double volumeXClose = 0;
			double volume = 0;
			for (int i = chartSize - 1; i >= chartSize - 14; i--) {
				BarData data = chart.getChart().get(i).getLeft();
				volumeXClose += (data.getVolume() * data.getClose());
				volume += data.getVolume();
			}
			return volumeXClose / volume;
		}
		return null;
	}

	private Double computeVWAPDeriv(Chart chart) {
		int chartSize = chart.getChart().size();
		if (chartSize > 23) {
			List<Double> x = new ArrayList<Double>();
			List<Double> y = new ArrayList<Double>();
			for (int i = 14; i < chartSize; i++) {
				x.add((double) (i - 13));
				y.add(chart.getChart().get(i).getRight().getVWAP());
			}
			double[] xArray = new double[x.size()];
			double[] yArray = new double[y.size()];
			for (int i = 0; i < x.size(); i++) {
				xArray[i] = x.get(i);
				yArray[i] = y.get(i);
			}

			SplineInterpolator interpolator = new SplineInterpolator();
			return interpolator.interpolate(xArray, yArray).derivative().value(xArray[xArray.length - 1]);
		}
		return null;
	}

	public Double getVWAP() {
		return vwap;
	}

	public Double getRSI() {
		return rsi;
	}

	public Double getRSIDeriv() {
		return rsiDeriv;
	}

	public Double getVWAPDeriv() {
		return vwapDeriv;
	}

}
