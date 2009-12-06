package dictionary.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PronunciationEntry {

	private static final int EDIT_PENALTY_CHANGE = 2;
	private static final int EDIT_PENALTY_ADD = 5;
	private static final int EDIT_PENALTY_REMOVE = 5;
	
	private static final String SYLLABLE_SPLIT_REGEX = "['-]+";
	
	private String word;
	private ArrayList<Integer> syllables;

	public PronunciationEntry(String aWord, String aPrimary) {
		word = aWord;
		syllables = determineSyllables(aPrimary, null);
	}

	public PronunciationEntry(String aWord, String aPrimary, String aSecondary) {
		word = aWord;
		syllables = determineSyllables(aPrimary, aSecondary);
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
			String primary = list[0];

			tEntry = new PronunciationEntry(word, primary);
			output.add(tEntry);

			for (int altInd = 1; altInd < list.length; altInd ++) {
				String alternate = list[altInd];
				tEntry = new PronunciationEntry(word, primary, alternate);
				output.add(tEntry);
			}
		} else {
			throw new IllegalArgumentException("Failed Parse: ");
		}

		return output;
	}

	public String toString() {
		String syllableVals = "";
		for (Integer syllable : syllables) {
			syllableVals += SyllableHash.get(syllable) + "-";
		}
		if (syllableVals.endsWith("-")) {
			syllableVals = syllableVals.substring(0, syllableVals.length()-1);
		}
		return String.format("[ %s, %s, %s ]", word, syllables.toString(), syllableVals);
	}

	public List<Integer> getReverseSyllables() {
		List<Integer> output;
		output = (ArrayList<Integer>)syllables.clone();
		Collections.reverse(output);
		return output;
	}

	public String getWord() {
		return word;
	}

	private static ArrayList<Integer> getSyllableKeys(String[] syllables) {
		ArrayList<Integer> output = new ArrayList<Integer>();
		for (String syllable : syllables) {
			output.add(SyllableHash.insert(syllable));
		}
		return output;
	}
	
	/**
	 * Figure out the correct replacement position and return the
	 * alternate pronunciation syllable list
	 */
	private static ArrayList<Integer> determineSyllables(String original, String replace) {
		if (original == null || original.length() == 0) {
			throw new IllegalArgumentException("Null or length zero original pronunciation");
		}

		String[] origSyl = original.split(SYLLABLE_SPLIT_REGEX);
		if (replace == null || replace.length() == 0) {
			return getSyllableKeys(origSyl);
		}

		int rLen = replace.length();
		boolean notFirst = replace.charAt(0) == '-' || replace.charAt(0) == '\'';
		boolean notLast = replace.charAt(rLen-1) == '-' || (replace.charAt(rLen-1) == '\'' && replace.charAt(rLen-2) == '-');

		if (!notFirst && !notLast) {
			// entire alternative is the new pronunciation
			String[] repSyll = replace.split(SYLLABLE_SPLIT_REGEX);
			ArrayList<Integer> syllableKeys = getSyllableKeys(repSyll);
			return syllableKeys;
		}
		
		if (notFirst && rLen >= 1) {
			replace = replace.substring(1);
			rLen --;
		}
		if (notLast && rLen >= 1) {
			replace = replace.substring(0, rLen - 1);
			rLen --;
		}
		
		String[] repSyll = replace.split(SYLLABLE_SPLIT_REGEX);
		System.out.println(replace);
		int bestCost = Integer.MAX_VALUE;
		int bestPosStart = -1;
		int bestPosEnd = -1;
		int curCost;

		for (int startPos = notFirst ? 1 : 0; startPos < origSyl.length - (notLast ? 1 : 0); startPos ++ ) { //TODO check bounds
			for (int endPos = startPos+1; endPos < origSyl.length; endPos ++) {
				curCost = editDistance(subSyll(origSyl, startPos, endPos), replace);

				bestPosStart = curCost <= bestCost ? startPos : bestPosStart;
				bestPosEnd = curCost <= bestCost ? endPos : bestPosEnd;
				bestCost = curCost <= bestCost ? curCost : bestCost;
			}
		}
System.out.println("" + bestPosStart + " " + bestPosEnd);
		String[] result = new String[origSyl.length + repSyll.length - (bestPosEnd - bestPosStart)];

		System.arraycopy(origSyl, 0, result, 0, bestPosStart);
		System.out.println(	Arrays.toString(result));
		System.arraycopy(repSyll, 0, result, bestPosStart, repSyll.length);
		System.out.println(	Arrays.toString(result));
		System.arraycopy(origSyl, bestPosEnd, result, bestPosStart + repSyll.length, result.length - bestPosEnd);
		System.out.println(	Arrays.toString(result));

		ArrayList<Integer> syllableKeys = getSyllableKeys(result);
		return syllableKeys;
	}

	/**
	 * Returns weighted edit distance heuristic between two strings
	 * to assist in determining correct syllable insertion for
	 * secondary pronunciations 
	 */
	private static int editDistance(String a, String b) {
		int[] score = new int[b.length()];
		int curScore;
		for (int bInd = 0; bInd < b.length(); bInd ++) {
			score[bInd] = bInd;
		}
		for (int aInd = 1; aInd < a.length(); aInd ++) {
			int[] nextScore = new int[b.length()];
			for (int bInd = 1; bInd < b.length(); bInd ++) {
				if (a.charAt(aInd) == b.charAt(bInd)) {
					curScore = score[bInd - 1];
				} else {
					curScore = score[bInd - 1] + EDIT_PENALTY_CHANGE;
				}
				curScore = Math.min(curScore, score[bInd] + EDIT_PENALTY_ADD);
				curScore = Math.min(curScore, nextScore[bInd-1] + EDIT_PENALTY_REMOVE);
				nextScore[bInd] = curScore;
			}
			score = nextScore;
		}
		curScore = b.length() > 0 ? score[b.length()-1] : a.length();
		return curScore;
	}

	/**
	 * Returns a concetenation of the aStart, aStart+1, ..., aEnd strings
	 * in the argument String array
	 */
	private static String subSyll(String[] aSyllables, int aStart, int aEnd) {
		StringBuilder buff = new StringBuilder();
		for (int i = aStart; i <= aEnd; i ++) {
			buff.append(aSyllables[i]);
		}
		return buff.toString();
	}

	public static void main(String[] args) {
		// (nōō'fən-lənd, -lānd', -fənd-, nyōō'-) 
		System.out.println(		determineSyllables("nōō'fən-lənd", "-fənd-"));
		System.out.println(		determineSyllables("nōō'fən-lənd", "-lind"));
	}
	
}
