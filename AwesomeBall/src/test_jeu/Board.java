package test_jeu;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
//import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
//import java.util.ArrayList;



import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


@SuppressWarnings("serial")
public class Board extends JPanel implements ActionListener {
	private Timer timer;
	private TextField title;
	private Ball ball;
	private Field field;
	private Button exit;
	private KeyIndicator keys;
	public Dimension size;
	
	// constants
	public static final int TOP_MENUS_X_POS = 50;
	public static final int TOP_MENUS_Y_POS = 15;
	public static final int BOARD_X_POS = 50;
	public static final int BOARD_Y_POS = 50;
	
	public Board(Dimension boardSize) {
		// get screen size
		size = boardSize;

		// setup game title
		title = new TextField("SpaceShip Collider");
		title.setSize(TOP_MENUS_X_POS, TOP_MENUS_Y_POS, 130, 22);
		
		// setup field 
		field = new Field();
		field.setSize(BOARD_X_POS, BOARD_Y_POS, 
				this.size.getWidth() - BOARD_X_POS - BOARD_X_POS,
				this.size.getHeight() - (3 * BOARD_Y_POS));
		
		// setup ball
		ball = new Ball();
		
		// setup key indicator
		keys = new KeyIndicator();
		keys.setSize(190, 15, 20, 22);
		
		// key listener and window settings
		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		setLayout(null);
		
		// quit button, add here and not in paint() 
		// or it won't work
		exit = new Button("EXIT", new CloseListener());
		exit.setBounds(BOARD_X_POS,
				BOARD_X_POS + (int)field.getHeight() + 
				(int)((this.size.getHeight() - (BOARD_Y_POS + field.getHeight())) / 4),
				(int)field.getWidth(), 22);
		add(exit);
		
		// timer
		timer = new Timer(5, this);
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
	    
	    // draw field
		field.draw(g2);
		field.drawSides(g2, ball.approaches(field));
		
		// draw ball
		ball.draw(g2);
		ball.drawSides(g2, ball.approaches(field));
		
		// draw key box
		keys.draw(g2);
		keys.drawSides(g2, keys.getPressedKeys());

		// clean
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}


	public void actionPerformed(ActionEvent e) {
		ball.move(field);
		repaint();
	}
	
	// listen to key events and update ball location
	private class TAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();

			if (key == KeyEvent.VK_LEFT) {
				ball.setDx(-1);
				keys.setPressedKey(0, 1);
				ball.setRotation(180);
			}
			
			if (key == KeyEvent.VK_UP) {
				ball.setDy(-1);
				keys.setPressedKey(1, 1);
				ball.setRotation(90);
			}

			if (key == KeyEvent.VK_RIGHT) {
				ball.setDx(1);
				keys.setPressedKey(2, 1);
				ball.setRotation(0);
			}
			
			if (key == KeyEvent.VK_DOWN) {
				ball.setDy(1);
				keys.setPressedKey(3, 1);
				ball.setRotation(270);
			}
		}

		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_LEFT) {
				ball.setDx(0);
				keys.setPressedKey(0, 0);
			}
			
			if (key == KeyEvent.VK_UP) {
				ball.setDy(0);
				keys.setPressedKey(1, 0);
			}
			
			if (key == KeyEvent.VK_RIGHT) {
				ball.setDx(0);
				keys.setPressedKey(2, 0);
			}	

			if (key == KeyEvent.VK_DOWN) {
				ball.setDy(0);
				keys.setPressedKey(3, 0);
			}
		}
	}
	
	// exit button action implementation
	private class CloseListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Container frame = exit.getParent();
			do
				frame = frame.getParent();
			while (!(frame instanceof JFrame));
			((JFrame) frame).dispose();
			System.exit(0);
		}
	}
}