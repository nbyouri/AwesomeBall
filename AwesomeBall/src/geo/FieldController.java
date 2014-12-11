package geo;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

@SuppressWarnings("serial")
public class FieldController extends FieldModel {
	
	public FieldController(double x, double y, double width, double height) {
		super(x, y, width, height);
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
}
