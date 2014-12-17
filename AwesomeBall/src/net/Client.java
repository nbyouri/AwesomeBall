package net;

import gui.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Client is a Runnable that will try to bind a socket to a given port until it
 * is actually bound every 200 microsecond.
 * 
 * When it binds, it continuously reads data from the socket.
 * 
 * If the socket gets disconnected, a dialog with an exit button will show and
 * exit the program.
 * 
 * @author youri
 *
 */
public class Client implements Runnable {
	private Socket socket;
	private String address;
	private int port;
	private String message;
	private Object objin;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	public Client(String addr, int inport) throws Exception {
		socket = null;
		port = inport;
		address = addr;
		objin = null;
	}

	public Object getObjectIn() {
		return objin;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public void send(PlayerPacket pp) throws IOException {
		if (oos == null) {
			oos = new ObjectOutputStream(socket.getOutputStream());
		}
		if (socket.isConnected()) {
			if (oos != null) {
				oos.writeObject(pp);
				oos.reset();
			}
		}
	}

	public void run() {
		try {
			Thread.sleep(2);
		} catch (Exception e) {
		}

		while (true) {
			try {
				socket = new Socket(address, port);
				if (socket != null && socket.isBound()) {
					System.out.println("connected");
					ois = new ObjectInputStream(socket.getInputStream());
					oos = new ObjectOutputStream(socket.getOutputStream());
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
				// System.out.println("Trying again");
				try {
					Thread.sleep(20);
				} catch (Exception e) {
				}
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

			if (socket != null && socket.isConnected() && ois != null) {
				try {
//					BufferedReader entree = new BufferedReader(
//							new InputStreamReader(socket.getInputStream()));
//					String mes = entree.readLine();
//					if (mes != null) {
//						this.message = mes;
//					}
					try {
						objin = ois.readObject();
					} catch (Exception e) {
						System.out.println("Object read not valid");
					}
				} catch (Exception se) {
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

				}
			}
		}
	}
}