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
	private TextField title;
	private TextField rotation;
	private Player player;
	private Ball ball;
	private Field field;
	private Button exit;
	private KeyIndicator keys;
	
	// constants
	public static final int TOP_MENUS_X_POS = 50;
	public static final int TOP_MENUS_Y_POS = 15;
	public static final int TOP_MENUS_HEIGHT = 22;
	public static final int TOP_TITLE_WIDTH = 130;
	public static final int BOARD_X_POS = 50;
	public static final int BOARD_Y_POS = 50;
	public static final int FPS = 5;
	public static final int EXIT_SUCCESS = 0;
	
	public Board(Dimension boardSize) {

		// proportional field , H = 60yds, W = 100yds, Center radius = 10yds
		double field_height = boardSize.getHeight() - (3 * BOARD_Y_POS);
		double field_width = (field_height / 6)*10;
		
		
		// setup game title
		title = new TextField("SpaceShip Collider");
		title.setSize(TOP_MENUS_X_POS, TOP_MENUS_Y_POS, 
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
		keys = new KeyIndicator();
		keys.setSize(190, TOP_MENUS_Y_POS, 20, TOP_MENUS_HEIGHT);
		
		// setup rotation indicator
		rotation = new TextField(null);
		rotation.setSize(220, 15, 38, TOP_MENUS_HEIGHT);
		
		
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
		field.drawSides(g2, player.approaches(field));
		field.drawCenterLines(g2);
		
		// draw player
		player.draw(g2);
		player.drawSides(g2, player.approaches(field));
		
		// draw ball
		ball.draw(g2);
		
		// draw key box
		keys.draw(g2);
		keys.drawSides(g2, keys.getPressedKeys());
		
		// draw rotation box
		String r = Integer.toString(player.getImg().getRotation());
		rotation.setStr(r);
		rotation.draw(g2);

		// clean
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}


	public void actionPerformed(ActionEvent e) {
		player.move(field);
		repaint();
	}
	
	// listen to key events and update player location
	private class TAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();

			if (key == KeyEvent.VK_LEFT) {
				player.setDx(-Player.SPEED_ONE);
				keys.setPressedKey(KeyIndicator.KEY_LEFT, KeyIndicator.KEY_ON);
				player.drawLeft();
			}
			
			if (key == KeyEvent.VK_UP) {
				player.setDy(-Player.SPEED_ONE);
				keys.setPressedKey(KeyIndicator.KEY_UP, KeyIndicator.KEY_ON);
				player.drawUp();
			}

			if (key == KeyEvent.VK_RIGHT) {
				player.setDx(Player.SPEED_ONE);
				keys.setPressedKey(KeyIndicator.KEY_RIGHT, KeyIndicator.KEY_ON);
				player.drawRight();
			}
			
			if (key == KeyEvent.VK_DOWN) {
				player.setDy(Player.SPEED_ONE);
				keys.setPressedKey(KeyIndicator.KEY_DOWN, KeyIndicator.KEY_ON);
				player.drawDown();
			}
		}

		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_LEFT) {
				player.setDx(Player.STOP);
				keys.setPressedKey(KeyIndicator.KEY_LEFT, KeyIndicator.KEY_OFF);
			}
			
			if (key == KeyEvent.VK_UP) {
				player.setDy(Player.STOP);
				keys.setPressedKey(KeyIndicator.KEY_UP, KeyIndicator.KEY_OFF);
			}
			
			if (key == KeyEvent.VK_RIGHT) {
				player.setDx(Player.STOP);
				keys.setPressedKey(KeyIndicator.KEY_RIGHT, KeyIndicator.KEY_OFF);
			}	

			if (key == KeyEvent.VK_DOWN) {
				player.setDy(Player.STOP);
				keys.setPressedKey(KeyIndicator.KEY_DOWN, KeyIndicator.KEY_OFF);
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