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
	public static final int GOAL_RIGHT = 7;

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

	public void setSize(double x, double y, double width, double height) {
		super.setRect(x, y, width, height);

		// setup goal rectangles
		goalleft.setRect(x - GOALS_WIDTH, this.getY() + (height / 3), GOALS_WIDTH, height / 3);
		goalright.setRect(x + width, this.getY() + (height / 3), GOALS_WIDTH, height / 3);

		// left goal line
		this.sides.add(new Line2D.Double(x, this.getY() + (height / 3), x, this.getY() + 2 * (height / 3)));
		// right goal line
		this.sides.add(new Line2D.Double(x + width, this.getY() + (height / 3), x + width, this.getY() + 2 * (height / 3)));
	}

	public void setCenterCircle() {
		double centerX = this.getCenterX();
		double centerY = this.getCenterY();
		// diameter is a  sixth of the height of the field
		double radius  = (this.getHeight() / 6)/2;
		this.center.setFrameFromCenter(centerX, centerY, centerX + radius, centerY + radius);
	}

	public void drawCenterLines(Graphics2D g2) {
		g2.draw(this.getSide(CENTER_V));
		//g2.draw(this.getSide(CENTER_H));
		g2.draw(this.center);
	}

	// draw side(s) of Field
	public void drawSides(Graphics2D g2, ArrayList<Integer> ar) {
		g2.setColor(Color.magenta);
		for (int i = 0; i < ar.size(); i++)
			g2.draw(this.getSide(ar.get(i)));
	}

	// draw field and sides
	public void draw(Graphics2D g2) {
		// draw rectangles
		g2.setColor(Color.cyan);
		g2.draw(this);
		g2.draw(this.goalleft);
		g2.draw(this.goalright);

		// draw goal lines in white
		g2.setColor(Color.white);
		g2.draw(this.getSide(GOAL_LEFT));
		g2.draw(this.getSide(GOAL_RIGHT));
	}
}
