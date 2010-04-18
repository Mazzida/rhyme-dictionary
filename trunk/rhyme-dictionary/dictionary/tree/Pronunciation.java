package dictionary.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Pronunciation implements Comparable<Pronunciation> {

	private List<SyllableKey> pronunciation;

	public Pronunciation() {
		pronunciation = new ArrayList<SyllableKey>();
	}

	public Pronunciation(List<SyllableKey> keys) {
		pronunciation = keys;
	}
	
	public List<SyllableKey> getReverseSyllables() {
		LinkedList<SyllableKey> output = new LinkedList<SyllableKey>();
		for (SyllableKey aKey : pronunciation) {
			output.addFirst(aKey);
		}
		return output;
	}

	public String toString() {
		StringBuilder output = new StringBuilder();
		for (SyllableKey key : pronunciation) {
			output.append(SyllableHash.get(key));
		}
		return output.toString();
	}

	public int compareTo(Pronunciation o) {
		//TODO
		return 0;
	}

}
