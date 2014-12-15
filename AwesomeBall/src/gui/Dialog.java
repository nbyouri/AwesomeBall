package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class Dialog extends JDialog {
	private Font font;
	private Color FgColor;
	private Color FbColor;
	private Border border;

	public static final int FONT_SIZE_NORMAL = 12;
	public static final int BORDER_NORMAL = 1;

	public Dialog(String message) {
		font = new Font("Helvetica", Font.BOLD, FONT_SIZE_NORMAL);
		border = new LineBorder(Color.cyan, BORDER_NORMAL);
		FgColor = Color.white;
		FbColor = Color.black;

		JPanel messagePane = new JPanel(new GridBagLayout());
		messagePane.add(createLabel(message, Color.cyan));
		getContentPane().add(messagePane);
		this.setModal(true);
		this.setSize(200, 200);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setUndecorated(true);
		this.setFont(font);
		messagePane.setBorder(this.border);
		this.setForeground(this.FgColor);
		this.setBackground(this.FbColor);
	}

	/**
	 * Cree un label 
	 * @param text String
	 * @param color Color
	 * @return JLabel
	 */
	public static JLabel createLabel(String text, Color color) {

		JLabel label = new JLabel(text);
		label.setForeground(color);

		return label;

	}
}
