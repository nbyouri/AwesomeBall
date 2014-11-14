package geo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

@SuppressWarnings("serial")
public class Ball extends Ellipse2D.Double {

	public Ball(double x, double y, double width, double height) {
		// ball initialization, a normal ball is 22cm in diameter
		super(x - (width / 2), y, width, height);
	}

	public Ball() {
		super(0, 0, 0, 0);
	}

	public void move(Player p, Field f) {
		if (this.inField(f)) {
			
			if (this.ballHit(p, Player.Side.UP.getId())) {
				y += Player.SPEED_ONE;
			}
			
			if (this.ballHit(p, Player.Side.LEFT.getId())) {
				x += Player.SPEED_ONE;
			}
			
			if (this.ballHit(p, Player.Side.DOWN.getId())) {
				y -= Player.SPEED_ONE;
			}
			
			if (this.ballHit(p, Player.Side.RIGHT.getId())) {
				x -= Player.SPEED_ONE;
			}
		}
	}

	public Boolean ballHit(Player p, int side) {
		//return	(this.getFrame().intersectsLine(p.getSide(side)));
		if (side == Player.Side.UP.getId()) {
			//return (p.getSide(side).intersectsLine(this.getX(), this.getY(), this.getX() + this.getWidth(),
			//		this.getY()));
			return p.approachesTopSideOut(this.getBounds());

		}
		return false;
	}
	
	public Boolean inField(Rectangle2D.Double r) {
		return (r.contains(this.getBounds2D()));
	}
	
	public void draw(Graphics2D g2) {
		g2.setColor(Color.yellow);
		g2.draw(this.getBounds());
		g2.fill(this);
		g2.draw(this);
	}

}
