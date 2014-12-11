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
    public final double SPEED_COLLISION = 0.005;
    public final double VELOCITY_LIMIT = 1;

    public Ball(double x, double y, double width, double height) {
        super.setFrame(x - (width / 2), y, width, height);
        vX = 0;
        vY = 0;
        this.rect = new Shape(this.getX(),this.getY(),this.getWidth(),this.getHeight());
    }

    /**
     * Creation graphique de la balle
     */
    public void draw(Graphics2D g2) {
        g2.setColor(Color.yellow);
        g2.draw(this);
        g2.fill(this);
        g2.draw(this.rect);
    }

    /**
     * Methode principale de la balle utilisÃ© par actionPerformed : applique et
     * modifie, si nÃ©cessaire, le mouvement aprÃ¨s un check de toutes les
     * collisions possibles.
     *
     * @param f
     * @param p
     */
    public void move(FieldController f, PlayerController p) {
        
        //Applique le mouvement de la balle si celle-ci est dans le terrain
        if (this.intersects(f)
                || this.intersects(f.getGoalright()) || this.intersects(f.getGoalleft())) {
            this.setMovement();
        }
        
        //Sinon, check collisions :
        this.checkCollisionField(f, p);
        this.checkCollisionPlayer(f, p);
        
    }
    
    /**
     * Repositionne la balle
     */
    public void setMovement() {
        this.setFrame(this.getX() + this.getVx(), this.getY() + this.getVy(), this.getWidth(), this.getHeight());
        this.rect.setFrame(this.getFrame());
    }

    /**
     * Applique un freinage Ã  la balle, limite la vitesse Ã  5
     */
    public void brake() {
        
        //Freinage
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
        //Limite de la vitesse
        if(this.getVy() > VELOCITY_LIMIT){
            this.setVy(VELOCITY_LIMIT);
        }
        
        if(this.getVx() > VELOCITY_LIMIT){
            this.setVx(VELOCITY_LIMIT);
        }
        
    }

    /**
     * Repositionne la balle au centre du field et rÃ©initialise vX et vY Ã  0
     *
     * @param f Field
     */
    public void centerBall(FieldController f) {
        this.setFrame(f.getCenterX() - this.getWidth() / 2,
                f.getY() + (f.getHeight() / 2) - 10, width, height);
        this.setVx(STOP);
        this.setVy(STOP);
    }

    /**
     * Modifie la vitesse de la balle en rapport Ã  la position du joueur p
     *
     * @param speed
     * @param p
     */
    public void modifySpeed(double speed, PlayerController p) {
        if (this.isBallRightShape(p)) {
            this.setVx(this.getVx()
                    + Math.abs(this.getX() - p.getX()) * speed);
        } else if (this.isBallLeftShape(p)) {
            this.setVx(this.getVx()
                    - Math.abs(this.getX() - p.getX()) * speed);
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
     * @param p Player
     * @param f Field
     */
    public void shootBall(FieldController f, PlayerController p) {
        if (p.near(this.rect)){
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
    public void goal(FieldController f, PlayerController p) {
        if (this.isGoalLeft(f) || this.isGoalRight(f)) {
            this.centerBall(f);
            p.setScore(p.getScore() + 1);
        }
    }
    
    /**
     * Verifie la collision joueur-balle
     * @param f Field
     * @param p Player
     */
    public void checkCollisionPlayer(FieldController f,PlayerController p){
        if (this.intersects(p)) {
            if (this.isTouchBorderOuterShapeX(p)) {
                if (this.isBallLeftShape(p)) {
                    if ((!(this.rect.touchRectInLeft(f)
                            || this.rect.touchRectInRight(f))
                            || this.isTouchGoalLeftX(f) || this.isTouchGoalRightX(f))) {
                        this.setX(p.getEll().getX() - this.getWidth());
                    }
                    this.setVx(-vX);
                } else if (this.isBallRightShape(p)) {
                    if (!(this.rect.touchRectInLeft(f)
                            || this.rect.touchRectInRight(f))
                            || this.isTouchGoalLeftX(f) || this.isTouchGoalRightX(f)) {
                        this.setX(p.getEll().getX() + this.getWidth());
                    }
                    this.setVx(-vX);
                }
            }
            if (this.isTouchBorderOuterShapeY(p)) {
                if (this.isBallAboveShape(p)) {
                    if (!(this.rect.touchRectInBottom(f)
                            || this.rect.touchRectInTop(f))
                            || this.isTouchGoalLeftX(f) || this.isTouchGoalRightX(f)) {
                        this.setY(p.getEll().getY() + this.getHeight());
                    }
                    this.setVy(-vY);
                } else if (this.isBallUnderShape(p)) {
                    if (!(this.rect.touchRectInTop(f)
                            || this.rect.touchRectInBottom(f))
                            || this.isTouchGoalLeftX(f) || this.isTouchGoalRightX(f)) {
                        this.setY(p.getEll().getY() - this.getHeight());
                    }
                    this.setVy(-vY);
                }
            }
        }
    }
    
    /**
     * Check collision entre la balle et les terrains ( dont les goals)
     * Active la méthode goal si besoin.
     * @param f Field
     * @param p Player
     */
    public void checkCollisionField(FieldController f, PlayerController p){
        if (this.rect.touchRectInTop(f)
                || this.rect.touchRectInBottom(f)) {
            this.setVy(-vY);
        }
        if (this.rect.touchRectInLeft(f)
                || this.rect.touchRectInRight(f)) {
            if ((!this.isTouchGoalLeftX(f)) && (!this.isTouchGoalRightX(f))) {
                this.setVx(-vX);
            } else {
                this.goal(f, p);
            }
        }
    }
    
    /**
     * Est-ce que la balle touche les bordures droites ou gauches d'un rectangle
     * dont la balle est Ã  l'extÃ©rieur de ce rectangle ?
     *
     * @param r
     * @return
     */
    public boolean isTouchBorderOuterShapeX(Shape r) {
        return (Math.abs(this.getMaxX() - r.getX()) <= r.getWidth() / 2)
                || (Math.abs(this.getX() - r.getMaxX()) <= r.getWidth() / 2);
        //(this.getX() + 1 >= r.getMaxX()));
    }

    /**
     * Est-ce que la balle touche les bordures hauts ou basses d'un rectangle
     * dont la balle est Ã  l'extÃ©rieur de ce rectangle ?
     *
     * @param r
     * @return
     */
    public boolean isTouchBorderOuterShapeY(Shape r) {
        return (Math.abs(this.getMaxY() - r.getY()) <= r.getHeight() / 2)
                || (Math.abs(this.getY() - r.getMaxY()) <= r.getHeight() / 2);
    }

    /**
     * Est-ce que la balle est au dessus du rectangle ?
     *
     * @param r Shape
     * @return
     */
    public boolean isBallAboveShape(Shape r) {
        return (this.getY() > r.getY());
    }

    /**
     * Est-ce que la balle est en dessous du rectangle ?
     *
     * @param r Shape
     * @return
     */
    public boolean isBallUnderShape(Shape r) {
        return (this.getY() < r.getY());
    }

    /**
     * Est-ce que la balle est Ã  droite du rectangle ?
     *
     * @param r Shape
     * @return
     */
    public boolean isBallRightShape(Shape r) {
        return (this.getX() > r.getX());
    }

    /**
     * Est-ce que la balle est Ã  gauche du rectangle ?
     *
     * @param r Shape
     * @return
     */
    public boolean isBallLeftShape(Shape r) {
        return (this.getX() < r.getX());
    }

    /**
     * Est-ce que la balle est dans le goal gauche pour les collisions 
     * de l'axe des X?
     *
     * @param f
     * @return
     */
    public boolean isTouchGoalLeftX(FieldController f) {
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
    public boolean isTouchGoalRightX(FieldController f) {
        return (f.getGoalright().getX() - this.getX() <= this.getWidth()
                && f.getGoalright().getY() <= this.getY()
                && f.getGoalright().getMaxY() >= this.getMaxY());
    }
    
    /**
     * Est-ce qu'il y a un goal Ã  gauche ?
     * @param f
     * @return 
     */
    public boolean isGoalLeft(FieldController f) {
        return (f.getGoalleft().getMaxX() - this.getX() >= this.getWidth());
    }
    
    /**
     * Est-ce qu'il y a un goal Ã  droite ?
     * @param f
     * @return 
     */
    public boolean isGoalRight(FieldController f) {
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
}
