package dictionary.tree;

import java.io.File;
import java.util.Collection;
import java.util.Vector;


public class RhymeTree {

	public RhymeTree() {
		
	}
	
	public void loadDictionary(File aInputFile) {
		if (aInputFile == null) {
			throw new IllegalArgumentException("Null input file");
		}
		
	}

	public Collection<String> getRhymes() {
		Collection<String> output = new Vector<String>();
		
		return output;
	}

	private class RhymeTreeNode {
		private long syllableValue;
		private Vector<RhymeTreeNode> childList;
		private String isTerminating;


		public RhymeTreeNode() {
			
		}
	}

}
