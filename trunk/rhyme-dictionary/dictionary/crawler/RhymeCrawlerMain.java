package dictionary.crawler;

public class RhymeCrawlerMain {

	public static void main(String[] args) {
		RhymeCrawler crawler = new DictionaryDotComCrawler();
//		RhymeCrawler crawler = new MerriamWebsterDotComCrawler();
		crawler.crawl();
	}
}
