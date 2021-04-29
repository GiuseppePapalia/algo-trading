package com.giuseppepapalia.algotrading.ibkr.constants;

import com.ib.client.Contract;
import com.ib.client.Types.Right;

public enum OptionType {

	CALL, PUT;

	public Right getRight() {
		switch (this) {
		case CALL:
			return Right.Call;
		case PUT:
			return Right.Put;
		default:
			return null;
		}
	}

	public static OptionType getOptionType(Contract contract) {
		switch (contract.right()) {
		case Call:
			return CALL;
		case Put:
			return PUT;
		default:
			return null;
		}
	}

	@Override
	public String toString() {
		switch (this) {
		case CALL:
			return "C";
		case PUT:
			return "P";
		default:
			return null;
		}
	}

}
