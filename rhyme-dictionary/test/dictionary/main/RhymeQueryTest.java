package test.dictionary.main;

import java.util.Collection;

import org.junit.Test;

import dictionary.main.RhymeQueryHandler;


public class RhymeQueryTest {

	@Test
	public void testLoader() {
		Collection<String> curResult;
		curResult = RhymeQueryHandler.getStrictRhymes("macaroni");
		for (String result : curResult) {
			System.out.println(result);
		}
		
//		curResult = RhymeQueryHandler.getRhymes("video");
		for (String result : curResult) {
//			System.out.println(result);
		}

	}

}
