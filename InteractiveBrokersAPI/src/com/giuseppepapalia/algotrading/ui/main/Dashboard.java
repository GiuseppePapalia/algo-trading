package com.giuseppepapalia.algotrading.ui.main;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.giuseppepapalia.algotrading.ibkr.InteractiveBrokersClient;
import com.giuseppepapalia.algotrading.ui.menu.MainMenu;
import com.giuseppepapalia.algotrading.ui.news.NewsPanel;
import com.giuseppepapalia.algotrading.ui.quotes.QuotePanel;
import com.giuseppepapalia.algotrading.ui.util.IconFactory;
import com.giuseppepapalia.algotrading.ui.util.Settings;

public class Dashboard extends JFrame {

	private JPanel mainPane;
	private InteractiveBrokersClient client;
	/**
	 * 
	 */
	private static final long serialVersionUID = 3774801634601775931L;

	public Dashboard() {
		new SplashScreen(this);
	}

	public void populate() {
		setTitle("Interactive Brokers Dashboard");
		setResizable(false);
		Settings settings = new Settings();
		setJMenuBar(new MainMenu(client, settings));
		mainPane = new JPanel();
		getContentPane().add(mainPane);
		NewsPanel newsPanel = new NewsPanel(settings);
		mainPane.add(newsPanel);
		QuotePanel optionPanel = new QuotePanel(client.getWatchlist(), settings);
		mainPane.add(optionPanel);
		setSize(900, 1000);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		IconFactory f = new IconFactory();
		setIconImage(f.createImageIcon("resources/dashboard_icon.png").getImage());
		setLocationRelativeTo(null);
		setVisible(true);

	}

	public void setClient(InteractiveBrokersClient client) {
		this.client = client;
	}

}
