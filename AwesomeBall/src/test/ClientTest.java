package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import net.initServer;

public class ClientTest extends Socket implements Runnable {
	
	private String message;
	
	public ClientTest() throws UnknownHostException, IOException {
		super("127.0.0.1", initServer.OUT_PORT);
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
		while (ct.isConnected() && ct.isBound()) {
			System.out.println("Waiting for info");
			try {
				Thread.sleep(20);
			} catch (Exception e) {}
			if (ct.getMessage() == null) {
				continue;
			}
			System.out.println("out <- " + ct.getMessage());
			System.out.println(ct.getLocalPort() + " " + ct.getPort() + ct.getRemoteSocketAddress());
		}
	}
}