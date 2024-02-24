package io.rik72.brew.engine.db.entities.abstractions;

import io.rik72.brew.engine.db.entities.CharacterStatus;
import io.rik72.brew.engine.db.entities.Location;
import io.rik72.brew.engine.db.entities.TextGroup;

public interface ConsequenceOnCharacter {
	public CharacterStatus getBeforeStatus();
	public CharacterStatus getAfterStatus();
	public Location getToLocation();
	public Boolean getAfterVisibility();
	public String getAfterText();
	public TextGroup getTransition();
	public TextGroup getFinale();
}
