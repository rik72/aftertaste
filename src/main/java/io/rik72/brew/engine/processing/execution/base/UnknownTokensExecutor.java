package io.rik72.brew.engine.processing.execution.base;

import java.util.List;
import java.util.stream.Collectors;

import io.rik72.vati.locale.Translations;

public class UnknownTokensExecutor extends Executor {

	private List<String> offendingTokens;

	public UnknownTokensExecutor(List<String> offendingTokens) {
		super();
		this.offendingTokens = offendingTokens;
	}

	public Results execute() {
		String tokenCommaList = offendingTokens.stream().collect(Collectors.joining(", "));
		return new Results(false, false, Translations.get("cant_understand", tokenCommaList));
	}
}
