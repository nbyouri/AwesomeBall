package unitTesting;

import static org.junit.Assert.*;
import org.junit.Test;
import geo.*;

public class TextFieldTest {

	TextField test = new TextField();
	TextField test_str = new TextField("text");
	
	@Test
	public void test() {
		assert(test.getStr() == null);
		assertEquals(test_str.getStr(), "text");
	}

}
