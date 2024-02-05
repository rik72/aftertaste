package io.rik72.brew.engine.processing.execution.actions;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.Location;
import io.rik72.brew.engine.db.entities.Thing;
import io.rik72.brew.engine.db.entities.abstractions.ConsequenceOnCharacter;
import io.rik72.brew.engine.db.entities.abstractions.ConsequenceOnLocation;
import io.rik72.brew.engine.db.entities.abstractions.ConsequenceOnThing;
import io.rik72.mammoth.db.DB;

public class ConsequenceHelper {

	public static void applyConsequenceOnThing(ConsequenceOnThing action, Thing before) {
		if (action.getAfterStatus() != null)
			before.setStatus(action.getAfterStatus().getLabel());
		if (action.getToLocation() != null)
			before.setLocation(action.getToLocation());
		if (action.getAfterVisibility() != null)
			before.setVisible(action.getAfterVisibility());
		DB.persist(before);
	}

	public static void applyConsequenceOnLocation(ConsequenceOnLocation action, Location before) {
		before.setStatus(action.getAfterStatus().getLabel());
		DB.persist(before);
	}

	public static void applyConsequenceOnCharacter(ConsequenceOnCharacter action, Character before) {
		if (action.getAfterStatus() != null)
			before.setStatus(action.getAfterStatus().getLabel());
		if (action.getToLocation() != null)
			before.setLocation(action.getToLocation());
		if (action.getAfterVisibility() != null)
			before.setVisible(action.getAfterVisibility());
		DB.persist(before);
	}
}
