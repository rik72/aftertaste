package io.rik72.brew.engine.db.entities.abstractions;

import io.rik72.brew.engine.db.entities.Location;
import io.rik72.mammoth.entities.AbstractEntity;

public abstract class Complement implements AbstractEntity {

	public static String name(String name) {
		return name != null ? (name.charAt(0) == ENTITY_PREFIX_CHAR ? name : ENTITY_PREFIX + name) : null;
	}

	public static String ucName(String name) {
		return name.charAt(0) == ENTITY_PREFIX_CHAR ?
			name.substring(1, 2).toUpperCase() + name.substring(2) : 
			name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	public abstract Status getStatus();
	public abstract String getName();
	public abstract boolean isPlural();
	public abstract boolean isDroppable();
	public abstract boolean isTakeable();
	public abstract void setLocation(Location location);

	private static final char ENTITY_PREFIX_CHAR = '.';
	private static final String ENTITY_PREFIX = " ".replace(' ', ENTITY_PREFIX_CHAR);
}
