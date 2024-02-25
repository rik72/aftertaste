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
import io.rik72.vati.locale.Translations;
import jakarta.persistence.*;

@Entity
public class Location extends Complement implements Deltable {

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
		this.name = Complement.name(name);
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

	public void unsetStatus() {
		this.status = null;
	}

	public String getDescription() {
		StringBuilder builder = new StringBuilder();
		builder.append(status.getDescription());
		List<Character> charactersHere = CharacterRepository.get().findByLocation(this);
		if (charactersHere.size() > 0) {
			for (Character character : charactersHere) {
				String name = character.getName();
				builder.append("\n").append(Complement.ucName(name))
				       .append(Translations.get("is_here"))
				       .append(character.getStatus().getBrief());
			}
		}
		List<Thing> thingsHere = ThingRepository.get().findByLocation(this, true, true);
		if (thingsHere.size() > 0) {
			String sep = "";
			StringBuilder listBuilder = new StringBuilder();
			for (Thing thing : thingsHere) {
				listBuilder.append(sep).append(thing.getListCanonical());
				sep = Translations.get("_comma") + Translations.get("_space");
			}
			builder.append("\n")
			       .append(Translations.get("you_see", listBuilder.toString()))
				   .append(Translations.get("_fullstop"));
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
			"id=" + id + ", " + 
			"name=" + TextUtils.denormalize(name) + ", " +
			"status=" + (status != null ? TextUtils.denormalize(status.getLabel()) : "null") +
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
