package io.rik72.brew.engine.db.entities;

import io.rik72.brew.engine.db.delta.ThingDelta;
import io.rik72.brew.engine.db.repositories.LocationRepository;
import io.rik72.brew.engine.db.repositories.ThingStatusRepository;
import io.rik72.brew.engine.utils.TextUtils;
import io.rik72.mammoth.delta.Delta;
import io.rik72.mammoth.delta.Deltable;
import io.rik72.mammoth.entities.AbstractEntity;
import io.rik72.mammoth.exceptions.EntityNotFoundException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Thing extends AbstractEntity implements Deltable {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private short id;

    @Column
	private String name;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private Location location;

    @Column
	private boolean visible = true;

    @Column
	private boolean takeable = false;

    @Column
	private boolean droppable = true;

    @Column
	private boolean plural = false;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private ThingStatus status;

	public Thing() {
	}

	public Thing(String name, String locationName) {
		this.name = name;
		setLocation(locationName);
	}

	@Override
	public short getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getListCanonical() {
		return (plural ? "" : "a ") + getStatus().getCanonical();
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setLocation(String locationName) {
		this.location = LocationRepository.get().getByName(locationName);
		if (this.location == null)
			throw new EntityNotFoundException("Location", locationName);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isTakeable() {
		return takeable;
	}

	public void setTakeable(boolean takeable) {
		this.takeable = takeable;
	}

	public boolean isDroppable() {
		return droppable;
	}

	public void setDroppable(boolean droppable) {
		this.droppable = droppable;
	}

	public boolean isPlural() {
		return plural;
	}

	public void setPlural(boolean plural) {
		this.plural = plural;
	}

	public ThingStatus getStatus() {
		return status;
	}

	public void setStatus(ThingStatus status) {
		this.status = status;
	}

	public void setStatus(String statusLabel) {
		this.status = ThingStatusRepository.get().getByThingAndLabel(this, statusLabel);
	}

    @Override
    public String toString() {
        return "{ Thing :: " + 
			id + " : " + 
			TextUtils.quote(name) + " : " +
			TextUtils.quote(location.getName()) + " : " +
			(visible ? "visible" : "invisible") + " : " +
			(takeable ? "takeable" : "untakeable") + 
			(takeable ? " : " + (droppable ? "droppable" : "undroppable") : "") + " : " +
			TextUtils.quote(status != null ? status.getLabel() : "-") +
		" }";
    }

	@Override
	public Delta getDelta() {
		return new ThingDelta(this);
	}
}