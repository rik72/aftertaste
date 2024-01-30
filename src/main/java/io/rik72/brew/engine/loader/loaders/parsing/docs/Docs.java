package io.rik72.brew.engine.loader.loaders.parsing.docs;

import java.util.List;

import io.rik72.brew.engine.loader.loaders.parsing.raw.CharacterRaw;
import io.rik72.brew.engine.loader.loaders.parsing.raw.DictionaryRaw;
import io.rik72.brew.engine.loader.loaders.parsing.raw.LocationRaw;
import io.rik72.brew.engine.loader.loaders.parsing.raw.StoryRaw;
import io.rik72.brew.engine.loader.loaders.parsing.raw.ThingRaw;

public class Docs {

	public static class Characters {
		public List<CharacterRaw> characters;
	}

	public static class Locations {
		public List<LocationRaw> locations;
	}

	public static class Story {
		public StoryRaw story;
	}

	public static class Things {
		public List<ThingRaw> things;
	}

	public static class Words {
		public DictionaryRaw words;
	}
}
