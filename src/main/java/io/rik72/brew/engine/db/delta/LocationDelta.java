package io.rik72.brew.engine.db.delta;

import java.io.Serializable;

import io.rik72.brew.engine.db.entities.Location;
import io.rik72.mammoth.delta.Delta;

public class LocationDelta extends Delta implements Serializable {

	private static final long serialVersionUID = 1L;

	private Short statusId;

	public LocationDelta() {}

	public LocationDelta(Location location) {
		super(location);
		this.statusId = location.getStatus() != null ? location.getStatus().getId() : null;
	}

	public short getStatusId() {
		return statusId;
	}

	@Override
	public String toString() {
		return "[ id=" + id + ", statusId=" + statusId + " ]";
	}
}
