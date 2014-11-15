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
	private Player player;
	private Ball ball;
	private Field field;
	private Button exit;
	private Keys keys;
	private Field goalleft;
	private Field goalright;
	
	// constants
	public static final int TOP_MENUS_X_POS = 50;
	public static final int TOP_MENUS_Y_POS = 15;
	public static final int TOP_MENUS_HEIGHT = 22;
	public static final int TOP_TITLE_WIDTH = 130;
	public static final int BOARD_X_POS = 50;
	public static final int BOARD_Y_POS = 50;
	public static final int GOALS_LEFT_X = 20;
	public static final int GOALS_WIDTH = 30;
	public static final int GOALS_HEIGHT = 80;
	public static final int KEYS_X_POS = 190;
	public static final int KEYS_WIDTH = 20;
	public static final int SCORES_X = 220;
	public static final int SCORES_WIDTH = 50;
	public static final int FPS = 5;
	public static final int EXIT_SUCCESS = 0;
	
	public Board(Dimension boardSize) {

		// proportional field , H = 60yds, W = 100yds, Center radius = 10yds
		double field_height = boardSize.getHeight() - (3 * BOARD_Y_POS);
		double field_width = (field_height / 6)*10;
		
		
		// setup game title
		title = new Text("SpaceShip Collider");
		title.setSize(TOP_MENUS_X_POS, TOP_MENUS_Y_POS, 
				TOP_TITLE_WIDTH, TOP_MENUS_HEIGHT);
		
		// setup field 
		field = new Field();
		field.setSize(BOARD_X_POS, BOARD_Y_POS, 
				field_width, field_height);
		field.setCenterCircle();
		
		// setup goals
		goalleft = new Field();
		goalright = new Field();
		goalleft.setSize(GOALS_LEFT_X, field.getY() + (field_height / 3), GOALS_WIDTH, field_height / 3);
		goalright.setSize(BOARD_X_POS + field_width, field.getY() + (field_height / 3), GOALS_WIDTH, field_height / 3);
		
		// setup player
		player = new Player();
		
		// setup ball
		ball = new Ball(field.getCenterX(), 
				field.getY() + (field_height / 2) - 10,
				player.getWidth(), player.getHeight());
				
		
		// setup key indicator
		keys = new Keys();
		keys.setSize(KEYS_X_POS, TOP_MENUS_Y_POS, KEYS_WIDTH, TOP_MENUS_HEIGHT);
		
		// setup rotation indicator
		rotation = new Text(null);
		rotation.setSize(SCORES_X, TOP_MENUS_Y_POS, SCORES_WIDTH, TOP_MENUS_HEIGHT);
		
		
		// key listener and window settings
		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		setLayout(null);
		setDoubleBuffered(true);
		
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
		field.drawSides(g2, player.approaches(field.getBounds()));
		field.drawCenterLines(g2);
		
		// draw goals
		goalleft.draw(g2);
		goalright.draw(g2);
		
		// draw player
		player.draw(g2);
		player.drawSides(g2, player.approaches(field.getBounds()));
		
		// draw ball
		ball.draw(g2);
		ball.move(player, field);
		
		// draw key box
		keys.draw(g2);
		keys.setSides();
		keys.drawSides(g2, keys.getPressedKeys());
		
		// draw rotation box => score box?
		rotation.setStr(Player.Direction.getName(player.getImg().getRotation()));
		rotation.draw(g2);

		// clean
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}


	public void actionPerformed(ActionEvent e) {
		player.moveIn(field.getBounds());
		repaint();
	}
	
	// listen to key events and update player location
	private class TAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();

			if (key == KeyEvent.VK_LEFT) {
				player.setDx(-Player.SPEED_ONE);
				keys.setPressedKey(Keys.KEY_LEFT, Keys.KEY_ON);
				player.drawLeft();
			}
			
			if (key == KeyEvent.VK_UP) {
				player.setDy(-Player.SPEED_ONE);
				keys.setPressedKey(Keys.KEY_UP, Keys.KEY_ON);
				player.drawUp();
			}

			if (key == KeyEvent.VK_RIGHT) {
				player.setDx(Player.SPEED_ONE);
				keys.setPressedKey(Keys.KEY_RIGHT, Keys.KEY_ON);
				player.drawRight();
			}
			
			if (key == KeyEvent.VK_DOWN) {
				player.setDy(Player.SPEED_ONE);
				keys.setPressedKey(Keys.KEY_DOWN, Keys.KEY_ON);
				player.drawDown();
			}
		}

		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_LEFT) {
				player.setDx(Player.STOP);
				keys.setPressedKey(Keys.KEY_LEFT, Keys.KEY_OFF);
			}
			
			if (key == KeyEvent.VK_UP) {
				player.setDy(Player.STOP);
				keys.setPressedKey(Keys.KEY_UP, Keys.KEY_OFF);
			}
			
			if (key == KeyEvent.VK_RIGHT) {
				player.setDx(Player.STOP);
				keys.setPressedKey(Keys.KEY_RIGHT, Keys.KEY_OFF);
			}	

			if (key == KeyEvent.VK_DOWN) {
				player.setDy(Player.STOP);
				keys.setPressedKey(Keys.KEY_DOWN, Keys.KEY_OFF);
			}
			
			if (key == KeyEvent.VK_ESCAPE) {
				exitProgram();
			}
		}
	}
	
	// exit button action implementation
	private class CloseListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			exitProgram();
		}
	}
	
	// exit in a clean way
	public void exitProgram() {
		Container frame = this.getParent();
		do {
			frame = frame.getParent();
		} while (!(frame instanceof JFrame));
		((JFrame) frame).dispose();
		System.exit(EXIT_SUCCESS);
	}

}