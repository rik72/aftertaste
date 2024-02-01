package io.rik72.brew.engine.db.entities;

import io.rik72.brew.engine.db.repositories.LocationRepository;
import io.rik72.brew.engine.db.repositories.WordRepository;
import io.rik72.brew.engine.utils.TextUtils;
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
public class LocationXLocation implements AbstractEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private short id;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private LocationStatus fromStatus;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private Location toLocation;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private Word direction;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private Word verb;

	public LocationXLocation() {
	}

	public LocationXLocation(LocationStatus fromStatus,
	                         String toLocationName, String direction, String verb) {
		this.fromStatus = fromStatus;
		setToLocation(toLocationName);
		setDirection(direction);
		setVerb(verb);
	}

	@Override
	public short getId() {
		return id;
	}

	public LocationStatus getFromStatus() {
		return fromStatus;
	}

	public Location getToLocation() {
		return toLocation;
	}

	public void setToLocation(String toLocationName) {
		this.toLocation = LocationRepository.get().getByName(toLocationName);
		if (this.toLocation == null)
			throw new EntityNotFoundException("Location", toLocationName);
	}

	public Word getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = WordRepository.get().getByText(direction);
		if (this.direction == null)
			throw new EntityNotFoundException("Word", direction);
	}

	public Word getVerb() {
		return verb;
	}

	public void setVerb(String verb) {
		this.verb = WordRepository.get().getByText(verb);
	}

    @Override
    public String toString() {
        return "{ LocationXLocation :: " + 
			id + " : " + 
			TextUtils.quote(fromStatus.getLocation().getName()) + " : " + 
			TextUtils.quote(fromStatus.getLabel()) + " : " + 
			TextUtils.quote(toLocation.getName()) + " : " + 
			TextUtils.quote(direction.getText()) + " : " + 
			TextUtils.quote(verb != null ? verb.getText() : "-") +
		" }";
    }
}
