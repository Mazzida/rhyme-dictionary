package dictionary.crawler;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;


public abstract class RhymeCrawler {

	private static final int THREADCOUNT = 20;
	private static final int URL_RETRY_MAX = 10;
	private static final int FLUSH_MS_INTERVAL = 10000;
	private static final String WORD_FILE_NAME = "wordlist_text.txt";
	private static final String RHYME_FILE_NAME = "rhyme_text.txt";
	private BufferedReader reader;
	private FileWriter writer;
	private Vector<Runner> runnerList;
	private long writeCount;

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
			WriteOverseer tOverseer = new WriteOverseer();
			tOverseer.start();
			tOverseer.join();
			writer.close();
			System.out.println("CRAWL SESSION COMPLETE");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void reset() {
		try {
			writeCount = 0;
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
				writeCount ++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private synchronized void writerFlush() {
		try {
			writer.flush();
		} catch (IOException e) {
			System.err.println("ERROR: writerflush(): " + e.getMessage());
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
			String urlString;
			URL curURL;
			InputStream inStream;
			StringBuilder contents = new StringBuilder();
			urlString = getSearchUrlString(curWord);
			for (int rep = 0; rep < URL_RETRY_MAX; rep ++) {
				try {
					curURL = new URL(urlString);
					inStream = curURL.openStream();
					goal = new Scanner(inStream);
					while (goal.hasNextLine()) {
						contents.append(goal.nextLine()).append('\n');
					}
					List<PronunciationResult> pronunciationList = processPageUrl(contents.toString());
					if (pronunciationList != null) {
						for (PronunciationResult result : pronunciationList) {
							writeEntry(result);
						}
					}
					break;
				} catch (Exception e) {
					System.err.printf("ERROR: url retrieval number %s : %s\n", rep, urlString);
					System.err.printf("\t%s\n", e.getMessage());
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				} finally {
					if (goal != null) {
						goal.close();
					}
				}
			}
		}
	}

	class WriteOverseer extends Thread {
		public void run() {
			while(runnerList.size() != 0) {
				try {
					writerFlush();
					sleep(FLUSH_MS_INTERVAL);
					System.out.printf("%s : %d word processed\n", new Date(System.currentTimeMillis()), writeCount, runnerList.size());
				} catch (Exception e) {
					System.err.println("ERROR: flush failed: " + e.getMessage());
				}
			}
		}
	}
}
