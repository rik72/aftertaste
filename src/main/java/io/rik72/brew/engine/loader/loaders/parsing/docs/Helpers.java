package io.rik72.brew.engine.loader.loaders.parsing.docs;

import java.util.ArrayList;
import java.util.List;

import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.repositories.WordRepository;
import io.rik72.brew.engine.loader.loaders.parsing.raw.WordRaw;
import io.rik72.mammoth.db.DB;

public class Helpers {

	public static void loadWordList(List<WordRaw> section, Word.Type type) {
		if (section != null)
			for (WordRaw word : section)
				loadWord(word, type);
	}

	public static void loadWord(WordRaw wItem, Word.Type type) {
		if (wItem != null && WordRepository.get().getByText(wItem.text) == null) {
			List<Word> words = new ArrayList<>();
			Word word = new Word(wItem.text, type);
			DB.persist(word);
			words.add(word);

			if (wItem.synonyms != null) {
				for (String synonym : wItem.synonyms) {
					Word existingSynonym = WordRepository.get().getByText(synonym);
					if (existingSynonym == null) {
						word = new Word(synonym, type, wItem.text);
						DB.persist(word);
						words.add(word);
					}
					else {
						existingSynonym.unsetCanonical();
						DB.persist(existingSynonym);
					}
				}
			}

			if (wItem.complement != null)
				for (Word w : words)
					w.setComplement(Word.Position.valueOf(wItem.complement));

			if (wItem.supplement != null)
				for (Word w : words)
					w.setSupplement(Word.Position.valueOf(wItem.supplement));
		}
	}

}
