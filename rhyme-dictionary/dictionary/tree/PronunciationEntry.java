package dictionary.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PronunciationEntry {

	private String word;
	private ArrayList<Integer> syllables;

	public PronunciationEntry() {
		word = "";
		syllables = new ArrayList<Integer>();
	}
	
	public static ArrayList<PronunciationEntry> makeEntry(String aEntryString) {
		ArrayList<PronunciationEntry> output = new ArrayList<PronunciationEntry>();
		aEntryString = aEntryString.trim();
		PronunciationEntry tEntry;
		String word;
		Pattern prPat = Pattern.compile("(.*?)\t\\((.*)\\)", Pattern.DOTALL);
		Matcher prMat = prPat.matcher(aEntryString);

		if (prMat.matches()) {
			word = prMat.group(1);
			String prString = prMat.group(2);
			String[] list = prString.split(", ");
			String[] primary = list[0].split("['-]");

			tEntry = new PronunciationEntry();
			tEntry.word = word;
			tEntry.syllables = getSyllableKeys(primary);
			output.add(tEntry);

			for (int altInd = 1; altInd < list.length; altInd ++) {
				
				String[] secondary = list[altInd].split("['-]");
			}
		} else {
			throw new IllegalArgumentException("Failed Parse: ");
		}

		return output;
	}

	private static ArrayList<Integer> getSyllableKeys(String[] syllables) {
		ArrayList<Integer> output = new ArrayList<Integer>();
		for (String syllable : syllables) {
			output.add(SyllableHash.insert(syllable));
		}
		return output;
	}
	
	public String toString() {
		String syllableVals = "";
		for (Integer syllable : syllables) {
			syllableVals += SyllableHash.get(syllable) + "-";
		}
		return String.format("[ %s %s %s ]", word, syllables.toString(), syllableVals);
	}
}
