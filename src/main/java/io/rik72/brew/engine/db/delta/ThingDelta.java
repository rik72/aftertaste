package io.rik72.brew.engine.db.delta;

import java.io.Serializable;

import io.rik72.brew.engine.db.entities.Thing;
import io.rik72.mammoth.delta.Delta;

public class ThingDelta extends Delta implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean visible;
	private boolean takeable;
	private boolean droppable;
	private Short locationId;
	private Short statusId;

	public ThingDelta() {}

	public ThingDelta(Thing thing) {
		super(thing);
		this.visible 	= thing.isVisible();
		this.takeable 	= thing.isTakeable();
		this.droppable 	= thing.isDroppable();
		this.locationId = thing.getLocation() != null ? thing.getLocation().getId() : null;
		this.statusId 	= thing.getStatus() != null ? thing.getStatus().getId() : null;
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

	public short getLocationId() {
		return locationId;
	}

	public short getStatusId() {
		return statusId;
	}

	@Override
	public String toString() {
		return "[ id=" + id + ", visible=" + visible + ", takeable=" + takeable + ", droppable=" + droppable
				+ ", locationId=" + locationId + ", statusId=" + statusId + " ]";
	}
}
