package dictionary.tree;

public class Syllable {

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

	public static final Syllable NULL = new Syllable("", false);

}
