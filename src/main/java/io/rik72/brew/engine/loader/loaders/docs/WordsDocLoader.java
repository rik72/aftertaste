package io.rik72.brew.engine.loader.loaders.docs;

import io.rik72.brew.engine.db.entities.Word.Type;
import io.rik72.brew.engine.loader.Loadable;
import io.rik72.brew.engine.loader.YmlParser;

public class WordsDocLoader implements Loadable {

	private YmlParser parser;
	private Docs.Words doc;

	public WordsDocLoader(String path) {
		this.parser = new YmlParser(Docs.Words.class);
		this.doc = (Docs.Words) parser.parse(path);
	}

	@Override
	public void load() {
		Helpers.loadWordList(doc.words.names, Type.NAME);
		Helpers.loadWordList(doc.words.prepositions, Type.PREPOSITION);
		Helpers.loadWordList(doc.words.directions, Type.DIRECTION);
		Helpers.loadWordList(doc.words.directionActions, Type._D_ACTION);
		Helpers.loadWordList(doc.words.zeroActions, Type._0_ACTION);
		Helpers.loadWordList(doc.words.oneActions, Type._1_ACTION);
		Helpers.loadWordList(doc.words.twoActions, Type._2_ACTION);
		Helpers.loadWordList(doc.words.commands, Type.COMMAND);
	}

	@Override
	public void register() {
		// NOP
	}

	@Override
	public void dump() {
		// NOP
	}
}
