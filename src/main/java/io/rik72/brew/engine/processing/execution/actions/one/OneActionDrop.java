package io.rik72.brew.engine.processing.execution.actions.one;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.entities.abstractions.Complement;
import io.rik72.brew.engine.processing.execution.base.Results;
import io.rik72.brew.engine.processing.parsing.mapping.WordMap;
import io.rik72.mammoth.db.DB;
import io.rik72.vati.locale.Translations;

public class OneActionDrop extends OneActionDo {

	protected OneActionDrop(WordMap wordMap, boolean toBeConfirmed, Word verb, Character subject, String additionalFeedback,
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
	protected String noSuchComplement() {
		if (complement == null)
			return "No such thing is in your inventory.";
		return Translations.get("you_do_not_own", cName.getCanonical().getText());
	}

	@Override
	protected String doneFeedback() {
		return Translations.get("you_done_drop", cName.getText());
	}

	@Override
	protected String cantDoThat() {
		return Translations.get("you_cant_drop", cName.getText());
	}

	@Override
	protected String complementNotInInventory() {
		return Translations.get("you_do_not_own", cName.getCanonical().getText());
	}
}
