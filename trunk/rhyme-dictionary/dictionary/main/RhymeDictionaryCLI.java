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
			if (command.matches("-s \\w+")) {
				command = command.substring(3);
				process(command);
			} else if (command.matches("")) {
			} else {
				printHelp();
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

	private static void printHelp() {
		System.out.println("Usage:  ");
	}

	private static void printPrompt() {
		System.out.print(">>");
	}

	public static void process(String aWord) {
		Set<String> result = RhymeQueryHandler.getStrictRhymes(aWord);
		if (result != null) {
			for (String rhyme : result) {
				System.out.println(rhyme);
			}
		} else {
			System.out.println();
		}
	}
}
