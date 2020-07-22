package com.giuseppepapalia.ibkr.constants;

public enum Duration {

	SECONDS, DAYS, WEEKS, MONTHS, YEARS;

	public String toDuration(int duration) {
		switch (this) {
		case DAYS:
			return duration + " D";
		case MONTHS:
			return duration + " M";
		case SECONDS:
			return duration + " S";
		case WEEKS:
			return duration + " W";
		case YEARS:
			return duration + " Y";
		default:
			return null;

		}
	}

}
