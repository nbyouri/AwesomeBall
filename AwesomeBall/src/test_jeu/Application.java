package test_jeu;

import java.awt.EventQueue;
//import java.awt.GraphicsDevice;
//import java.awt.GraphicsEnvironment;


import javax.swing.JFrame;

/*
 * TODO:
 * more constants
 * javadoc
 * block comments
 * 
 * FUNCTIONALITY:
 * acceleration
 * sounds
 * shoot bullets
 * threading
 * 
 * QUICKFIX:
 * spaceship rotation : fix left
 */

@SuppressWarnings("serial")
public class Application extends JFrame {

	public Application() {

        // full screen code
        /*GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = env.getDefaultScreenDevice();
        setUndecorated(true);
        defaultScreen.setFullScreenWindow(this);*/
		
		// our main window is 800x600 for now.
		this.setSize(800, 600);
		
		// initialize a board which takes the whole screen
		Board mainBoard = new Board(this.getSize());

        add(mainBoard);

        this.setTitle("Application");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
      
    }    
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
        	// main application
            @Override
            public void run() {
                Application ex = new Application();
                ex.setVisible(true);
            }
        });
    }
}