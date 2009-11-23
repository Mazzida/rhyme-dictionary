package test.dictionary.crawler;

import static org.junit.Assert.*;

import org.junit.Test;

import dictionary.crawler.HtmlUtil;


public class HtmlUtilTest {

	@Test
	public void testStripTags() {
		String[] testInputs = new String[]{
				"<span class=\"boldface\">dee</span>-<span class=\"boldface\">ey</span>",
				"",
				"alpha<kappa>beta<><rhombus>delta",
				"<hi there>",
				"<>",
		};
		String[] testOutputs = new String[]{
				"dee-ey",
				"",
				"alphabetadelta",
				"",
				""
		};

		String input, expOutput, output;
		for (int i = 0; i < testInputs.length; i ++) {
			input = testInputs[i];
			expOutput = testOutputs[i];
			output = HtmlUtil.stripTags(input);
			assertEquals("strip tags failure for string #" + i + " : " + input, expOutput, output);
		}
	}

	

}
