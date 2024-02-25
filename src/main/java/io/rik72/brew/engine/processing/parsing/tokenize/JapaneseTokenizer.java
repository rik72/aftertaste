package io.rik72.brew.engine.processing.parsing.tokenize;

import java.util.Arrays;

import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.entities.Word.EntityType;
import io.rik72.brew.engine.db.entities.Word.Type;
import io.rik72.brew.engine.db.repositories.WordRepository;

public class JapaneseTokenizer implements Tokenizer {

	@Override
	public TokenizeResults tokenize(String str) {
		TokenizeResults results = new TokenizeResults();
		String[] inputTokens = str.split("");
		for (int i = 0; i < inputTokens.length; i++) {

			Word word = null;
			if (isShort(inputTokens[i]))
				word = new Word(inputTokens[i], Type.number, EntityType.none);
			else {
				WordToken wordToken = lookAheadParse(inputTokens, i);
				if (wordToken != null) {
					word = wordToken.getWord();
					i = wordToken.getEndPosition();
				}
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

	private WordToken lookAheadParse(String[] inputTokens, int startPos) {
		WordToken wordToken = null;

		for (int endPos = inputTokens.length - 1; endPos >= endPos; endPos--) {
			String[] tokens = Arrays.copyOfRange(inputTokens, startPos, endPos + 1);
			Word word = WordRepository.get().getByTokens(tokens);
			if (word != null)
				return new WordToken(word, endPos);
		}

		return wordToken;
	}

	private static class WordToken {
		private Word word;
		private int endPosition;

		private WordToken(Word word, int endPosition) {
			this.word = word;
			this.endPosition = endPosition;
		}

		public int getEndPosition() {
			return endPosition;
		}
		
		public Word getWord() {
			return word;
		}
	}
}
