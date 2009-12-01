package dictionary.tree;

import java.util.Collection;
import java.util.List;
import java.util.Vector;


public class RhymeTree {

	private RhymeTreeNode root;
	
	public RhymeTree() {
		root = new RhymeTreeNode(0);
	}
	
	public boolean insert(PronunciationEntry aPronunciation) {
		List<Integer> rSyllables = aPronunciation.getReverseSyllables();
		RhymeTreeNode curNode = root;

		for (Integer syllableKey : rSyllables) {
			curNode = curNode.getChild(syllableKey);
		}
		curNode.setEndNode(aPronunciation.getWord());

		return true;
	}

	public Collection<String> getRhymes() {
		Collection<String> output = new Vector<String>();
		
		return output;
	}

	public String toString() {
		return root.toRecursiveString("");
	}

	private class RhymeTreeNode {
		private int syllableValue;
		private Vector<RhymeTreeNode> childList;
		private Vector<String> isTerminating;

		public RhymeTreeNode(int aKey) {
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

		private RhymeTreeNode getChild(int aKey) {
			if (childList == null) {
				RhymeTreeNode output = new RhymeTreeNode(aKey);
				childList = new Vector<RhymeTreeNode>();
				childList.add(output);
				return output;
			} else {
				for (RhymeTreeNode child : childList) {
					if (child.syllableValue == aKey) {
						return child;
					}
				}
				RhymeTreeNode output = new RhymeTreeNode(aKey);
				childList.add(output);
				return output;
			}
		}

		private String toRecursiveString(String accumulatedString) {
			String output = "";
			if (isTerminating != null)
				return  SyllableHash.get(syllableValue) + accumulatedString + "\t\t" + isTerminating + "\n";
			if (childList != null) {
				for (RhymeTreeNode child : childList) {
					output += child.toRecursiveString(SyllableHash.get(syllableValue) + accumulatedString);
				}
			}
			return output;
		}
	}

}
