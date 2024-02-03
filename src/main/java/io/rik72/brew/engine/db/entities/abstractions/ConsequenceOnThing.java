package io.rik72.brew.engine.db.entities.abstractions;

import io.rik72.brew.engine.db.entities.Location;
import io.rik72.brew.engine.db.entities.ThingStatus;

public interface ConsequenceOnThing {
	public ThingStatus getBeforeStatus();
	public ThingStatus getAfterStatus();
	public Location getToLocation();
	public Boolean getAfterVisibility();
	public String getAfterText();
}
