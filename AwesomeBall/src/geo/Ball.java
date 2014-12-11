package geo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Mouton Youri and Sias Nicolas
 *
 * TODO : Les goals
 *
 */
@SuppressWarnings("serial")
public class Ball extends Ellipse2D.Double {
    
    private double vX;
    private double vY;
    public final int STOP = 0;
    public final double SPEED_SHOOT = 0.05;
    public final double BRAKE = -0.005;
    public final double SPEED_COLLISION = 0.005;

    public Ball(double x, double y, double width, double height) {
        super.setFrame(x - (width / 2), y, width, height);
        vX = 0;
        vY = 0;
    }

    /**
     * Création graphique de la balle
     */
    public void draw(Graphics2D g2) {
        this.setFrame(this.getBounds2D());
        g2.setColor(Color.yellow);
        g2.draw(this);
        g2.fill(this);
    }

    /**
     * Methode principale de la balle utilisé par actionPerformed : applique et
     * modifie, si nécessaire, le mouvement après un check de toutes les
     * collisions possibles.
     *
     * @param f
     * @param p
     */
    public void move(Field f, Player p) {
        
        //Applique le mouvement de la balle si celle-ci est dans le terrain
        if (this.intersects(f)
                || this.intersects(f.getGoalright()) || this.intersects(f.getGoalleft())) {
            this.setMovement();
        }
        
        //Sinon, check collisions :
        //Collision Field - Ball :
        if (this.IsTouchBorderInnerShapeTop(f)
                || this.IsTouchBorderInnerShapeBottom(f)) {
            this.setVy(-vY);
        }
        if (this.IsTouchBorderInnerShapeLeft(f)
                || this.IsTouchBorderInnerShapeRight(f)) {
            if ((!this.IsTouchGoalLeftX(f)) && (!this.IsTouchGoalRightX(f))) {
                this.setVx(-vX);
            } else {
                this.goal(f, p);
            }
        }
        
        //Collision Player - Ball :
        if (this.intersects(p)) {
            if (this.IsTouchBorderOuterShapeX(p)) {
                if (this.IsBallLeftShape(p)) {
                    if ((!(this.IsTouchBorderInnerShapeLeft(f)
                            || this.IsTouchBorderInnerShapeRight(f))
                            || this.IsTouchGoalLeftX(f) || this.IsTouchGoalRightX(f))) {
                        this.setX(p.getEll().getX() - this.getWidth());
                    }
                    this.setVx(-vX);
                } else if (this.IsBallRightShape(p)) {
                    if (!(this.IsTouchBorderInnerShapeLeft(f)
                            || this.IsTouchBorderInnerShapeRight(f))
                            || this.IsTouchGoalLeftX(f) || this.IsTouchGoalRightX(f)) {
                        this.setX(p.getEll().getX() + this.getWidth());
                    }
                    this.setVx(-vX);
                }
            }
            if (this.IsTouchBorderOuterShapeY(p)) {
                if (this.IsBallAboveShape(p)) {
                    if (!(this.IsTouchBorderInnerShapeBottom(f)
                            || this.IsTouchBorderInnerShapeTop(f))
                            || this.IsTouchGoalLeftX(f) || this.IsTouchGoalRightX(f)) {
                        this.setY(p.getEll().getY() + this.getHeight());
                    }
                    this.setVy(-vY);
                } else if (this.IsBallUnderShape(p)) {
                    if (!(this.IsTouchBorderInnerShapeTop(f)
                            || this.IsTouchBorderInnerShapeBottom(f))
                            || this.IsTouchGoalLeftX(f) || this.IsTouchGoalRightX(f)) {
                        this.setY(p.getEll().getY() - this.getHeight());
                    }
                    this.setVy(-vY);
                }
            }
        }
    }

    /**
     * Repositionne la balle
     */
    public void setMovement() {
        this.setFrame(this.getX() + this.getVx(), this.getY() + this.getVy(), this.getWidth(), this.getHeight());
    }

    /**
     * Applique un freinage à la balle
     */
    public void brake() {
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
    }

    /**
     * Repositionne la balle au centre du field et réinitialise vX et vY à 0
     *
     * @param f Field
     */
    public void centerBall(Field f) {
        this.setFrame(f.getCenterX() - this.getWidth() / 2,
                f.getY() + (f.getHeight() / 2) - 10, width, height);
        this.setVx(STOP);
        this.setVy(STOP);
    }

    /**
     * Modifie la vitesse de la balle en rapport à la position du joueur p
     *
     * @param speed
     * @param p
     */
    public void modifySpeed(double speed, Player p) {
        if (this.IsBallRightShape(p)) {
            this.setVx(this.getVx()
                    + Math.abs(this.getX() - p.getX()) * speed);
        } else if (this.IsBallLeftShape(p)) {
            this.setVx(this.getVx()
                    - Math.abs(this.getX() - p.getX()) * speed);
        }
        if (this.IsBallAboveShape(p)) {
            this.setVy(this.getVy()
                    + (Math.abs(this.getY() - p.getY()) * speed));
        } else if (this.IsBallUnderShape(p)) {
            this.setVy(this.getVy()
                    - (Math.abs(this.getY() - p.getY()) * speed));
        }
    }

    /**
     * Methode pour le tir de la balle
     *
     * @param p Player
     * @param f Field
     */
    public void shootBall(Field f, Player p) {
        if (p.near(new Rectangle2D.Double(this.getX(), this.getY(), this.getWidth(),this.getHeight()))) {
            this.modifySpeed(SPEED_SHOOT, p);
        }
    }

    /**
     * Augmenter le score du joueur de 1 et recentre la balle A MODIFIER QUAND
     * CE SERA MULTIJOUEUR ?
     *
     * @param f Field
     * @param p Player
     */
    public void goal(Field f, Player p) {
        if (this.IsGoalLeft(f) || this.IsGoalRight(f)) {
            this.centerBall(f);
            p.setScore(p.getScore() + 1);
        }
    }

     /**
     * Est-ce que la balle touche les bordures bas d'un rectangle dont la balle
     * est à l'intérieur de ce rectangle ?
     * @param r
     * @return 
     */
    public boolean IsTouchBorderInnerShapeBottom(Shape r) {
        return (this.getMaxY() - 1 > r.getMaxY());
    }

    /**
     * Est-ce que la balle touche les bordures hauts d'un rectangle dont la
     * balle est à l'extérieur de ce rectangle ?
     *
     * @param r Shape
     * @return
     */
    public boolean IsTouchBorderInnerShapeTop(Shape r) {
        return (this.getY() - 1 < r.getY()); //Top
    }

    /**
     * Est-ce que la balle touche les bordures gauches d'un rectangle dont la
     * balle est à l'intérieur de ce rectangle ?
     *
     * @param r Shape
     * @return true if collision ball-right or left border, false if not
     */
    public boolean IsTouchBorderInnerShapeLeft(Shape r) {
        return (this.getX() + 1 < r.getX()); //Right
    }

    /**
     * Est-ce que la balle touche les bordures droites d'un rectangle dont la
     * balle est à l'intérieur de ce rectangle ?
     *
     * @param r Shape
     * @return true if collision ball-right or left border, false if not
     */
    public boolean IsTouchBorderInnerShapeRight(Shape r) {
        return (this.getMaxX() + 1 > r.getMaxX()); //Right
    }

    /**
     * Est-ce que la balle touche les bordures droites ou gauches d'un rectangle
     * dont la balle est à l'extérieur de ce rectangle ?
     *
     * @param r
     * @return
     */
    public boolean IsTouchBorderOuterShapeX(Shape r) {
        return (Math.abs(this.getMaxX() - r.getX()) <= r.getWidth() / 2)
                || (Math.abs(this.getX() - r.getMaxX()) <= r.getWidth() / 2);
        //(this.getX() + 1 >= r.getMaxX()));
    }

    /**
     * Est-ce que la balle touche les bordures hauts ou basses d'un rectangle
     * dont la balle est à l'extérieur de ce rectangle ?
     *
     * @param r
     * @return
     */
    public boolean IsTouchBorderOuterShapeY(Shape r) {
        return (Math.abs(this.getMaxY() - r.getY()) <= r.getHeight() / 2)
                || (Math.abs(this.getY() - r.getMaxY()) <= r.getHeight() / 2);
    }

    /**
     * Est-ce que la balle est au dessus du rectangle ?
     *
     * @param r Shape
     * @return
     */
    public boolean IsBallAboveShape(Shape r) {
        return (this.getY() > r.getY());
    }

    /**
     * Est-ce que la balle est en dessous du rectangle ?
     *
     * @param r Shape
     * @return
     */
    public boolean IsBallUnderShape(Shape r) {
        return (this.getY() < r.getY());
    }

    /**
     * Est-ce que la balle est à droite du rectangle ?
     *
     * @param r Shape
     * @return
     */
    public boolean IsBallRightShape(Shape r) {
        return (this.getX() > r.getX());
    }

    /**
     * Est-ce que la balle est à gauche du rectangle ?
     *
     * @param r Shape
     * @return
     */
    public boolean IsBallLeftShape(Shape r) {
        return (this.getX() < r.getX());
    }

    /**
     * Est-ce que la balle est dans le goal gauche pour les collisions 
     * de l'axe des X?
     *
     * @param f
     * @return
     */
    public boolean IsTouchGoalLeftX(Field f) {
        return (f.getGoalleft().getX() - this.getX() <= this.getWidth()
                && f.getGoalleft().getY() <= this.getY()
                && f.getGoalleft().getMaxY() >= this.getMaxY());
    }

    /**
     * Est-ce que la balle est dans le goal droit pour les collisions de
     * l'axe des X ?
     *
     * @param f
     * @return
     */
    public boolean IsTouchGoalRightX(Field f) {
        return (f.getGoalright().getX() - this.getX() <= this.getWidth()
                && f.getGoalright().getY() <= this.getY()
                && f.getGoalright().getMaxY() >= this.getMaxY());
    }
    /**
     * Est-ce qu'il y a un goal à gauche ?
     * @param f
     * @return 
     */
    public boolean IsGoalLeft(Field f) {
        return (f.getGoalleft().getMaxX() - this.getX() >= this.getWidth());
    }
    /**
     * Est-ce qu'il y a un goal à droite ?
     * @param f
     * @return 
     */
    public boolean IsGoalRight(Field f) {
        return (this.getMaxX() - f.getGoalright().getX() >= this.getWidth());
    }
    public void setVx(double d) {
        this.vX = d;
    }

    public void setVy(double d) {
        this.vY = d;
    }

    public double getVx() {
        return this.vX;
    }

    public double getVy() {
        return this.vY;
    }

    public void setY(double d) {
        this.y = d;
    }

    public void setX(double d) {
        this.x = d;
    }
    
    public boolean near(Shape s) {
		// top or down side of the ball
		// XXX replace with distance?
		return (this.getY()    - 5 <= s.getMaxY() &&
				this.getMaxY() + 5 >= s.getY()    &&
				this.getX()    - 5 <= s.getMaxX() &&
				this.getMaxX() + 5 >= s.getX());
}


}


/*
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

 */
