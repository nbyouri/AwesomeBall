package net;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class ballClient {
	public static byte[] oldb = null;
	public static void send(byte[] b, InetAddress ia) throws Exception {
		if (!Arrays.equals(b, oldb)) {
			DatagramSocket ds = new DatagramSocket();
			DatagramPacket dp = new DatagramPacket(b, b.length, 
					ia, ballServer.PORT);

			ds.send(dp);
			ds.close();
		}

		oldb = b;
	}
}
