package com.giuseppepapalia.algotrading.ibkr.security;

import java.util.Date;

import com.giuseppepapalia.algotrading.ibkr.Quote;
import com.giuseppepapalia.algotrading.ibkr.Security;
import com.giuseppepapalia.algotrading.ibkr.constants.GFormatter;
import com.giuseppepapalia.algotrading.ibkr.constants.OptionType;
import com.ib.client.Contract;

public class Option extends Security {

	private final OptionType optionType;
	private final Date expiryDate;
	private final double strikePrice;

	public Option(Contract contract, Quote quote, Date expiryDate, double strikePrice, OptionType optionType) {
		super(contract, quote);
		this.expiryDate = expiryDate;
		this.strikePrice = strikePrice;
		this.optionType = optionType;
	}

	public OptionType getOptionType() {
		return optionType;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public double getStrikePrice() {
		return strikePrice;
	}

	@Override
	public String toString() {
		return contract.symbol() + " " + GFormatter.getOptionDateFormat().format(expiryDate) + " " + strikePrice + optionType.toString();
	}

}
