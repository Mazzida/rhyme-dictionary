package dictionary.tree;

public class SyllableKey {

	private int key;

	protected SyllableKey(int aKey) {
		key = aKey;
	}

	public SyllableKey increment() {
		return new SyllableKey(key + 1);
	}
	
	public int getIndex() {
		return key;
	}

	public int hashCode() {
		return key;
	}
	
	public boolean equals(Object o) {
		if (o instanceof SyllableKey) {
			SyllableKey s = (SyllableKey) o;
			return s.key == key;
		}
		return false;
	}
	
	public static final SyllableKey NULL = new SyllableKey(0);
}
