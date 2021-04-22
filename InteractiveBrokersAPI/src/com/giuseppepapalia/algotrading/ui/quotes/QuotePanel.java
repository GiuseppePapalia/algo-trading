package com.giuseppepapalia.algotrading.ui.quotes;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.giuseppepapalia.algotrading.ibkr.CurrencyValue;
import com.giuseppepapalia.algotrading.ibkr.Watchlist;
import com.giuseppepapalia.algotrading.ui.util.Settings;

public class QuotePanel extends JPanel {

	private static final long serialVersionUID = -3101206056606595465L;
	private JScrollPane pane;
	private JLabel lblPL;
	private JTextField txtPL;
	private JLabel lblClosePL;
	private JTextField txtClosePL;

	public QuotePanel(Watchlist watchlist, Settings settings) {
		setSize(750, 600);
		setPreferredSize(new Dimension(750, 600));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		QuoteTableModel tableModel = new QuoteTableModel();
		QuoteTable table = new QuoteTable(tableModel);
		pane = new JScrollPane(table);
		pane.setSize(750, 500);
		pane.setPreferredSize(new Dimension(750, 500));
		add(pane);

		JPanel panelPL = new JPanel();
		panelPL.setSize(750, 100);
		panelPL.setPreferredSize(new Dimension(750, 100));
		lblPL = new JLabel("Open P / L");
		lblPL.setPreferredSize(new Dimension(75, 50));
		txtPL = new JTextField(new CurrencyValue(0).toString());
		txtPL.setPreferredSize(new Dimension(100, 50));
		txtPL.setEditable(false);
		txtPL.setHorizontalAlignment(JTextField.CENTER);

		lblClosePL = new JLabel("Closed P / L");
		lblClosePL.setPreferredSize(new Dimension(75, 50));
		txtClosePL = new JTextField(new CurrencyValue(0).toString());
		txtClosePL.setPreferredSize(new Dimension(100, 50));
		txtClosePL.setEditable(false);
		txtClosePL.setHorizontalAlignment(JTextField.CENTER);

		panelPL.add(lblPL);
		panelPL.add(txtPL);
		panelPL.add(lblClosePL);
		panelPL.add(txtClosePL);

		add(panelPL);

		QuoteWatcher watcher = new QuoteWatcher(watchlist, tableModel, table, txtPL, txtClosePL);
		watcher.start();
	}

}
