package io.rik72.brew.engine.db.entities;

import io.rik72.brew.engine.db.entities.abstractions.Status;
import io.rik72.brew.engine.db.repositories.ThingRepository;
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
public class ThingStatus implements AbstractEntity, Status {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private short id;

    @Column
    private String label;

    @Column(length=1024)
    private String description;

    @Column
    private String canonical;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private Thing thing;

	public ThingStatus() {
	}

	public ThingStatus(String thingName, String label, String description, String canonical) {
		setThing(thingName);
		this.label = label;
		this.description = description;
		this.canonical = canonical;
	}

	@Override
	public short getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public String getCanonical() {
		return canonical;
	}

	public Thing getThing() {
		return thing;
	}

	public void setThing(String thingName) {
		this.thing = ThingRepository.get().getByName(thingName);
		if (this.thing == null)
			throw new EntityNotFoundException("Thing", thingName);
	}

    @Override
    public String toString() {
        return "{ ThingStatus :: " + 
			id + " : " + 
			TextUtils.quote(thing.getName()) + " : " + 
			TextUtils.quote(label) + " : " + 
			TextUtils.quote(canonical) +
		" }";
    }
}
