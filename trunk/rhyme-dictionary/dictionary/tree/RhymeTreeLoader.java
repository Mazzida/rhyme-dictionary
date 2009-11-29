package dictionary.tree;

import java.io.File;
import java.util.Scanner;

public class RhymeTreeLoader {

	private static final String PRONUNCIATION_DICTIONARY = "pronunciations.txt";

	public RhymeTree loadTree() {
		RhymeTree tree = new RhymeTree();
		try {
			Scanner fReader = new Scanner( new File(PRONUNCIATION_DICTIONARY));
			while (fReader.hasNextLine()) {
				String entryString = fReader.nextLine();
				PronunciationEntry entry = PronunciationEntry.makeEntry(entryString);
				tree.insert(entry);
			}
		} catch(Exception e) {
			System.err.println("ERROR: tree load failure");
			System.err.println(e.getLocalizedMessage());
		}
		return tree;
	}

}
