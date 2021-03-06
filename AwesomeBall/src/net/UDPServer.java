package net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class UDPServer implements Runnable {
	public byte[] oldb = null;
	public InetAddress address = null;
	public int port;

	public UDPServer(InetAddress ad, int p) {
		address = ad;
		port = p;
	}
	
	public void run() {
		DatagramPacket dp = null;
		DatagramSocket ds = null;

		try {
			ds = new DatagramSocket(port, InetAddress.getLocalHost());
			ds.setSoTimeout(1000);
		} catch (UnknownHostException e) {
			System.out.println("unknown host");
		} catch (SocketException e) {
			System.out.println("socket exception");
		}

		while (true) {
			try {
				byte[] buf = new byte[1000];
				dp = new DatagramPacket(buf, buf.length, address, port);

				try {
					if (ds != null) {
						ds.receive(dp);
					}
				} catch (Exception e) {
				}

				if (!Arrays.equals(dp.getData(), oldb)) {
					oldb = dp.getData();
				}

			} catch (Exception ex) {
				continue;
			}
		}
	}

	public byte[] getBytes() {
		return oldb;
	}
}
