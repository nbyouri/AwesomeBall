package geo;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

@SuppressWarnings("serial")
public class FieldController extends FieldModel {
	
	public FieldController(double x, double y, double width, double height) {
		super(x, y, width, height);
	}
	/**
         * Récupère le goal gauche
         * @return goalleft
         */
	public Rectangle2D getGoalleft() {
		return goalleft;
	}
        /**
         * Modifie le goal gauche
         * @param goalleft 
         */
	public void setGoalleft(Rectangle2D goalleft) {
		this.goalleft = goalleft;
	}
        /**
         * Récupère le goal droit
         * @return goalright
         */
	public Rectangle2D getGoalright() {
		return goalright;
	}
        /**
         * Modifie le goal droit
         * @param goalright 
         */
	public void setGoalright(Rectangle2D goalright) {
		this.goalright = goalright;
	}

	/**
         * Modifie la taille du terrain et donc les goals
         * @param x Position x
         * @param y Position y
         * @param width Largeur
         * @param height Hauteur
	 */
	public void setSize(double x, double y, double width, double height) {
		super.setRect(x, y, width, height);

		double goalW = GOALS_WIDTH;
		double goalH = height / 3;
		double goalY = this.getY() + goalH;
		double goalMaxY = this.getY() + (2 * goalH);
		double goalLX = x - goalW;
		double goalRX = x + width;
                
                //Initialisation les rectangles des goals.
		goalleft.setRect(goalLX, goalY, goalW, goalH);
		goalright.setRect(goalRX, goalY, goalW, goalH);

		// Lignes du goal gauche :
		// 1) ligne verticale
		this.getSides().add(new Line2D.Double(x, goalY, x, goalMaxY));
		// 2) ligne du haut
		this.getSides().add(new Line2D.Double(x, this.getY(), x, goalY));
		// 3) ligne du bas
		this.getSides().add(new Line2D.Double(x, goalMaxY, x, this.getMaxY()));


		// Lignes du goal droit :
                // 1) ligne verticale
		this.getSides().add(new Line2D.Double(this.getMaxX(), goalY, 
				this.getMaxX(), goalMaxY));

		// 2) ligne du haut 
		this.getSides().add(new Line2D.Double(this.getMaxX(), this.getY(), 
				this.getMaxX(), goalY));

		// 3) ligne du bas
		this.getSides().add(new Line2D.Double(getMaxX(), goalMaxY, 
				this.getMaxX(), this.getMaxY()));
	}

	/**
         * Modifie la taille du cercle centrale du terrain proportionnellement
         * à la taille du terrain. PS : son diamètre est égale à 1/6 de la 
         * hauteur du terrain
	 */
	public void setCenterCircle() {
		double centerX = this.getCenterX();
		double centerY = this.getCenterY();

		double radius  = (this.getHeight() / 6)/2;
		this.center.setFrameFromCenter(centerX, centerY, 
				centerX + radius, centerY + radius);
	}
}
