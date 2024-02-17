package io.rik72.brew.engine.processing.execution;

import java.util.Vector;

public class AmbiguousTokenExecutor extends Executor {

	private String offendingToken;

	public AmbiguousTokenExecutor(String offendingToken) {
		super(new Vector<>(), false);
		this.offendingToken = offendingToken;
	}

	public Results execute() {
		return new Results(false, false, "\"" + offendingToken + "\" is ambiguous, please specify.");
	}
}
