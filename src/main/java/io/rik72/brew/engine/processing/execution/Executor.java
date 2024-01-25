package io.rik72.brew.engine.processing.execution;

import java.util.Vector;

import io.rik72.brew.engine.db.entities.Word;

public abstract class Executor {
	protected Vector<Word> words;
	protected boolean toBeConfirmed;

	public Executor(Vector<Word> words, boolean toBeConfirmed) {
		this.words = words;
		this.toBeConfirmed = toBeConfirmed;
	}

	public abstract Results execute() throws Exception;

	public Vector<Word> getWords() {
		return words;
	}

	public boolean isToBeConfirmed() {
		return toBeConfirmed;
	}
}
