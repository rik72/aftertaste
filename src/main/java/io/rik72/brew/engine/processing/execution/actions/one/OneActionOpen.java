package io.rik72.brew.engine.processing.execution.actions.one;

import java.util.Vector;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.Thing;
import io.rik72.brew.engine.db.entities.Word;

public class OneActionOpen extends OneActionDo {

	protected OneActionOpen(Vector<Word> words, boolean toBeConfirmed, Word verb, Character subject, String additionalFeedback,
	                        Word cName, Thing complement, boolean complementIsInInventory) {
		super(words, toBeConfirmed, verb, subject, additionalFeedback, cName, complement, complementIsInInventory);
	}

	@Override
	protected String alreadyDoneFeedback() {
		return "The " + cName.getText() + " " + (complement.isPlural() ? "are" : "is") + " already open.";
	}
}
