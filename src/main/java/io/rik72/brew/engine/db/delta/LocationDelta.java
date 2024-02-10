package io.rik72.brew.engine.db.delta;

import java.io.Serializable;

import io.rik72.brew.engine.db.entities.Location;
import io.rik72.mammoth.delta.Delta;

public class LocationDelta extends Delta implements Serializable {

	private static final long serialVersionUID = 1L;

	private String statusLabel;

	public LocationDelta() {}

	public LocationDelta(Location location) {
		super(location);
		this.statusLabel = location.getStatus() != null ? location.getStatus().getLabel() : null;
	}

	public String getStatusLabel() {
		return statusLabel;
	}

	@Override
	public String toString() {
		return "[ name=" + name + ", statusLabel=" + statusLabel + " ]";
	}
}
