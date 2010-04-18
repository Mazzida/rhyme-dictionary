package dictionary.main;

import java.util.Collection;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class RhymeDictionaryCLI {

	
	/**
	 * Read from the standard input and handle the responses until 
	 * the user closes the program.
	 */
	public static void start() {
		Scanner inScanner = new Scanner(System.in);
		load();
		while (inScanner.hasNextLine()) {
			String command = inScanner.nextLine();

			// preformat input string for whitespace
			command = command.trim();
			command = command.replaceAll("\\s+", " ");

			// determine command
			if (matchStrictRhyme(command)) {
				respondStrictRhyme(command);
			} else if (matchPronunciation(command)) {
				respondPronunciation(command);
			} else if (matchHelp(command)) {
				respondHelp();
			} else if (matchExit(command)) {
				respondExit();
			} else {
				respondUnrecognized();
			}
			printPrompt();
		}
	}

	private static void load() {
		System.out.println("loading dictionary..");
		RhymeQueryHandler.touch();
		System.out.println("load complete");
		printPrompt();
	}
	
	private static void respondUnrecognized() {
		System.out.println("Unrecognized command.  Query '-h' or 'help' for help text.");
	}

	private static void printPrompt() {
		System.out.print(">>");
	}

	private static boolean matchStrictRhyme(String aCommand) {
		return aCommand.matches("-s \\w+");
	}

	private static boolean matchPronunciation(String aCommand) {
		return aCommand.matches("-p \\w+");
	}

	private static boolean matchHelp(String aCommand) {
		return aCommand.contains("help") || aCommand.contains("-h");
	}

	private static boolean matchExit(String aCommand) {
		return aCommand.contains("exit");
	}

	public static void respondStrictRhyme(String command) {
		String aWord = command.substring(3);
		Map<String, Set<String>> result = RhymeQueryHandler.getStrictRhymes(aWord);
		if (result != null) {
			for (String pronunciation : result.keySet()) {
				System.out.println(pronunciation);
				for (String rhyme : result.get(pronunciation)) {
					System.out.println("\t" + rhyme);
				}
			}
		} else {
			System.out.println();
		}
	}
	
	private static void respondPronunciation(String command) {
		String aWord = command.substring(3);
		Collection<String> pronunciationCollection = RhymeQueryHandler.getPronunciations(aWord);
		if (pronunciationCollection != null) {
			for (String pronunciation : pronunciationCollection) {
				System.out.print(pronunciation);
			}
		} else {
			System.err.println("word not found");
		}
	}

	private static void respondHelp() {
		System.out.println("Usage:  ");
		System.out.println("	-h	help	prints this help message");
		System.out.println("	-p		returns the set of pronunciations for this word");
		System.out.println("	-s		returns the set of strict rhyme results");
		System.out.println("	exit		exits program");
	}

	private static void respondExit() {
		System.exit(0);
	}

}
