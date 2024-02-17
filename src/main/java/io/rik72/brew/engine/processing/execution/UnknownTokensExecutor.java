package io.rik72.brew.engine.processing.execution;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class UnknownTokensExecutor extends Executor {

	private List<String> offendingTokens;

	public UnknownTokensExecutor(List<String> offendingTokens) {
		super(new Vector<>(), false);
		this.offendingTokens = offendingTokens;
	}

	public Results execute() {
		String tokenCommaList = offendingTokens.stream().collect(Collectors.joining(", "));
		return new Results(false, false, "I do not understand the following words: " + tokenCommaList);
	}
}
