package geo;

import gui.Images;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class Player extends Field {
	private Images img;
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
		try { 
			img = new Images();
		} catch (IOException ioex) {
			System.out.println("couldn't load image");
			System.exit(0);
		}

		// calculate rectangle width and height from image
		this.width = img.getWidth();
		this.height = img.getHeight();

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
		g2.drawImage(img.getPlayer(), 
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
			ImageIcon ii = new ImageIcon(img.getPlayer());
			BufferedImage blankCanvas = new BufferedImage(ii.getIconWidth(),
					ii.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = (Graphics2D)blankCanvas.getGraphics();

			// rotate around the center
			g2.rotate(Math.toRadians(rotation), ii.getIconWidth() / 2, ii.getIconHeight() / 2);
			g2.drawImage(img.getPlayer(), 0, 0, null);
			img.setPlayer(blankCanvas);
		}
	}
	
	// flip image around vertical axis
	public void flip(int rotation) {
		if (this.getRotation() != rotation) {
			ImageIcon ii = new ImageIcon(img.getPlayer());
			BufferedImage blankCanvas = new BufferedImage(ii.getIconWidth(),
					ii.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = (Graphics2D)blankCanvas.getGraphics();
			// flip vertically
			g2.drawImage(img.getPlayer(), (int)this.getWidth(), 0, 0, (int)this.getHeight(),
					0, 0, (int)this.getWidth(), (int)this.getHeight(), null);
			img.setPlayer(blankCanvas);
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
	
}