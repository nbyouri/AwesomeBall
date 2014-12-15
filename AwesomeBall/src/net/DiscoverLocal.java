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

@SuppressWarnings("serial")
public class DiscoverLocal extends Dialog implements ActionListener,
		PropertyChangeListener {
	private static ArrayList<InetAddress> addresses;
	private Button startButton;
	private Button exitButton;
	private JProgressBar progressBar;
	private static String ip;

	public static final int MAX_IP = 254;
	public static final int MIN_IP = 2;
	public static final int PING_TIMEOUT = 100;

	public DiscoverLocal() {
		super("Scanning Local Network.");

		startButton = new Button("Start", this);
		startButton.setActionCommand("start");
		startButton.addActionListener(this);

		progressBar = new JProgressBar(0, MAX_IP);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		exitButton = new Button(
				"Scanning Local Network. \nClick to cancel and exit",
				new CloseListener());
		add(progressBar, BorderLayout.SOUTH);
		// add(startButton, BorderLayout.NORTH);
		add(exitButton);
	}

	public static String getSubnet(InetAddress address) {
		String host = address.getHostAddress();
		String[] ip = host.split("\\.");
		StringBuilder ret = new StringBuilder();

		ret.append(ip[0] + "." + ip[1] + "." + ip[2]);
		return ret.toString();
	}

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

	public String selectIP() throws Exception {
		ArrayList<String> ips = new ArrayList<String>();

		// add found ips to a string arraylist
		for (int i = 0; i < addresses.size(); i++) {
			if (!addresses.get(i).equals(InetAddress.getLocalHost())) {
				ips.add(addresses.get(i).getHostAddress());
			}
		}

		if (ips.size() == 0) {
			System.out.println("No Local IPs Found");
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

	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
		}
	}

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

	public static String getIp() {
		DiscoverLocal swt = new DiscoverLocal();
		// XXX: ugly hack
		swt.startButton.doClick();
		swt.setVisible(true);

		if (addresses.size() == 0) {
			System.out.println("Error Discovering local net");
		}

		try {
			ip = swt.selectIP();
		} catch (Exception e) {
		}

		return ip;
	}
}