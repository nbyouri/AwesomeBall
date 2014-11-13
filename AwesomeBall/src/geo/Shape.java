package geo;

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
	
	// precise collision check of the ball relative to the field position
	public Boolean insideRect(Field r) {
		if ((this.touchRectLeft(r)) || (this.touchRectRight(r)) ||
				(this.touchRectTop(r)) || (this.touchRectBottom(r))) {
			return false;
		} else {
			return true;
		}
	}

	// returns true if the ball touches the inner left
	// side of the Field minus the border
	public Boolean touchRectLeft(Field r) {
		return (this.getX() - 1 < r.getX());
	}

	public Boolean approachesLeftSide(Field r) {
		return (this.getX() - 2 < r.getX());
	}

	// returns true if the ball touches the top 
	// side of the Field minus the border
	public Boolean touchRectTop(Field r) {
		return (this.getY() - 1 < r.getY());
	}

	public Boolean approachesTopSide(Field r) {
		return (this.getY() - 2 < r.getY());
	}

	// returns true if the ball touches the inner right 
	// side of the Field minus the border
	public Boolean touchRectRight(Field r) {
		return (this.getX() + this.getWidth() - 
				r.getX() + 1 > r.getWidth());
	}

	public Boolean approachesRightSide(Field r) {
		return (this.getX() + this.getWidth() -
				r.getX() + 2 > r.getWidth());
	}

	// returns true if the ball touches the inner bottom 
	// side of the Field minus the border
	public Boolean touchRectBottom(Field r) {
		return (this.getY() + this.getHeight() -
				r.getY() + 1 > r.getHeight());
	}

	public Boolean approachesBottomSide(Field r) {
		return (this.getY() + this.getHeight() -
				r.getY() + 2 > r.getHeight());
	}

	// detect whether ball is in one of the field rectangle's 4 corners
	// if so, return arraylist of sides indexes
	public ArrayList<Integer> approaches(Field r) {
		ArrayList<Integer> ids = new ArrayList<Integer>();

		if (this.approachesLeftSide(r)) {
			ids.add(Field.SIDE_LEFT);
		} 

		if (this.approachesTopSide(r)) {
			ids.add(Field.SIDE_UP);
		} 

		if (this.approachesRightSide(r)) {
			ids.add(Field.SIDE_RIGHT);
		} 

		if (this.approachesBottomSide(r)) {
			ids.add(Field.SIDE_DOWN);
		}

		return ids;
	}

	public abstract void draw(Graphics2D g2);
	public abstract void drawSides(Graphics2D g2, ArrayList<Integer> ar);
}
