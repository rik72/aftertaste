package io.rik72.brew.engine.loader.loaders.parsing.parsers;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import io.rik72.brew.engine.loader.loaders.parsing.parsers.exceptions.EmptyOrMissingParseException;
import io.rik72.brew.engine.loader.loaders.parsing.raw.Raw;
import io.rik72.vati.parselocale.ParseLocale;
import io.rik72.vati.parselocale.ParseLocalized;

public abstract class Parser {
	
	public static Parser parse(Raw raw) {
		throw new UnsupportedOperationException();
	}

	protected static ParseLocalized<String> wordP =
		new ParseLocalized<>("Parser.wordP", " *([a-z \\.]+) *", "　*(.+)　*");
	protected static ParseLocalized<String> textP =
		new ParseLocalized<>("Parser.textP", " *([^()]+) *", "　*([^「」]+)　*");

	protected static ParseLocalized<Pattern> pattern(String uniqueId, String pattern) {
		return new ParseLocalized<>(uniqueId,
			pattern(pattern, ParseLocale.ASCII), pattern(pattern, ParseLocale.JAPANESE));
	}

	private static Pattern pattern(String pattern, ParseLocale type) {
		return Pattern.compile(pattern
			.replace("[", "\\(")
			.replace("]", "\\)")
			.replace("word", wordP.get(type))
			.replace("text", textP.get(type))
		);
	}

	public static String trim(String str) {
		return str != null ? str.strip() : null;
	}

	public static void checkNotEmpty(String entityName, String raw) {
		if (raw == null || raw.length() == 0) {
			throw new EmptyOrMissingParseException(entityName);
		}
	}

	public static void checkNotEmpty(String entityName, List<?> arr) {
		if (arr == null || arr.size() == 0) {
			throw new EmptyOrMissingParseException(entityName);
		}
	}

	public static void checkNotEmpty(String entityName, Raw raw) {
		if (raw == null) {
			throw new EmptyOrMissingParseException(entityName);
		}
	}

	@Override
	public String toString() {
		List<Field> allFields = Arrays.asList(this.getClass().getDeclaredFields());

		StringBuilder buf = new StringBuilder();
		buf.append("{ ");
		for (Field field : allFields) {
			try {
				if (!Modifier.isStatic(field.getModifiers())) {
					buf.append(field.getName()).append("=");
					if (field.getType() == String.class)
						buf.append("\"");
					buf.append(field.get(this));
					if (field.getType() == String.class)
						buf.append("\"");
					buf.append(" ");
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		buf.append("}");

		return buf.toString();
	}
}
