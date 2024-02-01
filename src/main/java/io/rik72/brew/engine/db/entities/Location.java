package io.rik72.brew.engine.db.entities;

import java.util.List;

import io.rik72.brew.engine.db.delta.LocationDelta;
import io.rik72.brew.engine.db.entities.abstractions.Complement;
import io.rik72.brew.engine.db.repositories.CharacterRepository;
import io.rik72.brew.engine.db.repositories.LocationStatusRepository;
import io.rik72.brew.engine.db.repositories.ThingRepository;
import io.rik72.brew.engine.utils.TextUtils;
import io.rik72.mammoth.delta.Delta;
import io.rik72.mammoth.delta.Deltable;
import jakarta.persistence.*;

@Entity
public class Location implements Complement, Deltable {

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

	@Override
	public String getName() {
		return name;
	}

	@Override
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
		List<Character> charactersHere = CharacterRepository.get().findByLocation(this);
		if (charactersHere.size() > 0) {
			for (Character character : charactersHere) {
				String name = character.getName();
				builder.append("\n").append(name.substring(0, 1).toUpperCase()).append(name.substring(1))
				       .append(" is here. ")
				       .append(character.getStatus().getBrief());
			}
		}
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
	public boolean isDroppable() {
		return false;
	}

	@Override
	public boolean isTakeable() {
		return false;
	}

	@Override
	public void setLocation(Location location) {
		throw new UnsupportedOperationException("Unsupported method 'setLocation'");
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

	@Override
	public boolean isPlural() {
		return false;
	}
}
