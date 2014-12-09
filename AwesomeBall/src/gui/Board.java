package gui;

import geo.*;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


@SuppressWarnings("serial")
public class Board extends JPanel implements ActionListener {
	private Timer timer;
	private Text title;
	private Text rotation;
	private Text score;
	private Player player;
	private Ball ball;
	private Field field;
	private Button exit;
	private Keys keys;

	// constants
	public static final int TOP_MENUS_X_POS = 50;
	public static final int TOP_MENUS_Y_POS = 15;
	public static final int TOP_MENUS_HEIGHT = 22;
	public static final int TOP_TITLE_WIDTH = 130;
	public static final int BOARD_X_POS = 50;
	public static final int BOARD_Y_POS = 50;
	public static final int KEYS_X_POS = 190;
	public static final int KEYS_WIDTH = 20;
	public static final int ROTATION_X = 220;
	public static final int ROTATION_WIDTH = 50;
	public static final int SCORES_X = 290;
	public static final int SCORES_WIDTH = 50;
	public static final int FPS = 5;
	public static final int EXIT_SUCCESS = 0;

	/**
	 * Propportionalising the field
	 * 
	 * Setup the game title, the field
	 * the player (two in the future), 
	 * the ball, key indicator,
	 * rotation indicator.
	 * 
	 * Configure the key listener
	 * and the window
	 * 
	 * Set a quit button and 
	 * a timer
	 * 
	 * @param Dimension : The screen size. 
	 */
	public Board(Dimension boardSize) {

		// proportional field , H = 60yds, W = 100yds, Center radius = 10yds
		double field_height = boardSize.getHeight() - (3 * BOARD_Y_POS);
		double field_width = (field_height / 6)*10;


		// setup game title
		title = new Text("SpaceShip Collider");
		title.setRect(TOP_MENUS_X_POS, TOP_MENUS_Y_POS, 
				TOP_TITLE_WIDTH, TOP_MENUS_HEIGHT);

		// setup field 
		field = new Field();
		field.setSize(BOARD_X_POS, BOARD_Y_POS, 
				field_width, field_height);
		field.setCenterCircle();


		// setup player
		player = new Player();

		// setup ball
		ball = new Ball(field.getCenterX(), 
				field.getY() + (field_height / 2) - 10,
				player.getWidth(), player.getHeight());


		// setup key indicator
		keys = new Keys();
		keys.setSize(KEYS_X_POS, TOP_MENUS_Y_POS, 
				KEYS_WIDTH, TOP_MENUS_HEIGHT);

		// setup rotation indicator
		rotation = new Text(null);
		rotation.setRect(ROTATION_X, TOP_MENUS_Y_POS, 
				ROTATION_WIDTH, TOP_MENUS_HEIGHT);

		// setup score indicator
		score = new Text(null);
		score.setRect(SCORES_X, TOP_MENUS_Y_POS, 
				SCORES_WIDTH, TOP_MENUS_HEIGHT);

		// key listener and window settings
		addKeyListener(new KeyEvents());
		setFocusable(true);
		setBackground(Color.black);
		setDoubleBuffered(true);
		setLayout(null);

		// quit button, add here and not in paint() 
		// or it won't work
		exit = new Button("EXIT", new CloseListener());
		// place below the field
		exit.setBounds(BOARD_X_POS,
				BOARD_X_POS + (int)field.getHeight() + 
				(int)((boardSize.getHeight() - 
						(BOARD_Y_POS + field.getHeight())) / 4),
						(int)field.getWidth(), TOP_MENUS_HEIGHT);
		add(exit);

		// timer
		timer = new Timer(FPS, this);
		timer.start();
	}

	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2 = (Graphics2D)g;

		// smooth rendering, otherwise fonts look bad
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, 
				RenderingHints.VALUE_RENDER_QUALITY);

		// draw title
		title.draw(g2);

		// draw field and it's center line
		field.draw(g2);
		field.setSides();
		field.drawCenterLines(g2);

		// draw player
		player.draw(g2);

		// draw ball
		ball.draw(g2);
		//ball.move(player, field);

		// draw key box
		keys.draw(g2);
		keys.setSides();
		keys.drawSides(g2, keys.getPressedKeys());

		// draw rotation box
		rotation.setStr(
				Player.Direction.getName(player.getImg().getRotation())
				);

		rotation.draw(g2);
		
		// draw score box
		score.setStr(Integer.toString(player.getScore()));
		score.draw(g2);

		// clean
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}
	
	/**
	 * Get the player
	 * @return 
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Set the player
	 * @param player 
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	public void actionPerformed(ActionEvent e) {
		player.moveIn(field);
		ball.move(field,player);
		ball.brake();
		repaint();
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
			} else if (key == KeyEvent.VK_SPACE) {
				ball.shootBall(field,player);
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
			} else if (key == KeyEvent.VK_ENTER) {
				ball.centerBall(field);
			} else if (key == KeyEvent.VK_ESCAPE) {
				exitProgram();
			}
		}
	}
	
	/**
	 * Exit button action implementation
	 */
	private class CloseListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			exitProgram();
		}
	}

	/**
	 * Exit in a clean way
	 */
	public void exitProgram() {
		Container frame = this.getParent();
		do {
			frame = frame.getParent();
		} while (!(frame instanceof JFrame));
		((JFrame) frame).dispose();
		System.exit(EXIT_SUCCESS);
	}

}