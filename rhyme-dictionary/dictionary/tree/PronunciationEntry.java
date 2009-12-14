package dictionary.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PronunciationEntry {

	private static final int EDIT_PENALTY_CHANGE = 2;
	/** penalty for adding/removing characters, should always be greater than the
	 * change penalty */
	private static final int EDIT_PENALTY_ADD = 5;
	private static final int EDIT_PENALTY_REMOVE = 5;
	/** penalty for difference in number of syllables between replacement, replaced 
	 * slot; this should always be greater than add/remove by at least 1 */
	private static final int SYLLABLE_PENALTY = 6;

	private static final String SYLLABLE_SPLIT_REGEX = "['-]+";
	private static final String SYLLABLE_NORMAL_PATTERN = "([^-']+?-).*";
	private static final String SYLLABLE_STRESS_PATTERN_1 = "([^-']+?'-).*";
	private static final String SYLLABLE_STRESS_PATTERN_2 = "([^-']+?').*";


	private String word;
	private List<SyllableKey> syllables;

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
		for (SyllableKey syllable : syllables) {
			syllableVals += SyllableHash.get(syllable) + "-";
		}
		if (syllableVals.endsWith("-")) {
			syllableVals = syllableVals.substring(0, syllableVals.length()-1);
		}
		return String.format("[ %s, %s, %s ]", word, syllables.toString(), syllableVals);
	}

	public List<SyllableKey> getReverseSyllables() {
		LinkedList<SyllableKey> output = new LinkedList<SyllableKey>();
		for (SyllableKey aKey : syllables) {
			output.addFirst(aKey);
		}
		return output;
	}

	public String getWord() {
		return word;
	}
	
	/**
	 * Figure out the correct replacement position and return the
	 * alternate pronunciation syllable list
	 */
	private static List<SyllableKey> determineSyllables(String original, String replace) {
		if (original == null || original.length() == 0) {
			throw new IllegalArgumentException("Null or length zero original pronunciation");
		}

		if (replace == null || replace.length() == 0) {
			return getKeyEncodeSyllables(getParsedSyllables(original));
		}

		int rLen = replace.length();
		boolean notFirst = replace.charAt(0) == '-' || replace.charAt(0) == '\'';
		boolean notLast = replace.charAt(rLen-1) == '-' || (replace.charAt(rLen-1) == '\'' && replace.charAt(rLen-2) == '-');

		if (!notFirst && !notLast) {
			// entire alternative is the new pronunciation
			return getKeyEncodeSyllables(getParsedSyllables(replace));
		}
		
		if (notFirst && rLen >= 1) {
			replace = replace.substring(1);
			rLen --;
		}
		if (notLast && rLen >= 1) {
			replace = replace.substring(0, rLen - 1);
			rLen --;
		}

		ArrayList<Syllable> origSyl = getParsedSyllables(original);
		ArrayList<Syllable> repSyl = getParsedSyllables(replace);
		String strippedReplace = subSyll(repSyl, 0, repSyl.size()-1);
		
		int repLen = repSyl.size();
		int bestCost = Integer.MAX_VALUE;
		int bestPosStart = -1;
		int bestPosEnd = -1;
		int curCost;

		if (!notFirst) {
			// first - startPos is first char of original
			int startPos = 0;
			for (int endPos = 0; endPos < origSyl.size(); endPos ++) {
				String curSubString = subSyll(origSyl, startPos, endPos);
				curCost = editDistance(curSubString, strippedReplace);
				curCost += getSyllablePenalty(repLen, 1 + endPos - startPos);

				bestPosStart = curCost <= bestCost ? startPos : bestPosStart;
				bestPosEnd = curCost <= bestCost ? endPos : bestPosEnd;
				bestCost = curCost <= bestCost ? curCost : bestCost;
			}			
		} else if (!notLast) {
			// last - endPos is last char of original
			int endPos = origSyl.size() - 1;
			for (int startPos = 0; startPos < origSyl.size(); startPos ++ ) {
				String curSubString = subSyll(origSyl, startPos, endPos);
				curCost = editDistance(curSubString, strippedReplace);
				curCost += getSyllablePenalty(repLen, 1 + endPos - startPos);

				bestPosStart = curCost <= bestCost ? startPos : bestPosStart;
				bestPosEnd = curCost <= bestCost ? endPos : bestPosEnd;
				bestCost = curCost <= bestCost ? curCost : bestCost;
			}			
		} else {
			for (int startPos = 1; startPos < origSyl.size(); startPos ++ ) {
				for (int endPos = startPos; endPos < origSyl.size()-1; endPos ++) {
					String curSubString = subSyll(origSyl, startPos, endPos);
					curCost = editDistance(curSubString, strippedReplace);
					curCost += getSyllablePenalty(repLen, 1 + endPos - startPos);

					bestPosStart = curCost <= bestCost ? startPos : bestPosStart;
					bestPosEnd = curCost <= bestCost ? endPos : bestPosEnd;
					bestCost = curCost <= bestCost ? curCost : bestCost;
				}
			}
		}

		LinkedList<SyllableKey> output = new LinkedList<SyllableKey>();

		for (int i = 0; i < bestPosStart; i ++)
			output.addLast(SyllableHash.insert(origSyl.get(i)));
		for (int i = 0; i < repSyl.size(); i ++)
			output.addLast(SyllableHash.insert(repSyl.get(i)));
		for (int i = 0; i < origSyl.size() - bestPosEnd - 1; i ++) 
			output.addLast(SyllableHash.insert(origSyl.get(bestPosEnd+1+i)));
		System.out.println("output: " + output); //TODO remove

		return output;
	}

	private static ArrayList<Syllable> getParsedSyllables(String aSyllables) {
		ArrayList<Syllable> output = new ArrayList<Syllable>();
		Matcher norMat = Pattern.compile(SYLLABLE_NORMAL_PATTERN, Pattern.DOTALL).matcher(aSyllables);
		Matcher st1Mat = Pattern.compile(SYLLABLE_STRESS_PATTERN_1, Pattern.DOTALL).matcher(aSyllables);
		Matcher st2Mat = Pattern.compile(SYLLABLE_STRESS_PATTERN_2, Pattern.DOTALL).matcher(aSyllables);

		while( aSyllables.length() > 0 ) {
			if (norMat.matches()) { //  - stress pattern
				String temp = norMat.group(1);
				output.add(new Syllable(temp.replace("-", ""), false));
				aSyllables = aSyllables.substring(temp.length());
			} else if (st1Mat.matches()) { //  '- stress pattern
				String temp = st1Mat.group(1);
				output.add(new Syllable(temp.replace("'-", ""), true));
				aSyllables = aSyllables.substring(temp.length());
			} else if (st2Mat.matches()) { //  ' stress pattern
				String temp = st2Mat.group(1);
				output.add(new Syllable(temp.replace("'", ""), true));
				aSyllables = aSyllables.substring(temp.length());
			} else {
				output.add(new Syllable(aSyllables, false));
				break;
			}

			norMat = Pattern.compile(SYLLABLE_NORMAL_PATTERN, Pattern.DOTALL).matcher(aSyllables);
			st1Mat = Pattern.compile(SYLLABLE_STRESS_PATTERN_1, Pattern.DOTALL).matcher(aSyllables);
			st2Mat = Pattern.compile(SYLLABLE_STRESS_PATTERN_2, Pattern.DOTALL).matcher(aSyllables);
		}

		return output;
	}
	
	private static List<SyllableKey> getKeyEncodeSyllables(List<Syllable> aSyllableList) {
		LinkedList<SyllableKey> output = new LinkedList<SyllableKey>();
		for (Syllable syllable : aSyllableList) {
			output.add(SyllableHash.insert(syllable));
		}
		return output;
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
			score[bInd] = bInd * EDIT_PENALTY_ADD;
		}
		for (int aInd = 1; aInd < a.length(); aInd ++) {
			int[] nextScore = new int[b.length()];
			nextScore[0] = aInd * EDIT_PENALTY_REMOVE;
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
		curScore = (b.length() > 0) ? score[b.length()-1] : a.length();
		return curScore;
	}

	private static int getSyllablePenalty(int aReplaceLen, int aSlotLen) {
		return Math.abs(aReplaceLen - aSlotLen) *  SYLLABLE_PENALTY;
	}
	
	/**
	 * Returns a concetenation of the aStart, aStart+1, ..., aEnd strings
	 * in the argument String array
	 */
	private static String subSyll(List<Syllable> aSyllables, int aStart, int aEnd) {
		StringBuilder buff = new StringBuilder();
		for (int i = aStart; i <= aEnd; i ++) {
			buff.append(aSyllables.get(i));
		}
		return buff.toString();
	}

}
