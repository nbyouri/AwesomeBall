package geo;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Shape extends Rectangle2D.Double {
	/*
	 * Vitesse de la forme
	 */
	private double dx; // Horizontal
	private double dy; // Vertical
	private ArrayList<Line2D.Double> sides; // Côtés de la forme

	/*
	 * Détermine les côtés de la forme en fonction de leur identification
	 * textuelle ( nom en anglais) et d'un nombre défini.
	 */
	public enum Side {
		LEFT(0, "left"), UP(1, "up"), RIGHT(2, "right"), DOWN(3, "down"), CENTER_V(
				4, "center_vertical"), CENTER_H(5, "center_horizontal");

		private final int id;
		private final String name;

		/**
		 * Un côté possède un nom en anglais et une id
		 * 
		 * @param s
		 *            son id
		 * @param n
		 *            son nom en anglais
		 */
		private Side(int s, String n) {
			this.id = s;
			this.name = n;
		}

		/**
		 * Récupère l'id du côté
		 * 
		 * @return l'id du côté
		 */
		public int getId() {
			return this.id;
		}

		/**
		 * Recupère le nom en anglais du côté
		 * 
		 * @return le nom en anglais du côté
		 */
		public String getName() {
			return this.name;
		}
	};

	/**
	 * Constructeur ...
	 * 
	 * @param x
	 *            Position X
	 * @param y
	 *            Position Y
	 * @param width
	 *            Largeur
	 * @param height
	 *            Hauteur
	 */
	public Shape(double x, double y, double width, double height) {
		// Initialise les valeurs
		super(x, y, width, height);

		sides = new ArrayList<Line2D.Double>();

		for (int i = 0; i < 6; i++)
			sides.add(new Line2D.Double(0, 0, 0, 0));
	}

	/**
	 * Récupère dx, la vitesse horizontale de la forme
	 * 
	 * @return dx, la vitesse horizontale de la forme
	 */
	public double getDx() {
		return dx;
	}

	/**
	 * Modifie la vitesse horizontale de la forme
	 * 
	 * @param dx
	 *            , la vitesse horizontale de la forme
	 */
	public void setDx(double dx) {
		this.dx = dx;
	}

	/**
	 * Recupère dy la vitesse verticale de la forme
	 * 
	 * @return dy, la vitesse verticale de la forme
	 */
	public double getDy() {
		return dy;
	}

	/**
	 * Modifie dy, la vitesse verticale de la forme
	 * 
	 * @param dy
	 *            , la vitesse verticale de la forme
	 */
	public void setDy(double dy) {
		this.dy = dy;
	}

	/**
	 * Récupère tout les côtés de la forme
	 * 
	 * @return les côtés de la forme
	 */
	public ArrayList<Line2D.Double> getSides() {
		return sides;
	}

	/**
	 * Recupère un des côtés de la forme en fonction de son id
	 * 
	 * @param i
	 *            l'id du côté choisi
	 * @return le côté choisi
	 */
	public Line2D getSide(int i) {
		return sides.get(i);
	}

	/**
	 * Est-ce que la forme est proche horizontalement un rectangle s ?
	 * 
	 * @param s
	 *            le rectangle en question
	 * @return Oui/Non à la question.
	 */
	public boolean nearX(Rectangle2D s) {
		return (this.getX() <= s.getMaxX() && this.getMaxX() >= s.getX());
	}

	/**
	 * Est-ce que la forme est proche verticallement d'un rectangle s ?
	 * 
	 * @param s
	 *            le rectangle en question
	 * @return Oui/Non à la question.
	 */
	public boolean nearY(Rectangle2D s) {
		return (this.getY() <= s.getMaxY() && this.getMaxY() >= s.getY());
	}

	/**
	 * Est-ce que la forme est proche verticallement et horizontallement d'un
	 * rectangle s ?
	 * 
	 * @param s
	 *            le rectangle en question
	 * @return Oui/Non à la question
	 */
	public boolean near(Rectangle2D s) {
		return (this.nearX(s) && this.nearY(s));
	}

	/**
	 * Est-ce que la forme est proche de son côté choisie d'un rectangle s?
	 * Déplace de deux pixels si il y a collision
	 * 
	 * @param s
	 *            le rectangle en question
	 * @param line
	 *            le côté choisie
	 * @return boolean
	 */
	public boolean near(Rectangle2D s, int line) {

		// transtype les positions en int pour éviter
		// les confusions après une diagonale
		int x = (int) this.getX();
		int mx = (int) this.getMaxX();
		int y = (int) this.getY();
		int my = (int) this.getMaxY();
		int sx = (int) s.getX();
		int smx = (int) s.getMaxX();
		int sy = (int) s.getY();
		int smy = (int) s.getMaxY();

		/*
		 * Déplace de deux pixels si il y a collision
		 */
		if (line == Side.UP.getId()) {
			return ((my + 1 >= sy) && (my - 1 <= sy) && this.nearX(s));
		}
		if (line == Side.DOWN.getId()) {
			return ((y - 1 <= smy) && (y + 1 >= smy) && this.nearX(s));
		}
		if (line == Side.LEFT.getId()) {
			return ((mx - 1 <= sx) && (mx + 1 >= sx) && this.nearY(s));
		}
		if (line == Side.RIGHT.getId()) {
			return ((x - 1 <= smx) && (x + 1 >= smx) && this.nearY(s));
		} else {
			return false;
		}
	}

	/**
	 * Est-ce que la forme est bien à l'intérieur d'un rectangle et donc ne
	 * touche pas un des côtés de ce rectangle ?
	 * 
	 * @param r
	 *            le rectangle en question
	 * @return Oui/Non à la question
	 */
	public boolean insideRect(Rectangle2D r) {
		return (!((this.touchRectInLeft(r)) || (this.touchRectInRight(r))
				|| (this.touchRectInTop(r)) || (this.touchRectInBottom(r))));
	}

	/**
	 * Est-ce que la forme touche le côté gauche d'un rectangle par son
	 * intérieur ? ( Donc on considère que la forme est à l'intérieur du
	 * rectangle )
	 * 
	 * @param r
	 *            le rectangle en question
	 * @return Oui/Non à la question
	 */
	public boolean touchRectInLeft(Rectangle2D r) {
		return (this.getX() + 1 < r.getX());
	}

	/**
	 * Est-ce que la forme touche le côté haut d'un rectangle par son intérieur
	 * ? (Donc on considère que la forme est à l'intérieur du rectangle )
	 * 
	 * @param r
	 *            le rectangle en question
	 * @return Oui/Non à la question
	 */
	public boolean touchRectInTop(Rectangle2D r) {
		return (this.getY() - 1 < r.getY());
	}

	/**
	 * Est-ce que la forme touche le côté droit d'un rectangle par son intérieur
	 * ? (Donc on considère que la forme est à l'intérieur du rectangle )
	 * 
	 * @param r
	 *            le rectangle en question
	 * @return Oui/Non à la question
	 */
	public boolean touchRectInRight(Rectangle2D r) {
		return (this.getMaxX() + 1 > r.getMaxX());
	}

	/**
	 * Est-ce que la forme touche le côté bas d'un rectangle par son intérieur ?
	 * (Donc on considère que la forme est à l'intérieur du rectangle )
	 * 
	 * @param r
	 *            le rectangle en question
	 * @return Oui/Non à la question
	 */
	public boolean touchRectInBottom(Rectangle2D r) {
		return (this.getMaxY() + 1 > r.getMaxY());
	}

	/**
	 * Reposition la forme ainsi que la position de ses côtés.
	 * 
	 * @param x
	 *            position x de la forme
	 * @param y
	 *            position y de la forme
	 */
	public void setLocation(double x, double y) {
		this.setRect(x, y, this.getWidth(), this.getHeight());
		this.setSides();
	}

	/*
	 * 
	 * Set the shape position and size.
	 */
	/**
	 * Modifie la position et ( si besoin ) la forme d'un côté de la forme
	 * 
	 * @param x
	 *            position du côté de la forme
	 * @param y
	 *            position x du côté de la forme
	 * @param width
	 *            largeur du côté de la forme
	 * @param height
	 *            hauteur du côté de la forme
	 */
	public void setSize(double x, double y, double width, double height) {
		super.setRect(x, y, width, height);
	}

	/**
	 * Bouge la forme dans un rectangle en vérifiant qu'il ne touche pas un des
	 * côtés du rectangle ( si oui, il est déplacé par sa vitesse horizontal
	 * et/ou vertical en fonction du/des côté(s) touché(s) )
	 * 
	 * @param r
	 *            le rectangle en question
	 */
	public void moveIn(Rectangle2D r) {
		if (this.insideRect(r)) {
			this.setLocation(this.getX() + this.getDx(),
					this.getY() + this.getDy());
		}

		if (this.touchRectInLeft(r)) {
			this.setLocation(this.getX() - this.getDx(), this.getY());
		}

		if (this.touchRectInTop(r)) {
			this.setLocation(this.getX(), this.getY() - this.getDy());
		}

		if (this.touchRectInRight(r)) {
			this.setLocation(this.getX() - this.getDx(), this.getY());
		}

		if (this.touchRectInBottom(r)) {
			this.setLocation(this.getX(), this.getY() - this.getDy());
		}

	}

	/**
	 * Modifie tout les côtés de la forme : sa position et, si besoin, sa forme
	 */
	public void setSides() {
		this.sides.get(Side.LEFT.getId()).setLine(this.getX(), this.getY(),
				this.getX(), this.getMaxY());

		this.sides.get(Side.UP.getId()).setLine(this.getX(), this.getY(),
				this.getMaxX(), this.getY());

		this.sides.get(Side.RIGHT.getId()).setLine(this.getMaxX(), this.getY(),
				this.getMaxX(), this.getMaxY());

		this.sides.get(Side.DOWN.getId()).setLine(this.getX(), this.getMaxY(),
				this.getMaxX(), this.getMaxY());

		this.sides.get(Side.CENTER_V.getId()).setLine(this.getCenterX(),
				this.getY(), this.getCenterX(), this.getY() + this.getHeight());

		this.sides.get(Side.CENTER_H.getId()).setLine(this.getX(),
				this.getCenterY(), this.getWidth() + this.getX(),
				this.getCenterY());
	}
}
