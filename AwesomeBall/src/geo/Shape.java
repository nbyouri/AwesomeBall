package geo;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

@SuppressWarnings("serial")
public abstract class Shape extends Rectangle2D.Double {
	/*
	 * Speed
	 */
	private double dx;
	private double dy;
	private ArrayList<Line2D.Double> sides;

	/*
	 * 
	 * 
	 * Shape sides, defined by their
	 * number and text identification.
	 * 
	 * 
	 */
	public enum Side { 
		LEFT     (0, "left"), 
		UP       (1, "up"), 
		RIGHT    (2, "right"), 
		DOWN     (3, "down"),
		CENTER_V (4, "center_vertical"),
		CENTER_H (5, "center_horizontal");

		private final int id;
		private final String name;

		private Side(int s, String n) {
			this.id = s;
			this.name = n;
		}

		public int getId() {
			return this.id;
		}

		public String getName() {
			return this.name;
		}
	};

	public Shape() {
		// initial values
		super(0, 0, 0, 0);

		sides = new ArrayList<Line2D.Double>();

		for (int i = 0; i < 6; i++)
			sides.add(new Line2D.Double(0,0,0,0));
	}

	public double getDx() {
		return dx;
	}


	public void setDx(double dx) {
		this.dx = dx;
	}


	public double getDy() {
		return dy;
	}


	public void setDy(double dy) {
		this.dy = dy;
	}

	public ArrayList<Line2D.Double> getSides() {
		return sides;
	}

	public Line2D getSide(int i) {
		return sides.get(i);
	}

	/*
	 * 
	 * Near any side
	 * 
	 */
	public boolean near(Rectangle2D s) {
		// top or down side of the ball
		// XXX replace with distance?
		return (this.getY()    - 5 <= s.getMaxY() &&
				this.getMaxY() + 5 >= s.getY()    &&
				this.getX()    - 5 <= s.getMaxX() &&
				this.getMaxX() + 5 >= s.getX());
	}

	/*
	 * 
	 * Near vertically
	 * 
	 */
	public boolean nearX(Rectangle2D s) {
		return (this.getX()    <= s.getMaxX() &&
				this.getMaxX() >= s.getX());
	}

	/*
	 * 
	 * Near horizontally
	 * 
	 */
	public boolean nearY(Rectangle2D s) {
		return (this.getY()    <= s.getMaxY() &&
				this.getMaxY() >= s.getY());
	}

	/*
	 * 
	 * Near a specific side
	 * 
	 */
	public boolean near(Rectangle2D s, int line) {

		// changes values to int to avoid 
		// confusion after diagonal change
		int x  = (int)this.getX();
		int mx = (int)this.getMaxX();
		int y  = (int)this.getY();
		int my = (int)this.getMaxY();

		/*
		 * 
		 * Leave two pixels of space where a hit happens
		 * 
		 */
		if (line == Side.UP.getId()) {

			return ((my + 1 >= s.getY()) &&
					(my - 1 <= s.getY()) && this.near(s));

		}  if (line == Side.DOWN.getId()) {

			return ((y - 1 <= s.getMaxY()) &&
					(y + 1 >= s.getMaxY()) && this.nearX(s));

		}  if (line == Side.LEFT.getId()) {

			return ((mx - 1 <= s.getX()) &&
					(mx + 1 >= s.getX()) && this.nearY(s));

		}  if (line == Side.RIGHT.getId()) {

			return ((x - 1 <= s.getMaxX()) &&
					(x + 1 >= s.getMaxX()) && this.nearY(s));

		} else {

			return false;

		}
	}

	/*
	 * 
	 * If the shape isn't touching any side in the other shape.
	 * 
	 */
	public boolean insideRect(Rectangle2D r) {
		return (!((this.touchRectInLeft(r)) ||
				(this.touchRectInRight(r))  ||
				(this.touchRectInTop(r))    ||
				(this.touchRectInBottom(r))));
	}

	/*
	 * 
	 * Does the shape touch or approach a shape's inner side?
	 * 
	 */
	public boolean touchRectInLeft(Rectangle2D r) {
		return (this.getX() + 1 < r.getX());
	}

	public boolean touchRectInTop(Rectangle2D r) {
		return (this.getY() - 1 < r.getY());
	}

	public boolean touchRectInRight(Rectangle2D r) {
		return (this.getMaxX() + 1 > r.getMaxX());
	}

	public boolean touchRectInBottom(Rectangle2D r) {
		return (this.getMaxY() + 1 > r.getMaxY());
	}

	/*
	 * 
	 * Update location and sides location.
	 * 
	 */
	public void setLocation(double x, double y) {
		this.setRect(x, y, this.getWidth(), this.getHeight());
		this.setSides();
	}

	/*
	 * 
	 * Set the shape position and size.
	 * 
	 * 
	 */
	public void setSize(double x, double y, double width, double height) {
		super.setRect(x, y, width, height);
	}

	/*
	 * 
	 * Move shape in another shape.
	 * 
	 */
	public void moveIn(Rectangle2D r) {
		if (this.insideRect(r)){
			this.setLocation(this.getX() + this.getDx(),
					this.getY() + this.getDy());
		}

		if (this.touchRectInLeft(r)) {
			this.setLocation(this.getX() - this.getDx(), this.getY());
		}

		if (this.touchRectInTop(r)) {
			this.setLocation(this.getX(), this.getY() - this.getDy());
		}

		if (this.touchRectInRight(r)) {
			this.setLocation(this.getX() - this.getDx(), this.getY());
		}

		if (this.touchRectInBottom(r)) {
			this.setLocation(this.getX(), this.getY() - this.getDy());
		}

	}

	/*
	 * 
	 * Set sides from shape location and size.
	 * 
	 * 
	 */
	public void setSides() {
		this.sides.get(Side.LEFT.getId()).setLine(this.getX(),  
				this.getY(), this.getX(), this.getMaxY());

		this.sides.get(Side.UP.getId()).setLine(this.getX(),		
				this.getY(), this.getMaxX(), this.getY());

		this.sides.get(Side.RIGHT.getId()).setLine(this.getMaxX(), this.getY(), 
				this.getMaxX(), this.getMaxY());

		this.sides.get(Side.DOWN.getId()).setLine(this.getX(),
				this.getMaxY(), this.getMaxX(), this.getMaxY());

		this.sides.get(Side.CENTER_V.getId()).setLine(this.getCenterX(), 
				this.getY(), this.getCenterX(), this.getY() + this.getHeight());

		this.sides.get(Side.CENTER_H.getId()).setLine(this.getX(), 
				this.getCenterY(), this.getWidth() + this.getX(), 
				this.getCenterY()); 
	}
}
