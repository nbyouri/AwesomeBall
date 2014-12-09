package net;

import geo.Player;

import java.net.*;
import java.io.*;

public class Server extends Thread {
	private ServerSocket serverSocket;
	private Player player;

	public static final int port = 1337;
	public static final String address = "localhost";

	public Server(Player p) throws IOException {
		player = p;
		serverSocket = new ServerSocket(port);
	}

	public void run() {
		
		while (true) {
			try {
				
				Socket socket = serverSocket.accept();

				PrintStream ps = new PrintStream(socket.getOutputStream());


				while (true) {
					ps.println(player.toString());
					
					try {
						sleep(100);
					} catch(Exception e) {
						e.printStackTrace();
					}
					
					//ps.close();
				}

				//socket.close();
			} catch(IOException e) {
				e.printStackTrace();
				break;
			}
		}

	} 
}