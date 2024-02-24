package io.rik72.brew.engine.db.entities;

import io.rik72.brew.engine.db.entities.abstractions.Status;
import io.rik72.brew.engine.db.repositories.LocationRepository;
import io.rik72.brew.engine.db.repositories.TextGroupRepository;
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
public class LocationStatus implements AbstractEntity, Status {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private short id;

    @Column
    private String label;

    @Column
    private String image;

    @Column(length=1024)
    private String description;

    @Column
    private String canonical;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
    private TextGroup transition;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
    private TextGroup finale;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private Location location;

	public LocationStatus() {
	}

	public LocationStatus(String locationName, String label, String image, String description, String canonical, String transition, String finale) {
		setLocation(locationName);
		this.label = label;
		this.image = image;
		this.description = description;
		this.canonical = canonical;
		if (transition != null)
			setTransition(transition);
		if (finale != null)
			setFinale(finale);
	}

	@Override
	public short getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public String getImage() {
		return image;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public String getCanonical() {
		return canonical;
	}

	public Location getLocation() {
		return location;
	}

	public void unsetLocation() {
		this.location = null;
	}

	public void setLocation(String locationName) {
		this.location = LocationRepository.get().getByName(locationName);
		if (this.location == null)
			throw new EntityNotFoundException("Location", locationName);
	}

	public TextGroup getFinale() {
		return finale;
	}

	public void setFinale(String textGroup) {
		this.finale = TextGroupRepository.get().getByName(textGroup);
		if (this.finale == null)
			throw new EntityNotFoundException("TextGroup", textGroup);
	}

	public TextGroup getTransition() {
		return transition;
	}

	public void setTransition(String textGroup) {
		this.transition = TextGroupRepository.get().getByName(textGroup);
		if (this.transition == null)
			throw new EntityNotFoundException("TextGroup", textGroup);
	}

    @Override
    public String toString() {
        return "{ LocationStatus :: " + 
			"id=" + id + ", " + 
			"location=" + TextUtils.denormalize(location.getName()) + ", " + 
			"label=" + TextUtils.denormalize(label) + ", " + 
			"canonical=" + TextUtils.denormalize(canonical) + ", " + 
			"image=" + TextUtils.denormalize(image) + ", " + 
			"description=" + TextUtils.denormalize(description) + ", " + 
			"transition=" + transition + ", " +
			"finale=" + finale +
		" }";
    }
}
