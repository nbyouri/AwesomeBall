package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import geo.FieldController;
import geo.FieldModel;
import geo.PlayerModel;
import geo.Shape.Side;

public class FieldView {
	public FieldController field;

	public FieldView(double x, double y, double width, double height) {
		field = new FieldController(x, y, width, height);
	}

	/**
	 * Draw the line vertically centered of the field.
	 * 
	 * @param g2
	 *            Graphics2D
	 */
	public void drawCenterLines(Graphics2D g2) {
		g2.setColor(Color.magenta);
		g2.draw(field.getSide(Side.CENTER_V.getId()));
		// g2.draw(this.getSide(CENTER_H));
		g2.draw(field.center);
	}

	/**
	 * Draw side(s) of Field.
	 * 
	 * @param g2
	 *            Graphics2D
	 * @param ar
	 *            ArrayList
	 */
	public void drawSides(Graphics2D g2, ArrayList<Integer> ar) {
		g2.setColor(Color.magenta);
		for (int i = 0; i < ar.size(); i++)
			g2.draw(field.getSide(ar.get(i)));
	}

	/**
	 * Draw field and goals.
	 * 
	 * @param g2
	 *            Graphics2D
	 */
	public void draw(Graphics2D g2, PlayerModel p1, PlayerModel p2) {
		g2.setColor(Color.cyan);
		g2.draw(field);
		
		
		g2.setColor(p1.host ? Color.blue : Color.red);
		g2.draw(field.goalleft);
		g2.setColor(p1.host ? Color.red : Color.blue);
		g2.draw(field.goalright);

		g2.setColor(Color.black);
		g2.draw(field.getSide(FieldModel.GOAL_LEFT));
		g2.draw(field.getSide(FieldModel.GOAL_RIGHT));

		this.drawCenterLines(g2);
	}
}
