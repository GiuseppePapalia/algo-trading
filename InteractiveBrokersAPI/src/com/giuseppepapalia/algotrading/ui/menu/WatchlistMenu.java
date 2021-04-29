package com.giuseppepapalia.algotrading.ui.menu;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.giuseppepapalia.algotrading.ibkr.InteractiveBrokersClient;
import com.giuseppepapalia.algotrading.ibkr.InteractiveBrokersFactory;
import com.giuseppepapalia.algotrading.ibkr.InteractiveBrokersCore;
import com.giuseppepapalia.algotrading.ui.util.IconFactory;
import com.ib.client.Contract;

public class WatchlistMenu extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3341890001005445584L;

	private DefaultListModel<String> model;
	private JList<String> list;
	private InteractiveBrokersCore watchlist;
	private InteractiveBrokersClient client;
	private JTextField txtTicker;

	public WatchlistMenu(InteractiveBrokersClient client) {
		this.client = client;
		this.watchlist = client.getWatchlist();
		populate();
	}

	private void populate() {
		setTitle("Watchlist");
		setSize(250, 250);
		setModal(true);
		setResizable(false);
		setIconImage(new IconFactory().createImageIcon("resources/dashboard_icon.png").getImage());
		setLocationRelativeTo(null);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 1));
		model = new DefaultListModel<String>();
		list = new JList<>(model);
		for (Contract contract : watchlist.getList()) {
			model.addElement(contract.symbol());
		}
		panel.add(new JScrollPane(list));
		txtTicker = new JTextField(4);
		txtTicker.setEditable(true);
		txtTicker.setEnabled(true);
		panel.add(txtTicker);
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(addAction);
		panel.add(btnAdd);
		JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(removeAction);
		panel.add(btnRemove);
		getContentPane().add(panel);
		// pack();
		setVisible(true);
	}

	private void repopulateModel() {
		model.removeAllElements();
		for (Contract contract : watchlist.getList()) {
			model.addElement(contract.symbol());
		}
		list.repaint();
	}

	private ActionListener removeAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg) {
			String ticker = txtTicker.getText();
			if (ticker != null) {
				for (Contract contract : watchlist.getList()) {
					if (ticker.equals(contract.symbol())) {
						client.stopWatchingStock(contract);
						repopulateModel();
					}
				}
			}
		}

	};

	private ActionListener addAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg) {
			String ticker = txtTicker.getText();
			if (ticker != null) {
				boolean notAdded = true;
				for (Contract contract : watchlist.getList()) {
					if (ticker.equals(contract.symbol())) {
						notAdded = false;
						break;
					}
				}
				if (notAdded) {
					try {
						Contract c = InteractiveBrokersFactory.createStock(ticker);
						client.watchStock(c);
						repopulateModel();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

	};

}
