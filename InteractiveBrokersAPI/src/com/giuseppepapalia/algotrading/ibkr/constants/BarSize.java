package com.giuseppepapalia.algotrading.ibkr.constants;

public enum BarSize {

	ONE_SECS, FIVE_SECS, TEN_SECS, FIFTEEN_SECS, THIRTY_SECS, ONE_MIN, TWO_MIN, THREE_MIN, FIVE_MIN, TEN_MIN,
	FIFTEEN_MIN, TWENTY_MIN, THIRTY_MIN, ONE_HOUR, TWO_HOUR, THREE_HOUR, FOUR_HOUR, EIGHT_HOUR, ONE_DAY, ONE_WEEK,
	ONE_MONTH;

	@Override
	public String toString() {
		switch (this) {
		case ONE_SECS:
			return "1 secs";
		case FIVE_SECS:
			return "5 secs";
		case TEN_SECS:
			return "10 secs";
		case FIFTEEN_SECS:
			return "15 secs";
		case THIRTY_SECS:
			return "30 secs";
		case ONE_MIN:
			return "1 min";
		case TWO_MIN:
			return "2 mins";
		case THREE_MIN:
			return "3 mins";
		case FIVE_MIN:
			return "5 mins";
		case TEN_MIN:
			return "10 mins";
		case FIFTEEN_MIN:
			return "15 mins";
		case TWENTY_MIN:
			return "20 mins";
		case THIRTY_MIN:
			return "30 mins";
		case ONE_HOUR:
			return "1 hour";
		case TWO_HOUR:
			return "2 hours";
		case THREE_HOUR:
			return "3 hours";
		case FOUR_HOUR:
			return "4 hours";
		case EIGHT_HOUR:
			return "8 hours";
		case ONE_DAY:
			return "1 day";
		case ONE_WEEK:
			return "1 week";
		case ONE_MONTH:
			return "1 month";
		default:
			return "";
		}
	}
}
