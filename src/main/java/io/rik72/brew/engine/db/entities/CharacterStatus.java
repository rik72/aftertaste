package io.rik72.brew.engine.db.entities;

import io.rik72.brew.engine.db.repositories.CharacterRepository;
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
public class CharacterStatus extends AbstractEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private short id;

    @Column
    private String label;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private Character character;

	public CharacterStatus() {
	}

	public CharacterStatus(String characterName, String label) {
		setCharacter(characterName);
		this.label = label;
	}

	@Override
	public short getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public Character getCharacter() {
		return character;
	}

	public void setCharacter(String characterName) {
		this.character = CharacterRepository.get().getByName(characterName);
		if (this.character == null)
			throw new EntityNotFoundException("Character", characterName);
	}

    @Override
    public String toString() {
        return "{ CharacterStatus :: " + 
			id + " : " + 
			TextUtils.quote(character.getName()) + " : " + 
			TextUtils.quote(label) +
		" }";
    }
}
