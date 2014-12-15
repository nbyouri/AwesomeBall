package geo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Keys extends Shape {
	private ArrayList<Integer> pressedkeys;

	// Constances
	public static final int KEY_OFF = 0;
	public static final int KEY_ON = 1;
	public static final int KEY_LEFT = 0;
	public static final int KEY_UP = 1;
	public static final int KEY_RIGHT = 2;
	public static final int KEY_DOWN = 3;

	public Keys(double x, double y, double width, double height) {
		super(x, y, width, height);
		pressedkeys = new ArrayList<Integer>();

		// initialize arrow keys
		pressedkeys.add(KEY_OFF); // KEY_UP
		pressedkeys.add(KEY_OFF); // KEY_LEFT
		pressedkeys.add(KEY_OFF); // KEY_RIGHT
		pressedkeys.add(KEY_OFF); // KEY_DOWN
	}

	/**
	 * Récupère toutes les touches directionnelles appuyées
	 * 
	 * @return pressedkeys : ArrayList
	 */
	public ArrayList<Integer> getPressedKeys() {
		return pressedkeys;
	}

	/**
	 * Modifie toutes les touches directionnelles appuyées
	 * 
	 * @param pressedkeys
	 *            : ArrayList
	 */
	public void setPressedKeys(ArrayList<Integer> pressedkeys) {
		this.pressedkeys = pressedkeys;
	}

	/**
	 * Récupère la touche directionnelle appuyée
	 * 
	 * @param i
	 *            de 0 à 3 , déterminée par les constances de la classe Keys
	 *            UP,DOWN,LEFT,RIGHT
	 * @return int
	 */
	public int getPressedKey(int i) {
		return this.pressedkeys.get(i);
	}

	/**
	 * Change la touche directionnelle appuyée
	 * 
	 * @param i
	 *            de 1 à 3 , déterminée par les constances de la classe Keys UP,
	 *            DOWN, LEFT,RIGHT
	 * @param k
	 *            de 0 à 1 , 0 = OFF, 1 = ON
	 */
	public void setPressedKey(int i, int k) {
		this.pressedkeys.set(i, k);
	}

	/**
	 * Dessine la case des touches directionnelles
	 * 
	 * @param g2
	 *            Graphics2D
	 */
	public void draw(Graphics2D g2) {
		this.setSides();
		g2.setColor(Color.cyan);
		g2.draw(this);
	}

	/**
	 * Dessine les côtés de la case des touches directionnnelles.
	 * 
	 * @param g2
	 *            Graphic2D
	 * @param ar
	 *            L'array list des touches directionnelles appuyées.
	 */
	public void drawSides(Graphics2D g2, ArrayList<Integer> ar) {
		g2.setColor(Color.yellow);

		for (int i = 0; i < ar.size(); i++) {
			if (this.getPressedKey(i) == KEY_ON)
				g2.draw(this.getSide(i));
		}
	}
}
