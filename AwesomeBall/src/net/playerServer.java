package net;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class playerServer implements Runnable {
	public byte[] oldb = null;
	public InetAddress address = null;
	public int port;

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
				byte[] buf = new byte[100];
				dp = new DatagramPacket(buf, buf.length, address, port);

				try {
					if (ds != null) {
						ds.receive(dp);
					}
				} catch (Exception e) {
				}

				if (!Arrays.equals(dp.getData(), oldb)) {
					oldb = dp.getData();
					final ByteArrayInputStream bais = new ByteArrayInputStream(
							oldb);
					final ObjectInputStream dais = new ObjectInputStream(bais);

					bais.close();
					dais.close();
					PlayerPacket pc = (PlayerPacket) dais.readObject();
					System.out.println(pc.toString());
				}

			} catch (Exception ex) {
				continue;
			}
		}
	}

	public void setAddr(InetAddress ad) {
		address = ad;
	}

	public void setPort(int p) {
		port = p;
	}

	public byte[] getBytes() {
		return oldb;
	}
}
