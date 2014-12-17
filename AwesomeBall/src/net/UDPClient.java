package net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class UDPClient {
	public static byte[] oldb = null;

	public static void send(byte[] b, InetAddress ia, int port)
			throws Exception {
		if (!Arrays.equals(b, oldb)) {
			DatagramSocket ds = new DatagramSocket();
			DatagramPacket dp = new DatagramPacket(b, b.length, ia, port);

			ds.send(dp);
			ds.close();
		}

		oldb = b;
	}
}
