package geo;

import geo.Shape.Side;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * Classe Balle, gère les collisions avec les joueurs et les bords du terrain.
 * Les joueurs peuvent marquer.
 * 
 * @author Mouton Youri and Sias Nicolas
 */
@SuppressWarnings("serial")
public class Ball extends Ellipse2D.Double {

	public Shape rect;
	private double Dx;
	private double Dy;

	public static final int BALL_SIZE = 15;
	public static final double BALL_RADIUS = BALL_SIZE / 2;
	public static final int STOP = 0;
	public static final double SPEED_ONE = 0.5;
	public static final double SPEED_ONE_DIAG = PlayerModel.SPEED_ONE_DIAG;
	public static final double SPEED_SHOOT = 0.05;
	public static final double BRAKE = -0.005;
	public static final double VELOCITY_LIMIT = 2;

	public Ball(FieldController field) {
		super.setFrame(field.getCenterX() - BALL_RADIUS, field.getCenterY()
				- BALL_RADIUS, BALL_SIZE, BALL_SIZE);
		Dx = STOP;
		Dy = STOP;
		this.rect = new Shape(this.getX(), this.getY(), this.getWidth(),
				this.getHeight());
	}

	public void setDx(double d) {
		this.Dx = d;
	}

	public void setDy(double d) {
		this.Dy = d;
	}

	public double getDx() {
		return this.Dx;
	}

	public double getDy() {
		return this.Dy;
	}

	public void setY(double d) {
		this.y = d;
	}

	public void setX(double d) {
		this.x = d;
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
	 * Met a jour la position de la balle.
	 * 
	 * @param x
	 *            double
	 * @param y
	 *            double
	 */
	public void setLocation(double x, double y) {
		this.setFrame(x, y, this.getWidth(), this.getHeight());
		this.rect.setFrame(this.getFrame());
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
		/*
		 * Collisions Terrain
		 */
		if (this.touchRectInLeft(f)) {
			this.setDx(SPEED_ONE);
		}
		if (this.rect.touchRectInTop(f) || this.touchGoalRightTop(f)
				|| this.touchGoalLeftTop(f)) {
			this.setDy(SPEED_ONE);
		}
		if (this.touchRectInRight(f)) {
			this.setDx(-SPEED_ONE);
		}
		if (this.rect.touchRectInBottom(f) || this.touchGoalRightBottom(f)
				|| this.touchGoalLeftBottom(f)) {
			this.setDy(-SPEED_ONE);
		}

		/*
		 * Collisions Joueurs
		 */
		if (this.rect.near((Rectangle2D) p1, Side.RIGHT.getId())) {
			p1.setDx(p2.movingWithBall(this) ? SPEED_ONE : STOP);
			this.setDx(SPEED_ONE);
		}
		if (this.rect.near((Rectangle2D) p1, Side.UP.getId())) {
			p1.setDy(p2.movingWithBall(this) ? -SPEED_ONE : STOP);
			this.setDy(-SPEED_ONE);
		}
		if (this.rect.near((Rectangle2D) p1, Side.LEFT.getId())) {
			p1.setDx(p2.movingWithBall(this) ? -SPEED_ONE : STOP);
			this.setDx(-SPEED_ONE);
		}
		if (this.rect.near((Rectangle2D) p1, Side.DOWN.getId())) {
			p1.setDy(p2.movingWithBall(this) ? SPEED_ONE : STOP);
			this.setDy(SPEED_ONE);
		}
		if (this.rect.near((Rectangle2D) p2, Side.RIGHT.getId())) {
			p2.setDx(p1.movingWithBall(this) ? SPEED_ONE : STOP);
			this.setDx(SPEED_ONE);
		}
		if (this.rect.near((Rectangle2D) p2, Side.UP.getId())) {
			p2.setDy(p1.movingWithBall(this) ? -SPEED_ONE : STOP);
			this.setDy(-SPEED_ONE);
		}
		if (this.rect.near((Rectangle2D) p2, Side.LEFT.getId())) {
			p2.setDx(p1.movingWithBall(this) ? -SPEED_ONE : STOP);
			this.setDx(-SPEED_ONE);
		}
		if (this.rect.near((Rectangle2D) p2, Side.DOWN.getId())) {
			p2.setDy(p1.movingWithBall(this) ? SPEED_ONE : STOP);
			this.setDy(SPEED_ONE);
		}

		/*
		 * test goal
		 */
		this.goal(f, p1, p2);

		/*
		 * bouger la balle
		 */
		if (this.rect.insideRect(f) || this.insideGoals(f)) {
			this.setLocation(this.getX() + this.getDx(),
					this.getY() + this.getDy());
		}

		this.brake();
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
	 * Applique un freinage a la balle
	 */
	public void brake() {
		if (this.getDx() != 0 && this.getDx() < VELOCITY_LIMIT) {
			if (this.getDx() > 0) {
				this.setDx(Dx + BRAKE);
			}
			if (this.getDx() < 0) {
				this.setDx(Dx - BRAKE);
			}
		}
		if (this.getDy() != 0 && this.getDy() < VELOCITY_LIMIT) {
			if (this.getDy() > 0) {
				this.setDy(Dy + BRAKE);
			}
			if (this.getDy() < 0) {
				this.setDy(Dy - BRAKE);
			}
		}
	}

	/**
	 * Repositionne la balle au centre du field et rÃ©initialise Dx et Dy a 0
	 *
	 * @param f
	 *            FieldController
	 */
	public void centerBall(FieldController f) {
		this.setFrame(f.getCenterX() - this.getWidth() / 2,
				f.getY() + (f.getHeight() / 2) - 10, width, height);
		this.setDx(STOP);
		this.setDy(STOP);
	}

	/**
	 * Methode pour le tir de la balle
	 *
	 * @param p
	 *            PlayerController
	 * @param f
	 *            FieldController
	 */
	public void shoot(FieldController f, PlayerController p) {
		int dist = 4;
		if (p.nearInPixels(this.rect, Side.LEFT.getId(), dist)) {
			this.setDx(this.getDx() + Math.abs(this.getX() - p.getX())
					* SPEED_SHOOT);
		} else if (p.nearInPixels(this.rect, Side.RIGHT.getId(), dist)) {
			this.setDx(this.getDx() - Math.abs(this.getX() - p.getX())
					* SPEED_SHOOT);
		}
		if (p.nearInPixels(this.rect, Side.DOWN.getId(), dist)) {
			this.setDy(this.getDy()
					+ (Math.abs(this.getY() - p.getY()) * SPEED_SHOOT));
		} else if (p.nearInPixels(this.rect, Side.UP.getId(), dist)) {
			this.setDy(this.getDy()
					- (Math.abs(this.getY() - p.getY()) * SPEED_SHOOT));
		}
		this.setLocation(this.getX(), this.getY());
		this.brake();
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
			p2.setScore(p2.getScore() + 1);
		} else if (this.isGoalRight(f)) {
			this.centerBall(f);
			p1.setScore(p1.getScore() + 1);
		}
	}

	/**
	 * il y a un goal Ã gauche
	 *
	 * @param f
	 *            FieldController
	 * @return boolean
	 */
	public boolean isGoalLeft(FieldController f) {
		return (this.getMaxX() <= f.getGoalleft().getMaxX());
	}

	/**
	 * il y a un goal a droite
	 *
	 * @param f
	 *            FieldController
	 * @return Oui/Non à la question
	 */
	public boolean isGoalRight(FieldController f) {
		return (this.getX() >= f.getGoalright().getX());
	}
}
