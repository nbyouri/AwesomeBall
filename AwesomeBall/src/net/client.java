package net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;


public class client {
	String data;
	Socket socket;
	public client() {
		try {
			socket = new Socket("localhost", Server.port);
		} catch (Exception e) {
			System.out.println("Failed to connect");
		}
	}
	public static void main(String[] args) {
		try {
			client client = new client();

			InputStreamReader ir =
					new InputStreamReader(client.socket.getInputStream());
			BufferedReader br =
					new BufferedReader(ir);
			PrintStream ps =
					new PrintStream(client.socket.getOutputStream());

			ps.println("Hello to server from client");

			while (true) {
				String msg = br.readLine();

				if (msg != null) {
					System.out.println("< " + msg);
				}

				//ps.close();
			}

			//client.socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
