package io.rik72.brew.engine.processing.parsing.mapping;

import java.util.Vector;

import io.rik72.brew.engine.db.entities.Word;
import io.rik72.vati.core.VatiLocale;

public class WordMap {

	protected WordMapper mapper;

	public WordMap(Vector<Word> words) {
		switch (VatiLocale.getCurrent().getParseLocale()) {
			case ASCII:
				this.mapper = new AsciiWordMapper(words);
				break;

			case JAPANESE:
				this.mapper = new JapaneseWordMapper(words);
				break;
		}
	}

	public Word getVerb() {
		return mapper.getVerb();
	}

	public Word getComplement() {
		return mapper.getComplement();
	}

	public Word getComplementParticle() {
		return mapper.getComplementParticle();
	}

	public Word getPreposition() {
		return mapper.getPreposition();
	}

	public Word getSupplement() {
		return mapper.getSupplement();
	}

	public Word getSupplementParticle() {
		return mapper.getSupplementParticle();
	}

	public Vector<Word> getWords() {
		return mapper.getWords();
	}	
}
