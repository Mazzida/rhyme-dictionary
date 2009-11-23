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
//		Pattern prPat = Pattern.compile(".*Pronunciation: <tt>'(.*?)</tt>.*", Pattern.DOTALL);
//		Pattern prPat = Pattern.compile(".*<span class=\"pron\">(.*?)</span><span class=\"prondelim\">].*", Pattern.DOTALL);
		Pattern prPat = Pattern.compile(".*&nbsp;&nbsp;(.*?)&nbsp;<a title=\"Click for guide to symbols.\".*", Pattern.DOTALL);
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
