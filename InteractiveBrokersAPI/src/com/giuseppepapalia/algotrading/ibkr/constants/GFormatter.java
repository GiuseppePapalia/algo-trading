package com.giuseppepapalia.algotrading.ibkr.constants;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GFormatter {

	public static SimpleDateFormat TIMESTAMP = new SimpleDateFormat("yyyyMMdd hh:mm:ss");

	public static SimpleDateFormat IBKR_DATE = new SimpleDateFormat("yyyyMMdd");

	public static SimpleDateFormat getOptionDateFormat() {
		SimpleDateFormat OPTION_DATE_FORMAT = new SimpleDateFormat("MM/dd/yy");
		try {
			OPTION_DATE_FORMAT.set2DigitYearStart(IBKR_DATE.parse("20000101"));
		} catch (ParseException e) {
			return new SimpleDateFormat("MM/dd/yyyy");
		}
		return OPTION_DATE_FORMAT;
	}

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
