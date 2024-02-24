package io.rik72.brew.engine.loader.loaders;

import java.util.List;

import io.rik72.amber.logger.Log;
import io.rik72.brew.engine.db.entities.Text;
import io.rik72.brew.engine.db.entities.TextGroup;
import io.rik72.brew.engine.db.repositories.TextGroupRepository;
import io.rik72.brew.engine.db.repositories.TextRepository;
import io.rik72.brew.engine.loader.LoadPath;
import io.rik72.brew.engine.loader.Loadable;
import io.rik72.brew.engine.loader.Loader;
import io.rik72.brew.engine.loader.YmlParser;
import io.rik72.brew.engine.loader.loaders.parsing.docs.Docs;
import io.rik72.brew.engine.loader.loaders.parsing.parsers.Parser;
import io.rik72.brew.engine.loader.loaders.parsing.raw.TextRaw;
import io.rik72.mammoth.db.DB;

public class TextLoader implements Loadable {

	@Override
	public void load(LoadPath loadPath) throws Exception {

		YmlParser parser = new YmlParser(Docs.Texts.class);
		Docs.Texts doc = (Docs.Texts) parser.parse(loadPath, "texts.yml");

		// insert words for things
		for (TextRaw tItem : doc.texts) {
			Parser.checkNotEmpty("text name", tItem.name);
			Parser.checkNotEmpty("text chunks", tItem.chunks);
			TextGroup group = new TextGroup(tItem.name);
			DB.persist(group);
			int position = 0;
			for (String chunk : tItem.chunks) {
				Text text = new Text(group, position++, chunk);
				DB.persist(text);
			}
		}
	}

	@Override
	public void register() {
		Loader.get().register(this);
	}

	@Override
	public void dump() {
		Log.skip();
		Log.debug("TEXT GROUPS ============================================");
		List<TextGroup> groups = TextGroupRepository.get().findAll();
		groups.forEach(s -> Log.debug(s));
		Log.skip();
		Log.debug("TEXTS ==================================================");
		List<Text> texts = TextRepository.get().findAll();
		texts.forEach(s -> Log.debug(s));
	}
}
