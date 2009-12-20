package dictionary.tree;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;


public class RhymeTree {

	private RhymeTreeNode root;
	
	/**
	 * Map from word strings to syllable pronunciation node from which one can traverse
	 * back-pointers to collect the entire word's pronunciation
	 */
	private Map<String, RhymeTreeNode> wordPronunciation = new HashMap<String, RhymeTreeNode>();

	public RhymeTree() {
		root = new RhymeTreeNode(null, SyllableKey.NULL);
	}
	
	public boolean insert(PronunciationEntry aPronunciation) {
		List<SyllableKey> rSyllables = aPronunciation.getReverseSyllables();
		RhymeTreeNode curNode = root;

		for (SyllableKey syllableKey : rSyllables) {
			curNode = curNode.getChild(syllableKey);
		}
		curNode.setEndNode(aPronunciation.getWord());
		wordPronunciation.put(aPronunciation.getWord(), curNode);

		return true;
	}

	public Set<String> getRhymes(String aWord) {
		TreeSet<String> output = new TreeSet<String>();
		RhymeTreeNode curNode = wordPronunciation.get(aWord);

		// that word is not in this list
		if (curNode == null)
			return null;
		
		while (curNode != root && curNode.parent != root) {
			curNode = curNode.parent;
		}
		curNode.getAllWords(output);

		return output;
	}

	public Set<String> getStrictRhymes(String aWord) {
		TreeSet<String> output = new TreeSet<String>();
		RhymeTreeNode curNode = wordPronunciation.get(aWord);

		// that word is not in this list
		if (curNode == null)
			return null;

		RhymeTreeNode lastStress = curNode;
		while (curNode != root) {
			if (curNode.isStressed()) {
				lastStress = curNode;
			}
			curNode = curNode.parent;
		}

		for (RhymeTreeNode eligibleSibling : lastStress.getSimilarVowelAndTrailingSoundSiblings()) {
			if (SyllableHash.get(eligibleSibling.syllableValue).isStressed()) {
				eligibleSibling.getAllWords(output);
			}
		}

		return output;		
	}

	public String toString() {
		return root.toRecursiveString("");
	}

	private class RhymeTreeNode {
		private RhymeTreeNode parent;
		private SyllableKey syllableValue;
		private Vector<RhymeTreeNode> childList;
		private Vector<String> isTerminating;

		public RhymeTreeNode(RhymeTreeNode aParent, SyllableKey aKey) {
			parent = aParent;
			syllableValue = aKey;
		}
		
		private void setEndNode(String setTermination) {
			if (isTerminating == null) {
				isTerminating = new Vector<String>();
			}
			isTerminating.add(setTermination);
		}

		private List<String> getWords() {
			return isTerminating;
		}

		private void getAllWords(Collection<String> aBucket) {
			if (isTerminating != null)
				aBucket.addAll(isTerminating);

			if (childList != null) {
				for (RhymeTreeNode node : childList) {
					node.getAllWords(aBucket);
				}
			}
		}

		private RhymeTreeNode getChild(SyllableKey aKey) {
			if (childList == null) {
				RhymeTreeNode output = new RhymeTreeNode(this, aKey);
				childList = new Vector<RhymeTreeNode>();
				childList.add(output);
				return output;
			} else {
				for (RhymeTreeNode child : childList) {
					if (child.syllableValue.equals(aKey)) {
						return child;
					}
				}
				RhymeTreeNode output = new RhymeTreeNode(this, aKey);
				childList.add(output);
				return output;
			}
		}

		private String toRecursiveString(String accumulatedString) {
			String output = "";
			if (isTerminating != null) {
				String lastSyl = SyllableHash.get(syllableValue).toString(); 
				return lastSyl + accumulatedString + "\t" + isTerminating + "\n";
			}
			if (childList != null) {
				String syllable = SyllableHash.get(syllableValue).toString();
				if (parent == root && syllable.endsWith("-")) {
					syllable = syllable.substring(0, syllable.length() - 1);
				}
				for (RhymeTreeNode child : childList) {
					output += child.toRecursiveString(syllable + accumulatedString);
				}
			}
			return output;
		}

//		private Set<RhymeTreeNode> getSimilarConsonantSiblings() {
//			//TODO
//		}

		private Set<RhymeTreeNode> getSimilarVowelAndTrailingSoundSiblings() {
			Set<RhymeTreeNode> output = new HashSet<RhymeTreeNode>();
			if (parent == null || parent.childList == null) {
				return output;
			}
			Syllable syllable = SyllableHash.get(syllableValue);
			for (RhymeTreeNode sibling : parent.childList) {
				if (Syllable.similarVowelAndTrailingSounds(SyllableHash.get(sibling.syllableValue), syllable)) {
					output.add(sibling);
				}
			}
			return output;
		}

		private boolean isStressed() {
			return SyllableHash.get(syllableValue).isStressed();
		}
	}

}
