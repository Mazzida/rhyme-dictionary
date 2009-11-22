package dictionary.crawler;

public class HtmlUtil {

	public static String stripTags(String htmlInput) {
		String output = htmlInput;
		output = output.replaceAll("<.*?>", "");
		return output;
	}
	
}
