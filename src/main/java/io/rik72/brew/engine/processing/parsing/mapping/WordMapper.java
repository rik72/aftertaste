package io.rik72.brew.engine.processing.parsing.mapping;

import java.util.Vector;

import io.rik72.brew.engine.db.entities.Word;

public interface WordMapper {
	public Word getVerb();
	public Word getComplement();
	public Word getComplementParticle();
	public Word getPreposition();
	public Word getSupplement();
	public Word getSupplementParticle();
	public Vector<Word> getWords();
}
