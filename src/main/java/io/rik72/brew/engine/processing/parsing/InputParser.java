package io.rik72.brew.engine.processing.parsing;

import io.rik72.brew.engine.processing.execution.Executor;
import io.rik72.brew.engine.processing.execution.ExecutorFactory;
import io.rik72.brew.engine.processing.parsing.tokenize.AsciiTokenizer;
import io.rik72.brew.engine.processing.parsing.tokenize.JapaneseTokenizer;
import io.rik72.brew.engine.processing.parsing.tokenize.TokenizeResults;
import io.rik72.brew.engine.processing.parsing.tokenize.Tokenizer;
import io.rik72.mammoth.MultipleResultsException;
import io.rik72.vati.parselocale.ParseLocalized;

public class InputParser {

	ParseLocalized<Tokenizer> tokenizer = new ParseLocalized<>("InputParser.tokenizer",
		new AsciiTokenizer(), new JapaneseTokenizer());

	private InputParser() {}

	public Executor parse(String str) throws Exception {
		try {
			TokenizeResults results = tokenizer.get().tokenize(str);
			if (results.getUnknownTokens().size() > 0)
				return ExecutorFactory.getUnknownTokensExecutor(results.getUnknownTokens());
			if (results.getAmbiguousWords().size() > 0)
				return ExecutorFactory.getAmbiguousTokenExecutor(results.getAmbiguousWords().get(0).getText());
			return ExecutorFactory.get(results.getWords(), results.isToBeConfirmed());
		}
		catch (MultipleResultsException ex) {
			return ExecutorFactory.getAmbiguousTokenExecutor(ex.getOffendingValue());
		}
	}

	///////////////////////////////////////////////////////////////////////////
	private static InputParser instance = new InputParser();
	public static InputParser get() {
		return instance;
	}
}
