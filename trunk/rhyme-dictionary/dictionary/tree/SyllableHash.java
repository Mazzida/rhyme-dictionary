package dictionary.tree;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class SyllableHash {

	/**
	 * Map from syllable strings to unique (incremented upon insertion) ids
	 */
	private static final Map<Syllable, SyllableKey> dictionary = new HashMap<Syllable, SyllableKey>();

	private static final Vector<Syllable> indexedVals = new Vector<Syllable>();

	/** Current insertion id value for next new syllable */
	private static SyllableKey curNewIndex = SyllableKey.NULL;

	static {
		dictionary.put(Syllable.NULL, SyllableKey.NULL);
		indexedVals.add(Syllable.NULL);
		curNewIndex = curNewIndex.increment();
	}
	
	public static boolean contains(Syllable aSyllable) {
		return dictionary.containsKey(aSyllable);
	}

	/**
	 * Returns the integer key for the syllable inserted
	 */
	public static SyllableKey insert(Syllable aSyllable) {
		if (!dictionary.containsKey(aSyllable)) {
			SyllableKey output;
			output = curNewIndex;
			dictionary.put(aSyllable, curNewIndex);
			curNewIndex = curNewIndex.increment();
			indexedVals.add(aSyllable);
			return output;
		}
		return dictionary.get(aSyllable);
	}

	/**
	 * Returns the syllable string for the key if exists
	 */
	public static Syllable get(SyllableKey aKey) {
		return indexedVals.get(aKey.getIndex());
	}

}
