package gui;

import geo.*;
import net.*;

import java.awt.BorderLayout;
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
import java.net.InetAddress;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;


@SuppressWarnings("serial")
public class Board extends JPanel implements ActionListener {
	private Timer timer;

	private Text title;
	private Text score;

	public PlayerView player1;
	public PlayerView player2;

	private Ball ball;
	private FieldView field;
	private Button exit;

	private UDPServer playerserv;
	private UDPServer ballserv;
	private InetAddress other_player_address;

	// constants
	public static final int TOP_MENUS_X_POS = 50;
	public static final int TOP_MENUS_Y_POS = 15;
	public static final int TOP_MENUS_HEIGHT = 22;
	public static final int TOP_TITLE_WIDTH = 79;
	public static final int BOARD_X_POS = 50;
	public static final int BOARD_Y_POS = 50;
	public static final int KEYS_X_POS = 190;
	public static final int KEYS_WIDTH = 20;
	public static final int ROTATION_X = 220;
	public static final int ROTATION_WIDTH = 50;
	public static final int SCORES_X = 290;
	public static final int SCORES_WIDTH = 155;
	public static final int FPS = 17; // 60 fps
	public static final int EXIT_SUCCESS = 0;
	public static final int PPORT = 1233;
	public static final int BPORT = 1234;
	public static final int P2PORT = 1235;
	public static final int B2PORT = 1236;

	/**
	 * Initialise le serveur
	 * 
	 * Proportionnalise le terrain
	 * 
	 * Initialise le titre du jeu, le terrain, les deux joueurs, la balle, la
	 * case indicateur des touches directionnelles ainsi que l'indicateur de
	 * rotation.
	 * 
	 * Configure le "key listener" et l'écran
	 * 
	 * Active un boutton "Quitter" et un timer.
	 * 
	 * @param boardSize
	 *            , la taille de l'écran.
	 */
	public Board(Dimension boardSize) throws Exception {
		JTextArea textarea = new JTextArea("vous êtes "
				+ InetAddress.getLocalHost().toString());
		 textarea.setEditable(true);
		int type = JOptionPane.showConfirmDialog(null, textarea,
				"Êtes vous un serveur ?", JOptionPane.INFORMATION_MESSAGE);

		if (type == JOptionPane.CANCEL_OPTION
				|| type == JOptionPane.CLOSED_OPTION) {
			System.exit(0);
		}
		boolean host = (type == 0);

		String ip = "";
		while (ip.isEmpty()) {
			ip = inputIp.getInput("Entrer l'IP de l'autre joueur", null);
		}
		try {
			other_player_address = InetAddress.getByName(ip);
		} catch (Exception uhe) {
			Dialog d = new Dialog("Adresse Erronée");
			ActionListener exit = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			};
			Button ex = new Button("exit", exit);
			ex.setPreferredSize(new Dimension(d.getWidth(), 50));
			d.add(ex, BorderLayout.SOUTH);
			d.setVisible(true);
		}
		// InetAddress.getByName("192.168.12.76");

		// Applique les proportions du terrains , H = 60yds, W = 100yds, Center
		// radius = 10yds
		double field_height = boardSize.getHeight() - (3 * BOARD_Y_POS);
		double field_width = (field_height / 6) * 10;

		// setup game title
		title = new Text("GénialBall");

		title.setRect(TOP_MENUS_X_POS, TOP_MENUS_Y_POS, TOP_TITLE_WIDTH,
				TOP_MENUS_HEIGHT);

		// setup field
		field = new FieldView(BOARD_X_POS, BOARD_Y_POS, field_width,
				field_height);
		field.field.setCenterCircle();

		// setup players and put them in the right place
		player1 = new PlayerView(field, ball, host);
		player2 = new PlayerView(field, ball, !host);

		PlayerController.initPosition(field.field, player1.player,
				player2.player);

		// setup ball
		ball = new Ball(field.field);

		try {
			playerserv = new UDPServer(other_player_address,
					player1.player.host ? P2PORT : PPORT);
			ballserv = new UDPServer(other_player_address,
					player1.player.host ? B2PORT : BPORT);
			Thread pserv = new Thread(playerserv);
			Thread bserv = new Thread(ballserv);
			pserv.start();
			bserv.start();
		} catch (Exception e) {
			System.out.println("Failed to start servers");
			System.exit(1);
		}

		// setup score indicator
		score = new Text(null);
		score.setRect(SCORES_X, TOP_MENUS_Y_POS, SCORES_WIDTH, TOP_MENUS_HEIGHT);

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
		exit.setBounds(
				BOARD_X_POS,
				BOARD_X_POS
				+ (int) field.field.getHeight()
				+ (int) ((boardSize.getHeight() - (BOARD_Y_POS + field.field
						.getHeight())) / 4),
						(int) field.field.getWidth(), TOP_MENUS_HEIGHT);
		add(exit);

		// timer
		timer = new Timer(FPS, this);
		timer.start();
	}

	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2 = (Graphics2D) g;

		// smooth rendering, otherwise fonts look bad
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		// listen to info if we're server , otherwise send it
		try {
			UDPClient.send(ball.toBytes(), other_player_address, 
					player1.player.host ? BPORT : B2PORT);
			ball.toBall(ballserv.getBytes());
		} catch (Exception e) {
		}

		try {
			UDPClient.send(player1.player.toBytes(), other_player_address,
					player1.player.host ? PPORT : P2PORT);
		} catch (Exception e) {
		}

		try {
			player2.player.toPlayer(playerserv.getBytes());
		} catch (Exception e) {
		}

		// draw title
		title.draw(g2);

		// draw field and it's center line
		field.draw(g2, player1.player, player2.player);

		// draw player
		player1.draw(g2);

		// draw player2
		player2.draw(g2);

		// draw ball
		ball.draw(g2);

		// draw key box
		player1.keys.draw(g2);

		// draw score box
		score.setStr("Player 1 : "
				+ Integer.toString(player1.player.getScore())
				+ " / Player 2 : "
				+ Integer.toString(player2.player.getScore()));
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
		ball.move(field.field, player1.player, player2.player);

		repaint();
	}

	private class KeyEvents extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_ENTER) {
				ball.centerBall(field.field);
			} else if (key == KeyEvent.VK_SPACE) {
				ball.shoot(field.field, player1.player);
			}
		}

		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_ESCAPE) {
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