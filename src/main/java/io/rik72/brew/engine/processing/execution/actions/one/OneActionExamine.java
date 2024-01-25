package io.rik72.brew.engine.processing.execution.actions.one;

import java.util.Vector;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.Location;
import io.rik72.brew.engine.db.entities.Thing;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.repositories.LocationRepository;
import io.rik72.brew.engine.processing.execution.Results;

public class OneActionExamine extends OneActionDo {

	protected OneActionExamine(Vector<Word> words, boolean toBeConfirmed, Word verb, Character subject, String additionalFeedback,
	                           Word cName, Thing complement, boolean complementIsInInventory) {
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

	@Override
	protected Results checkComplement() {
		if (complement == null) {
			Location locationComplement = LocationRepository.get().getByStatusCanonical(cName.getCanonical().getText());
			if (locationComplement != null && subject.getLocation().getId() == locationComplement.getId())
				return new Results(true, false, locationComplement.getDescription());
			else
				return new Results(false, false, noSuchThing());
		}
		return null;
	}
}
