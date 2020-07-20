package com.giuseppepapalia.ibkr;

import java.util.ArrayList;
import java.util.List;

import com.ib.client.Contract;

public class Portfolio {

	private final String accID;
	private List<Position> positions;
	private CurrencyValue unrealizedPnL;
	private CurrencyValue realizedPnL;

	public Portfolio(String accID) {
		this.accID = accID;
		positions = new ArrayList<Position>();
	}

	public void setUnrealizedPnL(double unrealizedPnL) {
		this.unrealizedPnL = new CurrencyValue(unrealizedPnL);
	}

	public void setRealizedPnL(double realizedPnL) {
		this.realizedPnL = new CurrencyValue(realizedPnL);
	}

	public void position(Contract contract, double quantity, double avgCost) {
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
				}
				break;
			}
		}

		if (toRemove != null) {
			positions.remove(toRemove);
		}

		if (!foundExistingPosition && quantity != 0) {
			positions.add(new Position(contract, quantity, avgCost));
		}
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

	public String getAccID() {
		return accID;
	}

	@Override
	public String toString() {
		return positions.toString();
	}

}
