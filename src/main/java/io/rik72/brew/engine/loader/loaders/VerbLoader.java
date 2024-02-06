package io.rik72.brew.engine.loader.loaders;

import io.rik72.brew.engine.loader.LoadPath;
import io.rik72.brew.engine.loader.Loadable;
import io.rik72.brew.engine.loader.Loader;
import io.rik72.brew.engine.loader.loaders.parsing.docs.WordsDocLoader;

public class VerbLoader implements Loadable {

	@Override
	public void load(LoadPath loadPath) throws Exception {
		WordsDocLoader loader = new WordsDocLoader("verbs.yml");
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
