package com.giuseppepapalia.algotrading.ibkr;

import com.giuseppepapalia.algotrading.ibkr.constants.OptionType;
import com.giuseppepapalia.algotrading.ibkr.security.Option;
import com.ib.client.Contract;

public class TradingClient {

	private InteractiveBrokersCore api;
	private Portfolio portfolio;

	public TradingClient(boolean liveTrading, String accID) {
		this.portfolio = new Portfolio(accID);
		this.api = new InteractiveBrokersCore(liveTrading, portfolio);
	}

	public Option getShortTermOption(Contract contract, OptionType optionType) {
		return api.getShortTermOption(contract, optionType);
	}

	public void watchStock(Contract contract) {
		api.watchStock(contract);
	}

	public Portfolio getPortfolio() {
		return portfolio;
	}

}
