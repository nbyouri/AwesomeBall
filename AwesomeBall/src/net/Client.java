package net;

import gui.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class Client implements Runnable {
	private Socket socket;
	private String address;
	private int port;
	private String message;

	public Client(String addr, int inport) {
		socket = null;
		port = inport;
		address = addr;
	}

	public String getMessage() {
		return this.message;
	}

	public void run() {
		try {
			Thread.sleep(20);
		} catch (Exception e) {
		}

		while (true) {
			try {
				socket = new Socket(address, port);
				if (socket != null && socket.isBound()) {
					System.out.println("connected");
					break;
				} else {
					System.out.println("no server detected");
					try {
						Thread.sleep(200);
					} catch (Exception e) {
					}
				}
			}

			catch (ConnectException ce) {
				//System.out.println("Trying again");
				continue;
			}

			catch (SocketTimeoutException ex) {
				System.out.println("Trying to connect to " + address + "...");
			}

			catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		while (!socket.isClosed()) {

			if (socket != null && socket.isConnected()) {
				try {
					BufferedReader entree = new BufferedReader(
							new InputStreamReader(socket.getInputStream()));
					String mes = entree.readLine();
					this.message = mes;
				} catch (SocketException se) {
					Dialog d = new Dialog("Player 2 Left");
					ActionListener exit = new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							System.exit(0);
						}
					};
					Button ex = new Button("exit", exit);
					ex.setPreferredSize(new Dimension(d.getWidth(), 50));
					d.add(ex, BorderLayout.SOUTH);
					d.setVisible(true);
					
				} catch (IOException ioex) {
					continue;
				}
			}
		}
	}
}