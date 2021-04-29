package com.giuseppepapalia.algotrading.ibkr;

import java.util.List;

import com.giuseppepapalia.algotrading.ibkr.constants.OptionType;
import com.giuseppepapalia.algotrading.ibkr.security.Option;

public class OptionFetcher {

	private Stock stock;
	private List<Option> optionChain;

	public OptionFetcher(Stock stock, List<Option> optionChain) {
		this.optionChain = optionChain;
		this.stock = stock;
	}

	public Option getShortTermOption(OptionType optionType) {
		if (stock.getQuote().isEmpty() || optionChain == null) {
			return null;
		}

		double price = stock.getQuote().getMidPrice();

		double closestStrike = 0;

		for (Option option : optionChain) {
			if (closestStrike == 0) {
				closestStrike = option.getStrikePrice();
			} else {
				double strikePrice = option.getStrikePrice();

				switch (optionType) {
				case CALL:
					if (strikePrice > price && strikePrice < closestStrike) {
						closestStrike = strikePrice;
					}
					break;
				case PUT:
					if (strikePrice < price && strikePrice > closestStrike) {
						closestStrike = strikePrice;
					}
				default:
					continue;
				}
			}
		}

		Option soonestExpiry = null;

		for (Option option : optionChain) {
			if (option.getOptionType().equals(optionType) && option.getStrikePrice() == closestStrike) {
				if (soonestExpiry == null || option.getExpiryDate().before(soonestExpiry.getExpiryDate())) {
					soonestExpiry = option;
				}
			}
		}

		return soonestExpiry;
	}

}
