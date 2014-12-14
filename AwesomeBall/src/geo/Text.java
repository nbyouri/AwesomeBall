package geo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

@SuppressWarnings("serial")
public class Text extends Rectangle2D.Double {
	private Font textFont;
	private String str;
	
	public Text() {
		super(0, 0, 0, 0);
		this.str = null;
		textFont = null;
	}
	
	/**
         * Modifie la police du texte en "Helvetica"
	 * @param str le texte en question
	 */
	public Text(String str) {
		super(0, 0, 0, 0);
		this.str = str;

		textFont = new Font("Helvetica", Font.BOLD, 12);
	}
        /**
         * Modifie la phrase du texte
         * @param str la phrase modifiée
         */
	public void setStr(String str) {
		this.str = str;
	}
        /**
         * Récupère la phrase du texte
         * @return la phrase en question
         */
	public String getStr() {
		return this.str;
	}
        /**
         * Ecrit le texte sur l'écran
         * @param g2 Graphics2D
         */
	public void draw(Graphics2D g2) {
		g2.setColor(Color.cyan);
		g2.setFont(this.textFont);
		g2.drawString(this.str, (int)this.getX() + 10, (int)this.getY() + 15);
		g2.draw(this);
	}

}
