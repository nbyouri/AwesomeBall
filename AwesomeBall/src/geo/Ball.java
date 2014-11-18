package geo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

@SuppressWarnings("serial")
public class Ball extends Shape {
	Ellipse2D circle;
	Boolean sticky;

	public Ball(double x, double y, double width, double height) {
		// ball not sticky by default
		sticky = false;

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
	
	// move ball in field
	public void moveBall(Shape f, Player p) {
		
		this.setLocation(this.getX() + this.getDx(),
				this.getY() + this.getDy());
		
	}

	public Boolean getSticky() {
		return this.sticky;
	}

	public void setSticky(Boolean s) {
		this.sticky = s;
	}

	public void toggleSticky(Player p) {
		// if we're near the ball and not sticky yet
		// OR 
		// if we're sticky and too far
		if ((!sticky && this.near(p)) || sticky) {
			sticky = !sticky;
		}
	}

	public Boolean near(Shape s) {
		// top or down side of the ball
		// XXX replace with distance?
		return (this.getY()    - 5 <= s.getMaxY() &&
				this.getMaxY() + 5 >= s.getY()    &&
				this.getX()    - 5 <= s.getMaxX() &&
				this.getMaxX() + 5 >= s.getX());
	}

	public void move(Player p, Field f) {

		this.setDx(0);
		this.setDy(0);

		// if the ball is in sticky mode
		// just follow the player
		if (sticky) {
			this.setDx(p.getDx());
			this.setDy(p.getDy());
		}

		// if player hits the ball, move it along with the player
		// if the ball touches the field sides, the ball stops.
		if (this.intersectSide(p, Player.Side.UP.getId())) {
			if (this.insideRect(f)) {
				this.setDy(5); 
			} else {
				this.setDy(-5);
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

		// bottom -> top
		if (this.intersectSide(p, Player.Side.DOWN.getId())) {
			if (this.insideRect(f)) { 
				this.setDy(-5);
			} else {
				this.setDy(5);
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

		this.moveBall(f, p);
		this.goal(p, f);

	}

	public void goal(Player p, Field f) {
		if  (this.getSide(Shape.Side.LEFT.getId()).intersectsLine(f.getSide(Field.GOAL_RIGHT)) ||
				this.getSide(Shape.Side.RIGHT.getId()).intersectsLine(f.getSide(Field.GOAL_LEFT))) {
			this.setSticky(false);
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
		g2.draw(this);
		g2.draw(this.circle);
		g2.fill(this.circle);
	}
}
