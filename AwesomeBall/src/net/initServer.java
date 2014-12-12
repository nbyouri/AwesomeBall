package net;

import java.io.IOException;

import javax.swing.JOptionPane;

public class initServer implements Runnable {
	private Server serv;
	private Client client;
	private int inport;
	private int outport;

	public static final int IN_PORT = 1337;
	public static final int OUT_PORT = 7331;

	public initServer() throws IOException {
		inport = IN_PORT;
		outport = OUT_PORT;

		int type = JOptionPane.showConfirmDialog(null, "Êtes vous un serveur ?");
		boolean host = (type == 0);
		
		if (!host) {
			int temp = inport;
			inport = outport;
			outport = temp;
		}

		serv = new Server(outport);
		Thread servth = new Thread(serv);
		servth.start();
		
		client = new Client(inport);
		Thread clienth = new Thread(client);
		clienth.start();
	}

	public Server getServ() {
		return serv;
	}

	public Client getClient() {
		return client;
	}

	public void closeSocket(){
		try {
			if (serv.getSocket() != null) {
				serv.getSocket().close();
			}

			if (serv != null) {
				serv.close();
			}
			
			/*if (client != null) {
				client.close();
			}*/
			
		} catch (Exception e) {
			System.out.println("Failed to close socket");
		}
	}
	
	public void run() {
	
	}
}