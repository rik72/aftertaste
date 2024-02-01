package io.rik72.brew.engine.db.entities;

import io.rik72.brew.engine.db.entities.abstractions.Status;
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
public class CharacterStatus extends AbstractEntity implements Status {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private short id;

    @Column
    private String label;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
	private Character character;

    @Column
    private String finale;

	public CharacterStatus() {
	}

	public CharacterStatus(String characterName, String label, String finale) {
		setCharacter(characterName);
		this.label = label;
		this.finale = finale;
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

	public String getFinale() {
		return finale;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getDescription'");
	}

    @Override
    public String toString() {
        return "{ CharacterStatus :: " + 
			id + " : " + 
			TextUtils.quote(character.getName()) + " : " + 
			TextUtils.quote(label) +
			(finale != null && finale.length() > 0 ? (" : " + TextUtils.quote(finale)) : "") +
		" }";
    }
}
