package dictionary.tree;

public class Syllable {

	private static final String VOWEL_REGEX = "[aäāâeəēěiīĭîoōŏôuûŭ]";
	private static final String CONSONANT_REGEX = "[^aäāâeəēěiīĭîoōŏôuûŭ]";
	
	private String syllable;
	private boolean stressed;

	protected Syllable(String aSyllable, boolean aStressed) {
		syllable = aSyllable;
		stressed = aStressed;
	}

	public int hashCode() {
		return syllable.hashCode();
	}

	public boolean equals(Object o) {
		if (o instanceof Syllable) {
			Syllable s = (Syllable) o;
			return s.syllable.equals(syllable) && s.stressed == stressed;
		}
		return false;
	}

	/**
	 * Print the pronunciation string plus appropriate stress/delimitation
	 * unless this is the null syllable
	 */
	public String toString() {
		return syllable + ((this == NULL) ? "" : (stressed ? "'" : "-"));
	}

	public String toPlainString() {
		return syllable;
	}

	public boolean isStressed() {
		return stressed;
	}

	public static boolean similarVowelSound(Syllable aSyl1, Syllable aSyl2) {
		String syl1 = aSyl1.toPlainString();
		String syl2 = aSyl2.toPlainString();
		syl1 = syl1.replaceAll(CONSONANT_REGEX, "");
		syl2 = syl2.replaceAll(CONSONANT_REGEX, "");
		return syl1.equals(syl2);
	}

	public static boolean similarVowelAndTrailingSounds(Syllable aSyl1, Syllable aSyl2) {
		String syl1 = aSyl1.toPlainString();
		String syl2 = aSyl2.toPlainString();

		if (syl1.matches(CONSONANT_REGEX + "*") || syl2.matches(CONSONANT_REGEX + "*")) {
			return syl1.equals(syl2);
		}
		
		while (String.valueOf(syl1.charAt(0)).matches(CONSONANT_REGEX)) {
			syl1 = syl1.substring(1);
		}
		while (String.valueOf(syl2.charAt(0)).matches(CONSONANT_REGEX)) {
			syl2 = syl2.substring(1);
		}
		
		return syl1.equals(syl2);
	}

	public static final Syllable NULL = new Syllable("", false);

}
