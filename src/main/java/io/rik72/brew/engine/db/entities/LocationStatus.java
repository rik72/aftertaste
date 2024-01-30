package io.rik72.brew.engine.db.entities;

import io.rik72.brew.engine.db.repositories.LocationRepository;
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
public class LocationStatus extends AbstractEntity {

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

    @Column
    private String finale;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private Location location;

	public LocationStatus() {
	}

	public LocationStatus(String locationName, String label, String image, String description, String canonical, String finale) {
		setLocation(locationName);
		this.label = label;
		this.image = image;
		this.description = description;
		this.canonical = canonical;
		this.finale = finale;
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

	public String getDescription() {
		return description;
	}

	public String getCanonical() {
		return canonical;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(String locationName) {
		this.location = LocationRepository.get().getByName(locationName);
		if (this.location == null)
			throw new EntityNotFoundException("Location", locationName);
	}

	public String getFinale() {
		return finale;
	}

    @Override
    public String toString() {
        return "{ LocationStatus :: " + 
			id + " : " + 
			TextUtils.quote(location.getName()) + " : " + 
			TextUtils.quote(label) + " : " + 
			TextUtils.quote(canonical) + " : " + 
			TextUtils.quote(image) + " : " + 
			TextUtils.quote(description) + " : " +
			(finale != null && finale.length() > 0 ? (" : " + TextUtils.quote(finale)) : "") +
		" }";
    }
}
