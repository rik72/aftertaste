package io.rik72.brew.engine.loader.loaders.parsing.docs;

import io.rik72.brew.engine.db.entities.Word.Type;
import io.rik72.brew.engine.loader.LoadPath;
import io.rik72.brew.engine.loader.Loadable;
import io.rik72.brew.engine.loader.YmlParser;

public class WordsDocLoader implements Loadable {

	private String fileName;
	private YmlParser parser;

	public WordsDocLoader(String fileName) {
		this.fileName = fileName;
		this.parser = new YmlParser(Docs.Words.class);
	}

	@Override
	public void load(LoadPath loadPath) {
		Docs.Words doc = (Docs.Words) parser.parse(loadPath, fileName);
		
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
