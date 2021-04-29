package com.giuseppepapalia.algotrading.ibkr;

import java.util.ArrayList;
import java.util.List;

import com.ib.client.Contract;

public class Portfolio {

	private final String accID;
	private List<Position> positions;
	private CurrencyValue cashBalance;
	private CurrencyValue unrealizedPnL;
	private CurrencyValue realizedPnL;

	public Portfolio(String accID) {
		this.accID = accID;
		positions = new ArrayList<Position>();
		unrealizedPnL = new CurrencyValue(0);
		realizedPnL = new CurrencyValue(0);
		cashBalance = new CurrencyValue(0);
	}

	public void setCashBalance(double cashBalance) {
		this.cashBalance = new CurrencyValue(cashBalance);
	}

	public void setUnrealizedPnL(double unrealizedPnL) {
		this.unrealizedPnL = new CurrencyValue(unrealizedPnL);
	}

	public void setRealizedPnL(double realizedPnL) {
		this.realizedPnL = new CurrencyValue(realizedPnL);
	}

	/*
	 * Returns the position which was updated, null if the position no longer exists 
	 */
	public Position updatePosition(Contract contract, double quantity, double avgCost) {
		boolean foundExistingPosition = false;
		Position toRemove = null;
		for (Position position : positions) {
			if (contract.equals(position.getContract())) {
				foundExistingPosition = true;
				if (quantity == 0) {
					toRemove = position;
				} else {
					position.setQuantity(quantity);
					position.setAvgCost(avgCost);
					return position;
				}
				break;
			}
		}

		if (toRemove != null) {
			positions.remove(toRemove);
			return null;
		}

		if (!foundExistingPosition && quantity != 0) {
			Position position = new Position(contract, quantity, avgCost);
			positions.add(position);
			return position;
		}

		return null;
	}

	public List<Position> getPositions() {
		return positions;
	}

	public CurrencyValue getUnrealizedPnL() {
		return unrealizedPnL;
	}

	public CurrencyValue getRealizedPnL() {
		return realizedPnL;
	}

	public CurrencyValue getCashBalance() {
		return cashBalance;
	}

	public String getAccID() {
		return accID;
	}

	@Override
	public String toString() {
		return "Cash: " + cashBalance + "\nPositions: " + positions.toString();
	}

}
