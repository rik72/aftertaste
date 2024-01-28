package io.rik72.brew.engine.loader.loaders;

import java.util.List;

import io.rik72.amber.logger.Log;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.repositories.WordRepository;
import io.rik72.brew.engine.loader.LoadPath;
import io.rik72.brew.engine.loader.Loadable;
import io.rik72.brew.engine.loader.Loader;
import io.rik72.brew.engine.loader.YmlParser;
import io.rik72.brew.engine.loader.loaders.docs.Docs;
import io.rik72.brew.engine.story.Story;
import io.rik72.brew.engine.story.StoryDescriptor;

public class StoryLoader implements Loadable {

	@Override
	public void load(LoadPath loadPath) {
		YmlParser parser = new YmlParser(Docs.Story.class);
		Docs.Story doc = (Docs.Story) parser.parse(loadPath, "story.yml");
		
		Story.get().clear();
		Story.get().setDescriptor(StoryDescriptor.load(loadPath));
		for (String item : doc.story.intro)
			Story.get().getIntro().add(item);
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
