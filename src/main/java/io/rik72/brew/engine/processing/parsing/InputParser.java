package io.rik72.brew.engine.processing.parsing;

import java.util.Vector;

import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.repositories.WordRepository;
import io.rik72.brew.engine.processing.execution.Executor;
import io.rik72.brew.engine.processing.execution.ExecutorFactory;
import io.rik72.mammoth.MultipleResultsException;

public class InputParser {

	private Vector<Word> tokens;
	private boolean toBeConfirmed;

	private InputParser() {}

	public Executor parse(String str) throws Exception {
		try {
			tokenize(str);
			return ExecutorFactory.get(tokens, toBeConfirmed);
		}
		catch (MultipleResultsException ex) {
			return ExecutorFactory.getAmbiguousExecutor(tokens, ex.getOffendingValue());
		}
	}

	private void tokenize(String str) {
		toBeConfirmed = false;
		tokens = new Vector<>();
		// for (String token : str.split(" ")) {
		String[] inputTokens = str.split(" ");
		for (int i = 0; i < inputTokens.length; i++) {
			Word word = null;
			if (isShort(inputTokens[i]))
				word = new Word(inputTokens[i], Word.Type.NUMBER);
			else if (i < inputTokens.length - 2) {
				word = WordRepository.get().getByTokens(inputTokens[i], inputTokens[i+1], inputTokens[i+2]);
				if (word == null) {
					word = WordRepository.get().getByTokens(inputTokens[i], inputTokens[i+1]);
					if (word == null) {
						word = WordRepository.get().getByTokens(inputTokens[i]);
					}
					else {
						i += 1;
					}
				}
				else {
					i += 2;
				}
			}
			else if (i < inputTokens.length - 1) {
				word = WordRepository.get().getByTokens(inputTokens[i], inputTokens[i+1]);
				if (word == null) {
					word = WordRepository.get().getByTokens(inputTokens[i]);
				}
				else {
					i += 1;
				}
			}
			else {
				word = WordRepository.get().getByTokens(inputTokens[i]);
			}
			if (word != null) {
				if (word.getCanonical() != null)
					tokens.add(word);
				else
					throw new MultipleResultsException("Ambiguous name: " + word.getText(), word.getText());
			}
			else {
				// ignoring a word, needs confirm...
				toBeConfirmed = true;
			}
		}
	}

	private boolean isShort(String str) { 
		try {  
			short n = Short.parseShort(str);  
			return n > 0;
		}
		catch(NumberFormatException e) {  
			return false;  
		}  
	}

	///////////////////////////////////////////////////////////////////////////
	private static InputParser instance = new InputParser();
	public static InputParser get() {
		return instance;
	}
}
