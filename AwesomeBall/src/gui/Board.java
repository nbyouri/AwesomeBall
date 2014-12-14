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

import net.initServer;


@SuppressWarnings("serial")
public class Board extends JPanel implements ActionListener {
	private Timer timer;

	private Text title;
	private Text score;

	public  PlayerView player1;
	public  PlayerView player2;

	private Ball ball;
	private FieldView field;
	private Button exit;

	private initServer serv;

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
         * Initialise le serveur
         * 
	 * Proportionnalise le terrain
         * 
         * Initialise le titre du jeu, le terrain,
         * les deux joueurs, la balle, la case indicateur des touches
         * directionnelles ainsi que l'indicateur de rotation.
         * 
         * Configure le "key listener" et l'écran
         * 
         * Active un boutton "Quitter" et un timer.
	 * @param boardSize, la taille de l'écran. 
	 */
	public Board(Dimension boardSize) {

		// Initialisation du serveur
		try {
			serv = new initServer();
			Thread servth = new Thread(serv);
			servth.start();
		} catch(Exception ex) {
			System.out.println("Failed to initServer");
		}


		// Applique les proportions du terrains , H = 60yds, W = 100yds, Center radius = 10yds
		double field_height = boardSize.getHeight() - (3 * BOARD_Y_POS);
		double field_width = (field_height / 6)*10;


		// setup game title
		title = new Text("SpaceShip Collider");

		title.setRect(TOP_MENUS_X_POS, TOP_MENUS_Y_POS, 
				TOP_TITLE_WIDTH, TOP_MENUS_HEIGHT);

		// setup field 
		field = new FieldView(BOARD_X_POS, BOARD_Y_POS, 
				field_width, field_height);
		field.field.setCenterCircle();

		// setup player 1
		player1 = new PlayerView(field, ball);
		player1.player.setLocation(100,  100);
		player2 = new PlayerView(field, ball);
		player2.player.setLocation(200,  200);

		// setup ball
		ball = new Ball(field.field.getCenterX(), 
				field.field.getY() + (field_height / 2) - 10,
				player1.player.getWidth(), player1.player.getHeight());

		// setup score indicator
		score = new Text(null);
		score.setRect(SCORES_X, TOP_MENUS_Y_POS, 
				SCORES_WIDTH, TOP_MENUS_HEIGHT);

		// key listener and window settings
		addKeyListener(player1.kev);
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
				BOARD_X_POS + (int)field.field.getHeight() + 
				(int)((boardSize.getHeight() - 
						(BOARD_Y_POS + field.field.getHeight())) / 4),
						(int)field.field.getWidth(), TOP_MENUS_HEIGHT);
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

		// update player 1 location on server/client
		if (serv.getServ().getSocket() != null &&
				serv.getServ().getSocket().isConnected()) {
			try {
				serv.getServ().sendMsg(player1.toString(ball));
			} catch (Exception ex) {
				System.out.println("Failed to send player coordinates to server");
			}
		}

		// receive player 2 info if the client is connected, otherwise, retry
		if (serv.getClient() != null) {
			player2.player.msgToCoord(serv.getClient().getMessage(), ball);
		}

		// draw title
		title.draw(g2);

		// draw field and it's center line
		field.draw(g2);

		// draw player
		player1.draw(g2);

		// draw player2
		player2.draw(g2);

		// draw ball
		ball.draw(g2);

		// draw key box
		player1.keys.draw(g2);
		player1.keys.drawSides(g2, player1.keys.getPressedKeys());

		// draw score box
		score.setStr(Integer.toString(player1.player.getScore()) + " / " + 
				Integer.toString(player2.player.getScore()));
		score.draw(g2);

		// clean
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	public void actionPerformed(ActionEvent e) {
		// move player
		player1.player.moveIn(field.field, player2.player);
		player2.player.moveIn(field.field, player1.player);

		// move ball
		ball.move(field.field, player1.player);
		ball.move(field.field, player2.player);
		ball.brake();

		repaint();
	}

	private class KeyEvents extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_SPACE) {
				ball.shootBall(field.field,player1.player);
			}
		}

		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_ENTER) {
				ball.centerBall(field.field);
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
		serv.closeSocket();
		Container frame = this.getParent();
		do {
			frame = frame.getParent();
		} while (!(frame instanceof JFrame));
		((JFrame) frame).dispose();

		System.exit(EXIT_SUCCESS);
	}
}