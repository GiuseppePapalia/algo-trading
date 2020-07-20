package com.giuseppepapalia.ibkr;

public class Main {

	public static void main(String args[]) throws InterruptedException {
		InteractiveBrokersClient client = new InteractiveBrokersClient(false, "DY1919358");
		Thread.sleep(20000);
		client.placeLongBracketOrder(InteractiveBrokersFactory.createStock("MSFT"), 5, 165.14, 168.14, 162.14);
		System.exit(0);

	}

}
