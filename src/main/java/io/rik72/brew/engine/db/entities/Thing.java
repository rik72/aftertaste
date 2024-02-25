package io.rik72.brew.engine.db.entities;

import io.rik72.brew.engine.db.delta.ThingDelta;
import io.rik72.brew.engine.db.entities.abstractions.Complement;
import io.rik72.brew.engine.db.repositories.LocationRepository;
import io.rik72.brew.engine.db.repositories.ThingStatusRepository;
import io.rik72.brew.engine.utils.TextUtils;
import io.rik72.mammoth.delta.Delta;
import io.rik72.mammoth.delta.Deltable;
import io.rik72.mammoth.exceptions.EntityNotFoundException;
import io.rik72.vati.locale.Translations;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Thing extends Complement implements Deltable {

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
		this.name = Complement.name(name);
		setLocation(locationName);
	}

	@Override
	public short getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	public String getListCanonical() {
		return (plural ? "" : Translations.get("_single_indeterminate_article")) + getStatus().getCanonical();
	}

	public Location getLocation() {
		return location;
	}

	@Override
	public void setLocation(Location location) {
		this.location = location;
	}

	public void setLocation(String locationName) {
		this.location = LocationRepository.get().getByName(locationName);
		if (this.location == null)
			throw new EntityNotFoundException("Location", locationName);
	}

	public void unsetLocation() {
		this.location = null;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public boolean isTakeable() {
		return takeable;
	}

	public void setTakeable(boolean takeable) {
		this.takeable = takeable;
	}

	@Override
	public boolean isDroppable() {
		return droppable;
	}

	public void setDroppable(boolean droppable) {
		this.droppable = droppable;
	}

	@Override
	public boolean isPlural() {
		return plural;
	}

	public void setPlural(boolean plural) {
		this.plural = plural;
	}

	@Override
	public ThingStatus getStatus() {
		return status;
	}

	public void setStatus(ThingStatus status) {
		this.status = status;
	}

	public void setStatus(String statusLabel) {
		this.status = ThingStatusRepository.get().getByThingAndLabel(this, statusLabel);
	}

	public void unsetStatus() {
		this.status = null;
	}

    @Override
    public String toString() {
        return "{ Thing :: " + 
			"id=" + id + ", " + 
			"name=" + TextUtils.denormalize(name) + ", " +
			"location=" + TextUtils.denormalize(location.getName()) + ", " +
			"visible=" + TextUtils.denormalize(visible) + ", " +
			"takeable=" + TextUtils.denormalize(takeable) + ", " +
			"droppable=" + TextUtils.denormalize(droppable) + ", " +
			"status=" + TextUtils.denormalize(status.getLabel()) +
		" }";
    }

	@Override
	public Delta getDelta() {
		return new ThingDelta(this);
	}
}
