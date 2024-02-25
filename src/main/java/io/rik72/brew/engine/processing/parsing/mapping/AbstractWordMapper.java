package io.rik72.brew.engine.processing.parsing.mapping;

import java.util.Vector;

import io.rik72.brew.engine.db.entities.Word;

public abstract class AbstractWordMapper implements WordMapper {
	protected Vector<Word> words;
	
	public AbstractWordMapper(Vector<Word> words) {
		this.words = words;
	}
}
