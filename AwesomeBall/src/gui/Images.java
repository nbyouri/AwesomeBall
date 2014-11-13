package gui;

import java.awt.Image;
import java.io.IOException;
import javax.swing.ImageIcon;

public class Images {
	private Image player;
	private int width;
	private int height;
	
	public Images() throws IOException {
		player = load("images/craft.png");
	}


	// load image
	public Image load(String path) throws IOException {
		ImageIcon ii = new ImageIcon(getClass().getResource(path));
		width = ii.getIconWidth();
		height = ii.getIconHeight();
		
		return ii.getImage();
	}


	public Image getPlayer() {
		return player;
	}


	public void setPlayer(Image player) {
		this.player = player;
	}


	public int getWidth() {
		return width;
	}


	public void setWidth(int width) {
		this.width = width;
	}


	public int getHeight() {
		return height;
	}


	public void setHeight(int height) {
		this.height = height;
	}
	
	

}
