package io.rik72.brew.engine.db.delta;

import java.io.Serializable;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.mammoth.delta.Delta;

public class CharacterDelta extends Delta implements Serializable {

	private static final long serialVersionUID = 1L;

	private String locationName;
	private String statusLabel;

	public CharacterDelta() {}

	public CharacterDelta(Character character) {
		super(character);
		this.locationName = character.getLocation() != null ? character.getLocation().getName() : null;
		this.statusLabel = character.getStatus() != null ? character.getStatus().getLabel() : null;
	}

	public String getLocationName() {
		return locationName;
	}

	public String getStatusLabel() {
		return statusLabel;
	}

	@Override
	public String toString() {
		return "[ name=" + name + ", locationName=" + locationName + ", statusLabel=" + statusLabel + " ]";
	}
}
