package geo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Field extends Shape {
	
	// sides of the field, parts where collision can be detected
	private ArrayList<Line2D.Double> sides;
	private Ellipse2D.Double center;
	
	// constants
	public static final int SIDE_LEFT = 0;
	public static final int SIDE_UP = 1;
	public static final int SIDE_RIGHT = 2;
	public static final int SIDE_DOWN = 3;
	public static final int CENTER_V = 4;
	public static final int CENTER_H = 5;
	public static final int CENTER_CIRCLE_DIAMETER = 120;
	
	public Field() {
		sides = new ArrayList<Line2D.Double>();
		
		// 4 sides of field rectangle, initialise to 0
		for (int i = 0; i < 4; i++)
			sides.add(new Line2D.Double(0,0,0,0));
		
		// center lines
		for (int i = 0; i < 2; i++)
			sides.add(new Line2D.Double(0,0,0,0));
		
		// center circle
		center = new Ellipse2D.Double(0, 0, 
				CENTER_CIRCLE_DIAMETER, CENTER_CIRCLE_DIAMETER);
	}
	
	public ArrayList<Line2D.Double> getSides() {
		return sides;
	}
	
	public Line2D getSide(int i) {
		return sides.get(i);
	}
	
	@Override
	public void setSize(double x, double y, double width, double height) {
		super.setSize(x, y, width, height);
		this.setSides();
	}
	
	public void setCenterCircle() {
		double centerX = this.getCenterX();
		double centerY = this.getCenterY();
		// diameter is a  sixth of the height of the field
		double radius  = (this.getHeight() / 6)/2;
		this.center.setFrameFromCenter(centerX, centerY, centerX + radius, centerY + radius);
	}
	
	public void setSides() {
		this.sides.get(SIDE_LEFT).setLine(this.getX(),  
				this.getY(), this.getDownX(), this.getDownY());
		
		this.sides.get(SIDE_UP).setLine(this.getX(),		
				this.getY(), this.getRightX(), this.getRightY());
		
		this.sides.get(SIDE_RIGHT).setLine(this.getRightX(), this.getRightY(), 
				this.getRightDownX(), this.getRightDownY());
		
		this.sides.get(SIDE_DOWN).setLine(this.getDownX(),
				this.getDownY(), this.getRightDownX(), this.getRightDownY());
		
		this.sides.get(CENTER_V).setLine(this.getCenterX(), this.getY(),
				this.getCenterX(), this.getY() + this.getHeight());
		
		this.sides.get(CENTER_H).setLine(this.getX(),  this.getCenterY(),
				this.getWidth() + this.getX(), this.getCenterY()); 
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
    	// draw rectangle
    	g2.setColor(Color.cyan);
		g2.draw(this);
    }
}
