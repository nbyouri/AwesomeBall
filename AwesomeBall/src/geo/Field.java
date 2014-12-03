package geo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Field extends Shape {
	private Ellipse2D.Double center;
	private Rectangle2D goalleft;
	private Rectangle2D goalright;

	// constants
	public static final int CENTER_CIRCLE_DIAMETER = 120;
	public static final int GOALS_WIDTH = 30;
	public static final int GOALS_HEIGHT = 80;
	public static final int GOAL_LEFT = 6;
	public static final int GOAL_LEFT_UP = 7;
	public static final int GOAL_LEFT_DOWN = 8;
	public static final int GOAL_RIGHT = 9;
	public static final int GOAL_RIGHT_UP = 10;
	public static final int GOAL_RIGHT_DOWN = 11;

	public Field() {
		// center circle
		center = new Ellipse2D.Double(0, 0, 
				CENTER_CIRCLE_DIAMETER, CENTER_CIRCLE_DIAMETER);
		goalleft = new Rectangle2D.Double(0, 0, 0, 0);
		goalright = new Rectangle2D.Double(0, 0, 0, 0);
	}

	public Rectangle2D getGoalleft() {
		return goalleft;
	}

	public void setGoalleft(Rectangle2D goalleft) {
		this.goalleft = goalleft;
	}

	public Rectangle2D getGoalright() {
		return goalright;
	}

	public void setGoalright(Rectangle2D goalright) {
		this.goalright = goalright;
	}

	/**
	 * Set size of the field and incidentally goals.
	 */
	public void setSize(double x, double y, double width, double height) {
		super.setRect(x, y, width, height);

		double goalW = GOALS_WIDTH;
		double goalH = height / 3;
		double goalY = this.getY() + goalH;
		double goalMaxY = this.getY() + (2 * goalH);
		double goalLX = x - goalW;
		double goalRX = x + width;

		// setup goal rectangles
		goalleft.setRect(goalLX, goalY, goalW, goalH);
		goalright.setRect(goalRX, goalY, goalW, goalH);

		// left goal lines
		// goal line 
		this.getSides().add(new Line2D.Double(x, goalY, x, goalMaxY));
		// line above left goal
		this.getSides().add(new Line2D.Double(x, this.getY(), x, goalY));
		// line under left goal
		this.getSides().add(new Line2D.Double(x, goalMaxY, x, this.getMaxY()));


		// right goal line
		this.getSides().add(new Line2D.Double(this.getMaxX(), goalY, 
				this.getMaxX(), goalMaxY));

		// line above right goal 
		this.getSides().add(new Line2D.Double(this.getMaxX(), this.getY(), 
				this.getMaxX(), goalY));

		// line under right goal
		this.getSides().add(new Line2D.Double(getMaxX(), goalMaxY, 
				this.getMaxX(), this.getMaxY()));
	}

	/**
	 * Set the center field circle proportionally to the field.
	 * The circle diameter is a  sixth of the height of the field.
	 */
	public void setCenterCircle() {
		double centerX = this.getCenterX();
		double centerY = this.getCenterY();

		double radius  = (this.getHeight() / 6)/2;
		this.center.setFrameFromCenter(centerX, centerY, 
				centerX + radius, centerY + radius);
	}

	/**
	 * Draw the line vertically centered of the field.
	 * @param g2
	 */
	public void drawCenterLines(Graphics2D g2) {
		g2.setColor(Color.magenta);
		g2.draw(this.getSide(Side.CENTER_V.getId()));
		//g2.draw(this.getSide(CENTER_H));
		g2.draw(this.center);
	}

	/**
	 * Draw side(s) of Field.
	 * 
	 * @param g2
	 * @param ar
	 */
	public void drawSides(Graphics2D g2, ArrayList<Integer> ar) {
		g2.setColor(Color.magenta);
		for (int i = 0; i < ar.size(); i++)
			g2.draw(this.getSide(ar.get(i)));
	}

	/**
	 * Draw field and goals.
	 * @param g2
	 */
	public void draw(Graphics2D g2) {
		g2.setColor(Color.cyan);
		g2.draw(this);
		g2.draw(this.goalleft);
		g2.draw(this.goalright);

		g2.setColor(Color.black);
		g2.draw(this.getSide(GOAL_LEFT));
		g2.draw(this.getSide(GOAL_RIGHT));
	}
}
