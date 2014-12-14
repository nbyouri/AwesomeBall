package test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetTests  {
	public static void main(String[] args) throws Exception {
		System.out.println(InetAddress.getLocalHost());
		// won't return 192.168.1.8's hostname. we need a library for that
		InetAddress addr8 = InetAddress.getByName("192.168.1.8");
		System.out.println(addr8.getHostName() + " => " + addr8.getHostAddress());
		
		InetAddress addr = InetAddress.getByName("74.125.67.100");
	    System.out.println("Host name is: "+addr.getHostName());
	    System.out.println("Ip address is: "+ addr.getHostAddress());
	    InetAddress address = null;
	    try {
	      address = InetAddress.getByName("www.java2s.com");
	    } catch (UnknownHostException e) {
	      System.exit(2);
	    }
	    System.out.println(address.getHostName() + "="
	        + address.getHostAddress());
	  //Get an instance of InetAddress for the IP address  
	    InetAddress inetAddress = InetAddress.getByName("208.29.194.106");  
	      
	    //Get the host name  
	    String ipAddress = inetAddress.getHostName();  
	      
	    //Print the host name  
	    System.out.println(ipAddress);  
	    System.exit(0);
	}
}
