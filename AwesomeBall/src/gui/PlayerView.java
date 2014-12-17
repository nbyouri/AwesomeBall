package gui;

import geo.Ball;
import geo.Keys;
import geo.PlayerController;
import geo.PlayerModel;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PlayerView implements ActionListener {
	public FieldView field;
	public PlayerController player;
	public Ball ball;
	public Keys keys;
	public KeyEvents kev;

	public PlayerView(FieldView f, Ball b, boolean host) {
		field = f;
		ball = b;

		// setup key events
		kev = new KeyEvents();

		// setup player
		player = new PlayerController(host);
		
		// setup key indicator
		keys = new Keys(Board.KEYS_X_POS, Board.TOP_MENUS_Y_POS,
				Board.KEYS_WIDTH, Board.TOP_MENUS_HEIGHT);
	}

	// draw rectangle and ball
	public void draw(Graphics2D g2) {
		player.setRotation();
		g2.drawImage(player.getImg().getPlayer(), (int) player.getX(),
				(int) player.getY(), (int) player.getWidth(),
				(int) player.getHeight(), null);
		g2.setColor(Color.WHITE);
		// g2.draw(player.getEll());
		// g2.draw(this.player);
	}

	/**
	 * Listen to key events and update player location
	 */
	private class KeyEvents extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_LEFT) {
				player.setDx(-PlayerModel.SPEED_ONE);
				keys.setPressedKey(Keys.KEY_LEFT, Keys.KEY_ON);
				player.left = true;
			} else if (key == KeyEvent.VK_UP) {
				player.setDy(-PlayerModel.SPEED_ONE);
				keys.setPressedKey(Keys.KEY_UP, Keys.KEY_ON);
				player.up = true;
			} else if (key == KeyEvent.VK_RIGHT) {
				player.setDx(PlayerModel.SPEED_ONE);
				keys.setPressedKey(Keys.KEY_RIGHT, Keys.KEY_ON);
				player.right = true;
			} else if (key == KeyEvent.VK_DOWN) {
				player.setDy(PlayerModel.SPEED_ONE);
				keys.setPressedKey(Keys.KEY_DOWN, Keys.KEY_ON);
				player.down = true;
			}
		}

		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_LEFT) {
				player.setDx(PlayerModel.STOP);
				keys.setPressedKey(Keys.KEY_LEFT, Keys.KEY_OFF);
				player.left = false;
			} else if (key == KeyEvent.VK_UP) {
				player.setDy(PlayerModel.STOP);
				keys.setPressedKey(Keys.KEY_UP, Keys.KEY_OFF);
				player.up = false;
			} else if (key == KeyEvent.VK_RIGHT) {
				player.setDx(PlayerModel.STOP);
				keys.setPressedKey(Keys.KEY_RIGHT, Keys.KEY_OFF);
				player.right = false;
			} else if (key == KeyEvent.VK_DOWN) {
				player.setDy(PlayerModel.STOP);
				keys.setPressedKey(Keys.KEY_DOWN, Keys.KEY_OFF);
				player.down = false;
			}
		}
	}

	public void actionPerformed(ActionEvent e) {

	}

	public String toString(Ball b) {
		return player.toString(b);
	}
}
