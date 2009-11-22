package dictionary.crawler;

public class DictionaryDotComCrawler extends RhymeCrawler {

	@Override
	protected String getSearchUrlString(String word) {
		return "http://dictionary.reference.com/browse/" + word;
	}

	@Override
	protected String processPageUrl(String word, String contents) {
		// TODO Auto-generated method stub
		return null;
	}

}
