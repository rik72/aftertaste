package io.rik72.brew.engine.processing.execution.actions.direction;

import java.util.List;
import java.util.Vector;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.LocationXLocation;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.repositories.LocationXLocationRepository;
import io.rik72.brew.engine.processing.execution.Results;
import io.rik72.mammoth.db.DB;

public class DirectionActionFly extends DirectionActionExecutor {

	protected DirectionActionFly(Vector<Word> words, boolean toBeConfirmed, Word verb, Character subject, String additionalFeedback, Word direction) {
		super(words, toBeConfirmed, verb, subject, additionalFeedback, direction);
	}

	@Override
	public Results execute() {

		setDoable(true);

		Results results = checkVerb();
		if (results != null)
			return results;

		List<LocationXLocation> connections = LocationXLocationRepository.get().findByFromStatus(subject.getLocation().getStatus());
		for (LocationXLocation connection : connections) {
			if (connection.getDirection().getText().equals(direction.getCanonical().getText()) && (connection.getVerb() == null || connection.getVerb().getText().equals(verb.getCanonical().getText()))) {
				subject.setLocation(connection.getToLocation());
				DB.persist(subject);
				return buildResults(true, true, doneFeedback(), null, null);
			} 
		}
		return new Results(false, false, cantDoThat());
	}
}
