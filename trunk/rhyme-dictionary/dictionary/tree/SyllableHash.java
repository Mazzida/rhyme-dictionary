package dictionary.tree;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class SyllableHash {

	/**
	 * Map from syllable strings to unique (incremented upon insertion) ids
	 */
	private static final Map<String, Integer> dictionary = new HashMap<String, Integer>();

	private static final Vector<String> indexedVals = new Vector<String>();

	/** Current insertion id value for next new syllable */
	private static int curNewIndex = 0;

	static {
		dictionary.put("", curNewIndex);
		indexedVals.add("");
		curNewIndex ++;
	}
	
	public static boolean contains(String aSyllable) {
		return dictionary.containsKey(aSyllable);
	}

	/**
	 * Returns the integer key for the syllable inserted
	 */
	public static int insert(String aSyllable) {
		if (!dictionary.containsKey(aSyllable)) {
			dictionary.put(aSyllable, curNewIndex);
			curNewIndex ++;
			indexedVals.add(aSyllable);
			return curNewIndex - 1;
		}
		return dictionary.get(aSyllable);
	}

	/**
	 * Returns the syllable string for the key if exists
	 */
	public static String get(int aKey) {
		if (aKey >= 0 && aKey < curNewIndex) {
			return indexedVals.get(aKey);
		} else {
			throw new IllegalArgumentException("Key value out of range");
		}
	}

}
