package geo;

import gui.Images;

import java.awt.geom.Ellipse2D;

@SuppressWarnings("serial")
public class PlayerModel extends FieldController {
	public boolean host;
	public Images img;
	public int score;
	public Ellipse2D.Double ell;
	public boolean left;
	public boolean right;
	public boolean up;
	public boolean down;

	// Constances
	public static final int SPEED_ONE = 1;
	// Vu que Dx et Dy peut-être les mêmes,
	// on fait pythagore des deux pour obtenir la vitesse diagonale.
	public static final double SPEED_ONE_DIAG = Math.sqrt(2 * SPEED_ONE);
	public static final int SPEED_TWO = 2;
	public static final int STOP = 0;
	public static final int INIT_X = 400;
	public static final int INIT_Y = 200;
	public static final int INIT_ROT = 0;

	/**
	 * Structure contenant les directions du joueur définies par la rotation et
	 * leurs noms en anglais de cette rotation.
	 */
	public enum Direction {
		LEFT(180, "left"), UP(-90, "up"), RIGHT(0, "right"), DOWN(90, "down");

		private final int id;
		private final String name;

		/**
		 * Une direction a son angle (id) et son nom en anglais (name)
		 * 
		 * @param s
		 * @param n
		 */
		private Direction(int s, String n) {
			this.id = s;
			this.name = n;
		}

		/**
		 * Recupère l'angle de la direction
		 * 
		 * @return l'angle de la direction
		 */
		public int getId() {
			return this.id;
		}

		/**
		 * Récupère le nom en anglais de la direction
		 * 
		 * @return le nom en anglais de la direction
		 */
		public String getName() {
			return this.name;
		}

		/**
		 * Récupère le nom en anglais de ces rotations en fonction de l'id.
		 * 
		 * @param rotation
		 *            int
		 * @return Le nom de la rotation en anglais par rapport à son id
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

	/**
	 * Constructeur de PlayerModel()
	 */
	public PlayerModel(boolean host) {
		super(0, 0, 0, 0);
		// Charge l'image
		try {
			img = new Images();
		} catch (Exception ioex) {
			System.out.println("couldn't load image");
			System.exit(0);
		}

		// Calcul de la largeur et de la hauteur de l'image du joueur
		this.width = img.getWidth();
		this.height = img.getHeight();

		// Modifie l'ellipse utilisée pour les collisions avec la ball.
		this.ell = new Ellipse2D.Double(this.getX(), this.getY(),
				this.getWidth(), this.getHeight());

		img.setRotation(INIT_ROT);

		// Initialise la position du joueur
		this.x = INIT_X;
		this.y = INIT_Y;

		this.up = false;
		this.down = false;
		this.left = false;
		this.right = false;

		this.host = host;

		if (!host) {
			img.applyFilter();
		}

		this.setSides();

	}
}