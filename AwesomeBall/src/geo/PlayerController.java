package geo;

import gui.Images;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

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

	public byte[] toBytes() throws Exception {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final DataOutputStream oos = new DataOutputStream(baos);
		oos.writeDouble(this.x);
		oos.writeDouble(this.y);
		oos.writeInt(this.score);
		oos.writeBoolean(this.up);
		oos.writeBoolean(this.down);
		oos.writeBoolean(this.left);
		oos.writeBoolean(this.right);
		oos.close();
		baos.close();
		return baos.toByteArray();
	}

	public void toPlayer(byte[] b) throws Exception {
		final ByteArrayInputStream bais = new ByteArrayInputStream(b);
		final DataInputStream dais = new DataInputStream(bais);

		double nx = dais.readDouble();
		double ny = dais.readDouble();

		if (nx > 0) {
			this.x = nx;
		}
		if (ny > 0) {
			this.y = ny;
		}

		this.score = dais.readInt();
		this.up = dais.readBoolean();
		this.down = dais.readBoolean();
		this.left = dais.readBoolean();
		this.right = dais.readBoolean();

		bais.close();
		dais.close();
	}

	/**
	 * Set players initial position
	 */
	public static void initPosition(FieldController f, PlayerController p1,
			PlayerController p2) {
		if (p1.host) {
			p1.setLocation(f.getCenterX() / 2 - p1.width, f.getCenterY()
					- p1.height);
			p2.setLocation(f.getCenterX() + f.getCenterX() / 2 - p2.width,
					f.getCenterY() - p2.height);
		} else {
			p1.setLocation(f.getCenterX() + f.getCenterX() / 2 - p1.width,
					f.getCenterY() - p1.height);
			p2.setLocation(f.getCenterX() / 2 - p2.width, f.getCenterY()
					- p2.height);
		}
	}
}
