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
		DIRECTION,
		_D_ACTION,
		_0_ACTION,
		_1_ACTION,
		_2_ACTION,
		COMMAND,
		NAME,
		PREPOSITION,
		NUMBER,
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
    private Position complement;

    @Column
    private Position supplement;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
    private Word canonical;

	public Word() {
	}

	public Word(String text, Type type) {
		this.text = text;
		this.type = type;
		this.canonical = this;
	}

	public Word(String text, Type type, String canonical) {
		this.text = text;
		this.type = type;
		if (canonical != null)
			setCanonical(canonical);
		else
			this.canonical = this;
	}

	public Word(String text, Type type, String canonical, Position complement, Position supplement) {
		this(text, type, canonical);
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

	public Position getComplement() {
		return complement;
	}

	public void setComplement(Position complement) {
		this.complement = complement;
	}

	public Position getSupplement() {
		return supplement;
	}

	public void setSupplement(Position supplement) {
		this.supplement = supplement;
	}

    @Override
    public String toString() {
        return "{ Word :: " + 
			id + " : " + 
			TextUtils.quote(text) + " : " +
			type + 
			(canonical != null ? " : " + TextUtils.quote(canonical.getText()) : "") +
			(complement != null ? " : " + "complement in " + complement : "") +
			(supplement != null ? " : " + "supplement in " + supplement : "") +
		" }";
    }
}
