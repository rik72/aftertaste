package io.rik72.brew.engine.loader.loaders;

import io.rik72.brew.engine.loader.Loadable;
import io.rik72.brew.engine.loader.Loader;
import io.rik72.brew.engine.loader.loaders.docs.WordsDocLoader;

public class VerbLoader implements Loadable {

	@Override
	public void load() {
		WordsDocLoader loader = new WordsDocLoader("brew/story/verbs.yml");
		loader.load();
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
