package test.dictionary.main;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

import dictionary.main.RhymeQueryHandler;


public class RhymeQueryTest {

	@Test
	public void testLoader() {
		Collection<String> curResult;
		String[] testWords = {"pancake","teapot", "almond", "rosebud", "lipstick", "charter"};
		
		
		for (String testWord : testWords) {
			curResult = RhymeQueryHandler.getStrictRhymes(testWord);
			System.out.println("testing: " + testWord);
			assertTrue("word not found: " + testWord, curResult != null);
			for (String result : curResult) {
				System.out.println("	" + result);
			}	
		}
	}

}
