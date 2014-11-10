package test_jeu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Field extends Shape {
	
	// sides of the field, parts where collision can be detected
	private ArrayList<Line2D.Double> sides;
	
	public Field() {
		sides = new ArrayList<Line2D.Double>();
		
		// 4 sides of field rectangle
		for (int i = 0; i < 4; i++)
			sides.add(new Line2D.Double(0,0,0,0));
	}
	
	public ArrayList<Line2D.Double> getSides() {
		return sides;
	}
	
	public Line2D getSide(int i) {
		return sides.get(i);
	}
	
	public void setSize(double x, double y, double width, double height) {
		super.setRect(x, y, width, height);
		this.setSides();
	}
	
	public void setSides() {
		// left
		this.sides.get(0).setLine(this.getX(),  
				this.getY(), 
				this.getX(),  
				this.getY() + this.getHeight());
		// top
		this.sides.get(1).setLine(this.getX(),
				this.getY(),
				this.getX() + this.getWidth(),
				this.getY());
		// right
		this.sides.get(2).setLine(this.getX() + this.getWidth(), 
				this.getY(), 
				this.getX() + this.getWidth(),
				this.getY() + this.getHeight());
		// bottom
		this.sides.get(3).setLine(this.getX(),
				this.getY() + this.getHeight(),
				this.getX() + this.getWidth(),
				this.getY() + this.getHeight());
	}
	
    // draw side(s) of Field
    public void drawSides(Graphics2D g2, ArrayList<Integer> ar) {
    	g2.setColor(Color.yellow);
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
