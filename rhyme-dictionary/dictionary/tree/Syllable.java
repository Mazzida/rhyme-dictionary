package dictionary.tree;

public class Syllable {

	private String syllable;

	protected Syllable(String aSyllable) {
		syllable = aSyllable;
	}

	public int hashCode() {
		return syllable.hashCode();
	}

	public boolean equals(Object o) {
		if (o instanceof Syllable) {
			Syllable s = (Syllable) o;
			return s.syllable.equals(syllable);
		}
		return false;
	}

	public static final Syllable NULL = new Syllable("");

}
