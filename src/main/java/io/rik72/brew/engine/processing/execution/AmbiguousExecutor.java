package io.rik72.brew.engine.processing.execution;

import java.util.Vector;

import io.rik72.brew.engine.db.entities.Word;

public class AmbiguousExecutor extends Executor {

	private String offendingToken;

	public AmbiguousExecutor(Vector<Word> words, String offendingToken) {
		super(words, false);
		this.offendingToken = offendingToken;
	}

	public Results execute() {
		return new Results(false, false, "\"" + offendingToken + "\" is ambiguous, please specify.");
	}
}
