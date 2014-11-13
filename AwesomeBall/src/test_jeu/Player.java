package test_jeu;

import java.awt.Graphics2D;
import java.awt.Image;
//import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class Player extends Field {
	private Image img;
	private double dx;
	private double dy;
	private int rotation;
	
	// constants
	// ball rotation angles
	public static final int UP = -90;
	public static final int LEFT = 180;
	public static final int RIGHT = 0;
	public static final int DOWN = 90;
	public static final int SPEED_ONE = 1;
	public static final int SPEED_TWO = 2;
	public static final int STOP = 0;
	public static final int INIT_X = 60;
	public static final int INIT_Y = 80;
	public static final int INIT_ROT = 0;
	
	public Player() {  
		// load image
		java.net.URL imgURL = this.getClass().getResource("images/craft2.png");
		if (imgURL == null) {
			System.out.println("Failed to load image.");
			System.exit(0);
		}
		ImageIcon ii = new ImageIcon(imgURL);
		img = ii.getImage();

		// calculate rectangle width and height from image
		this.width = ii.getIconWidth();
		this.height = ii.getIconHeight();

		this.rotation = INIT_ROT;
		
		// initial position
		this.x = INIT_X;
		this.y = INIT_Y;

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

	public Image getImage() {
		return img;
	}

	public void setLocation(double x, double y) {
		this.setSize(x, y, this.getWidth(), this.getHeight());
		this.setSides();
	}
	
	// return normalised angle
	// but don't change 360 to 0
	public int getRotation() {
	    while (rotation <= -180) rotation += 360;
	    while (rotation > 180) rotation -= 360;
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
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
		//g2.setColor(Color.red);
		//g2.draw(this);
	}

	// rotate the image
	public void rotate(int rotation) {
		// only rotate if change of key
		if (this.getRotation() != rotation) {
			ImageIcon ii = new ImageIcon(this.getImage());
			BufferedImage blankCanvas = new BufferedImage(ii.getIconWidth(),
					ii.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = (Graphics2D)blankCanvas.getGraphics();

			// rotate around the center
			g2.rotate(Math.toRadians(rotation), ii.getIconWidth() / 2, ii.getIconHeight() / 2);
			g2.drawImage(this.getImage(), 0, 0, null);
			this.setImg(blankCanvas);
		}
	}
	
	// flip image around vertical axis
	public void flip(int rotation) {
		if (this.getRotation() != rotation) {
			ImageIcon ii = new ImageIcon(this.getImage());
			BufferedImage blankCanvas = new BufferedImage(ii.getIconWidth(),
					ii.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = (Graphics2D)blankCanvas.getGraphics();
			// flip vertically
			g2.drawImage(this.getImage(), (int)this.getWidth(), 0, 0, (int)this.getHeight(),
					0, 0, (int)this.getWidth(), (int)this.getHeight(), null);
			this.setImg(blankCanvas);
		}
	}

	// different cases of rotating and flipping
	public void drawLeft() {
		switch(this.getRotation()) {
		
		case RIGHT:
			this.flip(LEFT);
			break;
		
		case UP:
			this.rotate(DOWN);
			this.flip(LEFT);
			break;
		
		case DOWN:
			this.rotate(UP);
			this.flip(LEFT);
			break;
			
		}
		this.setRotation(LEFT);
	}
	
	public void drawRight() {
		switch(this.getRotation()) {
		
			case LEFT:
				this.flip(RIGHT);
				break;
	
			case UP:
				this.rotate(DOWN);
				break;
				
			case DOWN:
				this.rotate(UP);
				break;
				
		}
		this.setRotation(RIGHT);
	}
	
	public void drawUp() {
		switch(this.getRotation()) {
		
		case LEFT:
			this.rotate(DOWN);
			this.flip(UP);
			break;
		
		case RIGHT:
			this.rotate(UP);
			break;
			
		case DOWN:
			this.rotate(LEFT);
			break;
			
		}
		this.setRotation(UP);
	}
	
	public void drawDown() {
		switch(this.getRotation()) {
		
		case LEFT:
			this.flip(DOWN);
			this.rotate(DOWN);
			break;
		
		case UP:
			this.rotate(LEFT);
			break;
			
		case RIGHT:
			this.rotate(DOWN);
			break;

		}
		this.setRotation(DOWN);
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
}