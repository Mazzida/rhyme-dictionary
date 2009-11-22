package dictionary.crawler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DictionaryDotComCrawler extends RhymeCrawler {

	@Override
	protected String getSearchUrlString(String word) {
		return "http://dictionary.reference.com/browse/" + word;
	}

	@Override
	protected String processPageUrl(String word, String contents) {
		Pattern prPat = Pattern.compile(".*Pronunciation: <tt>(.*?)</tt>.*", Pattern.DOTALL);
		Matcher prMat = prPat.matcher(contents);
		if (prMat.matches()) {
			System.out.println(word + "		" + prMat.group(1));
		} else {
//			System.out.println("faile");
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		String test = "<br />Main Entry: <b>syph·i·lis</b><br /> Pronunciation: <tt>'sif-(&-)l&s</tt><br /> Function: <i>noun</i><br /> <b>:</b> a chronic contagious usually venereal and often congenital diseasethat is caused by a spirochete of the genus <i>Treponema</i> (<i>T. pallidum</i>) and if left untreated produces chancres, rashes, and systemic lesions in a clinical course with three stages continuedover many years called also <i>lues</i>; &#8212;see <a href=\"/medical/search?db=mwmed&nq=primarysyphilis\"><font size=\"-1\">PRIMARY SYPHILIS</font></a>, <ahref=\"/medical/search?db=mwmed&nq=secondarysyphilis\"><font size=\"-1\">SECONDARY SYPHILIS</font></a>, <a href=\"/medical/search?db=mwmed&nq=tertiarysyphilis\"><font size=\"-1\">TERTIARY SYPHILIS</font></a><br /><b>Sypháiálus</b> <tt>/'sif-&-l&s,/</tt> literary character. Syphilus is the hero of the 1530 Latin poem “Syphilis or the French Disease,” which was written by the Italianphysician and poet Girolamo Fracastoro. Syphilus is a swineherd who blasphemes a sun god and as a punishment is afflicted with a disease.\nhiethere";
		DictionaryDotComCrawler t = new DictionaryDotComCrawler();
		t.processPageUrl("fea", test);
	}

}
