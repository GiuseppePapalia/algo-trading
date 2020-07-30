package com.giuseppepapalia.ibkr.constants;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GFormatter {

	public static SimpleDateFormat TIMESTAMP = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

	public static String formatPercent(double number) {
		DecimalFormat df = new DecimalFormat("#,##0.000");
		df.setPositivePrefix("+");
		df.setRoundingMode(RoundingMode.HALF_UP);
		String formattedValue = df.format(number);
		formattedValue = formattedValue.replace("+0.000", "0");
		return formattedValue;
	}

	public static Date parseLong(long time) {
		return new Date(Long.parseLong("" + time) * 1000);
	}

}
