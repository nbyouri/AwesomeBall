package gui;

import geo.Ball;
import geo.Keys;
import geo.Player;
import geo.Field;
import geo.Text;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PlayerControls implements ActionListener {
	public Field field;
	public Player player;
	public Ball ball;
	public Keys keys;
	public KeyEvents kev;
	
	public PlayerControls(Field f, Ball b) {
		field = f;
		ball = b;
		
		// setup key events
		kev = new KeyEvents();
		
		// setup player
		player = new Player();
		
		// setup key indicator
		keys = new Keys(Board.KEYS_X_POS, Board.TOP_MENUS_Y_POS, 
				Board.KEYS_WIDTH, Board.TOP_MENUS_HEIGHT);
	}
	
	/**
	 * Listen to key events and update player location
	 */
	private class KeyEvents extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_LEFT) {
				player.setDx(-Player.SPEED_ONE);
				keys.setPressedKey(Keys.KEY_LEFT, Keys.KEY_ON);
				player.left = true;
			} else if (key == KeyEvent.VK_UP) {
				player.setDy(-Player.SPEED_ONE);
				keys.setPressedKey(Keys.KEY_UP, Keys.KEY_ON);
				player.up = true;
			} else if (key == KeyEvent.VK_RIGHT) {
				player.setDx(Player.SPEED_ONE);
				keys.setPressedKey(Keys.KEY_RIGHT, Keys.KEY_ON);
				player.right = true;
			} else if (key == KeyEvent.VK_DOWN) {
				player.setDy(Player.SPEED_ONE);
				keys.setPressedKey(Keys.KEY_DOWN, Keys.KEY_ON);
				player.down = true;
			} 
		}

		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_LEFT) {
				player.setDx(Player.STOP);
				keys.setPressedKey(Keys.KEY_LEFT, Keys.KEY_OFF);
				player.left = false;
			} else if (key == KeyEvent.VK_UP) {
				player.setDy(Player.STOP);
				keys.setPressedKey(Keys.KEY_UP, Keys.KEY_OFF);
				player.up = false;
			} else if (key == KeyEvent.VK_RIGHT) {
				player.setDx(Player.STOP);
				keys.setPressedKey(Keys.KEY_RIGHT, Keys.KEY_OFF);
				player.right = false;
			} else if (key == KeyEvent.VK_DOWN) {
				player.setDy(Player.STOP);
				keys.setPressedKey(Keys.KEY_DOWN, Keys.KEY_OFF);
				player.down = false;
			}
		}
	}
	
	public void actionPerformed(ActionEvent e) {
	
	}
}
