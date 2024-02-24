package io.rik72.brew.engine.processing.execution.actions.zero;

import java.util.List;
import java.util.Vector;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.LocationXLocation;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.repositories.LocationXLocationRepository;
import io.rik72.brew.engine.processing.execution.Results;
import io.rik72.mammoth.db.DB;

public class ZeroActionDirection extends ZeroActionExecutor {

	protected ZeroActionDirection(Vector<Word> words, boolean toBeConfirmed, Word verb, Character subject, String additionalFeedback) {
		super(words, toBeConfirmed, verb, subject, additionalFeedback);
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
		return "You can't go that way.";
	}

	@Override
	protected String doneFeedback() {
		return "You go " + verb.getCanonical().getText() + ".";
	}
}
