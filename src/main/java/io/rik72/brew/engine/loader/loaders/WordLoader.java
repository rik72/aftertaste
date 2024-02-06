package io.rik72.brew.engine.loader.loaders;

import java.util.List;

import io.rik72.amber.logger.Log;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.repositories.WordRepository;
import io.rik72.brew.engine.loader.LoadPath;
import io.rik72.brew.engine.loader.Loadable;
import io.rik72.brew.engine.loader.Loader;
import io.rik72.brew.engine.loader.loaders.parsing.docs.WordsDocLoader;

public class WordLoader implements Loadable {

	@Override
	public void load(LoadPath loadPath) throws Exception {
		WordsDocLoader loader = new WordsDocLoader("other_names.yml");
		loader.load(loadPath);

		// Additional loading is delegated to other loaders in the same package
	}

	@Override
	public void register() {
		Loader.get().register(this);
	}

	@Override
	public void dump() {
		Log.skip();
		Log.debug("WORDS ==================================================");
		List<Word> words = WordRepository.get().findAll();
		words.forEach(s -> Log.debug(s));
	}
}
