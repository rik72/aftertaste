package io.rik72.brew.engine.processing.execution.actions.one;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.entities.abstractions.Complement;
import io.rik72.brew.engine.processing.execution.base.Results;
import io.rik72.brew.engine.processing.parsing.mapping.WordMap;

public class OneActionExamine extends OneActionDo {

	protected OneActionExamine(WordMap wordMap, boolean toBeConfirmed, Word verb, Character subject, String additionalFeedback,
	                           Word cName, Complement complement, boolean complementIsInInventory) {
		super(wordMap, toBeConfirmed, verb, subject, additionalFeedback, cName, complement, complementIsInInventory);
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
		results.setEmphasis(true);
		mergeFeedbacks(results);
		return results;
	}
}
