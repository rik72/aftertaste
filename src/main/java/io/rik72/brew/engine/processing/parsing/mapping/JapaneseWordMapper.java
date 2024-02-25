package io.rik72.brew.engine.processing.parsing.mapping;

import java.util.Collections;
import java.util.Vector;

import io.rik72.brew.engine.db.entities.Word;

public class JapaneseWordMapper extends AbstractWordMapper {

	public JapaneseWordMapper(Vector<Word> words) {
		super(words);
		Collections.reverse(this.words);
	}

	@Override
	public Word getVerb() {
		return words.get(0);
	}

	@Override
	public Word getComplement() {
		return words.get(2);
	}

	@Override
	public Word getComplementParticle() {
		return words.get(1);
	}

	@Override
	public Word getPreposition() {
		return null;
	}

	@Override
	public Word getSupplement() {
		return words.get(4);
	}

	@Override
	public Word getSupplementParticle() {
		return words.get(3);
	}

	@Override
	public Vector<Word> getWords() {
		return words;
	}
}
