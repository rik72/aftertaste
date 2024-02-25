package io.rik72.brew.engine.processing.parsing.tokenize;

import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.entities.Word.EntityType;
import io.rik72.brew.engine.db.entities.Word.Type;
import io.rik72.brew.engine.db.repositories.WordRepository;

public class JapaneseTokenizer implements Tokenizer {

	@Override
	public TokenizeResults tokenize(String str) {
		TokenizeResults results = new TokenizeResults();
		String[] inputTokens = str.split(" ");
		for (int i = 0; i < inputTokens.length; i++) {
			Word word = null;
			if (isShort(inputTokens[i]))
				word = new Word(inputTokens[i], Type.number, EntityType.none);
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
				if (word.getType() == Type.stop_word)
					results.getStopWords().add(word);
				else if (word.getCanonical() != null)
					results.getWords().add(word);
				else
					results.getAmbiguousWords().add(word);
			}
			else {
				results.getUnknownTokens().add(inputTokens[i]);
				results.setToBeConfirmed(true);
			}
		}

		return results;
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
}
