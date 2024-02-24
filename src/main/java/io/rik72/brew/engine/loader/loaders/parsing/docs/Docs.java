package io.rik72.brew.engine.loader.loaders.parsing.docs;

import java.util.List;

import io.rik72.brew.engine.loader.loaders.parsing.raw.CharacterRaw;
import io.rik72.brew.engine.loader.loaders.parsing.raw.DictionaryRaw;
import io.rik72.brew.engine.loader.loaders.parsing.raw.LocationRaw;
import io.rik72.brew.engine.loader.loaders.parsing.raw.StoryRaw;
import io.rik72.brew.engine.loader.loaders.parsing.raw.TextRaw;
import io.rik72.brew.engine.loader.loaders.parsing.raw.ThingRaw;

public class Docs {

	public static interface Mergeable {
		public void merge(Mergeable other);
	}

	public static class Characters implements Mergeable {
		public List<CharacterRaw> characters;

		@Override
		public void merge(Mergeable other) {
			if (characters != null) {
				if (((Characters)other).characters != null)
					characters.addAll(((Characters)other).characters);
			}
			else
				characters = ((Characters)other).characters;
		}
	}

	public static class Locations implements Mergeable {
		public List<LocationRaw> locations;

		@Override
		public void merge(Mergeable other) {
			if (locations != null) {
				if (((Locations)other).locations != null)
					locations.addAll(((Locations)other).locations);
			}
			else
				locations = ((Locations)other).locations;
		}
	}

	public static class Story implements Mergeable {
		public StoryRaw story;

		@Override
		public void merge(Mergeable other) {
			throw new UnsupportedOperationException("Story must be specified in only one story.yml file");
		}
	}

	public static class Things implements Mergeable {
		public List<ThingRaw> things;

		@Override
		public void merge(Mergeable other) {
			if (things != null) {
				if (((Things)other).things != null)
					things.addAll(((Things)other).things);
			}
			else
				things = ((Things)other).things;
		}
	}

	public static class Texts implements Mergeable {
		public List<TextRaw> texts;

		@Override
		public void merge(Mergeable other) {
			if (texts != null) {
				if (((Texts)other).texts != null)
					texts.addAll(((Texts)other).texts);
			}
			else
				texts = ((Texts)other).texts;
		}
	}

	public static class Words implements Mergeable {
		public DictionaryRaw words;

		@Override
		public void merge(Mergeable other) {
			if (words.commands != null) {
				if (((Words)other).words.commands != null)
					words.commands.addAll(((Words)other).words.commands);
			}
			else
				words.commands = ((Words)other).words.commands;

			if (words.directionActions != null) {
				if (((Words)other).words.directionActions != null)
					words.directionActions.addAll(((Words)other).words.directionActions);
			}
			else
				words.directionActions = ((Words)other).words.directionActions;

			if (words.directions != null) {
				if (((Words)other).words.directions != null)
					words.directions.addAll(((Words)other).words.directions);
			}
			else
				words.directions = ((Words)other).words.directions;

			if (words.names != null) {
				if (((Words)other).words.names != null)
					words.names.addAll(((Words)other).words.names);
			}
			else
				words.names = ((Words)other).words.names;

			if (words.oneActions != null) {
				if (((Words)other).words.oneActions != null)
					words.oneActions.addAll(((Words)other).words.oneActions);
			}
			else
				words.oneActions = ((Words)other).words.oneActions;

			if (words.prepositions != null) {
				if (((Words)other).words.prepositions != null)
					words.prepositions.addAll(((Words)other).words.prepositions);
			}
			else
				words.prepositions = ((Words)other).words.prepositions;

			if (words.particles != null) {
				if (((Words)other).words.particles != null)
					words.particles.addAll(((Words)other).words.particles);
			}
			else
				words.particles = ((Words)other).words.particles;

			if (words.stopWords != null) {
				if (((Words)other).words.stopWords != null)
					words.stopWords.addAll(((Words)other).words.stopWords);
			}
			else
				words.stopWords = ((Words)other).words.stopWords;

			if (words.twoActions != null) {
				if (((Words)other).words.twoActions != null)
					words.twoActions.addAll(((Words)other).words.twoActions);
			}
			else
				words.twoActions = ((Words)other).words.twoActions;

			if (words.zeroActions != null) {
				if (((Words)other).words.zeroActions != null)
					words.zeroActions.addAll(((Words)other).words.zeroActions);
			}
			else
				words.zeroActions = ((Words)other).words.zeroActions;
		}
	}
}
