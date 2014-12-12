package net;

import java.net.*;
import java.io.*;

public class Server extends ServerSocket implements Runnable {
	private Socket socket;
	private String msg;
	private BufferedReader in;
	private PrintWriter out;


	public static final int port = 13337;
	public static final String addr = "127.0.0.1";

	public Server() throws IOException {
		super(port);

		try {
			socket = this.accept();
		} catch (Exception e) {
			System.out.println("Failed to accept");
		}
	}

	public String getMessage() {
		return msg;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public synchronized void sendMsg(String msg) throws IOException {
		if (socket != null && socket.isConnected()){
			out = new PrintWriter(socket.getOutputStream());
			out.println(msg);
			out.flush();
		}
	}

	public void run() {
		while (!this.isClosed()){
			if (socket != null && socket.isConnected()){
				try {
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String mes = in.readLine();
					if (mes != null){
						this.msg = mes;
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}