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
 * rotate ship when moving
 * acceleration
 * sounds
 * shoot bullets
 */

@SuppressWarnings("serial")
public class Application extends JFrame {

	public Application() {

        add(new Board());


        // full screen code
        /*GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = env.getDefaultScreenDevice();
        setUndecorated(true);
        defaultScreen.setFullScreenWindow(this);*/


        setSize(800, 600);
        setTitle("Application");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }    
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Application ex = new Application();
                ex.setVisible(true);
            }
        });
    }
}