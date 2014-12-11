package geo;

import gui.Images;

import java.awt.geom.Ellipse2D;

@SuppressWarnings("serial")
public class PlayerModel extends Field {
	public Images img;
	public int score;
	public Ellipse2D.Double ell;
	public  Boolean left;
	public  Boolean right;
	public  Boolean up;
	public  Boolean down;

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

	public PlayerModel() {  
		super(0, 0, 0, 0);
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
}