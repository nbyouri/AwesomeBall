package net;

import gui.Button;
import gui.Dialog;

import java.net.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Server extends ServerSocket implements Runnable {
	private Socket socket;
	private String msg;
	private BufferedReader in;
	private PrintWriter out;

	public Server(int outport) throws Exception {
		super(outport, 50, InetAddress.getLocalHost());
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
		if (socket != null && socket.isConnected() && msg != null) {
			out = new PrintWriter(socket.getOutputStream());
			out.println(msg);
			out.flush();
		}
	}

	public void run() {
		while (true) {
			try {
				socket = this.accept();
			} catch (SocketException se) {
				continue;
			}

			catch (SocketTimeoutException ex) {
				System.out.println("Trying to accept");
			}

			catch (IOException ex) {
				ex.printStackTrace();
			}
			while (!this.isClosed()) {
				if (socket != null && socket.isConnected()) {
					try {
						in = new BufferedReader(new InputStreamReader(
								socket.getInputStream()));
						String mes = in.readLine();
						if (mes != null) {
							this.msg = mes;
						}
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

					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			/*
			 * try { Thread.sleep(50); } catch (Exception e) {}
			 */
		}
	}
}