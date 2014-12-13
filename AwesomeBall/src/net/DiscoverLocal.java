package net;

import java.net.InetAddress;
import java.net.Socket;

public class DiscoverLocal {
	public static String getSubnet(InetAddress address) {
		String host = address.getHostAddress();
        String[] ip = host.split("\\.");
        StringBuilder ret = new StringBuilder();
        
        ret.append(ip[0] + "." + ip[1] + "." + ip[2]);
        return ret.toString();
    }  
	
	public static InetAddress checkHosts(int port) throws Exception {
		String subnet = DiscoverLocal.getSubnet(InetAddress.getLocalHost());
		InetAddress ip = null;
		Socket so = null;
		int timeout=100;
		while (ip == null) {
			for (int i=1;i<254;i++){
				String host=subnet + "." + i;
				System.out.println("trying : " + host);
				try {
					ip = InetAddress.getByName(host);
				} catch (Exception e) {
					System.out.println("discovery error");
				}
				if (ip.isReachable(timeout)){
					try {
						so = new Socket(host, port);
					} catch (Exception e) {
					}
					if (so != null) {
						if (so.isConnected()) {
							return ip;
						}
					}
				}
			}
		}
		return ip;
	}

//	public static void main(String []args) throws Exception {
//		InetAddress iparray = DiscoverLocal.checkHosts(DiscoverLocal.getSubnet(InetAddress.getLocalHost()), 7331);
//		System.out.println(iparray.getHostAddress() + " = reachable");
//		System.exit(0);
//	}
}