package io.rik72.brew.engine.loader.loaders.parsing.docs;

import io.rik72.brew.engine.db.entities.Word.EntityType;
import io.rik72.brew.engine.db.entities.Word.Type;
import io.rik72.brew.engine.loader.LoadPath;
import io.rik72.brew.engine.loader.Loadable;
import io.rik72.brew.engine.loader.loaders.parsing.YmlParser;

public class WordsDocLoader implements Loadable {

	private String fileName;
	private YmlParser parser;

	public WordsDocLoader(String fileName) {
		this.fileName = fileName;
		this.parser = new YmlParser(Docs.Words.class);
	}

	@Override
	public void load(LoadPath loadPath) throws Exception {
		Docs.Words doc = (Docs.Words) parser.parse(loadPath, fileName);
		if (doc != null) {
			Helpers.loadWordList(doc.words.names, Type.name, EntityType.none);
			Helpers.loadWordList(doc.words.stopWords, Type.stop_word, EntityType.none);
			Helpers.loadWordList(doc.words.prepositions, Type.preposition, EntityType.none);
			Helpers.loadWordList(doc.words.particles, Type.particle, EntityType.none);
			Helpers.loadWordList(doc.words.directions, Type.direction, EntityType.none);
			Helpers.loadWordList(doc.words.directionActions, Type._d_action, EntityType.none);
			Helpers.loadWordList(doc.words.zeroActions, Type._0_action, EntityType.none);
			Helpers.loadWordList(doc.words.oneActions, Type._1_action, EntityType.none);
			Helpers.loadWordList(doc.words.twoActions, Type._2_action, EntityType.none);
			Helpers.loadWordList(doc.words.commands, Type.command, EntityType.none);
		}
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
