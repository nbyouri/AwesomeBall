package geo;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Text extends Field {
	private Font textFont;
	private String str;
	public Text() {
		super();
		this.str = null;
		textFont = null;
	}
	public Text(String str) {
		super();
		this.str = str;
		
		textFont = new Font("Helvetica", Font.BOLD, 12);
	}
	
	public void setStr(String str) {
		this.str = str;
	}
	
	public String getStr() {
		return this.str;
	}
	
	@Override 
	public void drawSides(Graphics2D g2, ArrayList<Integer> ar) {
		// empty method, do nothing yet
	}
	
	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2);
		g2.setFont(this.textFont);
		g2.drawString(this.str, (int)this.getX() + 10, (int)this.getY() + 15);
		g2.draw(this);
	}

}
