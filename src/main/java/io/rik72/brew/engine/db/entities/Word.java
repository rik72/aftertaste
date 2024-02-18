package io.rik72.brew.engine.db.entities;

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
public class Word implements AbstractEntity {

	public static enum Type {
		command,
		direction,
		_d_action,
		_0_action,
		_1_action,
		_2_action,
		entity,
		name,
		number,
		preposition,
		particle,
		stop_word,
	}

	public static enum EntityType {
		none,
		character,
		location,
		thing,
	}

	public static enum Position {
		inventory,
		location,
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private short id;

    @Column
    private String text;

    @Column
    private Type type;

    @Column
    private EntityType entityType;

    @Column
    private Position complement;

    @Column
    private Position supplement;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
    private Word canonical;

	public Word() {
	}

	public Word(String text, Type type, EntityType entityType) {
		this.text = text;
		this.type = type;
		this.entityType = entityType;
		this.canonical = this;
	}

	public Word(String text, Type type, EntityType entityType, String canonical) {
		this.text = text;
		this.type = type;
		this.entityType = entityType;
		if (canonical != null)
			setCanonical(canonical);
		else
			this.canonical = this;
	}

	public Word(String text, Type type, EntityType entityType, String canonical, Position complement, Position supplement) {
		this(text, type, entityType, canonical);
		this.complement = complement;
		this.supplement = supplement;
	}

	@Override
	public short getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public Type getType() {
		return type;
	}

	public EntityType getEntityType() {
		return entityType;
	}

	public Word getCanonical() {
		return canonical;
	}

	public void setCanonical(String text) {
		this.canonical = WordRepository.get().getByText(text);
		if (this.canonical == null)
			throw new EntityNotFoundException("Word", text);
	}

	public void unsetCanonical() {
		this.canonical = null;
	}

	public Position getComplementPosition() {
		return complement;
	}

	public void setComplementPosition(Position complement) {
		this.complement = complement;
	}

	public Position getSupplementPosition() {
		return supplement;
	}

	public void setSupplementPosition(Position supplement) {
		this.supplement = supplement;
	}

    @Override
    public String toString() {
        return "{ Word :: " + 
			"id=" + id + ", " + 
			"text=" + TextUtils.denormalize(text) + ", " +
			"type=" + type + 
			(entityType != EntityType.none ? ", entityType=" + entityType : "") +
			(canonical != null ? ", canonical=" + TextUtils.quote(canonical.getText()) : "") +
			(complement != null ? ", compl. position=" + complement : "") +
			(supplement != null ? ", suppl. position=" + supplement : "") +
		" }";
    }
}
