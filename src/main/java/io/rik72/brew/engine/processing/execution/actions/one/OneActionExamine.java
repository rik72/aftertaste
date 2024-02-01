package io.rik72.brew.engine.processing.execution.actions.one;

import java.util.Vector;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.entities.abstractions.Complement;
import io.rik72.brew.engine.processing.execution.Results;

public class OneActionExamine extends OneActionDo {

	protected OneActionExamine(Vector<Word> words, boolean toBeConfirmed, Word verb, Character subject, String additionalFeedback,
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

		setDoable(true);

		results = super.execute();

		results.setSuccess(true);
		results.setFeedback(complement.getStatus().getDescription());
		mergeFeedbacks(results);
		return results;
	}
}
