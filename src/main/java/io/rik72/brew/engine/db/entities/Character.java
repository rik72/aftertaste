package io.rik72.brew.engine.db.entities;

import io.rik72.brew.engine.db.delta.CharacterDelta;
import io.rik72.brew.engine.db.entities.abstractions.Complement;
import io.rik72.brew.engine.db.repositories.CharacterStatusRepository;
import io.rik72.brew.engine.db.repositories.CharacterXTextGroupRepository;
import io.rik72.brew.engine.db.repositories.LocationRepository;
import io.rik72.brew.engine.db.repositories.TextGroupRepository;
import io.rik72.brew.engine.utils.TextUtils;
import io.rik72.mammoth.db.DB;
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
public class Character extends Complement implements Deltable {

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

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private Location inventory;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private CharacterStatus status;

	public Character() {
	}

	public Character(String name, String locationName) {
		this.name = Complement.name(name);
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

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
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

	public void unsetStatus() {
		this.status = null;
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
			"id=" + id + ", " + 
			"name=" + TextUtils.denormalize(name) + ", " + 
			"location=" + TextUtils.denormalize(location.getName()) + ", " +
			"visible=" + TextUtils.denormalize(visible) + ", " +
			"status=" + TextUtils.denormalize(status.getLabel()) +
		" }";
    }

	public static String inventory(String name) {
		return Complement.name(name) + ".inventory";
	}

	@Override
	public Delta getDelta() {
		return new CharacterDelta(this);
	}

	public void remember(short textGroupId) {
		TextGroup toBeRemembered = TextGroupRepository.get().getById(textGroupId);
		if (!CharacterXTextGroupRepository.get().existsByCharacterAndTextGroup(this, toBeRemembered)) {
			CharacterXTextGroup newMemory = new CharacterXTextGroup(this, toBeRemembered);
			DB.persist(newMemory);
		}
	}

	public void remember(TextGroup toBeRemembered) {
		if (!CharacterXTextGroupRepository.get().existsByCharacterAndTextGroup(this, toBeRemembered)) {
			CharacterXTextGroup newMemory = new CharacterXTextGroup(this, toBeRemembered);
			DB.persist(newMemory);
		}
	}
}
