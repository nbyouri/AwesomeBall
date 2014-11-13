package test_jeu;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

@SuppressWarnings("serial")
public abstract class Shape extends Rectangle.Double {
	/*
	 *  coordonnees de droite
	 * |--> x,y
	 * |      
	 * +–––––+--> rightx, righty
	 * |     |
	 * |     |
	 * +–––––+--> rightdownx, rightdowny
	 * |
	 * |--> downx, downy
	 * 
	 */
	private double downx;
	private double downy;
	private double rightx;
	private double righty;
	private double rightdownx;
	private double rightdowny;
	
	public Shape() {
		// initial values
		super(0, 0, 0, 0);
	}
	
	public double getRightX() {
		return rightx;
	}


	public void setRightX() {
		this.rightx = x + width;
	}


	public double getRightY() {
		return righty;
	}


	public void setRightY() {
		this.righty = y;
	}
	
	public double getRightDownX() {
		return rightdownx;
	}

	public void setRightDownX() {
		this.rightdownx = rightx;
	}

	public double getRightDownY() {
		return rightdowny;
	}

	public void setRightDownY() {
		this.rightdowny = righty + height;
	}
	
	public double getDownX() {
		return downx;
	}

	public void setDownX() {
		this.downx = x;
	}

	public double getDownY() {
		return downy;
	}

	public void setDownY() {
		this.downy = y + height;
	}

	public void setPoints() {
		this.setDownX();
		this.setDownY();
		this.setRightX();
		this.setRightY();
		this.setRightDownX();
		this.setRightDownY();
	}

	public void setSize(double x, double y, double width, double height) {
		this.setRect(x, y, width, height);
		this.setPoints();
	}

	public abstract void draw(Graphics2D g2);
	public abstract void drawSides(Graphics2D g2, ArrayList<Integer> ar);
}
