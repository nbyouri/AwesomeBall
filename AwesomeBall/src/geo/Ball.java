package geo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author Mouton Youri and Sias Nicolas
 */
@SuppressWarnings("serial")
public class Ball extends Ellipse2D.Double {

	public Shape rect;
	private double vX;
	private double vY;
	public final int STOP = 0;
	public final double SPEED_SHOOT = 0.05;
	public final double BRAKE = -0.005;
	public final double VELOCITY_LIMIT = 1;

	public Ball(double x, double y, double width, double height) {
		super.setFrame(x - (width / 2), y, width, height);
		this.setVx(STOP);
		this.setVy(STOP);
		this.rect = new Shape(this.getX(), this.getY(), this.getWidth(),
				this.getHeight());
	}

	/**
	 * Creation graphique de la balle
	 *
	 * @param g2
	 *            Graphics2D
	 */
	public void draw(Graphics2D g2) {
		g2.setColor(Color.yellow);
		g2.draw(this);
		g2.fill(this);
		// g2.draw(this.rect);
	}

	/**
	 * Methode principale de la balle utilisé par actionPerformed : applique et
	 * modifie, si neccessaire, le mouvement apres un check de toutes les
	 * collisions possibles.
	 *
	 * @param f
	 *            FieldController
	 * @param p1
	 *            PlayerController
	 * @param p2
	 *            PlayerController
	 */
	public void move(FieldController f, PlayerController p1, PlayerController p2) {

		// Applique le mouvement de la balle si celle-ci est dans le terrain
		if (this.isInsideField(f)) {
			this.setMovement();
		}

		// Sinon, on vérifie les collisions:
		this.checkCollisionField(f, p1, p2);
		this.checkCollisionPlayer(f, p1, p2);

		this.brake();
	}

	/**
	 * Repositionne la balle
	 *
	 */
	public void setMovement() {
		this.setFrame(this.getX() + this.getVx(), this.getY() + this.getVy(),
				this.getWidth(), this.getHeight());
		this.rect.setFrame(this.getFrame());
	}

	/**
	 * Repositionne la balle
	 * 
	 * @param x
	 *            double : position en x
	 * @param y
	 *            double : la position en y
	 * @param vx
	 *            double : la vitesse en x
	 * @param vy
	 *            double : la vitesse en y
	 */
	public void setLocation(double x, double y, double vx, double vy) {
		this.setFrame(x, y, this.getWidth(), this.getHeight());
		this.rect.setLocation(x, y);
		this.setVx(vx);
		this.setVy(vy);
	}

	/**
	 * Applique un freinage a la balle, limite la vitesse a 5
	 */
	public void brake() {

		// Freinage
		if (this.getVx() != 0) {
			if (this.getVx() > 0) {
				this.setVx(vX + BRAKE);
			}
			if (this.getVx() < 0) {
				this.setVx(vX - BRAKE);
			}
		}
		if (this.getVy() != 0) {
			if (this.getVy() > 0) {
				this.setVy(vY + BRAKE);
			}
			if (this.getVy() < 0) {
				this.setVy(vY - BRAKE);
			}
		}

		// Limite de la vitesse
		if (this.getVy() > VELOCITY_LIMIT) {
			this.setVy(VELOCITY_LIMIT);
		}

		if (this.getVx() > VELOCITY_LIMIT) {
			this.setVx(VELOCITY_LIMIT);
		}

	}

	/**
	 * Repositionne la balle au centre du field et rÃ©initialise vX et vY a 0
	 *
	 * @param f
	 *            FieldController
	 */
	public void centerBall(FieldController f) {
		this.setFrame(f.getCenterX() - this.getWidth() / 2,
				f.getY() + (f.getHeight() / 2) - 10, width, height);
		this.setVx(STOP);
		this.setVy(STOP);
	}

	/**
	 * Modifie la vitesse de la balle en rapport a la position du joueur p
	 *
	 * @param speed
	 *            Vitesse en plus qu'aura notre balle ( verticalement et/ou
	 *            horizontalement)
	 * @param p
	 *            PlayerController
	 */
	public void modifySpeed(double speed, PlayerController p) {
		if (this.isBallRightShape(p)) {
			this.setVx(this.getVx() + Math.abs(this.getX() - p.getX()) * speed);
		} else if (this.isBallLeftShape(p)) {
			this.setVx(this.getVx() - Math.abs(this.getX() - p.getX()) * speed);
		}
		if (this.isBallAboveShape(p)) {
			this.setVy(this.getVy()
					+ (Math.abs(this.getY() - p.getY()) * speed));
		} else if (this.isBallUnderShape(p)) {
			this.setVy(this.getVy()
					- (Math.abs(this.getY() - p.getY()) * speed));
		}
	}

	/**
	 * Methode pour le tir de la balle
	 *
	 * @param p
	 *            PlayerController
	 * @param f
	 *            FieldController
	 */
	public void shootBall(FieldController f, PlayerController p) {
		if (p.near(this.rect)) {
			this.modifySpeed(SPEED_SHOOT, p);
		}
	}

	/**
	 * Augmenter le score du joueur de 1 et recentre la balle
	 *
	 * @param f
	 *            FieldController
	 * @param p1
	 *            PlayerController
	 * @param p2
	 *            PlayerController
	 */
	public void goal(FieldController f, PlayerController p1, PlayerController p2) {
		if (this.isGoalLeft(f)) {
			this.centerBall(f);
			p1.setScore(p1.getScore() + 1);
		} else if (this.isGoalRight(f)) {
			this.centerBall(f);
			p2.setScore(p2.getScore() + 1);
		}
	}

	/**
	 * Verifie la collision joueur-balle (c'est ici qu'on utilise l'ellipse du
	 * joueur)
	 *
	 * @param f
	 *            FieldController
	 * @param p1
	 *            PlayerController
	 * @param p2
	 *            PlayerController
	 */
	public void checkCollisionPlayer(FieldController f, PlayerController p1,
			PlayerController p2) {
		if (this.intersects(p1)) {

			// Pour l'axe des X ( gauche et droite du joueur)
			if (this.isTouchBorderOuterShapeX(p1)) {
				if (!(this.rect.touchRectInLeft(f) || this.rect
						.touchRectInRight(f))
						|| this.isTouchGoalLeft(f)
						|| this.isTouchGoalRight(f)) {

					if (this.isBallLeftShape(p1)) {
						this.setX(p1.getEll().getX() - this.getWidth());
					} else if (this.isBallRightShape(p1))
						this.setX(p1.getEll().getX() + this.getWidth());
				}
			}

			// Pour l'axe des Y ( bas et haut du joueur )
			if (this.isTouchBorderOuterShapeY(p1)) {
				if (!(this.rect.touchRectInBottom(f) || this.rect
						.touchRectInTop(f))
						|| this.isTouchGoalLeft(f)
						|| this.isTouchGoalRight(f)) {

					if (this.isBallAboveShape(p1)) {
						this.setY(p1.getEll().getY() + this.getHeight());
					} else if (this.isBallUnderShape(p1)) {
						this.setY(p1.getEll().getY() - this.getHeight());
					}
				}
			}
		}
	}

	/**
	 * Verifie la collision entre la balle et les terrains ( dont les goals)
	 * Active la méthode goal si besoin.
	 *
	 * @param f
	 *            FieldController
	 * @param p1
	 *            PlayerController
	 * @param p2
	 *            PlayerController
	 */
	public void checkCollisionField(FieldController f, PlayerController p1,
			PlayerController p2) {

		// Pour l'axe des X
		if (this.rect.touchRectInTop(f) || this.rect.touchRectInBottom(f)) {
			this.setVy(-vY);
		}

		// Pour l'axe des Y
		if (this.rect.touchRectInLeft(f) || this.rect.touchRectInRight(f)) {
			if ((!this.isTouchGoalLeft(f)) && (!this.isTouchGoalRight(f))) {
				this.setVx(-vX);
			} else {
				this.goal(f, p1, p2);
			}
		}
	}

	/**
	 * la balle touche les bordures droites ou gauches d'un rectangle dont la
	 * balle est a l'exterieur de ce rectangle
	 *
	 * @param r
	 *            Shape
	 * @return Oui/Non à la question
	 */
	public boolean isTouchBorderOuterShapeX(Shape r) {
		return (Math.abs(this.getMaxX() - r.getX()) <= r.getWidth() / 2)
				|| (Math.abs(this.getX() - r.getMaxX()) <= r.getWidth() / 2);
		// (this.getX() + 1 >= r.getMaxX()));
	}

	/**
	 * la balle touche les bordures hauts ou basses d'un rectangle dont la balle
	 * est a l'exterieur de ce rectangle ?
	 *
	 * @param r
	 *            Shape
	 * @return Oui/Non à la question
	 */
	public boolean isTouchBorderOuterShapeY(Shape r) {
		return (Math.abs(this.getMaxY() - r.getY()) <= r.getHeight() / 2)
				|| (Math.abs(this.getY() - r.getMaxY()) <= r.getHeight() / 2);
	}

	/**
	 * la balle est au dessus du rectangle
	 *
	 * @param r
	 *            Shape
	 * @return Oui/Non à la question
	 */
	public boolean isBallAboveShape(Shape r) {
		return (this.getY() > r.getY());
	}

	/**
	 * la balle est en dessous du rectangle
	 *
	 * @param r
	 *            Shape
	 * @return Oui/Non à la question
	 */
	public boolean isBallUnderShape(Shape r) {
		return (this.getY() < r.getY());
	}

	/**
	 * la balle est Ã droite du rectangle
	 *
	 * @param r
	 *            Shape
	 * @return Oui/Non à la question
	 */
	public boolean isBallRightShape(Shape r) {
		return (this.getX() > r.getX());
	}

	/**
	 * Est-ce que la balle est Ã gauche du rectangle ?
	 *
	 * @param r
	 *            Shape
	 * @return Oui/Non à la question
	 */
	public boolean isBallLeftShape(Shape r) {
		return (this.getX() < r.getX());
	}

	/**
	 * Est-ce que la balle est dans le goal gauche pour les collisions de l'axe
	 * des X?
	 *
	 * @param f
	 *            FieldController
	 * @return Oui/Non à la question
	 */
	public boolean isTouchGoalLeft(FieldController f) {
		// Pour le goal gauche
		return (f.getGoalleft().getX() - this.getX() <= this.getWidth()
				&& f.getGoalleft().getY() <= this.getY() && f.getGoalleft()
				.getMaxY() >= this.getMaxY());
	}

	/**
	 * la balle est dans le goal droit
	 *
	 * @param f
	 *            FieldController
	 * @return boolean
	 */
	public boolean isTouchGoalRight(FieldController f) {
		return (f.getGoalright().getX() - this.getX() <= this.getWidth()
				&& f.getGoalright().getY() <= this.getY() && f.getGoalright()
				.getMaxY() >= this.getMaxY());
	}

	/**
	 * il y a un goal Ã gauche
	 *
	 * @param f
	 *            FieldController
	 * @return boolean
	 */
	public boolean isGoalLeft(FieldController f) {
		return (f.getGoalleft().getMaxX() - this.getX() >= this.getWidth());
	}

	/**
	 * il y a un goal a droite
	 *
	 * @param f
	 *            FieldController
	 * @return Oui/Non à la question
	 */
	public boolean isGoalRight(FieldController f) {
		return (this.getMaxX() - f.getGoalright().getX() >= this.getWidth());
	}

	/**
	 * la balle est dans le terrain
	 * 
	 * @param f
	 *            FieldController
	 * @return Oui/Non à la question
	 */
	public boolean isInsideField(FieldController f) {
		return this.intersects(f) || this.intersects(f.getGoalright())
				|| this.intersects(f.getGoalleft());
	}

	/**
	 * Modifie la vitesse horizontale de la balle
	 * 
	 * @param d
	 *            la nouvelle vitesse horizontale de la balle
	 */
	public void setVx(double d) {
		this.vX = d;
	}

	/**
	 * Modifie la vitesse verticale de la balle
	 * 
	 * @param d
	 *            la nouvelle vitesse verticale de la balle
	 */
	public void setVy(double d) {
		this.vY = d;
	}

	/**
	 * Récupère la vitesse horizontale de la balle
	 * 
	 * @return la vitesse horizontale actuelle de la balle
	 */
	public double getVx() {
		return this.vX;
	}

	/**
	 * Récupère la vitesse verticale de la balle
	 * 
	 * @return la vitesse verticale actuelle de la balle
	 */
	public double getVy() {
		return this.vY;
	}

	/**
	 * Modifie la position verticale Y de la balle
	 * 
	 * @param d
	 *            la nouvelle position verticale Y de la balle
	 */
	public void setY(double d) {
		this.y = d;
	}

	/**
	 * Modifie la position horizontale X de la balle
	 * 
	 * @param d
	 *            la nouvelle position horizontale X de la balle
	 */
	public void setX(double d) {
		this.x = d;
	}
}
