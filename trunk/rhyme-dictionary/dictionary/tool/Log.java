package dictionary.tool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Log {

	private static Map<String, File> classLogMap = new HashMap<String, File>();
	private static final String LOG_PATH_EXPR = "rhyme-dictionary-log-$CLASS-$DATE.txt";
	
	public static void log(String input, String className) {
		if (classLogMap.get(className) == null) {
			classLogMap.put(className, new File(getLogPath(className)));
		}
		FileWriter w = null;
		try {
			w = new FileWriter(classLogMap.get(className), true);
			w.append(input + "\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (w != null) {
				try {
					w.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	private static String getLogPath(String className) {
		String date = getDateString();
		String path = LOG_PATH_EXPR.replace("$CLASS", className).replace("$DATE", date);
		return path;
	}

	private static String getDateString() {
		Calendar c = Calendar.getInstance();
		StringBuilder out = new StringBuilder();
		out.append(c.get(Calendar.YEAR)).append("-");
		out.append(c.get(Calendar.MONTH)).append("-");
		out.append(c.get(Calendar.DATE));
		return out.toString();
	}

}
