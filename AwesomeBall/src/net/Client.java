package net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client implements Runnable {
	private Socket socket;
	private int port;
	private String message;

	public Client(int inport) {
		port = inport;
		socket = null;
	}

	public String getMessage(){
		return this.message;
	}

	//Socket en ecoute 
	public void run() {
		try { 
			Thread.sleep(20); 
		} catch (Exception e) {}
		
		while (socket == null) {
			try {
				socket = new Socket("127.0.0.1", port);
				if (socket != null) {
					break;
				} else {
					try { 
						Thread.sleep(200); 
					} catch (Exception e) {}
				}
			} catch (Exception e) {}
		}
		
		while (!socket.isClosed()) {

			if (socket != null && socket.isConnected()){
				try {
					BufferedReader entree = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String mes = entree.readLine();
					this.message = mes;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}