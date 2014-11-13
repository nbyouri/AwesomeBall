package unitTesting;

import java.awt.Rectangle;
import java.awt.geom.Line2D;

import org.junit.Test;

import test_jeu.Field;

public class FieldTest {

	Field test_field = new Field();
	@Test
	public void test() {
		// test rectangle
		assert(test_field.getBounds() == new Rectangle(0, 0, 0, 0));
		
		// test côtés
		assert(test_field.getSide(5) == new Line2D.Double(0, 0, 0, 0));
	}

}
