package geo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Field extends Shape {
	
	// sides of the field, parts where collision can be detected
	//private ArrayList<Line2D.Double> sides;
	private Ellipse2D.Double center;
	
	// constants
	public static final int CENTER_CIRCLE_DIAMETER = 120;
	
	public Field() {
		// center circle
		center = new Ellipse2D.Double(0, 0, 
				CENTER_CIRCLE_DIAMETER, CENTER_CIRCLE_DIAMETER);
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
