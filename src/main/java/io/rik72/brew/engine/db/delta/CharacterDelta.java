package io.rik72.brew.engine.db.delta;

import java.io.Serializable;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.mammoth.delta.Delta;

public class CharacterDelta extends Delta implements Serializable {

	private static final long serialVersionUID = 1L;

	private Short locationId;
	private Short statusId;

	public CharacterDelta() {}

	public CharacterDelta(Character character) {
		super(character);
		this.locationId = character.getLocation() != null ? character.getLocation().getId() : null;
		this.statusId = character.getStatus() != null ? character.getStatus().getId() : null;
	}

	public short getLocationId() {
		return locationId;
	}

	public Short getStatusId() {
		return statusId;
	}

	@Override
	public String toString() {
		return "[ id=" + id + ", locationId=" + locationId + ", statusId=" + statusId + " ]";
	}
}
