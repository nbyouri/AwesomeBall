package net;

import gui.*;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

/**
 * DiscoverLocal permet de trouver les machines actives sur le reseau local sur
 * base de leur IPv4.
 * 
 * @author youri
 *
 */
@SuppressWarnings("serial")
public class DiscoverLocal extends Dialog implements ActionListener,
		PropertyChangeListener {
	private static ArrayList<InetAddress> addresses;
	private Button startButton;
	private Button exitButton;
	private JProgressBar progressBar;
	private static String ip;

	public static final int MAX_IP = 10;
	public static final int MIN_IP = 2;
	public static final int PING_TIMEOUT = 200;

	/**
	 * Constructeur initialisant un dialog, un bouton start, une bare de
	 * progres.
	 */
	public DiscoverLocal() {
		super("Scanning Local Network.");

		startButton = new Button("Start", this);
		startButton.setActionCommand("start");
		startButton.addActionListener(this);

		progressBar = new JProgressBar(0, MAX_IP);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		exitButton = new Button("<html>Scanning Local Network. "
				+ "<br>Click to cancel and exit</html>", new CloseListener());

		add(progressBar, BorderLayout.SOUTH);
		// add(startButton, BorderLayout.NORTH);
		add(exitButton);
	}

	/**
	 * Retourne le sous-reseau sur base d'une IPv4.
	 * 
	 * @param address
	 *            String
	 * @return String
	 */
	public static String getSubnet(InetAddress address) {
		String host = address.getHostAddress();
		String[] ip = host.split("\\.");
		StringBuilder ret = new StringBuilder();

		ret.append(ip[0] + "." + ip[1] + "." + ip[2]);
		return ret.toString();
	}

	/**
	 * Ping toutes les IP du sous-réseau et ajoute les IP qui repondent dans un
	 * arraylist et incrémente la barre de progres
	 * 
	 * @throws Exception
	 *             Exception
	 */
	public void checkHosts() throws Exception {
		addresses = new ArrayList<InetAddress>();
		String subnet = DiscoverLocal.getSubnet(InetAddress.getLocalHost());
		InetAddress ip = null;
		int timeout = PING_TIMEOUT;
		progressBar.setValue(0);
		for (int i = MIN_IP; i < MAX_IP; i++) {
			String host = subnet + "." + i;
			try {
				ip = InetAddress.getByName(host);
				if (ip.isReachable(timeout)) {
					addresses.add(ip);
				}
			} catch (Exception e) {
				continue;
			}
			progressBar.setValue(i);
		}
	}

	/**
	 * Dialogue permettant de selectionner des ip detectees par checkHosts(). On
	 * utilise un LinkedHashList pour retirer les addresses en double et
	 * l'addresse du routeur et locale sont omis.
	 * 
	 * @return String
	 * @throws Exception
	 *             Exception
	 */
	public String selectIP() throws Exception {
		ArrayList<String> ips = new ArrayList<String>();

		// add found ips to a string arraylist
		for (int i = 0; i < addresses.size(); i++) {
			if (!addresses.get(i).equals(InetAddress.getLocalHost())) {
				ips.add(addresses.get(i).getHostAddress());
			}
		}

		// remove duplicates in string arraylist
		LinkedHashSet<String> hs = new LinkedHashSet<String>();
		hs.addAll(ips);
		ips.clear();
		ips.addAll(hs);

		// show options
		Object[] ipsarray = ips.toArray();
		String ip = (String) JOptionPane.showInputDialog(null, "Choose IP",
				"NET", JOptionPane.YES_OPTION, null, ipsarray, ipsarray[0]);

		// return selected options
		return ip;
	}

	/**
	 * Un SwingWorker permet de lancer un processus une fois dans le fond et
	 * d'executer ce qu'on veut quand le processus a finit. Dans notre cas, il
	 * permet de travailler en mettant a jour notre barre de progres swing.
	 * 
	 * @author youri
	 *
	 */
	class task extends SwingWorker<Void, Void> {
		@Override
		protected Void doInBackground() throws Exception {
			checkHosts();
			return null;
		}

		@Override
		protected void done() {
			setVisible(false);
			setCursor(null);
		}
	};

	/**
	 * Change la valeur de la barre de progres
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
		}
	}

	/**
	 * Démarre le SwingWorker
	 */
	public void actionPerformed(ActionEvent evt) {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		task t = new task();
		t.addPropertyChangeListener(this);
		t.execute();
	}

	/**
	 * Exit button action implementation
	 */
	private class CloseListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}

	/**
	 * Initialise un DiscoverLocal et récupère une ip depuis un dialogue.
	 * 
	 * @return ip : String
	 */
	public static String getIp() {
		DiscoverLocal swt = new DiscoverLocal();
		// XXX: ugly hack
		swt.startButton.doClick();
		swt.setVisible(true);

		if (addresses.size() == 0) {
			Dialog d = new Dialog("");
			swt.exitButton.setText("<html><center>" + "Failed to find IPs"
					+ "<br>" + "Click to Exit" + "<center><html>");
			d.add(swt.exitButton);
			d.setVisible(true);
		}

		try {
			ip = swt.selectIP();
		} catch (Exception e) {
		}

		return ip;
	}
}