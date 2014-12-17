package test;

import java.io.ObjectInputStream;
import java.net.Socket;

import com.sun.xml.internal.rngom.parse.compact.EOFException;

import net.PlayerPacket;

public class ClientTest extends Socket implements Runnable {

	private ObjectInputStream ois;
	private PlayerPacket pp;

	public ClientTest() throws Exception {
		super("10.99.3.134", 1337);
		ois = new ObjectInputStream(this.getInputStream());
		pp = (PlayerPacket)ois.readObject();
	}

	public PlayerPacket getPlayer() { 
		return pp;
	}

	public void run() {
		while (this.isConnected()) {
			try {
				Thread.sleep(200);
			} catch (Exception e) {
			}
			if (this != null && this.isConnected() && ois != null) {
				try {
					//Object o = ois.readObject();
					pp = (PlayerPacket)ois.readObject();
					//ois.reset();
					//pp = (PlayerPacket)o;
					System.out.println(pp.toString());
					/*if (o instanceof PlayerPacket) {
						if (!pp.equals(o)) {
							pp = (PlayerPacket)o;
						} else {
							System.out.println(pp.equals(o));
						}
					} else {
						System.out.println("Wrong Object read");
					}*/
				} catch (EOFException e) {
					System.out.println("Server disconnected");
					System.exit(0);
				} catch (Exception e) {
					continue;
				}
			}
		}
		while (isConnected() && isBound()) {
			if (pp == null) {
				continue;
			}
			System.out.println("out <- " + pp.toString());
			try {
				ois.reset();
			} catch (Exception e) {
				System.out.println("failed to reset ois");
			}
		}
	}

	public static void main(String[] args) throws Exception {
		//ClientTest ct = new ClientTest();
		Thread t = new Thread(new ClientTest());
		t.start();
	}
}