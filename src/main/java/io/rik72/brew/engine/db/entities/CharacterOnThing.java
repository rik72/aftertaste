package io.rik72.brew.engine.db.entities;

import io.rik72.brew.engine.db.entities.abstractions.ConsequenceOnThing;
import io.rik72.brew.engine.db.repositories.CharacterStatusRepository;
import io.rik72.brew.engine.db.repositories.LocationRepository;
import io.rik72.brew.engine.db.repositories.TextGroupRepository;
import io.rik72.brew.engine.db.repositories.ThingRepository;
import io.rik72.brew.engine.db.repositories.ThingStatusRepository;
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
public class CharacterOnThing implements AbstractEntity, ConsequenceOnThing {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private short id;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private Word action;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private CharacterStatus complementStatus;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private ThingStatus beforeStatus;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private ThingStatus afterStatus;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private Location toLocation;

	@Column
	Boolean afterVisibility;

	@Column
	String afterText;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
    private TextGroup transition;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
    private TextGroup finale;

	public CharacterOnThing() {
	}

	public CharacterOnThing(String action,
							Character complement, String complementStatusLabel,
							String beforeName, String beforeStatusLabel,
							String afterStatusLabel,
							String toLocationName,
							Boolean afterVisibility,
							String afterText,
							String transition,
							String finale) {
		setAction(action);
		setComplementStatus(complement, complementStatusLabel);
		setBeforeStatus(beforeName, beforeStatusLabel);
		if (afterStatusLabel != null)
			setAfterStatus(afterStatusLabel);
		if (toLocationName != null)
			setToLocation(toLocationName);
		this.afterVisibility = afterVisibility;
		this.afterText = afterText;
		if (transition != null)
			setTransition(transition);
		if (finale != null)
			setFinale(finale);
	}

	@Override
	public short getId() {
		return id;
	}

	public Word getAction() {
		return action;
	}

	private void setAction(String action) {
		this.action = WordRepository.get().getByText(action);
		if (this.action == null)
			throw new EntityNotFoundException("Verb", action);
	}

	public CharacterStatus getComplementStatus() {
		return complementStatus;
	}

	private void setComplementStatus(Character complement, String complementStatusLabel) {
		this.complementStatus = CharacterStatusRepository.get().getByCharacterAndLabel(complement, complementStatusLabel);
		if (this.complementStatus == null)
			throw new EntityNotFoundException("Status", complementStatusLabel, "complement", complement.getName());
	}

	@Override
	public ThingStatus getBeforeStatus() {
		return beforeStatus;
	}

	private void setBeforeStatus(String beforeName, String beforeStatusLabel) {
		Thing thing = ThingRepository.get().getByName(beforeName);
		this.beforeStatus = ThingStatusRepository.get().getByThingAndLabel(thing, beforeStatusLabel);
		if (this.beforeStatus == null)
			throw new EntityNotFoundException("Status", beforeStatusLabel, "thing", beforeName);
	}

	@Override
	public ThingStatus getAfterStatus() {
		return afterStatus;
	}

	private void setAfterStatus(String afterStatusLabel) {
		Thing thing = ThingRepository.get().getByName(beforeStatus.getThing().getName());
		this.afterStatus = ThingStatusRepository.get().getByThingAndLabel(thing, afterStatusLabel);
		if (this.afterStatus == null)
			throw new EntityNotFoundException("Status", afterStatusLabel, "thing", thing.getName());
	}

	private void setToLocation(String toLocationName) {
		this.toLocation = LocationRepository.get().getByName(toLocationName);
		if (this.toLocation == null)
			throw new EntityNotFoundException("Location", toLocationName);
	}

	@Override
	public Location getToLocation() {
		return toLocation;
	}

	@Override
	public Boolean getAfterVisibility() {
		return afterVisibility;
	}

	@Override
	public String getAfterText() {
		return afterText;
	}

	@Override
	public TextGroup getTransition() {
		return transition;
	}

	public void setTransition(String textGroup) {
		this.transition = TextGroupRepository.get().getByName(textGroup);
		if (this.transition == null)
			throw new EntityNotFoundException("TextGroup", textGroup);
	}

	@Override
	public TextGroup getFinale() {
		return finale;
	}

	public void setFinale(String textGroup) {
		this.finale = TextGroupRepository.get().getByName(textGroup);
		if (this.finale == null)
			throw new EntityNotFoundException("TextGroup", textGroup);
	}

    @Override
    public String toString() {
        return "{ CharacterOnThing :: " + 
			"id=" + id + ", " + 
			"action=" + TextUtils.quote(action.getText()) + ", " + 
			"complement=" + TextUtils.quote(complementStatus.getCharacter().getName()) + ", " + 
			"complementStatus=" + TextUtils.quote(complementStatus.getLabel()) + ", " + 
			"before=" + TextUtils.quote(beforeStatus.getThing().getName()) + ", " + 
			"beforeStatus=" + TextUtils.quote(beforeStatus.getLabel()) + ", " + 
			"afterStatus=" + (afterStatus != null ? TextUtils.quote(afterStatus.getLabel()) : null) + ", " + 
			"toLocation=" + (toLocation != null ? TextUtils.quote(toLocation.getName()) : null) + ", " + 
			"afterVisibility=" + (afterVisibility != null ? (afterVisibility ? "visible" : "invisible") : null) + ", " + 
			"afterText=" + afterText + ", " +
			"transition=" + (transition != null ? TextUtils.quote(transition.getName()) : null) + ", " +
			"finale=" + (finale != null ? TextUtils.quote(finale.getName()) : null) +
		" }";
	}
}

