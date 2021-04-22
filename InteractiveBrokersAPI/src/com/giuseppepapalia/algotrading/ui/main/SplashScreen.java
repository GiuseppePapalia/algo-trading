package com.giuseppepapalia.algotrading.ui.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.giuseppepapalia.algotrading.ibkr.InteractiveBrokersClient;
import com.giuseppepapalia.algotrading.ui.util.IconFactory;

public class SplashScreen extends JFrame {

	private static final long serialVersionUID = -2383405811428507600L;

	private Dashboard mainFrame;
	private JButton liveTrading;
	private JButton paperTrading;

	public SplashScreen(Dashboard frame) {
		this.mainFrame = frame;
		populate();
	}

	public void populate() {
		setSize(100, 1000);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(new IconFactory().createImageIcon("resources/dashboard_icon.png").getImage());
		setLocationRelativeTo(null);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(new JLabel("Welcome to Interactive Brokers Dashboard", SwingConstants.CENTER), BorderLayout.NORTH);
		panel.add(new JLabel("Select your mode:"), BorderLayout.WEST);
		this.liveTrading = new JButton("Live Trading");
		this.paperTrading = new JButton("Paper Trading");
		liveTrading.setEnabled(true);
		paperTrading.setEnabled(true);
		liveTrading.addActionListener(action);
		paperTrading.addActionListener(action);
		panel.add(liveTrading, BorderLayout.CENTER);
		panel.add(paperTrading, BorderLayout.EAST);
		getContentPane().add(panel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private ActionListener action = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg) {

			InteractiveBrokersClient client = new InteractiveBrokersClient(false, "DU1919358");
			mainFrame.setClient(client);
			setVisible(false);
			dispose();
			mainFrame.populate();
		}

	};

}