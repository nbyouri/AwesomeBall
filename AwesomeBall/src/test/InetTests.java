package test;

import java.net.InetAddress;

public class InetTests  {
	public static void main(String[] args) throws Exception {
		System.out.println(InetAddress.getLocalHost());
		// won't return 192.168.1.8's hostname. we need a library for that
		System.out.println(InetAddress.getByName("192.168.1.8").getLocalHost());
	}
}
