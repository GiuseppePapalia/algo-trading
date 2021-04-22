package com.giuseppepapalia.algotrading.ui.menu;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import com.giuseppepapalia.algotrading.ibkr.InteractiveBrokersClient;
import com.giuseppepapalia.algotrading.ui.util.IconFactory;
import com.giuseppepapalia.algotrading.ui.util.Settings;

public class MainMenu extends JMenuBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7486665047058598737L;

	private InteractiveBrokersClient client;

	public MainMenu(InteractiveBrokersClient client, Settings settings) {
		this.client = client;

		setBorderPainted(true);
		setBorder(BorderFactory.createBevelBorder(500));
		add(new NotificationCheckbox(settings));
		IconFactory f = new IconFactory();
		JMenu notifSettings = new JMenu("Notification Settings");
		notifSettings.setIcon(f.createImageIcon("resources/settings.png"));
		add(notifSettings);
		JMenu watchlist = new JMenu("Watchlist");
		watchlist.setIcon(f.createImageIcon("resources/research_tools.png"));
		watchlist.addMouseListener(watchlistAction);
		add(watchlist);
	}

	private MouseListener watchlistAction = new MouseListener() {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			new WatchlistMenu(client);

		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

	};

}
