package test.dictionary.tree;

import org.junit.Test;

import dictionary.tree.RhymeTree;
import dictionary.tree.RhymeTreeLoader;


public class RhymeTreeLoaderTest {

	@Test
	public void testLoader() {
		RhymeTree tree = RhymeTreeLoader.loadTree();
		System.out.println(tree);
	}

	

}
