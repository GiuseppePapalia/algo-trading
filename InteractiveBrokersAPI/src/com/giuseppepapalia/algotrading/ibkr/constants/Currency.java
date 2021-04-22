package com.giuseppepapalia.algotrading.ibkr.constants;

public enum Currency {

	USD, EUR, AUD, CAD, NZD, JPY, CHF;

	@Override
	public String toString() {
		switch (this) {
		case AUD:
			return "AUD";
		case CAD:
			return "CAD";
		case CHF:
			return "CHF";
		case EUR:
			return "EUR";
		case JPY:
			return "JPY";
		case NZD:
			return "NZD";
		case USD:
			return "USD";
		default:
			return null;
		}
	}

}
