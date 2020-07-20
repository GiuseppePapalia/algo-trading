package com.giuseppepapalia.ibkr;

import java.io.IOException;

import com.ib.client.Contract;

public class DeepLearnTest {

	public static void main(String args[]) throws IOException, InterruptedException {
		InteractiveBrokersClient client = new InteractiveBrokersClient(false, "DU1919358");
		Contract c = InteractiveBrokersFactory.createStock("MSFT");
		client.client.reqHistoricalData(157646, c, "20200720 12:00:00 GMT", "1 D", "1 min", "MIDPOINT", 1, 1, false, null);
	}

}
