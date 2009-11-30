package dictionary.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PronunciationEntry {

	private static final int EDIT_PENALTY_CHANGE = 1;
	private static final int EDIT_PENALTY_ADD = 2;
	private static final int EDIT_PENALTY_REMOVE = 2;
	
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
				String[] secondaryPron = getAlternate(list[0].split("['-]"), secondary);
				tEntry = new PronunciationEntry();
				tEntry.word = word;
				tEntry.syllables = getSyllableKeys(secondaryPron);
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
	private static String[] getAlternate(String[] original, String[] replace) {
		if (replace.length == 0 || original.length == 0) {
			throw new IllegalArgumentException("Length zero pronunciation");
		}
		String[] alternate = new String[original.length];
		boolean notFirst = replace[0].length() == 0;
		if (notFirst) {
			replace = Arrays.copyOfRange(replace, 1, replace.length);
		}
		
		int repLen = replace.length;
		int bestScore = Integer.MAX_VALUE;
		int bestPos = Integer.MAX_VALUE;
		int curScore;
		int curPos = notFirst ? 1 : 0;

		while (curPos < original.length - (repLen - 1)) {
			curScore = 0;
			for (int cmpVal = 0; cmpVal < repLen; cmpVal ++) {
				curScore += editDistance(original[curPos + cmpVal], replace[cmpVal]);
			}
			bestScore = curScore < bestScore ? curScore : bestScore;
			bestPos = curScore == bestScore ? curPos : bestPos;
			curPos ++;
		}

		System.arraycopy(original, 0, alternate, 0, original.length);
		System.arraycopy(replace, 0, alternate, bestPos, repLen);
//		System.out.println("original: " + Arrays.toString(original));
//		System.out.println("replace: " + Arrays.toString(replace));
//		System.out.println("alternate: " + Arrays.toString(alternate));
		return alternate;
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

}
