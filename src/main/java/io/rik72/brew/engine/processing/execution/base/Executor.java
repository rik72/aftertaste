package io.rik72.brew.engine.processing.execution.base;

import java.util.Vector;

import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.processing.parsing.mapping.WordMap;

public abstract class Executor {
	protected WordMap wordMap;
	protected boolean toBeConfirmed;

	protected Executor() {}

	public Executor(WordMap wordMap, boolean toBeConfirmed) {
		this.wordMap = wordMap;
		this.toBeConfirmed = toBeConfirmed;
	}

	public abstract Results execute() throws Exception;

	public Vector<Word> getWords() {
		return wordMap.getWords();
	}

	public boolean isToBeConfirmed() {
		return toBeConfirmed;
	}
}
