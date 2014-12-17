package net;

import geo.PlayerController;

public class PlayerPacket {
	private double x;
	private double y;
	private int score;
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;

	public PlayerPacket(PlayerController pc) {
		x = pc.getX();
		y = pc.getY();
		score = pc.getScore();
		left = pc.left;
		right = pc.right;
		up = pc.up;
		down = pc.down;
	}

	public static void toCtrl(PlayerPacket pp, PlayerController pm) {
		pm.x = pp.x;
		pm.y = pp.y;
		pm.score = pp.score;
		pm.left = pp.left;
		pm.right = pp.right;
		pm.up = pp.up;
		pm.down = pp.down;
	}

	@Override
	public String toString() {
		return "PlayerPacket [x=" + x + ", y=" + y + ", score=" + score
				+ ", left=" + left + ", right=" + right + ", up=" + up
				+ ", down=" + down + "]";
	}

}
