package io.rik72.brew.engine.db.entities;

import io.rik72.brew.engine.db.entities.abstractions.Status;
import io.rik72.brew.engine.db.repositories.CharacterRepository;
import io.rik72.brew.engine.db.repositories.TextGroupRepository;
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
public class CharacterStatus implements AbstractEntity, Status {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private short id;

    @Column
    private String label;

    @Column
    private String brief;

    @Column(length=1024)
    private String description;

    @Column
    private String canonical;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private Character character;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
    private TextGroup transition;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
    private TextGroup finale;

	public CharacterStatus() {
	}

	public CharacterStatus(String characterName, String label, String brief, String description, String canonical, String transition, String finale) {
		setCharacter(characterName);
		this.label = label;
		this.brief = brief;
		this.description = description;
		this.canonical = canonical;
		if (transition != null)
			setTransition(transition);
		if (finale != null)
			setFinale(finale);
	}

	@Override
	public short getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public String getBrief() {
		return brief;
	}

	public Character getCharacter() {
		return character;
	}

	public void unsetCharacter() {
		this.character = null;
	}

	public void setCharacter(String characterName) {
		this.character = CharacterRepository.get().getByName(characterName);
		if (this.character == null)
			throw new EntityNotFoundException("Character", characterName);
	}

	public TextGroup getTransition() {
		return transition;
	}

	public void setTransition(String textGroup) {
		this.transition = TextGroupRepository.get().getByName(textGroup);
		if (this.transition == null)
			throw new EntityNotFoundException("TextGroup", textGroup);
	}

	public TextGroup getFinale() {
		return finale;
	}

	public void setFinale(String textGroup) {
		this.finale = TextGroupRepository.get().getByName(textGroup);
		if (this.finale == null)
			throw new EntityNotFoundException("TextGroup", textGroup);
	}

	@Override
	public String getDescription() {
		return description;
	}

	public String getCanonical() {
		return canonical;
	}

    @Override
    public String toString() {
        return "{ CharacterStatus :: " + 
			"id=" + id + ", " + 
			"character=" + TextUtils.denormalize(character.getName()) + ", " + 
			"label=" + TextUtils.denormalize(label) + ", " + 
			"canonical=" + TextUtils.denormalize(canonical) + ", " + 
			"brief=" + TextUtils.denormalize(brief) + ", " + 
			"description=" + TextUtils.denormalize(description) + ", " + 
			"transition=" + transition + ", " +
			"finale=" + finale +
		" }";
    }
}
