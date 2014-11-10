package test_jeu;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


@SuppressWarnings("serial")
public class Button extends JButton {
	private Font buttonFont;
	private Color buttonFgColor;
	private Color buttonFbColor;
	private Color buttonPressedFgColor;
	private Border buttonBorder;
	private Border buttonPressedBorder;
	public Button(String title, ActionListener al) {
		super(title);
		
		buttonFont = new Font("Helvetica", Font.BOLD, 12);
		buttonBorder = new LineBorder(Color.cyan, 1);
		buttonPressedBorder = new LineBorder(Color.yellow, 1);
		buttonFgColor = Color.cyan;
		buttonPressedFgColor = Color.yellow;
		buttonFbColor = Color.black;
		
		this.setFont(this.buttonFont);
		this.setBorder(this.buttonBorder);
		this.setForeground(this.buttonFgColor);
		this.setBackground(this.buttonFbColor);
		this.setFocusPainted(false);
		this.setContentAreaFilled(false);
		this.addActionListener(al);
		this.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent evt) {
				if (getModel().isPressed()) {
					// don't use this here
					setForeground(buttonPressedFgColor);
					setBorder(buttonPressedBorder);
				}
			}
		});
	}
}
