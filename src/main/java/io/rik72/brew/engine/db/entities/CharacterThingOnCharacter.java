package io.rik72.brew.engine.db.entities;

import io.rik72.brew.engine.db.entities.abstractions.ConsequenceOnCharacter;
import io.rik72.brew.engine.db.repositories.CharacterRepository;
import io.rik72.brew.engine.db.repositories.CharacterStatusRepository;
import io.rik72.brew.engine.db.repositories.LocationRepository;
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
public class CharacterThingOnCharacter implements AbstractEntity, ConsequenceOnCharacter {
	
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
	private Word preposition;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private ThingStatus supplementStatus;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private CharacterStatus beforeStatus;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private CharacterStatus afterStatus;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private Location toLocation;

	@Column
	Boolean afterVisibility;

	@Column
	String afterText;

	public CharacterThingOnCharacter() {
	}

	public CharacterThingOnCharacter(String action,
							         Character complement, String complementStatusLabel,
							         String preposition,
							   	     Thing supplement, String supplementStatusLabel,
							         String beforeName, String beforeStatusLabel,
							         String afterStatusLabel,
								     String toLocationName,
							     	 Boolean afterVisibility,
							         String afterText) {
		setAction(action);
		setPreposition(preposition);
		setComplementStatus(complement, complementStatusLabel);
		setSupplementStatus(supplement, supplementStatusLabel);
		setBeforeStatus(beforeName, beforeStatusLabel);
		if (afterStatusLabel != null)
			setAfterStatus(afterStatusLabel);
		if (toLocationName != null)
			setToLocation(toLocationName);
		this.afterVisibility = afterVisibility;
		this.afterText = afterText;
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

	public Word getPreposition() {
		return preposition;
	}

	private void setPreposition(String preposition) {
		this.preposition = WordRepository.get().getByText(preposition);
		if (this.preposition == null)
			throw new EntityNotFoundException("Word", preposition);
	}

	public CharacterStatus getComplementStatus() {
		return complementStatus;
	}

	private void setComplementStatus(Character complement, String complementStatusLabel) {
		this.complementStatus = CharacterStatusRepository.get().getByCharacterAndLabel(complement, complementStatusLabel);
		if (this.complementStatus == null)
			throw new EntityNotFoundException("Status", complementStatusLabel, "complement", complement.getName());
	}

	public ThingStatus getSupplementStatus() {
		return supplementStatus;
	}

	private void setSupplementStatus(Thing supplement, String supplementStatusLabel) {
		this.supplementStatus = ThingStatusRepository.get().getByThingAndLabel(supplement, supplementStatusLabel);
		if (this.supplementStatus == null)
			throw new EntityNotFoundException("Status", supplementStatusLabel, "supplement", supplement.getName());
	}

	@Override
	public CharacterStatus getBeforeStatus() {
		return beforeStatus;
	}

	private void setBeforeStatus(String beforeName, String beforeStatusLabel) {
		Character character = CharacterRepository.get().getByName(beforeName);
		this.beforeStatus = CharacterStatusRepository.get().getByCharacterAndLabel(character, beforeStatusLabel);
		if (this.beforeStatus == null)
			throw new EntityNotFoundException("Status", beforeStatusLabel, "thing", beforeName);
	}

	@Override
	public CharacterStatus getAfterStatus() {
		return afterStatus;
	}

	private void setAfterStatus(String afterStatusLabel) {
		Character character = CharacterRepository.get().getByName(beforeStatus.getCharacter().getName());
		this.afterStatus = CharacterStatusRepository.get().getByCharacterAndLabel(character, afterStatusLabel);
		if (this.afterStatus == null)
			throw new EntityNotFoundException("Status", afterStatusLabel, "character", character.getName());
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
    public String toString() {
        return "{ CharacterThingOnCharacter :: " + 
			id + " : " + 
			TextUtils.quote(action.getText()) + " : " + 
			TextUtils.quote(complementStatus.getCharacter().getName()) + " : " + 
			TextUtils.quote(complementStatus.getLabel()) + " : " + 
			TextUtils.quote(preposition.getText()) + " : " + 
			TextUtils.quote(supplementStatus.getThing().getName()) + " : " + 
			TextUtils.quote(supplementStatus.getLabel()) + " : " + 
			TextUtils.quote(beforeStatus.getCharacter().getName()) + " : " + 
			TextUtils.quote(beforeStatus.getLabel()) + " : " + 
			(afterStatus != null ? TextUtils.quote(afterStatus.getLabel()) : "-") + " : " + 
			(toLocation != null ? TextUtils.quote(toLocation.getName()) : "-") + " : " + 
			(afterVisibility != null ? (afterVisibility ? "visible" : "invisible") : "-") + " : " + 
			(afterText != null ? afterText : "-") +
		" }";
	}
}

