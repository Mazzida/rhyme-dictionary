package dictionary.main;

import java.util.Set;

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

	public static Set<String> getRhymes(String aWord) {
		System.out.println(singleton.trie.toString()); //TODO remove
		System.out.println("\n\n"); //TODO remove
		return singleton.trie.getRhymes(aWord);
	}

}
