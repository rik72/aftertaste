package io.rik72.brew.engine.processing.parsing.mapping;

import java.util.Vector;

import io.rik72.brew.engine.db.entities.Word;

public class AsciiWordMapper extends AbstractWordMapper {

	public AsciiWordMapper(Vector<Word> words) {
		super(words);
	}

	@Override
	public Word getVerb() {
		return words.get(0);
	}

	@Override
	public Word getComplement() {
		return words.get(1);
	}

	@Override
	public Word getComplementParticle() {
		return null;
	}

	@Override
	public Word getPreposition() {
		return words.get(2);
	}

	@Override
	public Word getSupplement() {
		return words.get(3);
	}

	@Override
	public Word getSupplementParticle() {
		return null;
	}

	@Override
	public Vector<Word> getWords() {
		return words;
	}
}
