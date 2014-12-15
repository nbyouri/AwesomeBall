package test;

import gui.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExitButtonTest {
	public static void main(String[] args) {
		Dialog d = new Dialog("Player 2 Left");
		ActionListener exit = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		};
		Button ex = new Button("exit", exit);
		ex.setPreferredSize(new Dimension(d.getWidth(), 50));
		d.add(ex, BorderLayout.SOUTH);
		d.setVisible(true);
	}
}
