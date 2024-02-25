package io.rik72.brew.engine.db.repositories;

import io.rik72.brew.engine.db.entities.Word;
import io.rik72.mammoth.repositories.AbstractRepository;
import io.rik72.vati.locale.Translations;

public class WordRepository extends AbstractRepository<Word> {

	private WordRepository() {
		super(Word.class);
	}

	public Word getByText(String value) {
		return getByField("text", value);
	}

	public Word getByTokens(String... tokens) {
		return getByField("text", String.join(Translations.get("_space"), tokens));
	}

	///////////////////////////////////////////////////////////////////////////
	private static WordRepository instance = new WordRepository();
	public static WordRepository get() {
		return instance;
	}
}
