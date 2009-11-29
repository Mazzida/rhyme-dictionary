package dictionary.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PronunciationEntry {

	private String word;
	private ArrayList<Integer[]> syllables;
	private ArrayList<Integer[]> alternates;

	public PronunciationEntry() {
		word = "";
		syllables = new ArrayList<Integer[]>();
		alternates = new ArrayList<Integer[]>();
	}
	
	public static PronunciationEntry makeEntry(String aEntryString) {
		PronunciationEntry output = new PronunciationEntry();
		aEntryString = aEntryString.trim();
		Pattern prPat = Pattern.compile("(.*?)\t\\((.*)\\)", Pattern.DOTALL);
		Matcher prMat = prPat.matcher(aEntryString);

		if (prMat.matches()) {
			output.word = prMat.group(1);
			String prString = prMat.group(2);
			String[] list = prString.split(", ");
			String[] primary = list[0].split("['-]");

			for (int altInd = 1; altInd < list.length; altInd ++) {
				System.out.println("process: " + list[altInd]);
				String[] secondary = list[altInd].split("['-]");
				System.out.println("\t\t\t" + Arrays.toString(secondary));
			}
			System.out.println("\t\t\t" + Arrays.toString(list) + "\t\t" + list.length);
			System.out.println("\t\t\t" + Arrays.toString(primary));
			System.out.println("\n\n");
		} else {
			throw new IllegalArgumentException("Failed Parse: ");
		}

		return output;
	}
}
