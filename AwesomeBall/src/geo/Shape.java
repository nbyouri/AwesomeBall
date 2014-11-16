package geo;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;

@SuppressWarnings("serial")
public abstract class Shape extends Rectangle.Double {
	/*
	 *  coordonnees de droite
	 * |--> x,y
	 * |      
	 * +–––––+--> rightx, righty
	 * |     |
	 * |     |
	 * +–––––+--> rightdownx, rightdowny
	 * |
	 * |--> downx, downy
	 * 
	 */
	public double dx;
	public double dy;
	public ArrayList<Line2D.Double> sides;
	
	public enum Side { 
		LEFT (0, "left"), 
		UP   (1, "up"), 
		RIGHT(2, "right"), 
		DOWN (3, "down");
		
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
	
	public static final int CENTER_V = 4;
	public static final int CENTER_H = 5;
	
	public Shape() {
		// initial values
		super(0, 0, 0, 0);
		
		sides = new ArrayList<Line2D.Double>();

		// 4 sides of field Shape, initialise to 0
		for (int i = 0; i < 4; i++)
			sides.add(new Line2D.Double(0,0,0,0));

		// center lines
		for (int i = 0; i < 2; i++)
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

	// check collision of two Shapes based on the sides.
	public Boolean intersectSide(Shape s, int i) {
		
		this.setSides();
		
		if (i == Side.UP.getId())
			return (this.getSide(i).intersectsLine(s.getSide(Side.DOWN.getId())));
		
		if (i == Side.DOWN.getId()) 
			return (this.getSide(i).intersectsLine(s.getSide(Side.UP.getId())));
		
		if (i == Side.LEFT.getId()) 
			return (this.getSide(i).intersectsLine(s.getSide(Side.RIGHT.getId())));
		
		if (i == Side.RIGHT.getId())
			return (this.getSide(i).intersectsLine(s.getSide(Side.LEFT.getId())));

		return false;
	}
	
	// precise collision check of the shape relative to the field position
	public Boolean insideRect(Shape r) {
		if ((this.touchRectLeft(r)) || (this.touchRectRight(r)) ||
				(this.touchRectTop(r)) || (this.touchRectBottom(r))) {
			return false;
		} else {
			return true;
		}
	}

	// returns true if the shape touches the inner left
	// side of the Field minus the border
	public Boolean touchRectLeft(Shape r) {
		return (this.getX() + 1 < r.getX());
	}

	public Boolean approachesLeftSide(Shape r) {
		return (this.getX() - 2 < r.getX());
	}

	// returns true if the shape touches the top 
	// side of the Field minus the border
	public Boolean touchRectTop(Shape r) {
		return (this.getY() + 2 < r.getY());
	}

	public Boolean approachesTopSide(Shape r) {
		return (this.getY() - 2 < r.getY());
	}
	
	public Boolean approachesTopSideOut(Shape r) {
		return (this.getY() - 2 == r.getY());
	}

	// returns true if the shape touches the inner right 
	// side of the Field minus the border
	public Boolean touchRectRight(Shape r) {
		return (this.getX() + this.getWidth() - 
				r.getX() > r.getWidth());
	}

	public Boolean approachesRightSide(Shape r) {
		return (this.getX() + this.getWidth() -
				r.getX() + 2 > r.getWidth());
	}

	// returns true if the shape touches the inner bottom 
	// side of the Field minus the border
	public Boolean touchRectBottom(Shape r) {
		return (this.getY() + this.getHeight() -
				r.getY() + 2 > r.getHeight());
	}

	public Boolean approachesBottomSide(Shape r) {
		return (this.getY() + this.getHeight() -
				r.getY() + 2 > r.getHeight());
	}

	// detect whether shape is in one of the field Shape's 4 corners
	// if so, return arraylist of sides indexes
	public ArrayList<Integer> approaches(Shape r) {
		ArrayList<Integer> ids = new ArrayList<Integer>();

		if (this.approachesLeftSide(r)) {
			ids.add(Field.Side.LEFT.getId());
		} 

		if (this.approachesTopSide(r)) {
			ids.add(Field.Side.UP.getId());
		} 

		if (this.approachesRightSide(r)) {
			ids.add(Field.Side.RIGHT.getId());
		} 

		if (this.approachesBottomSide(r)) {
			ids.add(Field.Side.DOWN.getId());
		}

		return ids;
	}

	public void setLocation(double x, double y) {
		this.setRect(x, y, this.getWidth(), this.getHeight());
		this.setSides();
	}
	
	
	public void setSize(double x, double y, double width, double height) {
		super.setRect(x, y, width, height);
	}
	
	// move shape in Shape
	public void moveIn(Shape r) {
		if (insideRect(r)){
			this.setLocation(this.getX() + this.getDx(),
					this.getY() + this.getDy());
		}

		if (this.touchRectLeft(r))
			this.setLocation(this.getX() - this.getDx(), this.getY());

		if (this.touchRectTop(r))
			this.setLocation(this.getX(), this.getY() - this.getDy());

		if (this.touchRectRight(r))
			this.setLocation(this.getX() - this.getDx(), this.getY());

		if (this.touchRectBottom(r))
			this.setLocation(this.getX(), this.getY() - this.getDy());

	}
		
	public void setSides() {
		this.sides.get(Side.LEFT.getId()).setLine(this.getX(),  
				this.getY(), this.getX(), this.getMaxY());
		
		this.sides.get(Side.UP.getId()).setLine(this.getX(),		
				this.getY(), this.getMaxX(), this.getY());
		
		this.sides.get(Side.RIGHT.getId()).setLine(this.getMaxX(), this.getY(), 
				this.getMaxX(), this.getMaxY());
		
		this.sides.get(Side.DOWN.getId()).setLine(this.getX(),
				this.getMaxY(), this.getMaxX(), this.getMaxY());
		
		this.sides.get(CENTER_V).setLine(this.getCenterX(), this.getY(),
				this.getCenterX(), this.getY() + this.getHeight());
		
		this.sides.get(CENTER_H).setLine(this.getX(),  this.getCenterY(),
				this.getWidth() + this.getX(), this.getCenterY()); 
	}
	
	public ArrayList<Line2D.Double> getSides() {
		return sides;
	}
	
	public Line2D getSide(int i) {
		return sides.get(i);
	}
	
	public abstract void draw(Graphics2D g2);
	public abstract void drawSides(Graphics2D g2, ArrayList<Integer> ar);
}
