package io.rik72.brew.engine.db.entities.abstractions;

import io.rik72.brew.engine.db.entities.Location;

public interface Complement {
	public Status getStatus();
	public String getName();
	public boolean isPlural();
	public boolean isDroppable();
	public boolean isTakeable();
	public void setLocation(Location location);
}
