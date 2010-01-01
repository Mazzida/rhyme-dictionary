package dictionary.main;

public class Main {

	public static void main(String[] args) {
		if (args.length == 0) {
			RhymeDictionaryCLI.start();
		} else {
			for (String arg : args) {
				RhymeDictionaryCLI.respondStrictRhyme(arg);
			}
		}
	}
}
