package io.rik72.brew.engine.processing.execution.actions.zero;

import java.util.Vector;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.Word;

public class ZeroActionSouth extends ZeroActionDirection {

	protected ZeroActionSouth(Vector<Word> words, boolean toBeConfirmed, Word verb, Character subject, String additionalFeedback) {
		super(words, toBeConfirmed, verb, subject, additionalFeedback);
	}
}
