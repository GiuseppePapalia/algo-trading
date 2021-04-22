package com.giuseppepapalia.algotrading.ibkr;

public class Quote {

	private double bid;
	private double ask;
	private int bidSize;
	private int askSize;

	private boolean empty = true;

	public boolean isEmpty() {
		return empty;
	}

	public double getBid() {
		return bid;
	}

	public void setBid(double bid) {
		empty = false;
		this.bid = bid;
	}

	public double getAsk() {
		return ask;
	}

	public void setAsk(double ask) {
		this.ask = ask;
	}

	public int getBidSize() {
		return bidSize;
	}

	public void setBidSize(int bidSize) {
		this.bidSize = bidSize;
	}

	public int getAskSize() {
		return askSize;
	}

	public void setAskSize(int askSize) {
		this.askSize = askSize;
	}

}
