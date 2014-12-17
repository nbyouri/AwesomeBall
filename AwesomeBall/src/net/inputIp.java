package net;

import gui.Dialog;
import gui.Button;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class inputIp {

	private Dialog dialog;
	private JTextField textField;

	private inputIp(String title, JFrame frame) {
		dialog = new Dialog(title);
		dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		dialog.setMinimumSize(new Dimension(200, 200));
		init();
	}

	public void setVisible(Boolean flag) {
		dialog.setVisible(flag);
	}

	public static String getInput(String title, JFrame frame) {
		inputIp input = new inputIp(title, frame);
		input.setVisible(true);
		String text = input.textField.getText();
		return text;
	}
	

	private void init() {
		ActionListener exit = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		};
		ActionListener disp = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}

		};
		textField = new JTextField();
		Button exitButton = new Button("exit", exit);
		Button okButton = new Button("ok", disp);
		
		dialog.setLayout(new GridLayout(2, 2, 5, 5));
		
		dialog.add(textField);
		dialog.add(exitButton);
		dialog.add(okButton);
		dialog.pack();
	}
}