package test_jeu;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class Board extends JPanel implements ActionListener {
	private Timer timer;
	private Ball ball;
	private Field field;
	private JButton exit;
	private int pressedkeys[];
	
	// constants
	public static final int TOP_MENUS_X_POS = 50;
	public static final int TOP_MENUS_Y_POS = 15;
	public static final int BOARD_X_POS = 50;
	public static final int BOARD_Y_POS = 50;
	
	public Board() {
		// setup field and ball
		field = new Field();
		ball = new Ball();
		pressedkeys = new int[]{0, 0, 0, 0};
		
		// key listener and window settings
		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		setLayout(null);
		
		// quit button, add here and not in paint() or it won't work
		exit = new JButton("EXIT");
		exit.addActionListener(new CloseListener());
		add(exit);
		
		// timer
		timer = new Timer(5, this);
		timer.start();
	}

	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2 = (Graphics2D)g;
	    
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
	    		RenderingHints.VALUE_ANTIALIAS_ON);
	    g2.setRenderingHint(RenderingHints.KEY_RENDERING, 
	    		RenderingHints.VALUE_RENDER_QUALITY);

		// get screen size
		Dimension size = getSize();
		double w = size.getWidth();
		double h = size.getHeight();
		
		// initialize field and draw it 
		// position is relative to the 
		// jframe size
		field.setRect(BOARD_X_POS, BOARD_Y_POS, 
				w - BOARD_X_POS - BOARD_X_POS,
				h - BOARD_Y_POS - BOARD_Y_POS);
		field.drawField(g2);
		
		// detect side of rectangle the ball sticks with
		field.setSides();
	
		// detect ball side field hit
		field.drawSides(g2, ball.approaches(field));
		// draw ball
		ball.draw(g2);
		
		// detect ball rotation 
		// XXX to fix
		//ball.rotate(g2, ball.getRotation());
		
		
		// draw position box
		// setup font settings
		Font myFont = new Font("Helvetica", Font.BOLD, 12);
		g2.setFont(myFont);
		Field pos = new Field();
		g2.setColor(Color.cyan);
		//String posxystr = ("Ball Position\n x = " + ball.getX() + "\ny = " + ball.getY());
		String posxystr = "SpaceCraft Collider";
		pos.setRect(TOP_MENUS_X_POS, TOP_MENUS_Y_POS, 130, 22);
		g2.drawString(posxystr, (int)pos.getX() + 10, (int)pos.getY() + 15);
		g2.draw(pos);

		// draw key box
		Field keys = new Field();
		keys.setBounds(190, 15, 20, 22);
		keys.drawField(g2);
		
		// detect key presses
		keys.setSides();
		ArrayList<Integer> arkeys = new ArrayList<Integer>();
		for (int i = 0; i < pressedkeys.length; i++) {
			if (pressedkeys[i] == 1)
				arkeys.add(i);
		}
		keys.drawSides(g2, arkeys);
		
		// exit button settings
		Border cyanborder = new LineBorder(Color.cyan, 1);
		exit.setFont(myFont);
		exit.setBorder(cyanborder);
		exit.setForeground(Color.cyan);
		exit.setBackground(Color.black);
		exit.setFocusPainted(false);
		exit.setContentAreaFilled(false);
		// set position relative to the field
		exit.setBounds(BOARD_X_POS,
				BOARD_X_POS + (int)field.getHeight() + 
				(int)((h - (BOARD_Y_POS + field.getHeight())) / 4),
				(int)field.getWidth(), 22);

		// clear things up
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
				ball.dx = -1;
				pressedkeys[0] = 1;
				ball.setRotation(180);
			}

			if (key == KeyEvent.VK_RIGHT) {
				ball.dx = 1;
				pressedkeys[2] = 1;
				ball.setRotation(0);
			}

			if (key == KeyEvent.VK_UP) {
				ball.dy = -1;
				pressedkeys[1] = 1;
				ball.setRotation(90);
			}

			if (key == KeyEvent.VK_DOWN) {
				ball.dy = 1;
				pressedkeys[3] = 1;
				ball.setRotation(270);
			}
		}

		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_LEFT) {
				ball.dx = 0;
				pressedkeys[0] = 0;
			}

			if (key == KeyEvent.VK_RIGHT) {
				ball.dx = 0;
				pressedkeys[2] = 0;
			}

			if (key == KeyEvent.VK_UP) {
				ball.dy = 0;
				pressedkeys[1] = 0;
			}

			if (key == KeyEvent.VK_DOWN) {
				ball.dy = 0;
				pressedkeys[3] = 0;
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
			//System.exit(0);
		}
	}
}