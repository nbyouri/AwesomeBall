package gui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class Images {
	private Image player;
	private int width;
	private int height;
	private int rotation;

	public Images() {
		player = load("images/craft.png");
	}


	// load image
	public Image load(String path) {
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

	// return normalised angle
	public int getRotation() {
		while (rotation <= -180) rotation += 360;
		while (rotation > 180) rotation -= 360;
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	// rotate the image
	public void rotate(Image img, int rotation) {
		// only rotate if change of key
		if (this.getRotation() != rotation) {
			ImageIcon ii = new ImageIcon(img);
			BufferedImage blankCanvas = new BufferedImage(ii.getIconWidth(),
					ii.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = (Graphics2D)blankCanvas.getGraphics();

			// rotate around the center
			g2.rotate(Math.toRadians(rotation), ii.getIconWidth() / 2, ii.getIconHeight() / 2);
			g2.drawImage(img, 0, 0, null);
			this.setPlayer(blankCanvas);
		}
	}

	// flip image around vertical axis
	public void flip(Image img, int rotation) {
		if (this.getRotation() != rotation) {
			ImageIcon ii = new ImageIcon(img);
			BufferedImage blankCanvas = new BufferedImage(ii.getIconWidth(),
					ii.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = (Graphics2D)blankCanvas.getGraphics();
			// flip vertically
			g2.drawImage(img, (int)this.getWidth(), 0, 0, (int)this.getHeight(),
					0, 0, (int)this.getWidth(), (int)this.getHeight(), null);
			this.setPlayer(blankCanvas);
		}
	}

}
