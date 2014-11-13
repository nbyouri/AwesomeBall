package geo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

@SuppressWarnings("serial")
public class Ball extends Ellipse2D.Double {

	public Ball(double x, double y, double width, double height) {
		// ball initialization, a normal ball is 22cm in diameter
		super(x - (width / 2), y, width, height);
	}
	
	public Ball() {
		super(0, 0, 0, 0);
	}

	
	public void draw(Graphics2D g2) {
		g2.setColor(Color.yellow);
		g2.fill(this);
		g2.draw(this);
	}

}
