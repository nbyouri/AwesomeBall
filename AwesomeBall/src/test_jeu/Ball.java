package test_jeu;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Color;
import java.awt.Rectangle;
//import java.awt.event.KeyEvent;

//import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class Ball extends Rectangle.Double {
	private Image img;
	private double dx;
	private double dy;
	private double rotation;

	public Ball() {  
		// load image
		ImageIcon ii = new ImageIcon(this.getClass().getResource("images/craft.png"));
		img = ii.getImage();

		// Calculate rectangle width and height from image
		this.width = ii.getIconWidth();
		this.height = ii.getIconHeight();

		// Normal position by default
		this.rotation = 0;

		// initial position
		this.x = 60;
		this.y = 80;
	}

	public double getDx() {
		return dx;
	}


	public void setDx(double dx) {
		this.dx = dx;
	}


	public double getDy() {
		return dy;
	}


	public void setDy(double dy) {
		this.dy = dy;
	}


	public void setImg(Image img) {
		this.img = img;
	}


	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public Image getImage() {
		return img;
	}

	public void setLocation(double x, double y) {
		this.setRect(x, y, this.getWidth(), this.getHeight());
	}
	
	// move ball in rectangle
	public void move(Field r) {
		if (insideRect(r)){
			this.setLocation(this.getX() + this.getDx(),
					this.getY() + this.getDy());
		}

		if (this.touchRectLeft(r))
			this.setLocation(this.getX() - this.getDx(), this.getY());

		if (this.touchRectTop(r))
			this.setLocation(this.getX(), this.getY() - this.getDy());

		if (this.touchRectRight(r))
			this.setLocation(this.getX() - this.getDx(), this.getY());

		if (this.touchRectBottom(r))
			this.setLocation(this.getX(), this.getY() - this.getDy());

	}

	// draw rectangle and ball
	public void draw(Graphics2D g2) {
		g2.drawImage(this.getImage(), 
				(int)this.getX(), 
				(int)this.getY(), 
				(int)this.getWidth(), 
				(int)this.getHeight(), null);
		g2.setColor(Color.RED);
		//g2.draw(this);
	}

	// rotate the player
	public void rotate(Graphics2D g2, double degrees) {
		/*AffineTransform transform = new AffineTransform();
    	transform.rotate(Math.toRadians(degrees), 
    			this.getX() + this.getWidth()/2, 
    			this.getY() + this.getHeight()/2);
    	g2.transform(transform);*/
		g2.translate(this.getWidth() / 2, this.getHeight() /  2);
		g2.rotate(Math.toRadians(degrees));
		g2.draw(this);
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
			ids.add(0);
		} 
		if (this.approachesRightSide(r)) {
			ids.add(2);
		} 
		if (this.approachesTopSide(r)) {
			ids.add(1);
		} 
		if (this.approachesBottomSide(r)) {
			ids.add(3);
		}

		return ids;
	}
}