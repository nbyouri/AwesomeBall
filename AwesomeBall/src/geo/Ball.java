package geo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

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
        // Initialisation de la balle
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
     * Methode principale de la balle utilisé par actionPerformed :
     * applique et modifie, si nécessaire,
     * le mouvement après un check de toutes les collisions possibles.
     * @param f
     * @param p 
     */
    public void move(Field f, Player p){
        //Collision Field - Ball :
        if(this.IsTouchBorderInnerShapeY(f)){
            this.setVy(-vY);
        }
        if(this.IsTouchBorderInnerShapeX(f)){
            this.setVx(-vX);
        }
        //Collision Joueur - Ball :
        if(this.intersects(p)){
            if(this.IsTouchBorderOuterShapeX(p)){
                if(this.IsBallLeftShape(p)){
                    if(!this.IsTouchBorderInnerShapeX(f)){
                        this.setX(p.getEll().getX() - this.getWidth());
                    }
                        this.setVx(-vX);
                }
                else if (this.IsBallRightShape(p)){
                    if(!this.IsTouchBorderInnerShapeX(f)){
                        this.setX(p.getEll().getX() + this.getWidth());
                    }
                        this.setVx(-vX);
                }
            }
            if(this.IsTouchBorderOuterShapeY(p)){
                if(this.IsBallAboveShape(p)){
                  if(!this.IsTouchBorderInnerShapeY(f)){
                  this.setY(p.getEll().getY()+ this.getHeight());
                  }
                      this.setVy(-vY);
                }
                else if(this.IsBallUnderShape(p)){
                    if(!this.IsTouchBorderInnerShapeY(f)){
                    this.setY(p.getEll().getY() - this.getHeight());
                    }
                        this.setVy(-vY);
                }
            }
        }
        //Après tous ces checks de collision, applique un mouvement
        this.setMovement();
    }
    
        ///////// ====> METHODES SECONDAIRES <==== //////////
    
    /**
     * Repositionne la balle
     */
    public void setMovement(){
        this.setFrame( this.getX() + this.getVx(), this.getY() + this.getVy(), this.getWidth(), this.getHeight());
    }
    
    /**
     * Applique un freinage à la balle
     */
    public void brake(){
        if(this.getVx() !=0){
            if(this.getVx() > 0){
                this.setVx(vX + BRAKE);
            }
            if (this.getVx() < 0){
                this.setVx(vX - BRAKE);
            }
        }
        if(this.getVy() != 0){
            if(this.getVy() > 0){
                this.setVy(vY + BRAKE);
            }
            if(this.getVy() < 0){
                this.setVy(vY - BRAKE);
            }
        }
    }
    
    /**
     * Repositionne la balle au centre du field et
     * réinitialise vX et vY à 0
     * @param f Field
     */
    public void centerBall(Field f){
        this.setFrame(f.getCenterX()-this.getWidth()/ 2, 
                    f.getY() + (f.getHeight() / 2) - 10, width, height);
        this.setVx(STOP);
        this.setVy(STOP);
    }
    /**
     * Modifie la vitesse de la balle en rapport à la position du joueur p
     * @param speed
     * @param p 
     */
    public void modifySpeed(double speed, Player p){
        if(this.IsBallRightShape(p)){
            this.setVx(this.getVx() +
                    Math.abs(this.getX()-p.getX())*speed);
        }
        else if(this.IsBallLeftShape(p)){
            this.setVx(this.getVx() -
                    Math.abs(this.getX()-p.getX())*speed);
        }
        if(this.IsBallAboveShape(p)){
            this.setVy(this.getVy() +
                   (Math.abs(this.getY()-p.getY())*speed));
        }
        else if(this.IsBallUnderShape(p)){
            this.setVy(this.getVy() -
                    (Math.abs(this.getY()-p.getY())*speed));
        }
    }
    /**
     * Methode pour le tir de la balle
     * @param p Player
     * @param f Field
     */
    public void shootBall(Field f, Player p){
       if(this.IsTouchBorderOuterShapeX(p) || this.IsTouchBorderOuterShapeY(p)){ 
           this.modifySpeed(SPEED_SHOOT,p);
       }
    }
    /**
     * Augmenter le score du joueur de 1
     * A MODIFIER QUAND CE SERA MULTIJOUEUR ?
     * @param p Player
     */
    public void goal(Player p) {
            p.setScore(p.getScore() + 1);
    }
    
            ////////===>LES METHODES CHECKS<====////////
    /**
     * Est-ce que la balle touche les bordures haut ou dessous d'un rectangle
     * dont la balle est à l'intérieur de ce rectangle ?
     * @param r Shape
     * @return 
     */
    public boolean IsTouchBorderInnerShapeY(Shape r){
        return (this.getMaxY() + 1 > r.getMaxY()) //Bottom
                || (this.getY() - 1 < r.getY()); //Top
    }
    /**
     * Est-ce que la balle touche les bordures droite ou gauches d'un rectangle
     * dont la balle est à l'intérieur de ce rectangle ?
     * @param r Shape
     * @return true if collision ball-right or left border, false if not
     */
    public boolean IsTouchBorderInnerShapeX(Shape r){
        return ( (this.getX() + 1 < r.getX() )|| //Left
                (this.getMaxX() + 1 > r.getMaxX()) ); //Right
    }
    
    public boolean IsTouchBorderOuterShapeX(Shape r){
        return (Math.abs(this.getMaxX() - r.getX()) <= r.getWidth()/2) ||
                (Math.abs(this.getX() - r.getMaxX()) <= r.getWidth()/2);
                //(this.getX() + 1 >= r.getMaxX()));
    }
    
    public boolean IsTouchBorderOuterShapeY(Shape r){
        return (Math.abs(this.getMaxY() - r.getY()) <= r.getHeight()/2) ||
                (Math.abs(this.getY() - r.getMaxY()) <= r.getHeight()/2);
    }
    
    /**
     * Est-ce que la balle est au dessus du rectangle ?
     * @param r Shape
     * @return
     */
    public boolean IsBallAboveShape(Shape r){
        return(this.getY() > r.getY());
    }
    /**
     * Est-ce que la balle est en dessous du rectangle ?
     * @param r Shape
     * @return 
     */
    public boolean IsBallUnderShape(Shape r) {
        return(this.getY() < r.getY());
    }
    /**
     * Est-ce que la balle est à droite du rectangle ?
     * @param r Shape
     * @return 
     */
    public boolean IsBallRightShape(Shape r){
        return(this.getX() > r.getX());
    }
    /**
     * Est-ce que la balle est à gauche du rectangle ?
     * @param r Shape
     * @return 
     */
    public boolean IsBallLeftShape(Shape r){
        return (this.getX() < r.getX());
    }
    
    /**
     * Est-ce que la balle est dans le goal gauche?
     * @param f
     * @return 
     */
    public boolean IsGoalLeft(Field f){
        return true;
    }
    /**
     * Est-ce que la balle est dans le goal droit?
     * @param f
     * @return 
     */
    public boolean IsGoalRight(Field f){
        return true;
    }
    
        //////====>LES GETTERS ET SETTERS <====///////
    
   public void setVx(double d){
       this.vX = d;
   }
   
   public void setVy(double d){
       this.vY = d;
   }
   
   public double getVx(){
       return this.vX;
   }
   
   public double getVy(){
       return this.vY;
   }
   public double getX(){
       return this.x;
   }
   public double getY(){
       return this.y;
   }
   public void setY(double d){
       this.y = d;
   }
   public void setX(double d){
       this.x = d;
   }
}


/*

 public int collisionShape (Shape rect)
{
    double circleDistanceX = Math.abs(this.x - rect.x);
    double circleDistanceY = Math.abs(this.y - rect.y);

    if (circleDistanceX > (rect.width/2 + this.width/2))  
        return NO_COLLISION; 
    if (circleDistanceY > (rect.height/2 + this.height/2)) 
        return NO_COLLISION;
    if (circleDistanceX <= rect.width/2) {
        System.out.println("COLLISION X");
       return COLLISION_X;
    }
    if (circleDistanceY <= rect.height/2){
        System.out.println("COLLISION Y");
        return COLLISION_Y;
    }

    double cornerDistanceSQR = ((circleDistanceX - rect.width/2) * (circleDistanceX - rect.width/2) )+
                         ((circleDistanceY - rect.height/2) * (circleDistanceY - rect.height/2));

    if (cornerDistanceSQR <= ((this.getWidth()/2) * (this.getWidth()/2))){
        System.out.println("COLLISION CORNER");
        return COLLISION_CORNER;
    }
    else 
        return NO_COLLISION;
}
*/
