package com.giuseppepapalia.ibkr;

import com.ib.client.Contract;

public class Position {

	private Contract contract;
	private double quantity;
	private double avgCost;

	public Position(Contract contract, double quantity, double avgCost) {
		this.contract = contract;
		this.quantity = quantity;
		this.avgCost = avgCost;
	}

	public Contract getContract() {
		return contract;
	}

	public double getQuantity() {
		return quantity;
	}

	public double getAvgCost() {
		return avgCost;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public void setAvgCost(double avgCost) {
		this.avgCost = avgCost;
	}

	@Override
	public String toString() {
		return contract.symbol() + " x " + quantity + " @ " + avgCost;
	}

}
