package test_jeu;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class Button extends JButton {
	private Font buttonFont;
	private Color buttonFgColor;
	private Color buttonFbColor;
	private Border buttonBorder;
	public Button(String title, ActionListener al) {
		super(title);
		
		buttonFont = new Font("Helvetica", Font.BOLD, 12);
		buttonBorder = new LineBorder(Color.cyan, 1);
		buttonFgColor = Color.cyan;
		buttonFbColor = Color.black;
		
		this.setFont(this.buttonFont);
		this.setBorder(this.buttonBorder);
		this.setForeground(this.buttonFgColor);
		this.setBackground(this.buttonFbColor);
		this.setFocusPainted(false);
		this.setContentAreaFilled(false);
		this.addActionListener(al);
	}
}
