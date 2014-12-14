package geo;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

@SuppressWarnings("serial")
public class FieldModel extends Shape {
	public Ellipse2D.Double center;
	public Rectangle2D goalleft;
	public Rectangle2D goalright;

	// Constances
	public static final int CENTER_CIRCLE_DIAMETER = 120;
	public static final int GOALS_WIDTH = 30;
	public static final int GOALS_HEIGHT = 80;
	public static final int GOAL_LEFT = 6;
	public static final int GOAL_LEFT_UP = 7;
	public static final int GOAL_LEFT_DOWN = 8;
	public static final int GOAL_RIGHT = 9;
	public static final int GOAL_RIGHT_UP = 10;
	public static final int GOAL_RIGHT_DOWN = 11;
        /**
         * Création du terrain ????? .
         * @param x position X
         * @param y position Y
         * @param width largeur
         * @param height hauteur
         */
	public FieldModel(double x, double y, double width, double height) {
		super(x, y, width, height);

		this.setSides();

		// Cercle central
		center = new Ellipse2D.Double(0, 0, 
				CENTER_CIRCLE_DIAMETER, CENTER_CIRCLE_DIAMETER);
		goalleft = new Rectangle2D.Double(0, 0, 0, 0);
		goalright = new Rectangle2D.Double(0, 0, 0, 0);

		this.setSize(x,  y,  width,  height);
	}
}
