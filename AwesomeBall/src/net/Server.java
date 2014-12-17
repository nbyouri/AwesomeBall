package net;

import java.net.*;
import java.io.*;

public class Server extends ServerSocket implements Runnable {
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public Server(int outport) throws Exception {
		super(outport, 50, InetAddress.getLocalHost());
		oos = null;
		socket = null;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public void send(PlayerPacket pp) throws IOException {
		if (oos == null) {
			oos = new ObjectOutputStream(socket.getOutputStream());
		}
		if (socket.isConnected() && oos != null) {
			oos.writeObject(pp);
			oos.reset();
		}
	}


	public PlayerPacket read() throws Exception {
		if (ois == null)  {
			ois = new ObjectInputStream(socket.getInputStream());
		}
		if (socket.isConnected() && ois != null) {
			return (PlayerPacket)ois.readObject();
		} else {
			return null;
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
		}
	}
}