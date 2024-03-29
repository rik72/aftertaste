package io.rik72.brew.engine.db.entities;

import io.rik72.brew.engine.db.entities.abstractions.ConsequenceOnLocation;
import io.rik72.brew.engine.db.repositories.CharacterStatusRepository;
import io.rik72.brew.engine.db.repositories.LocationRepository;
import io.rik72.brew.engine.db.repositories.LocationStatusRepository;
import io.rik72.brew.engine.db.repositories.TextGroupRepository;
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
public class CharacterThingOnLocation implements AbstractEntity, ConsequenceOnLocation {
	
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
	private LocationStatus beforeStatus;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private LocationStatus afterStatus;

	@Column
	String afterText;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
    private TextGroup transition;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
    private TextGroup finale;

	public CharacterThingOnLocation() {
	}

	public CharacterThingOnLocation(String action,
							        Character complement, String complementStatusLabel,
							        String preposition,
							   	    Thing supplement, String supplementStatusLabel,
							        String beforeName, String beforeStatusLabel,
							        String afterStatusLabel,
							        String afterText,
									String transition,
									String finale) {
		setAction(action);
		setPreposition(preposition);
		setComplementStatus(complement, complementStatusLabel);
		setSupplementStatus(supplement, supplementStatusLabel);
		setBeforeStatus(beforeName, beforeStatusLabel);
		setAfterStatus(afterStatusLabel);
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
	public LocationStatus getBeforeStatus() {
		return beforeStatus;
	}

	private void setBeforeStatus(String beforeName, String beforeStatusLabel) {
		Location location = LocationRepository.get().getByName(beforeName);
		this.beforeStatus = LocationStatusRepository.get().getByLocationAndLabel(location, beforeStatusLabel);
		if (this.beforeStatus == null)
			throw new EntityNotFoundException("Status", beforeStatusLabel, "thing", beforeName);
	}

	@Override
	public LocationStatus getAfterStatus() {
		return afterStatus;
	}

	private void setAfterStatus(String afterStatusLabel) {
		Location location = LocationRepository.get().getByName(beforeStatus.getLocation().getName());
		this.afterStatus = LocationStatusRepository.get().getByLocationAndLabel(location, afterStatusLabel);
		if (this.afterStatus == null)
			throw new EntityNotFoundException("Status", afterStatusLabel, "location", location.getName());
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
        return "{ CharacterThingOnLocation :: " + 
			"id=" + id + ", " + 
			"action=" + TextUtils.quote(action.getText()) + ", " + 
			"preposition=" + TextUtils.quote(preposition.getText()) + ", " + 
			"complement=" + TextUtils.quote(complementStatus.getCharacter().getName()) + ", " + 
			"complementStatus=" + TextUtils.quote(complementStatus.getLabel()) + ", " + 
			"supplement=" + TextUtils.quote(supplementStatus.getThing().getName()) + ", " + 
			"supplementStatus=" + TextUtils.quote(supplementStatus.getLabel()) + ", " + 
			"before=" + TextUtils.quote(beforeStatus.getLocation().getName()) + ", " + 
			"beforeStatus=" + TextUtils.quote(beforeStatus.getLabel()) + ", " + 
			"afterStatus=" + (afterStatus != null ? TextUtils.quote(afterStatus.getLabel()) : null) + ", " + 
			"afterText=" + TextUtils.quote(afterText) + ", " + 
			"transition=" + (transition != null ? TextUtils.quote(transition.getName()) : null) + ", " +
			"finale=" + (finale != null ? TextUtils.quote(finale.getName()) : null) +
		" }";
	}
}

