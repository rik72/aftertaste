package io.rik72.amber.logger;

import io.rik72.brew.engine.utils.TextUtils;

public class Log {

	private static Level level = Level.DEBUG;
	private static int gameCols = 40;

	public static void debug(Object msg) {
		if (level.equals(Level.DEBUG) || level.compareTo(Level.DEBUG) < 0)
			logln(msg, Level.DEBUG);
	}

	public static void info(Object msg) {
		if (level.equals(Level.INFO) || level.compareTo(Level.INFO) < 0)
			logln(msg, Level.INFO);
	}

	public static void warning(Object msg) {
		if (level.equals(Level.WARNING) || level.compareTo(Level.WARNING) < 0)
			logln(msg, Level.WARNING);
	}

	public static void error(Object msg) {
		if (level.equals(Level.ERROR) || level.compareTo(Level.ERROR) < 0)
			logln(msg, Level.ERROR);
	}

	public static void game(Object msg) {
		game(msg, 0);
	}

	public static void game(Object msg, int indent) {
		if (level.equals(Level.GAME) || level.compareTo(Level.GAME) < 0)
			 gamencols(msg.toString(), indent);
	}

	public static void gameln(Object msg) {
		gameln(msg, 0);
	}

	public static void gameln(Object msg, int indent) {
		if (level.equals(Level.GAME) || level.compareTo(Level.GAME) < 0)
			 gamencolsln(msg.toString(), indent);
	}

	public static void prompt(Object msg) {
		if (level.equals(Level.PROMPT) || level.compareTo(Level.PROMPT) < 0)
			log(msg, Level.PROMPT);
	}

	public static void skip() {
		println("");
	}

	///////////////////////////////////////////////////////////////////////////

	private static void gamencols(String txt, int indent) {
		if (txt.length() <= gameCols - indent && !txt.contains("\n")) {
			log(TextUtils.spaces(indent) + txt, Level.GAME);
		}
		else {
			boolean firstPhrase = true;
			for (String phrase : txt.split("\n")) {
				if (firstPhrase)
					firstPhrase = false;
				else
					System.out.println("");
				StringBuilder line = null;
				boolean firstToken = true;
				for (String token : phrase.split(" ")) {
					if (firstToken) {
						line = new StringBuilder(TextUtils.spaces(indent) + token);
						firstToken = false;
					}
					else if (line.length() + token.length() + 1 < gameCols - indent) {
						line.append(" ").append(token);
					}
					else {
						logln(line.toString(), Level.GAME);
						line = new StringBuilder(TextUtils.spaces(indent) + token);
					}
				}
				if (line.length() > 0)
					log(line.toString(), Level.GAME);
			}
		}
	}

	private static void gamencolsln(String txt, int cols) {
		gamencols(txt, cols);
		System.out.println("");
	}

	private static enum Level {
		DEBUG		("\033[95;1m"),
		INFO		("\033[94;1m"),
		WARNING		("\033[93;1m"),
		ERROR		("\033[91;1m"),
		GAME		("\033[96;1m"),
		PROMPT		("\033[95;1m"),
		;

		private String colorSeq;

		private Level(String colorSeq) {
			this.colorSeq = colorSeq;
		}
	}

	private static String normalColorSeq = "\033[0m";

	private static void log(Object msg, Level lvl) {
		print("[" + lvl.colorSeq + lvl + normalColorSeq + "] " + msg);
	}

	private static void logln(Object msg, Level lvl) {
		println("[" + lvl.colorSeq + lvl + normalColorSeq + "] " + msg);
	}

	private static void print(String str) {
		System.out.print(str);
	}

	private static void println(String str) {
		System.out.print(str + "\n");
	}
}
