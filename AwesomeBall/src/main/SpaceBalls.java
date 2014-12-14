package main;

/*
 * 
 * TODO : 
 * 
 * detect other player connection better
 * send ball location as well (totest)
 * create customized optionpane
 * don't try if network is disabled
 * correct goals
 */
import gui.Board;

import java.awt.EventQueue;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class SpaceBalls extends JFrame {

	Board mainBoard;

	public SpaceBalls() throws Exception {
		// our main window is 850x600 for now.
		this.setSize(850, 600);

		// initialize a board which takes the whole screen
		mainBoard = new Board(this.getSize());

		// add the jpanel in the jframe
		add(mainBoard);

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