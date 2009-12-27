package dictionary.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DictionaryDotComCrawler extends RhymeCrawler {

	private static String NAME_AND_PRONUNCIATION_PATTERN = ".*<b>((.*?)(?:<sup>.*?</sup>.*)?</b>.*?&nbsp;&nbsp;(.*?)&nbsp;<a title=\"Click for guide to symbols.\").*";
//	Pattern prPat = Pattern.compile(".*Pronunciation: <tt>'(.*?)</tt>.*", Pattern.DOTALL);
//	Pattern prPat = Pattern.compile(".*<span class=\"pron\">(.*?)</span><span class=\"prondelim\">].*", Pattern.DOTALL);

	
	@Override
	protected String getSearchUrlString(String word) {
		return "http://dictionary.reference.com/browse/" + word;
	}

	@Override
	public List<PronunciationResult> processPageUrl(String contents) {
		List<PronunciationResult> resultList = new ArrayList<PronunciationResult>();
		Pattern prPat = Pattern.compile(NAME_AND_PRONUNCIATION_PATTERN, Pattern.DOTALL);
		Matcher prMat = prPat.matcher(contents);

		while (prMat.matches()) {
			String word = cleanupWord(prMat.group(2));
			String pronunciation = HtmlUtil.stripTags(prMat.group(3));
			String toStrip = prMat.group(1);
			resultList.add(new PronunciationResult(word, pronunciation));
			contents = contents.replace(toStrip, "");
			prMat = prPat.matcher(contents);
		}
		return resultList;
	}

	public String cleanupWord(String aWordString) {
		return aWordString.replaceAll("·","");
	}

}
