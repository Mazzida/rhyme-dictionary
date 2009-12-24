package dictionary.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DictionaryDotComCrawler extends RhymeCrawler {

	@Override
	protected String getSearchUrlString(String word) {
		return "http://dictionary.reference.com/browse/" + word;
	}

	@Override
	public List<PronunciationResult> processPageUrl(String contents) {
		List<PronunciationResult> resultList = new ArrayList<PronunciationResult>();
//		Pattern prPat = Pattern.compile(".*Pronunciation: <tt>'(.*?)</tt>.*", Pattern.DOTALL);
//		Pattern prPat = Pattern.compile(".*<span class=\"pron\">(.*?)</span><span class=\"prondelim\">].*", Pattern.DOTALL);
		Pattern prPat = Pattern.compile(".*&nbsp;&nbsp;(.*?)&nbsp;<a title=\"Click for guide to symbols.\".*", Pattern.DOTALL);
		Matcher prMat = prPat.matcher(contents);
		//works for scientific dictionary case
Pattern testPat = Pattern.compile(".*<b>(.*?)(?:<sup>.*?</sup>.*)?</b>.*?&nbsp;&nbsp;(.*?)&nbsp;<a title=\"Click for guide to symbols.\".*", Pattern.DOTALL);
Matcher testMat = testPat.matcher(contents);

		if (prMat.matches() && testMat.matches()) {
			PronunciationResult result;
			String word = "";
			String pronunciation = HtmlUtil.stripTags(prMat.group(1));
			result = new PronunciationResult(word, pronunciation);
			System.out.println(result);
			System.out.println(testMat.group(1));
		} else {
			return null;
		}
		return resultList;
	}

}
