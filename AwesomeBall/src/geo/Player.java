package geo;

import gui.Images;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

@SuppressWarnings("serial")
public class Player extends Field {
	private Images img;
	private int score;
	private Ellipse2D.Double ell;
	public Boolean left;
	public Boolean right;
	public Boolean up;
	public Boolean down;

	// constants
	public static final int SPEED_ONE = 1;
	// since Dx and Dy can be the same, 
	// pythagore the two as a constant.
	public static final double SPEED_ONE_DIAG = Math.sqrt(2 * SPEED_ONE);
	public static final int SPEED_TWO = 2;
	public static final int STOP = 0;
	public static final int INIT_X = 400;
	public static final int INIT_Y = 200;
	public static final int INIT_ROT = 0;

	/*
	 * 
	 * Structure containing the player's
	 * direction defined by the rotation
	 * and the english name of the rotation.
	 * 
	 */
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

		/*
		 * 
		 * Get the name from rotation
		 * 
		 */
		public static String getName(int rotation) {
			for (Direction d : Direction.values()) {
				if (d.getId() == rotation) {
					return d.getName();
				}
			}
			return null;
		}
	};

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

		// set ellipse for ball collision
		this.ell = new Ellipse2D.Double(
				this.getX(), this.getY(), this.getWidth(), this.getHeight());

		img.setRotation(INIT_ROT);

		// initial position
		this.x = INIT_X;
		this.y = INIT_Y;

		this.up = false;
		this.down = false;
		this.left = false;
		this.right = false;

		this.setSides();

	}

	public Images getImg() {
		return img;
	}

	public void setImg(Images img) {
		this.img = img;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int sore) {
		this.score = sore;
	}


	public Ellipse2D.Double getEll() {
		return ell;
	}

	public void setEll(Ellipse2D.Double ell) {
		this.ell = ell;
	}

	// set ellipse location as well
	@Override
	public void setLocation(double x, double y) {
		this.setRect(x, y, this.getWidth(), this.getHeight());
		this.ell.setFrame(x, y, this.getWidth(), this.getHeight());
	}

	// draw rectangle and ball
	public void draw(Graphics2D g2) {
		this.setRotation();
		g2.drawImage(img.getPlayer(), 
				(int)this.getX(), 
				(int)this.getY(), 
				(int)this.getWidth(), 
				(int)this.getHeight(), null);
		g2.setColor(Color.WHITE);
		g2.draw(this.ell);
	}

	/*
	 * Set rotation from key events
	 */
	public void setRotation() {
		if (left) { 
			setLeft(); 
		}

		if (right) { 
			setRight(); 
		}

		if (up) { 
			setUp(); 
		}

		if (down) { 
			setDown(); 
		}
	}

	/*
	 * 
	 * Draw image left, right, up, down
	 * and rotate and or flip the image
	 * depending on the previous direction.
	 * 
	 */
	public void setLeft() {
		int rotation = img.getRotation();
		if (rotation == Direction.RIGHT.getId()) {
			img.flip(img.getPlayer(), Direction.LEFT.getId());
		} else if (rotation == Direction.UP.getId()) {
			img.rotate(img.getPlayer(), Direction.DOWN.getId());
			img.flip(img.getPlayer(), Direction.LEFT.getId());
		} else if (rotation == Direction.DOWN.getId()) {
			img.rotate(img.getPlayer(), Direction.UP.getId());
			img.flip(img.getPlayer(), Direction.LEFT.getId());
		}
		img.setRotation(Direction.LEFT.getId());
	}

	public void setRight() {
		int rotation = img.getRotation();
		if (rotation == Direction.LEFT.getId()) {
			img.flip(img.getPlayer(), Direction.RIGHT.getId());
		} else if (rotation == Direction.UP.getId()) {
			img.rotate(img.getPlayer(), Direction.DOWN.getId());
		} else if (rotation == Direction.DOWN.getId()) {
			img.rotate(img.getPlayer(), Direction.UP.getId());
		}
		img.setRotation(Direction.RIGHT.getId());
	}

	public void setUp() {
		int rotation = img.getRotation();
		if (rotation == Direction.LEFT.getId()) {
			img.rotate(img.getPlayer(), Direction.DOWN.getId());
			img.flip(img.getPlayer(), Direction.UP.getId());
		} else if (rotation == Direction.RIGHT.getId()) {
			img.rotate(img.getPlayer(), Direction.UP.getId());
		} else if (rotation == Direction.DOWN.getId()) {
			img.rotate(img.getPlayer(), Direction.LEFT.getId());
		}
		img.setRotation(Direction.UP.getId());
	}

	public void setDown() {
		int rotation = img.getRotation();
		if (rotation == Direction.LEFT.getId()) {
			img.flip(img.getPlayer(), Direction.DOWN.getId());
			img.rotate(img.getPlayer(), Direction.DOWN.getId());
		} else if (rotation == Direction.UP.getId()) {
			img.rotate(img.getPlayer(), Direction.LEFT.getId());
		} else if (rotation == Direction.RIGHT.getId()) {
			img.rotate(img.getPlayer(), Direction.DOWN.getId());
		}
		img.setRotation(Direction.DOWN.getId());
	}

	/*
	 * 
	 * Check whether the player is in a goal
	 * 
	 */
	public boolean insideGoals(Field f) {
		return ((this.getMaxX() < f.getGoalright().getMaxX() &&
				this.getY() - 1 >= f.getGoalright().getY()) ||
				(this.getX() > f.getGoalleft().getX() &&
						this.getY() - 1 >= f.getGoalleft().getY()));
	}

	/*
	 * 
	 * Right Goal Collisions
	 * 
	 */
	public boolean touchGoalRightTop(Field f) {
		return (this.getMaxX() >= f.getGoalright().getX() &&
				this.getY() - 1 <= f.getGoalright().getY());
	}

	public boolean touchGoalRightBottom(Field f) {
		return (this.getMaxX() >= f.getGoalright().getX() &&
				this.getMaxY() + 1 >= f.getGoalright().getMaxY());
	}

	public boolean touchRectInRight(Field f) {

		return (f.getMaxX() - this.getMaxX() <= 1.5 && 
				(this.getY() < f.getGoalright().getY() ||
						this.getMaxY() > f.getGoalright().getMaxY()
						) || this.getMaxX() + 1 >= f.getGoalright().getMaxX());
	}

	/*
	 * 
	 * Left Goal Collisions
	 * 
	 */
	public boolean touchRectInLeft(Field f) {
		return (this.getX() - f.getX() <= 1.5 &&
				(this.getY() < f.getGoalright().getY() ||
						this.getMaxY() > f.getGoalright().getMaxY()
						) || this.getX() <= f.getGoalleft().getX());
	}

	public boolean touchGoalLeftBottom(Field f) {
		return (this.getX() <= f.getGoalleft().getMaxX() &&
				this.getMaxY() >= f.getGoalleft().getMaxY());
	}

	public boolean touchGoalLeftTop(Field f) {
		return (this.getX() <= f.getGoalleft().getMaxX() &&
				this.getY() - 1 <= f.getGoalleft().getY());
	}

	/*
	 * 
	 * Set Dy and Dx based on the direction
	 * when two keys are pressed at the same 
	 * time so the movement is realistic.
	 * 
	 */
	public void setMovement() {
		if (this.getDx() == SPEED_ONE) {
			if (this.getDy() == SPEED_ONE) {
				this.setDx(SPEED_ONE_DIAG);
				this.setDy(SPEED_ONE_DIAG);
			} else if (this.getDy() == -SPEED_ONE) {
				this.setDx(SPEED_ONE_DIAG);
				this.setDy(-SPEED_ONE_DIAG);
			}
		} else if (this.getDx() == -SPEED_ONE) {
			if (this.getDy() == SPEED_ONE) {
				this.setDx(-SPEED_ONE_DIAG);
				this.setDy(SPEED_ONE_DIAG);
			} else if (this.getDy() == -SPEED_ONE) {
				this.setDx(-SPEED_ONE_DIAG);
				this.setDy(-SPEED_ONE_DIAG);
			}
		}
	}

	/*
	 * 
	 * 
	 * Move the player in the field
	 * 
	 * 
	 */
	public void moveIn(Field f) {
		// update speed
		this.setMovement();

		// actually move if in the field or in a goal
		if (this.insideRect(f) || this.insideGoals(f)){
			this.setLocation(this.getX() + this.getDx(),
					this.getY() + this.getDy());
		}

		// otherwise, back up a little
		if (this.touchRectInLeft(f)) {
			this.setLocation(this.getX() - this.getDx(), this.getY());
		}

		if (this.touchRectInTop(f) ||
				this.touchGoalRightTop(f) ||
				this.touchGoalLeftTop(f)) {
			this.setLocation(this.getX(), this.getY() - this.getDy());
		}

		if (this.touchRectInRight(f)) {
			this.setLocation(this.getX() - this.getDx(), this.getY());
		}

		if (this.touchRectInBottom(f) ||
				this.touchGoalRightBottom(f) ||
				this.touchGoalLeftBottom(f)) {
			this.setLocation(this.getX(), this.getY() - this.getDy());
		}

	}

	@Override
	public String toString() {
		return "Player [x=" + x + ", y=" + y + "]";
	}
}