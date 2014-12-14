package gui;

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

	public static final int FONT_SIZE_NORMAL = 12;
	public static final int BORDER_NORMAL = 1;

	public Button(String title, ActionListener al) {
		super(title);

		buttonFont = new Font("Helvetica", Font.BOLD, FONT_SIZE_NORMAL);
		buttonBorder = new LineBorder(Color.cyan, BORDER_NORMAL);
		buttonPressedBorder = new LineBorder(Color.yellow, BORDER_NORMAL);
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
		this.setRolloverEnabled(true);

		/**
		 * change color when clicked or hovered.
		 */
		this.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent evt) {
				if (getModel().isPressed()) {
					setForeground(buttonPressedFgColor);
					setBorder(buttonPressedBorder);
				} else if (getModel().isRollover()) {
					setForeground(buttonPressedFgColor);
					setBorder(buttonPressedBorder);
				} else {
					setForeground(buttonFgColor);
					setBorder(buttonBorder);
				}
			}
		});
	}
}
