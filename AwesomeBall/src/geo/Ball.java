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
		super.setRect(x - (width / 2), y, width, height);
		circle = new Ellipse2D.Double(0, 0, 0, 0);
		this.setSides();
	}

	public Ball() {
		super();
		circle = new Ellipse2D.Double(0, 0, 0, 0);
	}

	public Boolean touchRightGoal(Shape f) {
		return (this.getSide(Shape.Side.RIGHT.getId()).intersectsLine(f.getSide(Field.GOAL_RIGHT)));
	}

	public Boolean touchLeftGoal(Shape f) {
		return (this.getSide(Shape.Side.LEFT.getId()).intersectsLine(f.getSide(Field.GOAL_LEFT)));
	}

	public Boolean touchBorders(Shape f) {
		return (this.intersectsLine(f.getSide(Field.GOAL_RIGHT_UP))   || 
				this.intersectsLine(f.getSide(Field.GOAL_RIGHT_DOWN)) ||
				this.intersectsLine(f.getSide(Field.GOAL_LEFT_UP))    ||
				this.intersectsLine(f.getSide(Field.GOAL_LEFT_DOWN)));
	}

	public void move(Player p, Field f) {

		this.setDx(0);
		this.setDy(0);

		// if player hits the ball, move it along with the player
		// if the ball touches the field sides, the ball stops.
		if (this.intersectSide(p, Player.Side.UP.getId())) {
			if (this.insideRect(f)) {
				this.setDy(5);
			}
		}

		// left -> right
		if (this.intersectSide(p, Player.Side.LEFT.getId())) {
			if (this.touchBorders(f)) {
				this.setDx(-5);
			} else {
				this.setDx(5);
			}
		}

		if (this.intersectSide(p, Player.Side.DOWN.getId())) {
			if (this.insideRect(f)) {
				this.setDy(-5);
			}
		}

		// right -> left
		if (this.intersectSide(p, Player.Side.RIGHT.getId())) {
			if (this.touchBorders(f)) {
				this.setDx(5);
			} else {
				this.setDx(-5);
			}
		}

		this.setLocation(this.getX() + this.getDx(),
				this.getY() + this.getDy());

		this.goal(p, f);

	}

	public void goal(Player p, Field f) {
		if  (this.getSide(Shape.Side.LEFT.getId()).intersectsLine(f.getSide(Field.GOAL_RIGHT)) ||
				this.getSide(Shape.Side.RIGHT.getId()).intersectsLine(f.getSide(Field.GOAL_LEFT))) {
			p.setScore(p.getScore() + 1);
			this.centerBall(f);
		}
	}

	public void centerBall(Field f) {
		this.setLocation(f.getCenterX() - this.getWidth() / 2, 
				f.getY() + (f.getHeight() / 2) - 10);
	}

	public void draw(Graphics2D g2) {
		this.circle.setFrame(this.getBounds2D());
		g2.setColor(Color.yellow);
		g2.draw(this.circle);
		g2.fill(this.circle);
	}

	public void drawSides(Graphics2D g2, ArrayList<Integer> ar) {
	}

}
