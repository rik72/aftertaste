package io.rik72.brew.engine.db.entities;

import io.rik72.brew.engine.db.entities.abstractions.ConsequenceOnLocation;
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
public class ThingOnLocation implements AbstractEntity, ConsequenceOnLocation {
	
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

	public ThingOnLocation() {
	}

	public ThingOnLocation(String action,
						   Thing complement, String complementStatusLabel,
						   String beforeName, String beforeStatusLabel,
						   String afterStatusLabel,
						   String afterText,
						   String transition,
						   String finale) {
		setAction(action);
		setComplementStatus(complement, complementStatusLabel);
		setBeforeStatus(beforeName, beforeStatusLabel);
		setAfterStatus(afterStatusLabel);
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

	public ThingStatus getComplementStatus() {
		return complementStatus;
	}

	private void setComplementStatus(Thing complement, String complementStatusLabel) {
		this.complementStatus = ThingStatusRepository.get().getByThingAndLabel(complement, complementStatusLabel);
		if (this.complementStatus == null)
			throw new EntityNotFoundException("Status", complementStatusLabel, "complement", complement.getName());
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
        return "{ ThingOnLocation :: " + 
			"id=" + id + ", " + 
			"action=" + TextUtils.quote(action.getText()) + ", " + 
			"complement=" + TextUtils.quote(complementStatus.getThing().getName()) + ", " + 
			"complementStatus=" + TextUtils.quote(complementStatus.getLabel()) + ", " + 
			"before=" + TextUtils.quote(beforeStatus.getLocation().getName()) + ", " + 
			"beforeStatus=" + TextUtils.quote(beforeStatus.getLabel()) + ", " + 
			"afterStatus=" + (afterStatus != null ? TextUtils.quote(afterStatus.getLabel()) : null) + ", " + 
			"afterText=" + TextUtils.quote(afterText) + ", " + 
			"transition=" + (transition != null ? TextUtils.quote(transition.getName()) : null) + ", " +
			"finale=" + (finale != null ? TextUtils.quote(finale.getName()) : null) +
		" }";
	}
}

