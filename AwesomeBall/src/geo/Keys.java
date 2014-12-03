package geo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Keys extends Shape {
	private ArrayList<Integer> pressedkeys;

	// constants
	public static final int KEY_OFF   = 0;
	public static final int KEY_ON    = 1;
	public static final int KEY_LEFT  = 0;
	public static final int KEY_UP    = 1;
	public static final int KEY_RIGHT = 2;
	public static final int KEY_DOWN  = 3;

	public Keys() {
		pressedkeys = new ArrayList<Integer>();

		// initialize arrow keys
		pressedkeys.add(KEY_OFF); // KEY_UP
		pressedkeys.add(KEY_OFF); // KEY_LEFT
		pressedkeys.add(KEY_OFF); // KEY_RIGHT
		pressedkeys.add(KEY_OFF); // KEY_DOWN
	}

	public ArrayList<Integer> getPressedKeys() {
		return pressedkeys;
	}

	public void setPressedKeys(ArrayList<Integer> pressedkeys) {
		this.pressedkeys = pressedkeys;
	}

	public int getPressedKey(int i) {
		return this.pressedkeys.get(i);
	}

	public void setPressedKey(int i, int k) {
		this.pressedkeys.set(i, k);
	}

	public void draw(Graphics2D g2) {
		g2.setColor(Color.cyan);
		g2.draw(this);
	}

	/**
	 * Draw sides of the key box.
	 * @param g2
	 * @param ar
	 */
	public void drawSides(Graphics2D g2, ArrayList<Integer> ar) {
		g2.setColor(Color.yellow);

		for (int i = 0; i < ar.size(); i++) {
			if (this.getPressedKey(i) == KEY_ON) 
				g2.draw(this.getSide(i));
		}
	}
}
