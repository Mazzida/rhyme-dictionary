package test.dictionary.main;

import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Set;

import org.junit.Test;

import dictionary.main.RhymeQueryHandler;


public class RhymeQueryTest {

	@Test
	public void testLoader() {
		Map<String, Set<String>> curResult;
		String[] testWords = {"pancake","teapot", "almond", "rosebud", "lipstick", "charter"};
		
		
		for (String testWord : testWords) {
			curResult = RhymeQueryHandler.getStrictRhymes(testWord);
			System.out.println("TESTING WORD : " + testWord);
			assertTrue("word not found: " + testWord, curResult != null);
			for (String resultPronunciation : curResult.keySet()) {				
				System.out.println("\t" + resultPronunciation);
				for (String rhyme : curResult.get(resultPronunciation)) {
					System.out.println("\t\t" + rhyme);
				}
			}
		}
	}

}
