package io.rik72.brew.engine.db.entities.abstractions;

import io.rik72.brew.engine.db.entities.LocationStatus;

public interface ConsequenceOnLocation {
	public LocationStatus getBeforeStatus();
	public LocationStatus getAfterStatus();
	public String getAfterText();
}
