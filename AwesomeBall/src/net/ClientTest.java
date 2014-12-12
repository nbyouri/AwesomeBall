package net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientTest extends Socket implements Runnable {
	
	private String message;
	
	public ClientTest() throws UnknownHostException, IOException {
		super(Server.addr, Server.port);
		Thread t = new Thread(this);
		t.start();
	}
	
	public String getMessage(){
		return this.message;
	}
	
	/**
	 * Methode qui envoie le score sur le socket.
	 * @param score score envoye
	 * @throws IOException
	 */
	public synchronized void sendMsg(String msg) throws IOException{
		if (this.isConnected()){
			PrintWriter sortie = new PrintWriter(this.getOutputStream());
			//System.out.println(msg);
			sortie.println(msg);
			sortie.flush();
		}
	}
	
	//Socket en ecoute 
	public void run() {
		while(!this.isClosed()){
			if (this != null && this.isConnected()){
				try {
					BufferedReader entree = new BufferedReader(
							new InputStreamReader(this.getInputStream()));
					String mes = entree.readLine();
					if (mes != null){
						this.message = mes;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		ClientTest ct = new ClientTest();
		while (ct.isConnected() && ct.getMessage() != null) {
			System.out.println(ct.getMessage());
		}
	}
}