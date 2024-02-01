package io.rik72.brew.engine.processing.execution.actions.one;

import java.util.Vector;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.entities.abstractions.Complement;
import io.rik72.brew.engine.processing.execution.Results;
import io.rik72.mammoth.db.DB;

public class OneActionDrop extends OneActionDo {

	protected OneActionDrop(Vector<Word> words, boolean toBeConfirmed, Word verb, Character subject, String additionalFeedback,
	                        Word cName, Complement complement, boolean complementIsInInventory) {
		super(words, toBeConfirmed, verb, subject, additionalFeedback, cName, complement, complementIsInInventory);
	}

	@Override
	public Results execute() throws Exception {

		Results results = checkVerb();
		if (results != null)
			return results;

		results = checkComplement();
		if (results != null)
			return results;

		if (!complement.isDroppable())
			return new Results(false, false, cantDoThat());

		setDoable(true);

		complement.setLocation(subject.getLocation());
		DB.persist(complement);

		results = super.execute();

		results.setSuccess(true);
		results.setFeedback(doneFeedback());
		mergeFeedbacks(results);
		return results;
	}

	@Override
	protected String noSuchThing() {
		if (complement == null)
			return "No such thing is in your inventory.";
		return "The " + cName.getText() + " " + (complement.isPlural() ? "are" : "is") + " not in your inventory.";
	}

	@Override
	protected String doneFeedback() {
		return "You drop the " + cName.getText() + ".";
	}

	@Override
	protected String cantDoThat() {
		return "You can't drop the " + cName.getText() + ".";
	}

	@Override
	protected String complementNotInInventory() {
		return "The " + cName.getText() + " " + (complement.isPlural() ? "are" : "is") + " not in your inventory.";
	}
}
