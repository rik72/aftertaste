package io.rik72.brew.engine.loader.loaders;

import io.rik72.brew.engine.loader.LoadPath;
import io.rik72.brew.engine.loader.Loadable;
import io.rik72.brew.engine.loader.Loader;
import io.rik72.brew.engine.loader.loaders.docs.WordsDocLoader;

public class PrepositionLoader implements Loadable {

	@Override
	public void load(LoadPath loadPath) {
		WordsDocLoader loader = new WordsDocLoader("prepositions.yml");
		loader.load(loadPath);
	}

	@Override
	public void register() {
		Loader.get().register(this);
	}

	@Override
	public void dump() {
		// Delegated to io.rik72.adventure.story.loader.WordLoader.java
	}
}
