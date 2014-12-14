package test;

import static org.junit.Assert.*;
import org.junit.Test;
import geo.*;

public class TextFieldTest {

	Text test = new Text();
	Text test_str = new Text("text");

	@Test
	public void test() {
		assert (test.getStr() == null);
		assertEquals(test_str.getStr(), "text");
	}

}
