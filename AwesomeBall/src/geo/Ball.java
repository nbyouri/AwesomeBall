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
    private final int NO_COLLISION = 0;
    private final int COLLISION_X = 1;
    private final int COLLISION_Y = 2;
    private final int COLLISION_CORNER = 3;
    private final double SPEED_SHOT_X = 0.05;
    private final double SPEED_SHOT_Y = 0.05;
    private final double BRAKE = 0.001;
    private double vX; // Velocity on axis X
    private double vY;// Velocity on axis Y

    public Ball(double x, double y, double width, double height) {
        // ball initialization
        super.setFrame(x - (width / 2), y, width, height);
        this.setVx(0);
        this.setVy(0);

    }

    /**
     * Drawing the ball
     */
    public void draw(Graphics2D g2) {
        this.setFrame(this.getBounds2D());
        g2.setColor(Color.yellow);
        g2.draw(this);
        g2.fill(this);
    }

    /**
     *
     * Put the ball in the center of the field.
     *
     * @param f
     */
    public void centerBall(Field f) {
        this.setX(f.getCenterX() - this.getWidth() / 2);
        this.setY(f.getY() + (f.getHeight() / 2) - 10);
        this.setVx(0);
        this.setVy(0);
    }

    /**
     *
     * Score of player is incremented if the ball goes in either goal.
     *
     * XXX : Implement side detection for multiplayer.
     *
     * @param p
     * @param f
     */
    public void goal(Player p, Field f) {
        //if  (this.touchRectInRight(f) || this.touchRectInLeft(f)) {
        p.setScore(p.getScore() + 1);
        this.centerBall(f);
        //}
    }

    /**
     * Does the ball's right side touch the goal's line ?
     *
     * @param f
     * @return true if the ball is in the right goal
     */
    public boolean touchGoalRight(Field f) {
        //return (this.getMaxX() + 5 < f.getGoalright().getMaxX());
        return this.intersects(f.getGoalright());
		    //return this.intersects(f.getGoalright().getX(),f.getGoalright().getY(),
        //              f.getGoalright().getWidth(),f.getGoalright().getHeight());
    }

    /**
     * Does the ball's left side touch the goal's line ?
     *
     * @param f
     * @return true if the ball is in the left goal
     */
    public boolean touchGoalLeft(Field f) {
        //return (this.getX() + 5 > f.getGoalleft().getX());
        return this.intersects(f.getGoalleft());
                //return this.intersects(f.getGoalleft().getX(),f.getGoalleft().getY(),
        //                        f.getGoalleft().getWidth(),f.getGoalleft().getHeight());
    }

    /**
     * Does the ball touch borders above and unders both goals ?
     *
     * @param f
     * @return true if the ball is still the field, false if not
     */
    public boolean insideField(Field f) {
        return (this.intersects(f.getX(), f.getY(), f.getWidth(), f.getHeight()));
    }

    /**
     *
     * @param p
     * @return true if ball is touched by a shape
     */
    public boolean touchPlayer(Player p) {
        return (this.intersects(p.getX() + 2, p.getY() + 2, p.getWidth() + 2, p.getHeight() + 2));
               //return( (touchRectInBottom(p)) || (touchRectInTop(p)) 
        //       || (touchRectInLeft(p)) || (touchRectInRight(p)) );
    }

/////////////////////
    public double getVx() {
        return vX;
    }

    public double getVy() {
        return vY;
    }

    public void setVx(double v) {
        vX = v;
    }

    public void setVy(double v) {
        vY = v;
    }

    public void setX(double newX) {
        this.x = newX;
    }

    public void setY(double newY) {
        this.y = newY;
    }

    public void shootBall(Field f, Player p) {
        if (touchPlayer(p)) {
            this.moveBall(p);
        }
    }

    /**
     * Ball's moving after a shooting, adding speed
     *
     * @param p
     */
    public void moveBall(Player p) {
        double vitX = 0;
        double vitY = 0;
        if (x < p.getX() + p.getWidth() / 8) // when the ball moves right and/or hits the right              
        {
           // vX is the ball velocity on X axis
            // set new velocity on x axis but convert it to negative
            // so the ball will move left
            vitX = vX - 1 * SPEED_SHOT_X;
            System.out.println("LEFT");
        } else if (x > p.getX() + p.getWidth() / 8) // when the ball move left and 
        //hits the left border of the shape
        {
           // set new velocity (Note that I didn't convert it to negative 
            // because i'm getting a positive value)
            vitX = vX + SPEED_SHOT_X;
            System.out.println("RIGHT");
        }

        if (y < p.getY() + p.getHeight() / 8) // when the ball hits the bottom border of the  
        //shape
        {
           // vY is the ball velocity on Y axis
            // set new velocity and convert it to negative 
            // so the ball will move up
            vitY = vY - 1 * SPEED_SHOT_Y;
            System.out.println("TOP");
        } else if (y > p.getY() + p.getHeight() / 8) // when the ball hits
        //the upper border of the shape
        {
           // set new velocity on Y axis
            // so the ball will move down
            vitY = vY + SPEED_SHOT_Y;
            System.out.println("BOTTOM");
        }
        this.setVx(vitX);
        this.setVy(vitY);
    }

    /**
     * Localisation of the ball
     */
    public void setMovement() {
        this.setFrame(x + vX, y + vY, width, height);
    }

    /**
     * Function using to slow the ball
     */

    public void brake() {
        if (vX != 0) {
            if (vX > 0) {
                this.setVx(vX - BRAKE);
            }
            if (vX < 0) {
                this.setVx(vX + BRAKE);
            }
        }
        if (vY != 0) {
            if (vY > 0) {
                this.setVy(vY - BRAKE);
            }
            if (vY < 0) {
                this.setVy(vY + BRAKE);
            }
        }
    }

    /**
     * Move the ball in the field Detecting goal
     *
     * @param f Field
     * @param p Player
     */
    public void move(Field f, Player p) {
            //Prevent bug if it does : center the ball if it goes out the field
        if(!this.intersects(f))
            this.centerBall(f);
            //For the field : borders collision
        if (this.touchRectInLeft(f)) {
            if (!touchGoalLeft(f)) //this.setFrame(this.getX() - this.getVx(), this.getY(),width,height);
            {
                this.setVx(-vX);
            } else {
                goal(p, f);
            }
        }

        if (this.touchRectInRight(f)) {
            if (!touchGoalRight(f)) //this.setFrame(this.getX() - this.getVx(), this.getY(), width, height);
            {
                this.setVx(-vX);
            } else {
                goal(p, f);
            }
        }

        if (this.touchRectInBottom(f) || this.touchRectInTop(f)) {
            //this.setFrame(this.getX(), this.getY() - this.getVy(),width, height);
            this.setVy(-vY);
        }
        //For the player - ball collision
        if (this.collisionShape(p) == COLLISION_X)
            this.setVy(-vY + p.SPEED_ONE/2);
        if (this.collisionShape(p) == COLLISION_Y)
            this.setVx(-vX + p.SPEED_ONE/2);
        if (this.collisionShape(p) == COLLISION_CORNER){
            this.setVx(-vX + p.SPEED_ONE_DIAG/2);
            this.setVy(-vY + p.SPEED_ONE_DIAG/2);
        }
        //Just to prevent if the ball goes into the shape player
        if (this.intersects(p)){
            this.setVx(-vX);
            this.setVy(-vY);
        }
        this.setMovement();
        /*
         if (this.touchPlayer(p)) {
         if (this.touchRectInBottom(p) || this.touchRectInTop(p))
         this.setVy(-vY + p.getDy());
         if (this.touchRectInLeft(p) || this.touchRectInRight(p))
         this.setVx(-vX + p.getDx());
         }*/
    }

    /**
     * Does the ball touch or approach a shap's inner left side ?
     *
     * @param rect
     * @return
     */

    public boolean touchRectInLeft(Shape rect) {
        return (this.getX() + 1 < rect.getX());
    }

    /**
     * Does the shape touch or approach a shape's inner top side ?
     *
     * @param rect
     * @return
     */
    public boolean touchRectInTop(Shape rect) {
        return (this.getY() - 1 < rect.getY());
    }

    /**
     * Does the ball touch or approach a right border ?
     *
     * @param rect
     * @return
     */
    public boolean touchRectInRight(Shape rect) {
        return (this.getMaxX() - 1 > rect.getMaxX());
    }

    /**
     * Does the ball touch or approach a field's inner bottom side ?
     *
     * @param rect
     * @return
     */
    public boolean touchRectInBottom(Shape rect) {
        return (this.getMaxY() - 1 > rect.getMaxY());
    }
    /**
     * 
     * @param rectangle
     * @return 0 if no collision, 1 if collision on axis X, 
     *          2 if collision on axis Y, 3 if collision on both axis
     * 
     */
 public int collisionShape (Shape rect)
{
    double circleDistanceX = Math.abs(this.x - rect.x);
    double circleDistanceY = Math.abs(this.y - rect.y);

    if (circleDistanceX > (rect.width/2 + this.width/2))  return NO_COLLISION; 
    if (circleDistanceY > (rect.height/2 + this.height/2)) return NO_COLLISION; 

    if (circleDistanceX <= (rect.width/2)) return COLLISION_X; 
    if (circleDistanceY <= (rect.height/2)) return COLLISION_Y;

    double cornerDistanceSQR = ((circleDistanceX - rect.width/2) * (circleDistanceX - rect.width/2) )+
                         ((circleDistanceY - rect.height/2) * (circleDistanceY - rect.height/2));

    if (cornerDistanceSQR <= ((this.getWidth()/2) * (this.getWidth()/2))){
        System.out.println("COLLISION CORNER");
        return COLLISION_CORNER;
    }
    else 
        return NO_COLLISION;
}
}
