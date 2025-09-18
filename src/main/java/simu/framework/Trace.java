package simu.framework;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * General output for the diagnostic messages. Every diagnostic message has a severity level.
 * It is possible to control which level of diagnostic messages is printed.
 * This version writes messages both to the console and to a log file.
 */
public class Trace {
	/**
	 * Diagnostic message severity level
	 * @see #INFO
	 * @see #WAR
	 * @see #ERR
	 */
	public enum Level {
		/**
		 * Messages just for your information only
		 */
		INFO,
		/**
		 * Warning messages
		 */
		WAR,
		/**
		 * Error messages
		 */
		ERR
	}

	private static Level traceLevel = Level.INFO;
	private static PrintWriter fileWriter;
	private static final String LOG_FILE = "trace.log";

	static {
		try {
			fileWriter = new PrintWriter(new BufferedWriter(new FileWriter(LOG_FILE, true)), true);
		} catch (IOException e) {
			System.err.println("Failed to open log file for Trace: " + e.getMessage());
			fileWriter = null;
		}
	}

	/**
	 * Set the filtering level of the diagnostic messages
	 *
	 * @param lvl filtering level. Severity level messages lower than this filtering level are not printed
	 */
	public static void setTraceLevel(Level lvl) {
		traceLevel = lvl;
	}

	/**
	 * Print the given diagnostic message to the console and the log file
	 *
	 * @param lvl severity level of the diagnostic message
	 * @param txt diagnostic message to be printed
	 */
	public static void out(Level lvl, String txt) {
		if (lvl.ordinal() >= traceLevel.ordinal()) {
			System.out.println(txt);

			if (fileWriter != null) {
				fileWriter.println(txt);
			}
		}
	}
	public static void out(Level lvl, String txt, String file) {
		if (lvl.ordinal() >= traceLevel.ordinal()) {
			System.out.println(txt);
			try{
				PrintWriter fileWriter = new PrintWriter(new BufferedWriter(new FileWriter(file, true)), true);
                fileWriter.println(txt);
            } catch (IOException ignored) {

			}

		}
	}

	/**
	 * Close the log file. Should be called when the program ends.
	 */
	public static void close() {
		if (fileWriter != null) {
			fileWriter.close();
		}
	}
}