package io.rik72.brew.engine.db.entities;

import io.rik72.brew.engine.db.repositories.CharacterRepository;
import io.rik72.brew.engine.db.repositories.CharacterStatusRepository;
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
public class CharacterStatusPossibility extends AbstractEntity implements Possibility {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private short id;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private CharacterStatus status;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private Word action;

    @Column
	private boolean possible;

    @Column
	private boolean important;

    @Column
	private String feedback;

	public CharacterStatusPossibility() {
	}

	public CharacterStatusPossibility(String characterName, String statusLabel, String action, Boolean possible, Boolean important, String feedback) {
		setStatus(characterName, statusLabel);
		setAction(action);
		this.possible = possible != null ? possible : false;
		this.important = important != null ? important : false;
		this.feedback = feedback;
	}

	@Override
	public short getId() {
		return id;
	}

	public CharacterStatus getStatus() {
		return status;
	}

	public void setStatus(String characterName, String statusLabel) {
		Character character = CharacterRepository.get().getByName(characterName);
		if (character == null)
			throw new EntityNotFoundException("Character", characterName);
		this.status = CharacterStatusRepository.get().getByCharacterAndLabel(character, statusLabel);
		if (this.status == null)
			throw new EntityNotFoundException("Status", statusLabel, "character", characterName);
	}

	public Word getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = WordRepository.get().getByText(action);
		if (this.action == null)
			throw new EntityNotFoundException("Verb", action);
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
        return "{ CharacterStatusPossibility :: " + 
			id + " : " + 
			TextUtils.quote(status.getCharacter().getName()) + " : " + 
			TextUtils.quote(status.getLabel()) + " : " + 
			TextUtils.quote(action.getText()) + " : " + 
			(possible ? "possible" : "impossible") + " : " + 
			(important ? "important" : "-") + " : " + 
			TextUtils.quote(feedback) +
		" }";
    }
}
