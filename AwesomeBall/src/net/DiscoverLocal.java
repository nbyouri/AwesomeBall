package net;

import java.net.InetAddress;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class DiscoverLocal {
	public static String getSubnet(InetAddress address) {
		String host = address.getHostAddress();
		String[] ip = host.split("\\.");
		StringBuilder ret = new StringBuilder();

		ret.append(ip[0] + "." + ip[1] + "." + ip[2]);
		return ret.toString();
	}  

	public static ArrayList<InetAddress> checkHosts(int port) throws Exception {
		ArrayList<InetAddress> ips = new ArrayList<InetAddress>();
		String subnet = DiscoverLocal.getSubnet(InetAddress.getLocalHost());
		InetAddress ip = null;
		int timeout=50;
		for (int i = 1; i <254; i++){
			String host=subnet + "." + i;
			try {
				ip = InetAddress.getByName(host);
				if (ip.isReachable(timeout)){
					ips.add(ip);
				}
			} catch (Exception e) {
				continue;
			}
		}
		return ips;
	}

	public static String selectIP() throws Exception {
		JDialog dialog = new JDialog();
		JLabel label = new JLabel("Please wait...");
		dialog.setLocationRelativeTo(null);
		dialog.setTitle("Please Wait...");
		dialog.add(label);
		dialog.setVisible(true);
		
		ArrayList<InetAddress>addresses = DiscoverLocal.checkHosts(7331);
		dialog.setVisible(false);
		
		ArrayList<String>ips = new ArrayList<String>();
		
		for (int i = 0; i < addresses.size(); i++) {
			ips.add(addresses.get(i).getHostAddress());
		}
		
		Object[] ipsarray = ips.toArray();
		String ip = (String)JOptionPane.showInputDialog(null, "Choose IP", 
				"NET", JOptionPane.YES_OPTION, null, ipsarray, ipsarray[0]);
		
		return ip;
	}
}