package io.rik72.brew.engine.db.entities;

import io.rik72.brew.engine.db.repositories.LocationRepository;
import io.rik72.brew.engine.db.repositories.LocationStatusRepository;
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
public class LocationTwoAction implements AbstractEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private short id;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private Word action;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private ThingStatus complementStatus;

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

	public LocationTwoAction(String action,
							      Thing complement, String complementStatusLabel,
							      String preposition,
							   	  Thing supplement, String supplementStatusLabel,
							      String beforeName, String beforeStatusLabel,
							      String afterStatusLabel,
							      String afterText) {
		setAction(action);
		setPreposition(preposition);
		setComplementStatus(complement, complementStatusLabel);
		setSupplementStatus(supplement, supplementStatusLabel);
		setBeforeStatus(beforeName, beforeStatusLabel);
		setAfterStatus(afterStatusLabel);
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

	public ThingStatus getComplementStatus() {
		return complementStatus;
	}

	private void setComplementStatus(Thing complement, String complementStatusLabel) {
		this.complementStatus = ThingStatusRepository.get().getByThingAndLabel(complement, complementStatusLabel);
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

	public LocationStatus getBeforeStatus() {
		return beforeStatus;
	}

	private void setBeforeStatus(String beforeName, String beforeStatusLabel) {
		Location location = LocationRepository.get().getByName(beforeName);
		this.beforeStatus = LocationStatusRepository.get().getByLocationAndLabel(location, beforeStatusLabel);
		if (this.beforeStatus == null)
			throw new EntityNotFoundException("Status", beforeStatusLabel, "thing", beforeName);
	}

	public LocationStatus getAfterStatus() {
		return afterStatus;
	}

	private void setAfterStatus(String afterStatusLabel) {
		Location location = LocationRepository.get().getByName(beforeStatus.getLocation().getName());
		this.afterStatus = LocationStatusRepository.get().getByLocationAndLabel(location, afterStatusLabel);
		if (this.afterStatus == null)
			throw new EntityNotFoundException("Status", afterStatusLabel, "location", location.getName());
	}

	public String getAfterText() {
		return afterText;
	}

    @Override
    public String toString() {
        return "{ Location 1-Action :: " + 
			id + " : " + 
			TextUtils.quote(action.getText()) + " : " + 
			TextUtils.quote(preposition.getText()) + " : " + 
			TextUtils.quote(complementStatus.getThing().getName()) + " : " + 
			TextUtils.quote(complementStatus.getLabel()) + " : " + 
			TextUtils.quote(supplementStatus.getThing().getName()) + " : " + 
			TextUtils.quote(supplementStatus.getLabel()) + " : " + 
			TextUtils.quote(beforeStatus.getLocation().getName()) + " : " + 
			TextUtils.quote(beforeStatus.getLabel()) + " : " + 
			TextUtils.quote(afterStatus.getLabel()) + " : " + 
			(afterText != null ? afterText : "-") +
		" }";
	}
}

