package dictionary.crawler;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;


public abstract class RhymeCrawler {

	private static final int THREADCOUNT = 20;
	private static final String WORD_FILE_NAME = "wordlist_text.txt";
	private static final String RHYME_FILE_NAME = "rhyme_text.txt";
	private BufferedReader reader;
	private FileWriter writer;
	private Vector<Runner> runnerList;

	public RhymeCrawler() {
		reset();
	}
	
	public void crawl() {
		try {
			runnerList = new Vector<Runner>();
			Runner tRunner;
			for (int i = 0; i < THREADCOUNT; i ++) {
				tRunner = new Runner();
				tRunner.start();
				runnerList.add(tRunner);
			}
			while (runnerList.size() != 0) {
				Thread.sleep(1500);
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void reset() {
		try {
			reader = new BufferedReader(new FileReader(WORD_FILE_NAME));
			writer = new FileWriter(RHYME_FILE_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private synchronized String getNextString() {
		try {
			int readAttempts = 0;
			while (!reader.ready()) {
				if (readAttempts >= 10) {
					return null;
				} else {
					readAttempts ++;
					Thread.sleep(10);
				}
			}
			if (reader.ready()) {
				return reader.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private synchronized void writeEntry(PronunciationResult entry) {
		if (entry != null) {
			try {
				writer.write(entry.toString() + '\n');
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected abstract String getSearchUrlString(String word);

	protected abstract List<PronunciationResult> processPageUrl(String contents);

	class Runner extends Thread {
		Scanner goal;

		public void run() {
			String curWord;
			while ((curWord = getNextString()) != null) {
				processString(curWord);
			}
			runnerList.remove(this);
		}
		
		private void processString(String curWord) {
			URL curURL;
			StringBuilder contents = new StringBuilder();
			try {
				curURL = new URL(getSearchUrlString(curWord));
				goal = new Scanner(curURL.openStream());
				while (goal.hasNextLine()) {
					contents.append(goal.nextLine()).append('\n');
				}
				goal.close();
				List<PronunciationResult> pronunciationList = processPageUrl(contents.toString());
				if (pronunciationList != null) {
					for (PronunciationResult result : pronunciationList) {
						writeEntry(result);
					}
				}
			} catch (Exception e) {
				System.err.println("ERROR: run()" + e.getLocalizedMessage());
			} finally {
				goal.close();
			}
		}
	}
	
}
