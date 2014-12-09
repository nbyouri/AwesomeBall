package main;

import gui.Board;
//import gui.PositionBox;


import java.awt.EventQueue;
//import java.awt.GraphicsDevice;
//import java.awt.GraphicsEnvironment;






import javax.swing.JFrame;

import net.Server;

/*
 * TODO:
 * more constants
 * javadoc
 * block comments
 * MVC
 */

@SuppressWarnings("serial")
public class SpaceBalls extends JFrame {

	Board mainBoard;
	public SpaceBalls() throws Exception {

		// full screen code
		/*GraphicsEnvironment env = 
         	GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = env.getDefaultScreenDevice();
        setUndecorated(true);
        defaultScreen.setFullScreenWindow(this);*/

		// our main window is 850x600 for now.
		this.setSize(850, 600);

		// initialize a board which takes the whole screen
		mainBoard = new Board(this.getSize());

		// add the jpanel in the jframe
		add(mainBoard);

		// server
		try {
			Server s = new Server(mainBoard.getPlayer());
			s.start();
			//s.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// jframe settings
		this.setTitle("SpaceBalls");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

	}    

	public static void main(String[] args) {
		// main loop
		EventQueue.invokeLater(new Runnable() {
			// main application
			@Override
			public void run() {
				try {
					SpaceBalls ex = new SpaceBalls();
					ex.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}