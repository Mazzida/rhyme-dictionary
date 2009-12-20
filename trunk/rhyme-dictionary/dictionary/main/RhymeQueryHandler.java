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

	/**
	 * Returns the set of rhyming words, or null if the
	 * argument word was not found in the dataset
	 */
	public static Set<String> getRhymes(String aWord) {
		return singleton.trie.getRhymes(aWord);
	}

	/**
	 * Returns the set of strict rhyming words, or null if the
	 * argument word was not found in the dataset.  A strict rhyme 
	 * occurs when words match in both final stressed vowel sounds
	 * and all subsequent syllables.
	 */
	public static Set<String> getStrictRhymes(String aWord) {
		return singleton.trie.getStrictRhymes(aWord);
	}
	
	public static void touch() {
		
	}

}
