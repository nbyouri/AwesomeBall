package geo;

import gui.Images;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

@SuppressWarnings("serial")
public class PlayerController extends PlayerModel {
	public PlayerController(boolean host) {
		super(host);
	}

	/**
	 * Récupère l'image du joueur
	 * 
	 * @return l'image du joueur
	 */
	public Images getImg() {
		return img;
	}

	/**
	 * Modifie l'image du joueur
	 * 
	 * @param img
	 *            l'image du joueur
	 */
	public void setImg(Images img) {
		this.img = img;
	}

	/**
	 * Récupère son score
	 * 
	 * @return le score du joueur
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Modifie le score du joueur
	 * 
	 * @param score
	 *            le score du joueur
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * Récupère l'ellipse du joueur utilisée pour certaines collisions
	 * 
	 * @return ell, l'ellipse utilisée pour certaines collisions
	 */
	public Ellipse2D.Double getEll() {
		return ell;
	}

	/**
	 * Modifie l'ellipse du joueur utilisée pour certaines collisions
	 * 
	 * @param ell
	 *            l'ellipse utilisée pour certaines collisions
	 */
	public void setEll(Ellipse2D.Double ell) {
		this.ell = ell;
	}

	/**
	 * Repositionne le joueur
	 * 
	 * @param x
	 *            Position X
	 * @param y
	 *            Position Y
	 */
	public void setLocation(double x, double y) {
		this.setRect(x, y, this.getWidth(), this.getHeight());
		this.getEll().setFrame(x, y, this.getWidth(), this.getHeight());
	}

	/**
	 * Rotationne l'image du joueur à partir des touches utilisées par le joueur
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

	/**
	 * Rotationne l'image du joueur à gauche ou fait un flip de l'image en
	 * fonction de la rotation précédente du joueur
	 */
	public void setLeft() {
		int rotation = getImg().getRotation();
		if (rotation == Direction.RIGHT.getId()) {
			getImg().flip(getImg().getPlayer(), Direction.LEFT.getId());
		} else if (rotation == Direction.UP.getId()) {
			getImg().rotate(getImg().getPlayer(), Direction.DOWN.getId());
			getImg().flip(getImg().getPlayer(), Direction.LEFT.getId());
		} else if (rotation == Direction.DOWN.getId()) {
			getImg().rotate(getImg().getPlayer(), Direction.UP.getId());
			getImg().flip(getImg().getPlayer(), Direction.LEFT.getId());
		}
		getImg().setRotation(Direction.LEFT.getId());
	}

	/**
	 * Rotationne l'image du joueur à droite
	 */
	public void setRight() {
		int rotation = getImg().getRotation();
		if (rotation == Direction.LEFT.getId()) {
			getImg().flip(getImg().getPlayer(), Direction.RIGHT.getId());
		} else if (rotation == Direction.UP.getId()) {
			getImg().rotate(getImg().getPlayer(), Direction.DOWN.getId());
		} else if (rotation == Direction.DOWN.getId()) {
			getImg().rotate(getImg().getPlayer(), Direction.UP.getId());
		}
		getImg().setRotation(Direction.RIGHT.getId());
	}

	/**
	 * Rotationne l'image du joueur vers le haut
	 */
	public void setUp() {
		int rotation = getImg().getRotation();
		if (rotation == Direction.LEFT.getId()) {
			getImg().rotate(getImg().getPlayer(), Direction.DOWN.getId());
			getImg().flip(getImg().getPlayer(), Direction.UP.getId());
		} else if (rotation == Direction.RIGHT.getId()) {
			getImg().rotate(getImg().getPlayer(), Direction.UP.getId());
		} else if (rotation == Direction.DOWN.getId()) {
			getImg().rotate(getImg().getPlayer(), Direction.LEFT.getId());
		}
		getImg().setRotation(Direction.UP.getId());
	}

	/**
	 * Rotationne l'image du joueur vers le bas
	 */
	public void setDown() {
		int rotation = getImg().getRotation();
		if (rotation == Direction.LEFT.getId()) {
			getImg().flip(getImg().getPlayer(), Direction.DOWN.getId());
			getImg().rotate(getImg().getPlayer(), Direction.DOWN.getId());
		} else if (rotation == Direction.UP.getId()) {
			getImg().rotate(getImg().getPlayer(), Direction.LEFT.getId());
		} else if (rotation == Direction.RIGHT.getId()) {
			getImg().rotate(getImg().getPlayer(), Direction.DOWN.getId());
		}
		getImg().setRotation(Direction.DOWN.getId());
	}

	/**
	 * Est-ce que le joueur est dans un des goals ?
	 * 
	 * @param f
	 *            FieldController
	 * @return Oui ou non à la réponse
	 */
	public boolean insideGoals(FieldController f) {
		return ((this.getMaxX() < f.getGoalright().getMaxX() && this.getY() - 1 >= f
				.getGoalright().getY()) || (this.getX() > f.getGoalleft()
						.getX() && this.getY() - 1 >= f.getGoalleft().getY()));
	}

	/**
	 * Est-ce que le joueur touche le haut du goal droit ?
	 * 
	 * @param f
	 *            FieldController
	 * @return Oui/Non à la question
	 */
	public boolean touchGoalRightTop(FieldController f) {
		return (this.getMaxX() >= f.getGoalright().getX() && this.getY() - 1 <= f
				.getGoalright().getY());
	}

	/**
	 * Est-ce que le joueur touche le bas du goal droit ?
	 * 
	 * @param f
	 *            FieldController
	 * @return Oui/Non à la question
	 */
	public boolean touchGoalRightBottom(FieldController f) {
		return (this.getMaxX() >= f.getGoalright().getX() && this.getMaxY() + 1 >= f
				.getGoalright().getMaxY());
	}

	/**
	 * Est-ce que le joueur touche le bord droit du terrain ( ou la ligne
	 * verticale du goal droit ) ?
	 * 
	 * @param f
	 *            FieldController
	 * @return Oui/Non à la question
	 */
	public boolean touchRectInRight(FieldController f) {
		return (f.getMaxX() - this.getMaxX() <= 1.5
				&& (this.getY() < f.getGoalright().getY() || this.getMaxY() > f
						.getGoalright().getMaxY()) || this.getMaxX() + 1 >= f
						.getGoalright().getMaxX());
	}

	/**
	 * Est-ce que le joueur touche le bord gauche du terrain (ou la ligne
	 * verticale du goal gauche) ?
	 * 
	 * @param f
	 *            FieldController
	 * @return Oui/Non à la question
	 */
	public boolean touchRectInLeft(FieldController f) {
		return (this.getX() - f.getX() <= 1.5
				&& (this.getY() < f.getGoalright().getY() || this.getMaxY() > f
						.getGoalright().getMaxY()) || this.getX() <= f
						.getGoalleft().getX());
	}

	/**
	 * Est-ce que le joueur touche le bas du goal gauche ?
	 * 
	 * @param f
	 *            FieldController
	 * @return Oui/Non à la question
	 */
	public boolean touchGoalLeftBottom(FieldController f) {
		return (this.getX() <= f.getGoalleft().getMaxX() && this.getMaxY() >= f
				.getGoalleft().getMaxY());
	}

	/**
	 * Est-ce que le joueur touche le haut du goal gauche ?
	 * 
	 * @param f
	 *            FieldController
	 * @return Oui/Non à la question
	 */
	public boolean touchGoalLeftTop(FieldController f) {
		return (this.getX() <= f.getGoalleft().getMaxX() && this.getY() - 1 <= f
				.getGoalleft().getY());
	}

	/**
	 * Modifie Dx et Dy en fonction de la direction quand deux touches
	 * directionnelles sont pressées en même temps pour avoir un mouvement plus
	 * réaliste
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

	/**
	 * Méthode utilisé par l'actionPerformed : bouge le joueur au fil du temps
	 * et, oblige le joueur à rester dans le terrain ( goals compris)
	 * 
	 * @param f
	 *            FieldController
	 * @param p
	 *            PlayerModel
	 */
	public void moveIn(FieldController f, PlayerModel p) {
		// Modifie la vitesse du joueur
		this.setMovement();

		// Positionne le joueur si il est dans le terrain ou dans un goal
		if (this.insideRect(f) || this.insideGoals(f) || !this.near(p)
				|| !this.inPlayer(p)) {
			this.setLocation(this.getX() + this.getDx(),
					this.getY() + this.getDy());
		}

		// Si pas : on recule
		if (this.touchRectInLeft(f)
				|| this.near((Rectangle2D) p, Side.RIGHT.getId())) {
			this.setLocation(this.getX() - this.getDx(), this.getY());
		}

		if (this.touchRectInTop(f) || this.touchGoalRightTop(f)
				|| this.touchGoalLeftTop(f)
				|| this.near((Rectangle2D) p, Side.UP.getId())) {
			this.setLocation(this.getX(), this.getY() - this.getDy());
		}

		if (this.touchRectInRight(f)
				|| this.near((Rectangle2D) p, Side.LEFT.getId())) {
			this.setLocation(this.getX() - this.getDx(), this.getY());
		}

		if (this.touchRectInBottom(f) || this.touchGoalRightBottom(f)
				|| this.touchGoalLeftBottom(f)
				|| this.near((Rectangle2D) p, Side.DOWN.getId())) {
			this.setLocation(this.getX(), this.getY() - this.getDy());
		}
	}

	/**
	 * detecte les collisions avec un autre joueur
	 * 
	 * @param p
	 *            PlayerModel
	 * @return boolean
	 */
	public boolean inPlayer(PlayerModel p) {
		int tx = (int) this.getX();
		int ty = (int) this.getY();
		int px = (int) p.getX();
		int py = (int) p.getY();

		return (new Shape(tx, ty, this.getWidth(), this.getHeight())
		.near(new Shape(px, py, p.width, p.height)));
	}

	/**
	 * Détecte si un autre joueur se déplace déjà avec une balle
	 * 
	 * @param b
	 *            Ball
	 * @return boolean
	 */
	public boolean movingWithBall(Ball b) {
		return ((this.getDx() > 0 || this.getDy() > 0) && this.near(b.rect, 5));
	}

	/**
	 * Réception du paquet serveur pour la position du joueur reçois aussi la
	 * position du ballon.
	 * 
	 * @param msg
	 *            Message reçue
	 * @param ball
	 *            Ballon
	 */
	public void msgToCoord(String msg, Ball ball) {

		if (msg != null) {

			String data[] = msg.split("/");


			double nx = java.lang.Double.parseDouble(data[0]);
			double ny = java.lang.Double.parseDouble(data[1]);

			this.setLocation(nx, ny);

			this.setScore(Integer.parseInt(data[2]));

			this.left = Boolean.parseBoolean(data[3]);
			this.right = Boolean.parseBoolean(data[4]);
			this.up = Boolean.parseBoolean(data[5]);
			this.down = Boolean.parseBoolean(data[6]);
			
			double bx = java.lang.Double.parseDouble(data[7]);
			double by = java.lang.Double.parseDouble(data[8]);

			ball.setX(bx);
			ball.setY(by);
		}
	}

	/**
	 * Création d'une chaine de caractère déterminant la position du joueur et
	 * de la balle
	 * 
	 * @param ball
	 *            Balle
	 * @return Un message utilisé pour l'envoi d'information au serveur et au
	 *         client.
	 */
	public String toString(Ball ball) {
		StringBuilder msg = new StringBuilder();

		msg.append(this.getX() + "/");
		msg.append(this.getY() + "/");

		msg.append(this.getScore() + "/");

		msg.append(this.left + "/");
		msg.append(this.right + "/");
		msg.append(this.up + "/");
		msg.append(this.down + "/");

		msg.append(ball.toString());

		return msg.toString();
	}

	/**
	 * Set players initial position
	 */
	public static void initPosition(FieldController f, 
			PlayerController p1, PlayerController p2) {
		if (p1.host) {
			p1.setLocation(f.getCenterX() / 2 - p1.width, 
					f.getCenterY() - p1.height);
			p2.setLocation(f.getCenterX()  +  f.getCenterX() / 2 - p2.width,
					f.getCenterY() - p2.height);
		} else {
			p1.setLocation(f.getCenterX()  +  f.getCenterX() / 2 - p1.width,
					f.getCenterY() - p1.height);
			p2.setLocation(f.getCenterX() / 2 - p2.width, 
					f.getCenterY() - p2.height);
		}
	}
}
