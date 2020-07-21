package com.giuseppepapalia.ibkr;

import java.io.IOException;

import com.ib.client.Contract;

public class DeepLearnTest {

	public static void main(String args[]) throws IOException, InterruptedException {
		InteractiveBrokersClient client = new InteractiveBrokersClient(false, "DU1919358");
		Contract c = InteractiveBrokersFactory.createStock("MSFT");
		Contract c2 = InteractiveBrokersFactory.createStock("TSLA");
		client.watchStock(c);
		client.watchStock(c2);
	}

}
