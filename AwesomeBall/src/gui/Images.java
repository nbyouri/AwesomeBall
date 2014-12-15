package gui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

import javax.swing.ImageIcon;

public class Images {
	private Image player;
	private int width;
	private int height;
	private int rotation;

	public Images() {
		player = load("images/craft.png");
	}

	/**
	 * load image
	 * 
	 * @param path
	 *            String
	 * @return Image
	 */
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

	/**
	 * return normalised angle
	 * 
	 * @return int : rotation
	 */
	public int getRotation() {
		while (rotation <= -180)
			rotation += 360;
		while (rotation > 180)
			rotation -= 360;
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	/**
	 * rotate the image.
	 * 
	 * @param img
	 *            Image
	 * @param rotation
	 *            int
	 */
	public void rotate(Image img, int rotation) {
		// only rotate if change of key
		if (this.getRotation() != rotation) {
			ImageIcon ii = new ImageIcon(img);
			BufferedImage blankCanvas = new BufferedImage(ii.getIconWidth(),
					ii.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = (Graphics2D) blankCanvas.getGraphics();

			// rotate around the center
			g2.rotate(Math.toRadians(rotation), ii.getIconWidth() / 2,
					ii.getIconHeight() / 2);

			g2.drawImage(img, 0, 0, null);
			this.setPlayer(blankCanvas);
		}
	}

	/**
	 * flip image around vertical axis
	 * 
	 * @param img
	 *            Image
	 * @param rotation
	 *            int
	 */
	public void flip(Image img, int rotation) {
		if (this.getRotation() != rotation) {
			ImageIcon ii = new ImageIcon(img);
			BufferedImage blankCanvas = new BufferedImage(ii.getIconWidth(),
					ii.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = (Graphics2D) blankCanvas.getGraphics();
			// flip vertically
			g2.drawImage(img, (int) this.getWidth(), 0, 0,
					(int) this.getHeight(), 0, 0, (int) this.getWidth(),
					(int) this.getHeight(), null);
			this.setPlayer(blankCanvas);
		}
	}

	/**
	 * Invert Red and Blue colors
	 */
	class RedBlueSwapFilter extends RGBImageFilter {
		public RedBlueSwapFilter() {
			// The filter's operation does not depend on the
			// pixel's location, so IndexColorModels can be
			// filtered directly.
			canFilterIndexColorModel = true;
		}

		public int filterRGB(int x, int y, int rgb) {
			return ((rgb & 0xff00ff00) | ((rgb & 0xff0000) >> 16) | ((rgb & 0xff) << 16));
		}
	}

	/**
	 * Apply the colors
	 */
	public void applyFilter() {
		ImageFilter colorfilter = new RedBlueSwapFilter();
		ImageProducer producer = player.getSource();
		FilteredImageSource filteredImageSource = new FilteredImageSource(
				producer, colorfilter);
		player = Toolkit.getDefaultToolkit().createImage(filteredImageSource);
	}
}
