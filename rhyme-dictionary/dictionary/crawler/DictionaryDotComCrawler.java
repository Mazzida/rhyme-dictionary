package dictionary.crawler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DictionaryDotComCrawler extends RhymeCrawler {

	@Override
	protected String getSearchUrlString(String word) {
		return "http://dictionary.reference.com/browse/" + word;
	}

	@Override
	public String processPageUrl(String word, String contents) {
		String proString;
		Pattern prPat = Pattern.compile(".*Pronunciation: <tt>'(.*?)</tt>.*", Pattern.DOTALL);
		Matcher prMat = prPat.matcher(contents);
		if (prMat.matches()) {
			proString = prMat.group(1);
		} else {
			System.err.println("no html regex match: " + word);
			return null;
		}
		
		return proString;
	}

}
