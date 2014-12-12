package net;

import java.io.IOException;

public class initServer {
	private Server serv;
	private Client client;
	private boolean host;

	public initServer(boolean host) throws IOException {
		this.host = host;
		this.newNet();
		this.waitSocket();
	}

	public Server getServ() {
		return serv;
	}

	public Client getClient() {
		return client;
	}

	public void newNet() throws IOException {
		if (host) {
			serv = new Server();
			client = null;
		} else {
			client = new Client();
			serv = null;
		}
	}

	public void waitSocket() throws IOException {
		boolean connect = false;
		while(!connect){
			if (host){
				if (serv.getSocket() != null){
					connect = true;
				}
			} else {
				if (client.isConnected()){
					connect = true;
				}
			}
		}
	}

	public void closeSocket(){
		try {
			if (host) {
				serv.getSocket().close();
				serv.close();
			} else {
				client.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean getHost() {
		return this.host;
	}
}