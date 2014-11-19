package main;

import gui.Board;

import java.awt.EventQueue;
//import java.awt.GraphicsDevice;
//import java.awt.GraphicsEnvironment;




import javax.swing.JFrame;

/*
 * TODO:
 * more constants
 * javadoc
 * block comments
 * MVC
 * 
 * 
 * FUNCTIONALITY:
 * acceleration
 * sounds
 * shoot bullets
 * threading
 * other jpanel for ball / player bounds
 * 
 * QUICKFIX:
 * update ball location on key release
 * show ball/spacecraft thruster
 * stick ball when in front + space key
 * shoot the ball when space key pressed
 * others can steal the ball if in front + space key
 * settings panel
 * 
 */

@SuppressWarnings("serial")
public class SpaceBalls extends JFrame {

	
	public SpaceBalls() {

        // full screen code
        /*GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = env.getDefaultScreenDevice();
        setUndecorated(true);
        defaultScreen.setFullScreenWindow(this);*/
		
		// our main window is 850x600 for now.
		this.setSize(850, 600);
		
		// initialize a board which takes the whole screen
		Board mainBoard = new Board(this.getSize());

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
                SpaceBalls ex = new SpaceBalls();
                ex.setVisible(true);
            }
        });
    }
}