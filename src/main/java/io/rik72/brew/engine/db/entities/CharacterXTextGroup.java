package io.rik72.brew.engine.db.entities;

import io.rik72.brew.engine.utils.TextUtils;
import io.rik72.mammoth.entities.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class CharacterXTextGroup implements AbstractEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private short id;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private Character character;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private TextGroup textGroup;

	public CharacterXTextGroup() {
	}

	public CharacterXTextGroup(Character character, TextGroup textGroup) {
		this.character = character;
		this.textGroup = textGroup;
	}

	@Override
	public short getId() {
		return id;
	}

	public Character getCharacter() {
		return character;
	}

	public TextGroup getTextGroup() {
		return textGroup;
	}

    @Override
    public String toString() {
        return "{ CharacterXTextGroup :: " + 
			id + " : " + 
			TextUtils.quote(character.getName()) + " : " + 
			TextUtils.quote(textGroup.getName()) +
		" }";
    }
}
