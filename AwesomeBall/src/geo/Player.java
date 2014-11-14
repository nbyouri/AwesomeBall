package geo;

import gui.Images;

import java.awt.Graphics2D;

@SuppressWarnings("serial")
public class Player extends Field {
	private Images img;

	// constants
	// ball rotation angles
	public static final int UP = -90;
	public static final int LEFT = 180;
	public static final int RIGHT = 0;
	public static final int DOWN = 90;
	public static final int SPEED_ONE = 1;
	public static final int SPEED_TWO = 2;
	public static final int STOP = 0;
	public static final int INIT_X = 400;
	public static final int INIT_Y = 200;
	public static final int INIT_ROT = 0;

	public enum Direction { 
		LEFT (180, "left"), 
		UP   (-90, "up"), 
		RIGHT(0, "right"), 
		DOWN (90, "down");
		
		private final int id;
		private final String name;
		
		private Direction(int s, String n) {
			this.id = s;
			this.name = n;
		}
		
		public int getId() {
			return this.id;
		}
		
		public String getName() {
			return this.name;
		}
		
		public static String getNameFromRotation(int rotation) {
			for (Direction d : Direction.values()) {
				if (d.getId() == rotation) {
					return d.getName();
				}
			}
			return null;
		}
	};
	
	public static String getDirection(int i) {
		return Direction.getNameFromRotation(i);
	}
	
	public Player() {  
		// load image
		try { 
			img = new Images();
		} catch (Exception ioex) {
			System.out.println("couldn't load image");
			System.exit(0);
		}

		// calculate rectangle width and height from image
		this.width = img.getWidth();
		this.height = img.getHeight();

		img.setRotation(INIT_ROT);

		// initial position
		this.x = INIT_X;
		this.y = INIT_Y;

	}

	public Images getImg() {
		return img;
	}

	public void setImg(Images img) {
		this.img = img;
	}

	// draw rectangle and ball
	public void draw(Graphics2D g2) {
		g2.drawImage(img.getPlayer(), 
				(int)this.getX(), 
				(int)this.getY(), 
				(int)this.getWidth(), 
				(int)this.getHeight(), null);
		g2.draw(this);
	}

	// different cases of rotating and flipping
	public void drawLeft() {
		switch(img.getRotation()) {

		case RIGHT:
			img.flip(img.getPlayer(), LEFT);
			break;

		case UP:
			img.rotate(img.getPlayer(), DOWN);
			img.flip(img.getPlayer(), LEFT);
			break;

		case DOWN:
			img.rotate(img.getPlayer(), UP);
			img.flip(img.getPlayer(), LEFT);
			break;

		}
		img.setRotation(LEFT);
	}

	public void drawRight() {
		switch(img.getRotation()) {

		case LEFT:
			img.flip(img.getPlayer(), RIGHT);
			break;

		case UP:
			img.rotate(img.getPlayer(), DOWN);
			break;

		case DOWN:
			img.rotate(img.getPlayer(), UP);
			break;

		}
		img.setRotation(RIGHT);
	}

	public void drawUp() {
		switch(img.getRotation()) {

		case LEFT:
			img.rotate(img.getPlayer(), DOWN);
			img.flip(img.getPlayer(), UP);
			break;

		case RIGHT:
			img.rotate(img.getPlayer(), UP);
			break;

		case DOWN:
			img.rotate(img.getPlayer(), LEFT);
			break;

		}
		img.setRotation(UP);
	}

	public void drawDown() {
		switch(img.getRotation()) {

		case LEFT:
			img.flip(img.getPlayer(), DOWN);
			img.rotate(img.getPlayer(), DOWN);
			break;

		case UP:
			img.rotate(img.getPlayer(), LEFT);
			break;

		case RIGHT:
			img.rotate(img.getPlayer(), DOWN);
			break;

		}
		img.setRotation(DOWN);
	}

}