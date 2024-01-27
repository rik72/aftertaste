package io.rik72.brew.engine.loader.loaders;

import java.util.List;

import io.rik72.amber.logger.Log;
import io.rik72.brew.engine.db.entities.Thing;
import io.rik72.brew.engine.db.entities.CharacterOneAction;
import io.rik72.brew.engine.db.entities.CharacterTwoAction;
import io.rik72.brew.engine.db.entities.LocationOneAction;
import io.rik72.brew.engine.db.entities.LocationTwoAction;
import io.rik72.brew.engine.db.entities.ThingStatus;
import io.rik72.brew.engine.db.entities.ThingStatusPossibility;
import io.rik72.brew.engine.db.entities.ThingOneAction;
import io.rik72.brew.engine.db.entities.ThingTwoAction;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.repositories.CharacterOneActionRepository;
import io.rik72.brew.engine.db.repositories.CharacterTwoActionRepository;
import io.rik72.brew.engine.db.repositories.LocationOneActionRepository;
import io.rik72.brew.engine.db.repositories.LocationTwoActionRepository;
import io.rik72.brew.engine.db.repositories.ThingRepository;
import io.rik72.brew.engine.db.repositories.ThingStatusPossibilityRepository;
import io.rik72.brew.engine.db.repositories.ThingStatusRepository;
import io.rik72.brew.engine.db.repositories.ThingOneActionRepository;
import io.rik72.brew.engine.db.repositories.ThingTwoActionRepository;
import io.rik72.brew.engine.finder.LoadPath;
import io.rik72.brew.engine.loader.Loadable;
import io.rik72.brew.engine.loader.Loader;
import io.rik72.brew.engine.loader.YmlParser;
import io.rik72.brew.engine.loader.loaders.docs.Docs;
import io.rik72.brew.engine.loader.loaders.docs.Helpers;
import io.rik72.brew.engine.loader.loaders.docs.types.parsers.Action;
import io.rik72.brew.engine.loader.loaders.docs.types.parsers.Consequence;
import io.rik72.brew.engine.loader.loaders.docs.types.parsers.EntityConsequence;
import io.rik72.brew.engine.loader.loaders.docs.types.parsers.OneAction;
import io.rik72.brew.engine.loader.loaders.docs.types.parsers.Parser;
import io.rik72.brew.engine.loader.loaders.docs.types.parsers.Possibility;
import io.rik72.brew.engine.loader.loaders.docs.types.parsers.TwoAction;
import io.rik72.brew.engine.loader.loaders.docs.types.parsers.exceptions.IllegalParseException;
import io.rik72.brew.engine.loader.loaders.docs.types.raw.ActionRaw;
import io.rik72.brew.engine.loader.loaders.docs.types.raw.PossibilityRaw;
import io.rik72.brew.engine.loader.loaders.docs.types.raw.ThingStatusRaw;
import io.rik72.brew.engine.loader.loaders.docs.types.raw.ThingRaw;
import io.rik72.mammoth.db.DB;

public class ThingLoader implements Loadable {

	@Override
	public void load(LoadPath loadPath) {

		YmlParser parser = new YmlParser(Docs.Things.class);
		Docs.Things doc = (Docs.Things) parser.parse(loadPath, "things.yml");

		// insert words for things
		for (ThingRaw thItem : doc.things) {
			Helpers.loadWord(thItem.word, Word.Type.NAME);
			Parser.checkNotEmpty("thing name", thItem.name);
			for (ThingStatusRaw stItem : thItem.statuses)
				Helpers.loadWord(stItem.word, Word.Type.NAME);
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
			for (ThingStatusRaw stItem : thItem.statuses) {
				ThingStatus status = new ThingStatus(thing.getName(), stItem.status, stItem.description,
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

		// 3rd pass to insert actions
		for (ThingRaw thItem : doc.things) {
			Thing complement = ThingRepository.get().getByName(thItem.name);
			for (ThingStatusRaw stItem : thItem.statuses) {
				if (stItem.actions != null) {
					for (ActionRaw actItem : stItem.actions) {
						Action actData = Action.parse(actItem);
						if (actData.oneAction != null) {
							OneAction oneActData = actData.oneAction;
							if (actData.consequences.size() > 0) {
								if (oneActData.feedback != null && oneActData.feedback.length() > 0)
									throw new IllegalParseException(
										"feedback: an action may have a feedback directly set or a consequence list, " +
										"but not both. Consider moving the feedback to one of the consequences. Offending context:",
										actItem.action);
								for (Consequence consequence : actData.consequences) {
									Consequence conData = (Consequence) consequence;
									if (conData.thingConsequence != null) {
										EntityConsequence entConData = conData.thingConsequence;

										Boolean afterVisibility = null;
										if ("visible".equals(entConData.after))
											afterVisibility = true;
										if ("invisible".equals(entConData.after))
											afterVisibility = false;

										String beforeName = entConData.entity != null ? entConData.entity : complement.getName();
										String beforeStatus = entConData.before != null ? entConData.before : stItem.status;
										String afterStatus = afterVisibility == null ? entConData.after : null;

										ThingOneAction action = new ThingOneAction(
											oneActData.verb,
											complement, 				stItem.status,
											beforeName, 				beforeStatus,
											afterStatus,				entConData.to,
											afterVisibility,			entConData.feedback);
										DB.persist(action);
									} else
									if (conData.locationConsequence != null) {
										EntityConsequence entConData = conData.locationConsequence;

										LocationOneAction action = new LocationOneAction(
											oneActData.verb,
											complement, 				stItem.status,
											entConData.entity, 			entConData.before,
											entConData.after,			entConData.feedback);
										DB.persist(action);
									} else
									if (conData.characterConsequence != null) {
										EntityConsequence entConData = conData.characterConsequence;

										CharacterOneAction action = new CharacterOneAction(
											oneActData.verb,
											complement, 				stItem.status,
											entConData.entity, 			entConData.before,
											entConData.after,			entConData.to,
											entConData.feedback);
										DB.persist(action);
									}
								}
							}
							else {
								ThingOneAction action = new ThingOneAction(
									oneActData.verb,
									complement, 						stItem.status,
									complement.getName(),				stItem.status,
									stItem.status, 						null,
									null,				oneActData.feedback);
								DB.persist(action);
							}
						}
						else {
							TwoAction twoActData = actData.twoAction;
							Thing supplement = ThingRepository.get().getByName(twoActData.supplement);
							if (actData.consequences.size() > 0) {
								if (twoActData.feedback != null && twoActData.feedback.length() > 0)
									throw new IllegalParseException(
										"feedback: an action may have a feedback directly set or a consequence list, " +
										"but not both. Consider moving the feedback to one of the consequences. Offending context:",
										actItem.action);
								for (Consequence conData : actData.consequences) {
									if (conData.thingConsequence != null) {
										EntityConsequence entConData = conData.thingConsequence;

										Boolean afterVisibility = null;
										if ("visible".equals(entConData.after))
											afterVisibility = true;
										if ("invisible".equals(entConData.after))
											afterVisibility = false;

										String beforeName = entConData.entity != null ? entConData.entity : complement.getName();
										String beforeStatus = entConData.before != null ? entConData.before : stItem.status;
										String afterStatus = afterVisibility == null ? entConData.after : null;

										ThingTwoAction action = new ThingTwoAction(
											twoActData.verb,
											complement, 				stItem.status,
											twoActData.preposition,
											supplement, 				twoActData.supplementStatus,
											beforeName, 				beforeStatus,
											afterStatus,				entConData.to,
											afterVisibility,			entConData.feedback);
										DB.persist(action);
									} else
									if (conData.locationConsequence != null) {
										EntityConsequence entConData = conData.locationConsequence;

										LocationTwoAction action = new LocationTwoAction(
											twoActData.verb,
											complement, 				stItem.status,
											twoActData.preposition,
											supplement, 				twoActData.supplementStatus,
											entConData.entity, 			entConData.before,
											entConData.after,			entConData.feedback);
										DB.persist(action);
									} else
									if (conData.characterConsequence != null) {
										EntityConsequence entConData = conData.characterConsequence;

										CharacterTwoAction action = new CharacterTwoAction(
											twoActData.verb,
											complement, 				stItem.status,
											twoActData.preposition,
											supplement, 				twoActData.supplementStatus,
											entConData.entity, 			entConData.before,
											entConData.after,			entConData.to,
											entConData.feedback);
										DB.persist(action);
									}
								}
							}
							else {
								ThingTwoAction action = new ThingTwoAction(
									twoActData.verb,
									complement, 						stItem.status,
									twoActData.preposition,
									supplement, 						twoActData.supplementStatus,
									complement.getName(),				stItem.status,
									stItem.status,						null,
									null,				twoActData.feedback);
								DB.persist(action);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void register() {
		Loader.get().register(this);
	}

	public void reload() {

		YmlParser parser = new YmlParser(Docs.Things.class);
		Docs.Things doc = (Docs.Things) parser.parse("brew/stories/test/things.yml");

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
		Log.skip();
		Log.debug("THING => THING 1-ACTIONS =================================");
		List<ThingOneAction> thingThing1Actions = ThingOneActionRepository.get().findAll();
		thingThing1Actions.forEach(s -> Log.debug(s));
		Log.skip();
		Log.debug("THING => THING 2-ACTIONS =================================");
		List<ThingTwoAction> thingThing2Actions = ThingTwoActionRepository.get().findAll();
		thingThing2Actions.forEach(s -> Log.debug(s));
		Log.skip();
		Log.debug("THING => LOCATION 1-ACTIONS ==============================");
		List<LocationOneAction> thingLocation1Actions = LocationOneActionRepository.get().findAll();
		thingLocation1Actions.forEach(s -> Log.debug(s));
		Log.skip();
		Log.debug("THING => LOCATION 2-ACTIONS ==============================");
		List<LocationTwoAction> thingLocation2Actions = LocationTwoActionRepository.get().findAll();
		thingLocation2Actions.forEach(s -> Log.debug(s));
		Log.skip();
		Log.debug("THING => CHARACTER 1-ACTIONS ==============================");
		List<CharacterOneAction> thingCharacter1Actions = CharacterOneActionRepository.get().findAll();
		thingCharacter1Actions.forEach(s -> Log.debug(s));
		Log.skip();
		Log.debug("THING => CHARACTER 2-ACTIONS ==============================");
		List<CharacterTwoAction> thingCharacter2Actions = CharacterTwoActionRepository.get().findAll();
		thingCharacter2Actions.forEach(s -> Log.debug(s));
	}
}
