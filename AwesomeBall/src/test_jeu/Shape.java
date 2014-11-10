package test_jeu;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

@SuppressWarnings("serial")
public abstract class Shape extends Rectangle.Double {
	public Shape() {
		// initial values
		super(0, 0, 0, 0);
	}
	
	public abstract void draw(Graphics2D g2);
	public abstract void drawSides(Graphics2D g2, ArrayList<Integer> ar);
}
