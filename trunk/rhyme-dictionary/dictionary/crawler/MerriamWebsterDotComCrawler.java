package dictionary.crawler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MerriamWebsterDotComCrawler extends RhymeCrawler {

	@Override
	protected String getSearchUrlString(String word) {
		return "http://www.merriam-webster.com/dictionary/" + word;
	}

	@Override
	public String processPageUrl(String word, String contents) {
		String proString;
		Pattern prPat = Pattern.compile(".*Pronunciation:(.*?)Function:.*", Pattern.DOTALL);
		Matcher prMat = prPat.matcher(contents);
		if (prMat.matches()) {
			proString = prMat.group(1);
			proString = HtmlUtil.stripTags(proString);
		} else {
			return null;
		}
		System.out.println(word + " : " + proString);
		return proString;
	}

}
