package com.giuseppepapalia.algotrading.ui.quotes;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import javax.swing.JTextField;

import com.giuseppepapalia.algotrading.ibkr.CurrencyValue;
import com.giuseppepapalia.algotrading.ibkr.Position;
import com.giuseppepapalia.algotrading.ibkr.Quote;
import com.giuseppepapalia.algotrading.ibkr.Watchlist;

public class QuoteWatcher extends Thread {

	private Watchlist watchlist;
	private QuoteTableModel model;
	private JTextField txtPL;
	private JTextField txtClosePL;
	private QuoteTable table;

	public QuoteWatcher(Watchlist watchlist, QuoteTableModel model, QuoteTable table, JTextField txtPL, JTextField txtClosePL) {
		this.watchlist = watchlist;
		this.model = model;
		this.txtPL = txtPL;
		this.table = table;
		this.txtClosePL = txtClosePL;

		for (Position pos : watchlist.getPortfolio().getPositions()) {
			Object[] o = { pos };
			model.addRow(o);
		}

	}

	@Override
	public void run() {
		while (true) {

			List<Position> newPos = watchlist.getPortfolio().getPositions();
			Map<Position, Quote> quoteMap = watchlist.getQuoteMap();
			if (newPos.size() != model.getRowCount()) { // New Positions - Rebuild the table model
				for (int i = 0; i < model.getRowCount(); i++) {
					model.removeRow(i);
				}
				table.refreshFromModel();
				for (int i = 0; i < newPos.size(); i++) {
					Position pos = newPos.get(i);
					Object[] o = { pos };
					model.addRow(o);

					Quote quote = quoteMap.get(pos);

					model.setValueAt(new CurrencyValue(computeAvgPrice(quote)), i, 1);
					// if (pos.getUnderlying() instanceof Option) {
					// model.setValueAt(new DollarValue((computeAvgPrice(quote) - pos.getAvgEntryPrice()) * 100 * pos.getOpenQuantity()), i, 2);
					// } else {
					model.setValueAt(new CurrencyValue((computeAvgPrice(quote) - pos.getAvgCost()) * pos.getQuantity()), i, 2);
					// }
					model.setValueAt(new CurrencyValue(quote.getBid()) + " / " + new CurrencyValue(quote.getAsk()), i, 3);
					model.setValueAt(quote.getBidSize() + " / " + quote.getAskSize(), i, 4);
				}

			} else {
				for (int i = 0; i < model.getRowCount(); i++) {
					Position pos = (Position) model.getValueAt(i, 0);
					Quote quote = quoteMap.get(pos);

					model.setValueAt(new CurrencyValue(computeAvgPrice(quote)), i, 1);
					// if (pos.getUnderlying() instanceof Option) {
					// model.setValueAt(new DollarValue((computeAvgPrice(quote) - pos.getAvgEntryPrice()) * 100 * pos.getOpenQuantity()), i, 2);
					// } else {
					model.setValueAt(new CurrencyValue((computeAvgPrice(quote) - pos.getAvgCost()) * pos.getQuantity()), i, 2);
					// }
					model.setValueAt(new CurrencyValue(quote.getBid()) + " / " + new CurrencyValue(quote.getAsk()), i, 3);
					model.setValueAt(quote.getBidSize() + " / " + quote.getAskSize(), i, 4);
				}
			}

			int j = QuoteTableModel.PL_COL;
//			double sum = 0;
//			for (int i = 0; i < model.getRowCount(); i++) {
//				sum += ((CurrencyValue) model.getValueAt(i, j)).getValue();
//			}
			CurrencyValue unrealizedPnL = watchlist.getPortfolio().getUnrealizedPnL();
			txtPL.setText(unrealizedPnL.toString());
			txtPL.setForeground(unrealizedPnL.getValue() >= 0 ? Color.GREEN : Color.RED);

			CurrencyValue realizedPnL = watchlist.getPortfolio().getRealizedPnL();
			txtClosePL.setText(realizedPnL.toString());
			txtClosePL.setForeground(realizedPnL.getValue() >= 0 ? Color.GREEN : Color.RED);

			table.refreshFromModel();

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private Double computeAvgPrice(Quote quote) {
		return (quote.getAsk() + quote.getBid()) / 2;
	}
}
