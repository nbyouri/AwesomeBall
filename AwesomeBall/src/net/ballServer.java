package net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class ballServer implements Runnable {
	public byte[] oldb = null;
	public InetAddress address = null;

	final public static int PORT = 1234;

	public void run() {
		DatagramPacket dp = null;
		DatagramSocket ds = null;

		try {
			ds = new DatagramSocket(1234, InetAddress.getLocalHost());
			ds.setSoTimeout(1000);
		} catch (UnknownHostException e) {
			System.out.println("unknown host");
		} catch (SocketException e) {
			System.out.println("socket exception");
		}


		while (true) {
			try {
				byte[] buf = new byte[100];
				dp = new DatagramPacket(buf, buf.length, address, PORT);

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

	public void setAddr(InetAddress ad) {
		address = ad;
	}

	public byte[] getBytes() {
		return oldb;
	}
}
