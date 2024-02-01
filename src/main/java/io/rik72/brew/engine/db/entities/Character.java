package io.rik72.brew.engine.db.entities;

import io.rik72.brew.engine.db.delta.CharacterDelta;
import io.rik72.brew.engine.db.entities.abstractions.Complement;
import io.rik72.brew.engine.db.repositories.CharacterStatusRepository;
import io.rik72.brew.engine.db.repositories.LocationRepository;
import io.rik72.brew.engine.utils.TextUtils;
import io.rik72.mammoth.delta.Delta;
import io.rik72.mammoth.delta.Deltable;
import io.rik72.mammoth.exceptions.EntityNotFoundException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Character implements Complement, Deltable {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private short id;

    @Column
    private String name;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private Location location;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private Location inventory;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private CharacterStatus status;

	public Character() {
	}

	public Character(String name, String locationName) {
		this.name = name;
		setLocation(locationName);
		setInventory();
	}

	@Override
	public short getId() {
		return id;
	}

	private void setInventory() {
		this.inventory = LocationRepository.get().getByName(inventory(name));
		if (this.inventory == null)
			throw new EntityNotFoundException("Character inventory location", inventory(name));
	}

	@Override
	public String getName() {
		return name;
	}

	public Location getLocation() {
		return location;
	}

	public Location getInventory() {
		return inventory;
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

	@Override
	public CharacterStatus getStatus() {
		return status;
	}

	public void setStatus(CharacterStatus status) {
		this.status = status;
	}

	public void setStatus(String statusLabel) {
		this.status = CharacterStatusRepository.get().getByCharacterAndLabel(this, statusLabel);
		if (this.status == null)
			throw new EntityNotFoundException("Status", statusLabel, "character", name);
	}

	@Override
	public boolean isPlural() {
		return false;
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
    public String toString() {
        return "{ Character :: " + 
			id + " : " + 
			TextUtils.quote(name) + " : " + 
			TextUtils.quote(status.getLabel()) + " : " + 
			TextUtils.quote(location.getName()) +
		" }";
    }

	public static String inventory(String name) {
		return name + " inventory";
	}

	@Override
	public Delta getDelta() {
		return new CharacterDelta(this);
	}
}
