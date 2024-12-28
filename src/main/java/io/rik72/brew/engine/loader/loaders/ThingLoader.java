package io.rik72.brew.engine.loader.loaders;

import java.util.List;

import io.rik72.amber.logger.Log;
import io.rik72.brew.engine.db.entities.Thing;
import io.rik72.brew.engine.db.entities.ThingStatus;
import io.rik72.brew.engine.db.entities.ThingStatusPossibility;
import io.rik72.brew.engine.db.entities.Word.EntityType;
import io.rik72.brew.engine.db.entities.Word.Type;
import io.rik72.brew.engine.db.repositories.ThingRepository;
import io.rik72.brew.engine.db.repositories.ThingStatusPossibilityRepository;
import io.rik72.brew.engine.db.repositories.ThingStatusRepository;
import io.rik72.brew.engine.loader.LoadPath;
import io.rik72.brew.engine.loader.Loadable;
import io.rik72.brew.engine.loader.Loader;
import io.rik72.brew.engine.loader.loaders.parsing.YmlParser;
import io.rik72.brew.engine.loader.loaders.parsing.docs.Docs;
import io.rik72.brew.engine.loader.loaders.parsing.docs.Helpers;
import io.rik72.brew.engine.loader.loaders.parsing.parsers.Parser;
import io.rik72.brew.engine.loader.loaders.parsing.parsers.Possibility;
import io.rik72.brew.engine.loader.loaders.parsing.parsers.exceptions.IllegalParseException;
import io.rik72.brew.engine.loader.loaders.parsing.raw.PossibilityRaw;
import io.rik72.brew.engine.loader.loaders.parsing.raw.ThingRaw;
import io.rik72.brew.engine.loader.loaders.parsing.raw.ThingStatusRaw;
import io.rik72.mammoth.db.DB;

public class ThingLoader implements Loadable {

	@Override
	public void load(LoadPath loadPath) throws Exception {

		YmlParser parser = new YmlParser(Docs.Things.class);
		Docs.Things doc = (Docs.Things) parser.parse(loadPath, "things.yml");

		// insert words for things
		for (ThingRaw thItem : doc.things) {
			Parser.checkNotEmpty("thing name", thItem.name);
			Helpers.loadEntityNameAsWord(thItem.name, Type.entity, EntityType.thing);
			Helpers.loadWord(thItem.word, Type.name, EntityType.thing);
			for (ThingStatusRaw stItem : thItem.statuses)
				Helpers.loadWord(stItem.word, Type.name, EntityType.thing);
		}

		// 2nd pass to insert things & statuses
		for (ThingRaw thItem : doc.things) {
			Thing thing = new Thing(thItem.name, thItem.location);
			if (!thItem.visible)
				thing.setVisible(false);
			if (thItem.takeable)
				thing.setTakeable(true);
			if (!thItem.droppable)
				thing.setDroppable(false);
			if (thItem.plural)
				thing.setPlural(true);
			DB.persist(thing);
			Parser.checkNotEmpty("thing status list", thItem.statuses);
			boolean firstStatus = true;
			String description = null;
			for (ThingStatusRaw stItem : thItem.statuses) {
				Parser.checkNotEmpty("thing status label", stItem.status);
				if (stItem.description == null) {
					if (description == null && "initial".equals(stItem.status))
						throw new Exception("Initial description of thing \"" + thing.getName() + "\" cannot be null");
				}
				else {
					description = stItem.description.strip();
				}
				ThingStatus status = new ThingStatus(thing.getName(), stItem.status, description,
					stItem.word != null ? stItem.word.text : thItem.word.text);
				DB.persist(status);
				if (firstStatus && !"initial".equals(stItem.status))
					throw new IllegalParseException("thing status list: first item must be the 'initial' status");
				firstStatus = false;
				if (stItem.possibilities != null) for (PossibilityRaw pDoc : stItem.possibilities) {
					Possibility pData = Possibility.parse(pDoc);
					ThingStatusPossibility possibility;
					if (pData.inherit != null) {
						ThingStatusPossibility parent =
							ThingStatusPossibilityRepository.get().getByThingStatusAction(
								thing, pData.inherit, pData.verb);
						if (parent == null)
							throw new IllegalParseException("inherit reference to unknown status", pData.inherit);
						possibility = new ThingStatusPossibility(
							thing.getName(), stItem.status, parent.getAction().getText(),
							parent.isPossible(), parent.isImportant(), parent.getFeedback());
						if (pData.possible != null)
							possibility.setPossible(pData.possible);
						if (pData.important != null)
							possibility.setImportant(pData.important);
						if (pData.feedback != null)
							possibility.setFeedback(pData.feedback);
					}
					else {
						possibility = new ThingStatusPossibility(
							thing.getName(), stItem.status, pData.verb, pData.possible, pData.important, pData.feedback);
					}
					DB.persist(possibility);
				}
			}
			thing.setStatus("initial");
			DB.persist(thing);
		}
	}

	@Override
	public void register() {
		Loader.get().register(this);
	}

	public void reset(LoadPath loadPath) throws Exception {

		YmlParser parser = new YmlParser(Docs.Things.class);
		Docs.Things doc = (Docs.Things) parser.parse(loadPath, "things.yml");

		for (ThingRaw thItem : doc.things) {
			Thing thing = ThingRepository.get().getByName(thItem.name);
			thing.setLocation(thItem.location);
			if (!thItem.visible)
				thing.setVisible(false);
			if (thItem.takeable)
				thing.setTakeable(true);
			if (!thItem.droppable)
				thing.setDroppable(false);
			if (thItem.plural)
				thing.setPlural(true);
			thing.setStatus("initial");
			DB.persist(thing);
		}
	}

	@Override
	public void dump() {
		Log.skip();
		Log.debug("THINGS =================================================");
		List<Thing> things = ThingRepository.get().findAll();
		things.forEach(s -> Log.debug(s));
		Log.skip();
		Log.debug("THING STATUSES =========================================");
		List<ThingStatus> statuses = ThingStatusRepository.get().findAll();
		statuses.forEach(s -> Log.debug(s));
		Log.skip();
		Log.debug("THING STATUS POSSIBILITIES =============================");
		List<ThingStatusPossibility> possibilities = ThingStatusPossibilityRepository.get().findAll();
		possibilities.forEach(s -> Log.debug(s));
	}
}
