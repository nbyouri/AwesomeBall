package geo;

import gui.Images;

import java.awt.Graphics2D;

@SuppressWarnings("serial")
public class Player extends Field {
	private Images img;
	private int score;

	// constants
	public static final int SPEED_ONE = 1;
	// since Dx and Dy can be the same, 
	// pythagore the two as a constant to
	// save jvm cycles. sqrt(2);
	public static final double SPEED_ONE_DIAG = 1.414213562373095;
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

		img.setRotation(INIT_ROT);

		// initial position
		this.x = INIT_X;
		this.y = INIT_Y;

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

	// draw rectangle and ball
	public void draw(Graphics2D g2) {
		g2.drawImage(img.getPlayer(), 
				(int)this.getX(), 
				(int)this.getY(), 
				(int)this.getWidth(), 
				(int)this.getHeight(), null);
		//g2.draw(this);
	}

	/*
	 * 
	 * Draw image left, right, up, down
	 * and rotate and or flip the image
	 * depending on the previous direction.
	 * 
	 */
	public void drawLeft() {
		int rotation = img.getRotation();
		if (rotation == Player.Direction.RIGHT.getId()) {
			img.flip(img.getPlayer(), Player.Direction.LEFT.getId());
		} else if (rotation == Player.Direction.UP.getId()) {
			img.rotate(img.getPlayer(), Player.Direction.DOWN.getId());
			img.flip(img.getPlayer(), Player.Direction.LEFT.getId());
		} else if (rotation == Player.Direction.DOWN.getId()) {
			img.rotate(img.getPlayer(), Player.Direction.UP.getId());
			img.flip(img.getPlayer(), Player.Direction.LEFT.getId());
		}
		img.setRotation(Player.Direction.LEFT.getId());
	}

	public void drawRight() {
		int rotation = img.getRotation();
		if (rotation == Player.Direction.LEFT.getId()) {
			img.flip(img.getPlayer(), Player.Direction.RIGHT.getId());
		} else if (rotation == Player.Direction.UP.getId()) {
			img.rotate(img.getPlayer(), Player.Direction.DOWN.getId());
		} else if (rotation == Player.Direction.DOWN.getId()) {
			img.rotate(img.getPlayer(), Player.Direction.UP.getId());
		}
		img.setRotation(Player.Direction.RIGHT.getId());
	}

	public void drawUp() {
		int rotation = img.getRotation();
		if (rotation == Player.Direction.LEFT.getId()) {
			img.rotate(img.getPlayer(), Player.Direction.DOWN.getId());
			img.flip(img.getPlayer(), Player.Direction.UP.getId());
		} else if (rotation == Player.Direction.RIGHT.getId()) {
			img.rotate(img.getPlayer(), Player.Direction.UP.getId());
		} else if (rotation == Player.Direction.DOWN.getId()) {
			img.rotate(img.getPlayer(), Player.Direction.LEFT.getId());
		}
		img.setRotation(Player.Direction.UP.getId());
	}

	public void drawDown() {
		int rotation = img.getRotation();
		if (rotation == Player.Direction.LEFT.getId()) {
			img.flip(img.getPlayer(), Player.Direction.DOWN.getId());
			img.rotate(img.getPlayer(), Player.Direction.DOWN.getId());
		} else if (rotation == Player.Direction.UP.getId()) {
			img.rotate(img.getPlayer(), Player.Direction.LEFT.getId());
		} else if (rotation == Player.Direction.RIGHT.getId()) {
			img.rotate(img.getPlayer(), Player.Direction.DOWN.getId());
		}
		img.setRotation(Player.Direction.DOWN.getId());
	}

	/*
	 * 
	 * Check whether the player is in a goal
	 * 
	 */
	public Boolean insideGoals(Field f) {
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
	public Boolean touchGoalRightTop(Field f) {
		return (this.getX() >= f.getGoalright().getX() &&
				this.getY() - 1 <= f.getGoalright().getY());
	}

	public Boolean touchGoalRightBottom(Field f) {
		return (this.getX() >= f.getGoalright().getX() &&
				this.getMaxY() + 1 >= f.getGoalright().getMaxY());
	}

	public Boolean touchRectRight(Field f) {
		return (this.intersectsLine(f.getSide(Field.GOAL_RIGHT_UP))   || 
				this.intersectsLine(f.getSide(Field.GOAL_RIGHT_DOWN)) ||
				this.getMaxX() + 1 >= f.getGoalright().getMaxX());
	}

	/*
	 * 
	 * Left Goal Collisions
	 * 
	 */
	public Boolean touchRectLeft(Field f) {
		return (this.intersectsLine(f.getSide(Field.GOAL_LEFT_UP))	 ||
				this.intersectsLine(f.getSide(Field.GOAL_LEFT_DOWN)) ||
				this.getX() - 1 <= f.getGoalleft().getX());
	}

	public Boolean touchGoalLeftBottom(Field f) {
		return (this.getX() <= f.getGoalleft().getMaxX() &&
				this.getMaxY() + 1 >= f.getGoalleft().getMaxY());
	}

	public Boolean touchGoalLeftTop(Field f) {
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
		if (this.getDx() == Player.SPEED_ONE) {
			if (this.getDy() == Player.SPEED_ONE) {
				this.setDx(Player.SPEED_ONE_DIAG);
				this.setDy(Player.SPEED_ONE_DIAG);
			} else if (this.getDy() == -Player.SPEED_ONE) {
				this.setDx(Player.SPEED_ONE_DIAG);
				this.setDy(-Player.SPEED_ONE_DIAG);
			}
		} else if (this.getDx() == -Player.SPEED_ONE) {
			if (this.getDy() == Player.SPEED_ONE) {
				this.setDx(-Player.SPEED_ONE_DIAG);
				this.setDy(Player.SPEED_ONE_DIAG);
			} else if (this.getDy() == -Player.SPEED_ONE) {
				this.setDx(-Player.SPEED_ONE_DIAG);
				this.setDy(-Player.SPEED_ONE_DIAG);
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
		if (this.touchRectLeft(f)) {
			this.setLocation(this.getX() - this.getDx(), this.getY());
		}

		if (this.touchRectTop(f)      ||
				this.touchGoalRightTop(f) ||
				this.touchGoalLeftTop(f)) {
			this.setLocation(this.getX(), this.getY() - this.getDy());
		}

		if (this.touchRectRight(f)) {
			this.setLocation(this.getX() - this.getDx(), this.getY());
		}

		if (this.touchRectBottom(f)      ||
				this.touchGoalRightBottom(f) ||
				this.touchGoalLeftBottom(f)) {
			this.setLocation(this.getX(), this.getY() - this.getDy());
		}

	}
}