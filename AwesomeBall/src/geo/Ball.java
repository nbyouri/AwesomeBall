package geo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Ball extends Shape {
	Ellipse2D circle;

	public Ball(double x, double y, double width, double height) {
		// ball initialization, a normal ball is 22cm in diameter
		super.setRect(x, y, width, height);
		circle = new Ellipse2D.Double(x - (width / 2), y, width, height);
		this.setSides();
	}

	public Ball() {
		super();
		circle = new Ellipse2D.Double(0, 0, 0, 0);
	}

	public void move(Player p, Field f) {

		// if player hits the ball, move it along with the player
		// if the ball touches the field sides, the ball stops.
		if (this.intersectSide(p, Player.Side.UP.getId())) {
			if (this.approachesBottomSide(f.getBounds())) {
				y -= Player.SPEED_ONE;
				p.setDy(-Player.SPEED_ONE);
			} else if (this.insideRect(f.getBounds())){
				y += Player.SPEED_ONE;
			}
		}

		if (this.intersectSide(p, Player.Side.LEFT.getId())) {
			if (this.approachesRightSide(f.getBounds())) {
				x -= Player.SPEED_ONE;
				p.setDx(-Player.SPEED_ONE);
			} else if (this.insideRect(f.getBounds())) {
				x += Player.SPEED_ONE;
			}
		}

		if (this.intersectSide(p, Player.Side.DOWN.getId())) {
			if (this.approachesTopSide(f.getBounds())) {
				y += Player.SPEED_ONE;
				p.setDy(Player.SPEED_ONE);
			} else if (this.insideRect(f.getBounds())) {
				y -= Player.SPEED_ONE;
			}
		}

		if (this.intersectSide(p, Player.Side.RIGHT.getId())) {
			if (this.approachesLeftSide(f.getBounds())) {
				x += Player.SPEED_ONE;
				p.setDx(Player.SPEED_ONE);
			} else if (this.insideRect(f.getBounds())) {
				x -= Player.SPEED_ONE;
			}
		}
	}

	public void draw(Graphics2D g2) {
		this.circle.setFrame(this.getBounds2D());
		g2.setColor(Color.yellow);
		g2.draw(this.circle);
		g2.fill(this.circle);
		//g2.draw(this);
	}

	public void drawSides(Graphics2D g2, ArrayList<Integer> ar) {
	}

}
