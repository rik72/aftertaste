package io.rik72.brew.engine.processing.execution.actions.zero;

import java.util.List;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.LocationXLocation;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.repositories.LocationXLocationRepository;
import io.rik72.brew.engine.processing.execution.base.Results;
import io.rik72.brew.engine.processing.parsing.mapping.WordMap;
import io.rik72.mammoth.db.DB;
import io.rik72.vati.locale.Translations;

public class ZeroActionDirection extends ZeroActionExecutor {

	protected ZeroActionDirection(WordMap wordMap, boolean toBeConfirmed, Word verb, Character subject, String additionalFeedback) {
		super(wordMap, toBeConfirmed, verb, subject, additionalFeedback);
	}

	@Override
	public Results execute() {

		List<LocationXLocation> connections = LocationXLocationRepository.get().findByFromStatus(subject.getLocation().getStatus());
		for (LocationXLocation connection : connections) {
			if (connection.getDirection().getText().equals(verb.getCanonical().getText()) && (connection.getVerb() == null || connection.getVerb().getText().equals("go"))) {
				subject.setLocation(connection.getToLocation());
				DB.persist(subject);
				setDoable(true);
				return buildResults(true, true, doneFeedback(), null, null);
			} 
		}
		return new Results(false, false, cantDoThat());
	}

	@Override
	protected String cantDoThat() {
		return Translations.get("you_cant_direction");
	}

	@Override
	protected String doneFeedback() {
		return Translations.get("you_done_direction", verb.getCanonical().getText());
	}
}
