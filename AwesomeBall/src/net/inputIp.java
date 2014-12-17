package net;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class inputIp {

	private JDialog dialog;
	private JTextField textField;

	private inputIp (String title, JFrame frame){
		dialog = new JDialog(frame, title, true);
		dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		dialog.setMinimumSize(new Dimension(200, 200));
		init();
	}

	public void setVisible(Boolean flag){
		dialog.setVisible(flag);
	}

	public static String getInput(String title, JFrame frame){
		inputIp input = new inputIp(title, frame);
		input.setVisible(true);
		String text = input.textField.getText();
		return text;
	}

	private void init(){

		textField = new JTextField();
		JButton okButton = new JButton("ok");
		okButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}

		});

		dialog.setLayout(new GridLayout(2, 1, 5, 5));

		dialog.add(textField);
		dialog.add(okButton);
		dialog.pack();     
	}
}