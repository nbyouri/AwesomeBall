package net;

import java.io.IOException;

import javax.swing.JOptionPane;

public class initServer implements Runnable {
	private Server serv;
	private Client client;
	private String address;
	private int inport;
	private int outport;

	public static final int IN_PORT = 1337;
	public static final int OUT_PORT = 7331;

	public initServer() throws IOException {
		inport = IN_PORT;
		outport = OUT_PORT;

		int type = JOptionPane.showConfirmDialog(null, "Êtes vous un serveur ?");
		if (type == JOptionPane.CANCEL_OPTION) {
			System.exit(0);
		}
		boolean host = (type == 0);

		/*
		 * 
		 * If we're the second player, invert the ports.
		 * 
		 */
		if (!host) {
			int temp = inport;
			inport = outport;
			outport = temp;
		}

		try {
			serv = new Server(outport);
		} catch (Exception e) {
			System.out.println("Failed to connect server socket to " + "/" + outport);
			System.exit(1);
		}

		Thread servth = new Thread(serv);
		servth.start();

		address = DiscoverLocal.getIp();
		
		client = new Client(address, inport);
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
		} catch (Exception e) {
			System.out.println("Failed to close socket");
		}
	}

	public void run() {

	}
}