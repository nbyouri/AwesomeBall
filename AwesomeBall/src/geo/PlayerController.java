package geo;

import gui.Images;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

@SuppressWarnings("serial")
public class PlayerController extends PlayerModel {

	public Images getImg() {
		return img;
	}

	public void setImg(Images img) {
		this.img = img;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}


	public Ellipse2D.Double getEll() {
		return ell;
	}

	public void setEll(Ellipse2D.Double ell) {
		this.ell = ell;
	}

	public void setLocation(double x, double y) {
		this.setRect(x, y, this.getWidth(), this.getHeight());
		this.getEll().setFrame(x, y, this.getWidth(), this.getHeight());
	}

	/*
	 * Set rotation from key events
	 */
	public void setRotation() {
		if (left) { 
			setLeft(); 
		}

		if (right) { 
			setRight(); 
		}

		if (up) { 
			setUp(); 
		}

		if (down) { 
			setDown(); 
		}
	}

	/*
	 * 
	 * Draw image left, right, up, down
	 * and rotate and or flip the image
	 * depending on the previous direction.
	 * 
	 */
	public void setLeft() {
		int rotation = getImg().getRotation();
		if (rotation == Direction.RIGHT.getId()) {
			getImg().flip(getImg().getPlayer(), Direction.LEFT.getId());
		} else if (rotation == Direction.UP.getId()) {
			getImg().rotate(getImg().getPlayer(), Direction.DOWN.getId());
			getImg().flip(getImg().getPlayer(), Direction.LEFT.getId());
		} else if (rotation == Direction.DOWN.getId()) {
			getImg().rotate(getImg().getPlayer(), Direction.UP.getId());
			getImg().flip(getImg().getPlayer(), Direction.LEFT.getId());
		}
		getImg().setRotation(Direction.LEFT.getId());
	}

	public void setRight() {
		int rotation = getImg().getRotation();
		if (rotation == Direction.LEFT.getId()) {
			getImg().flip(getImg().getPlayer(), Direction.RIGHT.getId());
		} else if (rotation == Direction.UP.getId()) {
			getImg().rotate(getImg().getPlayer(), Direction.DOWN.getId());
		} else if (rotation == Direction.DOWN.getId()) {
			getImg().rotate(getImg().getPlayer(), Direction.UP.getId());
		}
		getImg().setRotation(Direction.RIGHT.getId());
	}

	public void setUp() {
		int rotation = getImg().getRotation();
		if (rotation == Direction.LEFT.getId()) {
			getImg().rotate(getImg().getPlayer(), Direction.DOWN.getId());
			getImg().flip(getImg().getPlayer(), Direction.UP.getId());
		} else if (rotation == Direction.RIGHT.getId()) {
			getImg().rotate(getImg().getPlayer(), Direction.UP.getId());
		} else if (rotation == Direction.DOWN.getId()) {
			getImg().rotate(getImg().getPlayer(), Direction.LEFT.getId());
		}
		getImg().setRotation(Direction.UP.getId());
	}

	public void setDown() {
		int rotation = getImg().getRotation();
		if (rotation == Direction.LEFT.getId()) {
			getImg().flip(getImg().getPlayer(), Direction.DOWN.getId());
			getImg().rotate(getImg().getPlayer(), Direction.DOWN.getId());
		} else if (rotation == Direction.UP.getId()) {
			getImg().rotate(getImg().getPlayer(), Direction.LEFT.getId());
		} else if (rotation == Direction.RIGHT.getId()) {
			getImg().rotate(getImg().getPlayer(), Direction.DOWN.getId());
		}
		getImg().setRotation(Direction.DOWN.getId());
	}

	/*
	 * 
	 * Check whether the player is in a goal
	 * 
	 */
	public boolean insideGoals(FieldController f) {
		return ((this.getMaxX() < f.getGoalright().getMaxX() &&
				this.getY() - 1 >= f.getGoalright().getY()) ||
				(this.getX() > f.getGoalleft().getX() &&
						this.getY() - 1 >= f.getGoalleft().getY()));
	}

	/*
	 * 
	 * Right Goal Collisions
	 * 
	 */
	public boolean touchGoalRightTop(FieldController f) {
		return (this.getMaxX() >= f.getGoalright().getX() &&
				this.getY() - 1 <= f.getGoalright().getY());
	}

	public boolean touchGoalRightBottom(FieldController f) {
		return (this.getMaxX() >= f.getGoalright().getX() &&
				this.getMaxY() + 1 >= f.getGoalright().getMaxY());
	}

	public boolean touchRectInRight(FieldController f) {
		return (f.getMaxX() - this.getMaxX() <= 1.5 && 
				(this.getY() < f.getGoalright().getY() ||
						this.getMaxY() > f.getGoalright().getMaxY()
						) || this.getMaxX() + 1 >= f.getGoalright().getMaxX());
	}

	/*
	 * 
	 * Left Goal Collisions
	 * 
	 */
	public boolean touchRectInLeft(FieldController f) {
		return (this.getX() - f.getX() <= 1.5 &&
				(this.getY() < f.getGoalright().getY() ||
						this.getMaxY() > f.getGoalright().getMaxY()
						) || this.getX() <= f.getGoalleft().getX());
	}

	public boolean touchGoalLeftBottom(FieldController f) {
		return (this.getX() <= f.getGoalleft().getMaxX() &&
				this.getMaxY() >= f.getGoalleft().getMaxY());
	}

	public boolean touchGoalLeftTop(FieldController f) {
		return (this.getX() <= f.getGoalleft().getMaxX() &&
				this.getY() - 1 <= f.getGoalleft().getY());
	}

	/*
	 * 
	 * Set Dy and Dx based on the direction
	 * when two keys are pressed at the same 
	 * time so the movement is realistic.
	 * 
	 */
	public void setMovement() {
		if (this.getDx() == SPEED_ONE) {
			if (this.getDy() == SPEED_ONE) {
				this.setDx(SPEED_ONE_DIAG);
				this.setDy(SPEED_ONE_DIAG);
			} else if (this.getDy() == -SPEED_ONE) {
				this.setDx(SPEED_ONE_DIAG);
				this.setDy(-SPEED_ONE_DIAG);
			}
		} else if (this.getDx() == -SPEED_ONE) {
			if (this.getDy() == SPEED_ONE) {
				this.setDx(-SPEED_ONE_DIAG);
				this.setDy(SPEED_ONE_DIAG);
			} else if (this.getDy() == -SPEED_ONE) {
				this.setDx(-SPEED_ONE_DIAG);
				this.setDy(-SPEED_ONE_DIAG);
			}
		}
	}

	/*
	 * 
	 * 
	 * Move the player in the field
	 * 
	 * 
	 */
	public void moveIn(FieldController f, PlayerModel p) {
		// update speed
		this.setMovement();
		
		// actually move if in the field or in a goal
		if (this.insideRect(f) || this.insideGoals(f)) {
			this.setLocation(this.getX() + this.getDx(),
					this.getY() + this.getDy());
		}

		// otherwise, back up a little
		if (this.touchRectInLeft(f) ||
				this.near((Rectangle2D)p, Side.RIGHT.getId())) {
			this.setLocation(this.getX() - this.getDx(), this.getY());
		}

		if (this.touchRectInTop(f) ||
				this.touchGoalRightTop(f) ||
				this.touchGoalLeftTop(f) ||
				this.near((Rectangle2D)p, Side.UP.getId())) {
			this.setLocation(this.getX(), this.getY() - this.getDy());
		}

		if (this.touchRectInRight(f) ||
				this.near((Rectangle2D)p, Side.LEFT.getId())) {
			this.setLocation(this.getX() - this.getDx(), this.getY());
		}

		if (this.touchRectInBottom(f) ||
				this.touchGoalRightBottom(f) ||
				this.touchGoalLeftBottom(f) ||
				this.near((Rectangle2D)p, Side.DOWN.getId())) {
			this.setLocation(this.getX(), this.getY() - this.getDy());
		}
	}

	/*
	 * 
	 * server packet to player position
	 * also send the ball info 
	 * 
	 */
	public void msgToCoord(String msg, Ball ball) {

		if (msg != null) {
			System.out.println(msg);
			String data[] = msg.split("/");

			double nx = java.lang.Double.parseDouble(data[0]); 
			double ny = java.lang.Double.parseDouble(data[1]);

			this.setLocation(nx, ny);

			this.setScore(Integer.parseInt(data[2]));
			
			double bx = java.lang.Double.parseDouble(data[3]);
			double by = java.lang.Double.parseDouble(data[4]);
			double vx = java.lang.Double.parseDouble(data[5]);
			double vy = java.lang.Double.parseDouble(data[6]);
			
			ball.setLocation(bx,  by, vx, vy);
		}
	}

	/*
	 * 
	 * player and ball coordinates and score to string
	 * 
	 */
	public String toString(Ball ball) {
		StringBuilder msg = new StringBuilder();

		msg.append(this.getX() + "/");
		msg.append(this.getY() + "/");
		msg.append(this.getScore() + "/");
		msg.append(ball.getX() + "/");
		msg.append(ball.getY() + "/");
		msg.append(ball.getVx() + "/");
		msg.append(ball.getVy() + "/");

		return msg.toString();
	}
}