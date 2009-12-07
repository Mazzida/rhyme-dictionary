package dictionary.main;

import java.util.Collection;

import dictionary.tree.RhymeTree;
import dictionary.tree.RhymeTreeLoader;

public class RhymeQueryHandler {

	private static final RhymeQueryHandler singleton = new RhymeQueryHandler();

	/**
	 * Rhyme Trie
	 */
	private RhymeTree trie;
	
	private RhymeQueryHandler() {
		trie = RhymeTreeLoader.loadTree();
	}

	public static Collection<String> getRhymes(String aWord) {
		return singleton.trie.getRhymes(aWord);
	}

}
