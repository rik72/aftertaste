package io.rik72.brew.engine.db.delta;

import java.io.Serializable;

import io.rik72.brew.engine.db.entities.Thing;
import io.rik72.mammoth.delta.Delta;

public class ThingDelta extends Delta implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean visible;
	private boolean takeable;
	private boolean droppable;
	private String locationName;
	private String statusLabel;

	public ThingDelta() {}

	public ThingDelta(Thing thing) {
		super(thing);
		this.visible 		= thing.isVisible();
		this.takeable 		= thing.isTakeable();
		this.droppable 		= thing.isDroppable();
		this.locationName	= thing.getLocation() != null ? thing.getLocation().getName() : null;
		this.statusLabel 	= thing.getStatus() != null ? thing.getStatus().getLabel() : null;
	}

	public boolean isVisible() {
		return visible;
	}

	public boolean isTakeable() {
		return takeable;
	}

	public boolean isDroppable() {
		return droppable;
	}

	public String getLocationName() {
		return locationName;
	}

	public String getStatusLabel() {
		return statusLabel;
	}

	@Override
	public String toString() {
		return "[ name=" + name + ", visible=" + visible + ", takeable=" + takeable + ", droppable=" + droppable
				+ ", locationName=" + locationName + ", statusLabel=" + statusLabel + " ]";
	}
}
