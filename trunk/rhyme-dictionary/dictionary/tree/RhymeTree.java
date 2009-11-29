package dictionary.tree;

import java.util.Collection;
import java.util.Vector;


public class RhymeTree {

	private RhymeTreeNode root;
	
	public RhymeTree() {
		root = new RhymeTreeNode(0);
	}
	
	public boolean insert(PronunciationEntry aPronunciation) {
		//TODO
		return true;
	}

	public Collection<String> getRhymes() {
		Collection<String> output = new Vector<String>();
		
		return output;
	}

	private class RhymeTreeNode {
		private int syllableValue;
		private Vector<RhymeTreeNode> childList;
		private String isTerminating;


		public RhymeTreeNode(int aKey) {
			syllableValue = aKey;
		}
	}

}
