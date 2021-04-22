package com.giuseppepapalia.algotrading.ibkr;

import java.text.NumberFormat;

public final class CurrencyValue {

	private final double value;

	public CurrencyValue(double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	@Override
	public String toString() {
		return NumberFormat.getCurrencyInstance().format(value);
	}

}
