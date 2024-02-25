package io.rik72.brew.engine.processing.parsing.tokenize;

import java.util.List;
import java.util.Vector;

import io.rik72.brew.engine.db.entities.Word;

public class TokenizeResults {
	private boolean toBeConfirmed = false;
	private Vector<Word> tokens = new Vector<>();
	private Vector<Word> ambiguousWords = new Vector<>();
	private Vector<Word> stopWords = new Vector<>();
	private List<String> unknownTokens = new Vector<>();

	public boolean isToBeConfirmed() {
		return toBeConfirmed;
	}

	public void setToBeConfirmed(boolean toBeConfirmed) {
		this.toBeConfirmed = toBeConfirmed;
	}

	public Vector<Word> getAmbiguousWords() {
		return ambiguousWords;
	}

	public Vector<Word> getStopWords() {
		return stopWords;
	}

	public Vector<Word> getWords() {
		return tokens;
	}

	public List<String> getUnknownTokens() {
		return unknownTokens;
	}
}
