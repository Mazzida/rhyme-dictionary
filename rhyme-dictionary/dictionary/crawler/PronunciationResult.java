package dictionary.crawler;

public class PronunciationResult {
	private String word;
	private String pronunciation;

	public PronunciationResult(String aWord, String aPronunciation) {
		word = aWord;
		pronunciation = aPronunciation;
	}

	public String getWord() {
		return word;
	}

	public String getPronunciation() {
		return pronunciation;
	}
	
	public String toString() {
		return String.format("%s\t%s", word, pronunciation);
	}
}
