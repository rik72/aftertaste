package io.rik72.brew.engine.db.entities;

import io.rik72.brew.engine.db.entities.abstractions.Possibility;
import io.rik72.brew.engine.db.repositories.LocationRepository;
import io.rik72.brew.engine.db.repositories.LocationStatusRepository;
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
public class LocationStatusPossibility implements AbstractEntity, Possibility {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private short id;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private LocationStatus status;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private Word action;

    @Column
	private boolean possible;

    @Column
	private boolean important;

    @Column
	private String feedback;

	public LocationStatusPossibility() {
	}

	public LocationStatusPossibility(String locationName, String statusLabel, String action, Boolean possible, Boolean important, String feedback) {
		setStatus(locationName, statusLabel);
		setAction(action);
		this.possible = possible != null ? possible : false;
		this.important = important != null ? important : false;
		this.feedback = feedback;
	}

	@Override
	public short getId() {
		return id;
	}

	public LocationStatus getStatus() {
		return status;
	}

	public void setStatus(String locationName, String statusLabel) {
		Location location = LocationRepository.get().getByName(locationName);
		this.status = LocationStatusRepository.get().getByLocationAndLabel(location, statusLabel);
		if (this.status == null)
			throw new EntityNotFoundException("Status", statusLabel, "location", locationName);
	}

	public Word getAction() {
		return action;
	}

	public void setAction(String verb) {
		this.action = WordRepository.get().getByText(verb);
		if (this.action == null)
			throw new EntityNotFoundException("Verb", verb);
	}

	public boolean isPossible() {
		return possible;
	}

	public void setPossible(boolean possible) {
		this.possible = possible;
	}

	public boolean isImportant() {
		return important;
	}

	public void setImportant(boolean important) {
		this.important = important;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

    @Override
    public String toString() {
        return "{ LocationStatusPossibility :: " + 
			id + " : " + 
			TextUtils.quote(status.getLocation().getName()) + " : " + 
			TextUtils.quote(status.getLabel()) + " : " + 
			TextUtils.quote(action.getText()) + " : " + 
			(possible ? "possible" : "impossible") + " : " + 
			(important ? "important" : "-") + " : " + 
			TextUtils.quote(feedback) +
		" }";
    }
}
