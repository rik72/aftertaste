package io.rik72.brew.engine.db.entities;

import java.util.List;

import io.rik72.brew.engine.db.delta.LocationDelta;
import io.rik72.brew.engine.db.repositories.LocationStatusRepository;
import io.rik72.brew.engine.db.repositories.ThingRepository;
import io.rik72.brew.engine.utils.TextUtils;
import io.rik72.mammoth.delta.Delta;
import io.rik72.mammoth.delta.Deltable;
import io.rik72.mammoth.entities.AbstractEntity;
import jakarta.persistence.*;

@Entity
public class Location extends AbstractEntity implements Deltable {

	public static String INVENTORY = "inventory";

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private short id;

    @Column
    private String name;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private LocationStatus status;

	public Location() {
	}

	public Location(String name) {
		this.name = name;
	}

	@Override
	public short getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public LocationStatus getStatus() {
		return status;
	}

	public void setStatus(LocationStatus status) {
		this.status = status;
	}

	public void setStatus(String statusLabel) {
		this.status = LocationStatusRepository.get().getByLocationAndLabel(this, statusLabel);
	}

	public String getDescription() {
		StringBuilder builder = new StringBuilder();
		builder.append(status.getDescription());
		List<Thing> thingsHere = ThingRepository.get().findByLocation(this, true, true);
		if (thingsHere.size() > 0) {
			builder.append("\nYou see: ");
			String sep = "";
			for (Thing thing : thingsHere) {
				builder.append(sep).append(thing.getListCanonical());
				sep = ", ";
			}
			builder.append(".");
		}
		return builder.toString();
	}

    @Override
    public String toString() {
        return "{ Location :: " + 
			id + " : " + 
			TextUtils.quote(name) +
			(status != null ? " : " + TextUtils.quote(status.getLabel()) : "") +
		" }";
    }

	@Override
	public Delta getDelta() {
		return new LocationDelta(this);
	}
}
