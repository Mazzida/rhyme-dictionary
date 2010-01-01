package dictionary.main;

import java.util.Scanner;
import java.util.Set;

public class RhymeDictionaryCLI {

	
	
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

	public static void respondStrictRhyme(String command) {
		String aWord = command.substring(3);
		Set<String> result = RhymeQueryHandler.getStrictRhymes(aWord);
		if (result != null) {
			for (String rhyme : result) {
				System.out.println(rhyme);
			}
		} else {
			System.out.println();
		}
	}
	
	private static void respondPronunciation(String command) {
		String aWord = command.substring(3);
		String pronunciation = RhymeQueryHandler.getPronunciation(aWord);
		if (pronunciation != null) {
			System.out.print(pronunciation);
		} else {
			System.err.println("word not found");
		}
	}

	private static void respondHelp() {
		System.out.println("Usage:  ");
	}

	private static void respondExit() {
		System.exit(0);
	}

}
