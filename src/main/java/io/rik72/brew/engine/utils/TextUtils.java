package io.rik72.brew.engine.utils;

import java.util.Collection;

import io.rik72.brew.engine.db.entities.Word;

public class TextUtils {

	public static String denormalize(Object obj) {
		return denormalize(obj, false);
	}

	public static String denormalize(Object obj, boolean full) {
		return
			(obj == null ? "null" :
			(obj instanceof String ? (full ? quote((String) obj) : quote((String) obj, 40)) :
			(obj instanceof Boolean ? bool((Boolean) obj) :
			obj.toString()
		)));
	}

	public static String bool(Boolean var) {
		return var != null ? (var ? "true" : "false" ) : "null";
	}

	public static String quote(String str) {
		return quote(str, Integer.MAX_VALUE);
	}

	public static String quote(String str, int maxLength) {
		return
			(str == null ? "null" :
			(str.length() > maxLength ? ("\"" + str.substring(0, maxLength) + "\"") + "..." :
			"\"" + str + "\""
		));
	}

	public static String ucFirst(String str) {
		return str != null ? str.substring(0, 1).toUpperCase() + str.substring(1) : null;
	}

	public static String repeat(String str, int n) {
		return new String(new char[n]).replace("\0", str);
	}

	public static String spaces(int n) {
		return new String(new char[n]).replace("\0", " ");
	}

	public static String wordsToText(Collection<Word> words) {
		StringBuilder builder = new StringBuilder();
		String separator = "";
		for (Word word : words) {
			builder.append(separator).append(word.getText());
			separator = " ";
		}
		return builder.toString();
	}


	public static String wrap(String txt, int indent, int colmax) {
		if (txt.length() <= colmax - indent && !txt.contains("\n")) {
			return spaces(indent) + txt;
		}

		StringBuilder result = new StringBuilder();
		for (String phrase : txt.split("\n")) {
			boolean firstToken = true;
			StringBuilder line = new StringBuilder();
			for (String token : phrase.split(" ")) {
				if (firstToken) {
					line.append(spaces(indent) + token);
					firstToken = false;
				}
				else if (line.length() + token.length() + 1 < colmax - indent) {
					line.append(" ").append(token);
				}
				else {
					result.append(line).append("\n");
					line = new StringBuilder(spaces(indent) + token);
				}
			}
			result.append(line).append("\n");
		}

		return result.toString();
	}
}
