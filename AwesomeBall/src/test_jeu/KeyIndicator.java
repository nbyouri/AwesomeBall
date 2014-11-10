package test_jeu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class KeyIndicator extends Field {
	private ArrayList<Integer> pressedkeys;
	
	public KeyIndicator() {
		pressedkeys = new ArrayList<Integer>();
		
		// initialize arrow keys
		pressedkeys.add(0); // 0 == left
		pressedkeys.add(0); // 1 == top
		pressedkeys.add(0); // 2 == right
		pressedkeys.add(0); // 3 == down
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
	
	@Override
	public void drawSides(Graphics2D g2, ArrayList<Integer> ar) {
		g2.setColor(Color.yellow);
		
		for (int i = 0; i < ar.size(); i++) {
			if (this.getPressedKey(i) == 1) 
				g2.draw(this.getSide(i));
		}
	}
}
