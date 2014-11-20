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

	/*
	 * 
	 * Does the ball's right/left side touch the goal's line?
	 * 
	 */
	public Boolean touchRightGoal(Shape f) {
		return (this.getSide(Shape.Side.RIGHT.getId()).intersectsLine(f.getSide(Field.GOAL_RIGHT)));
	}

	public Boolean touchLeftGoal(Shape f) {
		return (this.getSide(Shape.Side.LEFT.getId()).intersectsLine(f.getSide(Field.GOAL_LEFT)));
	}

	/*
	 * 
	 * Does the ball touch borders above and unders both goals?
	 * 
	 */
	public Boolean touchBorders(Shape f) {
		return (this.intersectsLine(f.getSide(Field.GOAL_RIGHT_UP))   || 
				this.intersectsLine(f.getSide(Field.GOAL_RIGHT_DOWN)) ||
				this.intersectsLine(f.getSide(Field.GOAL_LEFT_UP))    ||
				this.intersectsLine(f.getSide(Field.GOAL_LEFT_DOWN)));
	}
	
	/*
	 * 
	 * Actually update the ball location.
	 * 
	 * Placeholder method waiting for 
	 * multiplayer implementation.
	 * 
	 */
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

	/*
	 * 
	 *  Toggles the ball sticky state: 
	 *  if we're near the ball and not sticky yet
	 *	OR 
	 *	if we're sticky and too far
	 * 
	 */
	public void toggleSticky(Player p) {
		if ((!sticky && this.near(p)) || sticky) {
			sticky = !sticky;
		}
	}

	/*
	 * 
	 * Move the ball from a player in a field.
	 * 
	 * First, the speed is set to 0 then, if the 
	 * ball is sticky, simply follow the player around.
	 * 
	 * Then, depending on the side and if the ball is in 
	 * the field, set the speed adequately.
	 * 
	 */
	public void move(Player p, Field f) {

		double speed = 0;
		
		this.setDx(0);
		this.setDy(0);
		
		// update ball speed according to the player's
		// XXX: fix
		if ((Math.abs(p.getDx()) == Player.SPEED_ONE_DIAG) &&
			(Math.abs(p.getDy()) == Player.SPEED_ONE_DIAG)) {
			speed = Player.SPEED_ONE_DIAG;
		} else if ((Math.abs(p.getDx()) == Player.SPEED_ONE) ||
				   (Math.abs(p.getDy()) == Player.SPEED_ONE)) {
			speed = Player.SPEED_ONE;
		}

		// if the ball is in sticky mode
		// just follow the player
		if (sticky) {
			this.setDx(p.getDx());
			this.setDy(p.getDy());
		}

		// if player hits the ball, move it along with the player
		// if the ball touches the field sides, the ball stops.
		if (p.near(this, Player.Side.UP.getId())) {
			if (this.insideRect(f)) {
				this.setDy(speed); 
			} else {
				this.setDy(-speed);
			}
		
		}

		// left -> right
		if (p.near(this, Player.Side.LEFT.getId())) {
			if (this.touchBorders(f)) {
				this.setDx(-speed);
			} else {
				this.setDx(speed);
			}

		}

		// bottom -> top
		if (p.near(this, Player.Side.DOWN.getId())) {
			if (this.insideRect(f)) { 
				this.setDy(-speed);
			} else {
				this.setDy(speed);
			}
		}

		// right -> left
		if (p.near(this, Player.Side.RIGHT.getId())) {
			if (this.touchBorders(f)) { 
				this.setDx(speed); 
			} else {
				this.setDx(-speed);
			}


		}
		
		this.moveBall(f, p);
		this.goal(p, f);
	}

	/*
	 * 
	 * Score of player is incremented if the ball goes in 
	 * either goal. 
	 * 
	 * XXX: Implement side detection for multiplayer.
	 * 
	 */
	public void goal(Player p, Field f) {
		if  (this.getSide(Shape.Side.LEFT.getId()).intersectsLine(f.getSide(Field.GOAL_RIGHT)) ||
				this.getSide(Shape.Side.RIGHT.getId()).intersectsLine(f.getSide(Field.GOAL_LEFT))) {
			this.setSticky(false);
			p.setScore(p.getScore() + 1);
			this.centerBall(f);
		}
	}

	/*
	 * 
	 * Put the ball in the center of the field.
	 * 
	 */
	public void centerBall(Field f) {
		this.setLocation(f.getCenterX() - this.getWidth() / 2, 
				f.getY() + (f.getHeight() / 2) - 10);
	}

	/*
	 * 
	 * Draw the ball from the rectangle 
	 * around it.
	 * 
	 */
	public void draw(Graphics2D g2) {
		this.circle.setFrame(this.getBounds2D());
		g2.setColor(Color.yellow);
		g2.draw(this);
		g2.draw(this.circle);
		g2.fill(this.circle);
	}
}
