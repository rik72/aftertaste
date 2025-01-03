package io.rik72.brew.engine.loader.loaders;

import java.util.List;

import io.rik72.amber.logger.Log;
import io.rik72.brew.engine.db.entities.CharacterOnCharacter;
import io.rik72.brew.engine.db.entities.CharacterOnLocation;
import io.rik72.brew.engine.db.entities.CharacterOnThing;
import io.rik72.brew.engine.db.entities.CharacterThingOnCharacter;
import io.rik72.brew.engine.db.entities.CharacterThingOnLocation;
import io.rik72.brew.engine.db.entities.CharacterThingOnThing;
import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.Thing;
import io.rik72.brew.engine.db.entities.ThingCharacterOnCharacter;
import io.rik72.brew.engine.db.entities.ThingCharacterOnLocation;
import io.rik72.brew.engine.db.entities.ThingCharacterOnThing;
import io.rik72.brew.engine.db.entities.ThingOnCharacter;
import io.rik72.brew.engine.db.entities.ThingThingOnCharacter;
import io.rik72.brew.engine.db.entities.ThingThingOnThing;
import io.rik72.brew.engine.db.entities.ThingOnLocation;
import io.rik72.brew.engine.db.entities.ThingThingOnLocation;
import io.rik72.brew.engine.db.entities.ThingOnThing;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.entities.Word.EntityType;
import io.rik72.brew.engine.db.entities.abstractions.Complement;
import io.rik72.brew.engine.db.repositories.CharacterOnCharacterRepository;
import io.rik72.brew.engine.db.repositories.CharacterOnLocationRepository;
import io.rik72.brew.engine.db.repositories.CharacterOnThingRepository;
import io.rik72.brew.engine.db.repositories.CharacterRepository;
import io.rik72.brew.engine.db.repositories.CharacterThingOnCharacterRepository;
import io.rik72.brew.engine.db.repositories.ThingOnCharacterRepository;
import io.rik72.brew.engine.db.repositories.ThingThingOnCharacterRepository;
import io.rik72.brew.engine.db.repositories.ThingOnLocationRepository;
import io.rik72.brew.engine.db.repositories.ThingThingOnLocationRepository;
import io.rik72.brew.engine.db.repositories.ThingRepository;
import io.rik72.brew.engine.db.repositories.ThingOnThingRepository;
import io.rik72.brew.engine.db.repositories.ThingThingOnThingRepository;
import io.rik72.brew.engine.db.repositories.WordRepository;
import io.rik72.brew.engine.loader.LoadPath;
import io.rik72.brew.engine.loader.Loadable;
import io.rik72.brew.engine.loader.Loader;
import io.rik72.brew.engine.loader.loaders.parsing.YmlParser;
import io.rik72.brew.engine.loader.loaders.parsing.docs.Docs;
import io.rik72.brew.engine.loader.loaders.parsing.parsers.Action;
import io.rik72.brew.engine.loader.loaders.parsing.parsers.Consequence;
import io.rik72.brew.engine.loader.loaders.parsing.parsers.OneAction;
import io.rik72.brew.engine.loader.loaders.parsing.parsers.TwoAction;
import io.rik72.brew.engine.loader.loaders.parsing.parsers.exceptions.IllegalParseException;
import io.rik72.brew.engine.loader.loaders.parsing.raw.ActionRaw;
import io.rik72.brew.engine.loader.loaders.parsing.raw.CharacterRaw;
import io.rik72.brew.engine.loader.loaders.parsing.raw.CharacterStatusRaw;
import io.rik72.brew.engine.loader.loaders.parsing.raw.ThingRaw;
import io.rik72.brew.engine.loader.loaders.parsing.raw.ThingStatusRaw;
import io.rik72.mammoth.db.DB;

public class ActionLoader implements Loadable {

	@Override
	public void load(LoadPath loadPath) throws Exception {
		loadThingActions(loadPath);
		loadCharacterActions(loadPath);
	}

	private void resetTransitionAndFinale(Action actData) {
		actData.transition = null;
		actData.finale = null;
	}

	private void loadThingActions(LoadPath loadPath) throws Exception {

		YmlParser parser = new YmlParser(Docs.Things.class);
		Docs.Things doc = (Docs.Things) parser.parse(loadPath, "things.yml");

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
										"feedback: an action may possess a feedback or a consequence list, " +
										"but not both. Consider moving the feedback to one of the consequences. Offending context:",
										actItem.action);
								for (Consequence conData : actData.consequences) {

									String entityName = Complement.name(conData.entity);
									if (conData.entity == null)
										entityName = complement.getName();

									Word entity = WordRepository.get().getByText(entityName);
									switch (entity.getEntityType()) {

										case thing: {
											Boolean afterVisibility = null;
											if ("visible".equals(conData.after))
												afterVisibility = true;
											if ("invisible".equals(conData.after))
												afterVisibility = false;

											String beforeName = conData.entity != null ? conData.entity : complement.getName();
											String beforeStatus = conData.before != null ? conData.before : stItem.status;
											String afterStatus = afterVisibility == null ? conData.after : null;

											ThingOnThing action = new ThingOnThing(
												oneActData.verb,
												complement, 				stItem.status,
												beforeName, 				beforeStatus,
												afterStatus,				conData.to,
												afterVisibility,			conData.feedback,
												actData.transition,			actData.finale);
											DB.persist(action);
											resetTransitionAndFinale(actData);
											break;
										}

										case location: {
											ThingOnLocation action = new ThingOnLocation(
												oneActData.verb,
												complement, 				stItem.status,
												conData.entity, 			conData.before,
												conData.after,				conData.feedback,
												actData.transition,			actData.finale);
											DB.persist(action);
											resetTransitionAndFinale(actData);
											break;
										}

										case character: {
											Boolean afterVisibility = null;
											if ("visible".equals(conData.after))
												afterVisibility = true;
											if ("invisible".equals(conData.after))
												afterVisibility = false;

											ThingOnCharacter action = new ThingOnCharacter(
												oneActData.verb,
												complement, 				stItem.status,
												conData.entity, 			conData.before,
												conData.after,				conData.to,
												afterVisibility, 			conData.feedback,
												actData.transition,			actData.finale);
											DB.persist(action);
											resetTransitionAndFinale(actData);
											break;
										}

										default: {
											throw new IllegalParseException(
												"consequence: illegal entity name",
												conData.entity);
										}
									}
								}
							}
							else {
								ThingOnThing action = new ThingOnThing(
									oneActData.verb,
									complement, 						stItem.status,
									complement.getName(),				stItem.status,
									stItem.status, 						null,
									null,				oneActData.feedback,
									actData.transition,					actData.finale);
								DB.persist(action);
								resetTransitionAndFinale(actData);
							}
						}
						else {
							TwoAction twoActData = actData.twoAction;
							Complement supplement = null;
							Word supplementWord = WordRepository.get().getByText(Complement.name(twoActData.supplement));
							if (supplementWord.getEntityType() == EntityType.character)
								supplement = CharacterRepository.get().getByName(twoActData.supplement);
							else
								supplement = ThingRepository.get().getByName(twoActData.supplement);
							if (actData.consequences.size() > 0) {
								if (twoActData.feedback != null && twoActData.feedback.length() > 0)
									throw new IllegalParseException(
										"feedback: an action may have a feedback directly set or a consequence list, " +
										"but not both. Consider moving the feedback to one of the consequences. Offending context:",
										actItem.action);
								for (Consequence conData : actData.consequences) {

									String entityName = Complement.name(conData.entity);
									if (conData.entity == null)
										entityName = complement.getName();
									
									Word entity = WordRepository.get().getByText(entityName);
									switch (entity.getEntityType()) {

										case thing: {
											Boolean afterVisibility = null;
											if ("visible".equals(conData.after))
												afterVisibility = true;
											if ("invisible".equals(conData.after))
												afterVisibility = false;

											String beforeName = conData.entity != null ? conData.entity : complement.getName();
											String beforeStatus = conData.before != null ? conData.before : stItem.status;
											String afterStatus = afterVisibility == null ? conData.after : null;

											if (supplementWord.getEntityType() == EntityType.character) {
												ThingCharacterOnThing action = new ThingCharacterOnThing(
													twoActData.verb,
													complement, 				stItem.status,
													twoActData.preposition,
													(Character) supplement, 	twoActData.supplementStatus,
													beforeName, 				beforeStatus,
													afterStatus,				conData.to,
													afterVisibility,			conData.feedback,
													actData.transition,			actData.finale);
												DB.persist(action);
												resetTransitionAndFinale(actData);
											}
											else {
												ThingThingOnThing action = new ThingThingOnThing(
													twoActData.verb,
													complement, 				stItem.status,
													twoActData.preposition,
													(Thing)supplement, 			twoActData.supplementStatus,
													beforeName, 				beforeStatus,
													afterStatus,				conData.to,
													afterVisibility,			conData.feedback,
													actData.transition,			actData.finale);
												DB.persist(action);
												resetTransitionAndFinale(actData);
											}
											break;
										}

										case location: {
											if (supplementWord.getEntityType() == EntityType.character) {
												ThingCharacterOnLocation action = new ThingCharacterOnLocation(
													twoActData.verb,
													complement, 				stItem.status,
													twoActData.preposition,
													(Character)supplement, 		twoActData.supplementStatus,
													conData.entity, 			conData.before,
													conData.after,				conData.feedback,
													actData.transition,			actData.finale);
												DB.persist(action);
												resetTransitionAndFinale(actData);
											}
											else {
												ThingThingOnLocation action = new ThingThingOnLocation(
													twoActData.verb,
													complement, 				stItem.status,
													twoActData.preposition,
													(Thing)supplement, 			twoActData.supplementStatus,
													conData.entity, 			conData.before,
													conData.after,				conData.feedback,
													actData.transition,			actData.finale);
												DB.persist(action);
												resetTransitionAndFinale(actData);
											}
											break;
										}

										case character: {
											Boolean afterVisibility = null;
											if ("visible".equals(conData.after))
												afterVisibility = true;
											if ("invisible".equals(conData.after))
												afterVisibility = false;

											String beforeName = conData.entity != null ? conData.entity : complement.getName();
											String beforeStatus = conData.before != null ? conData.before : stItem.status;
											String afterStatus = afterVisibility == null ? conData.after : null;

											if (supplementWord.getEntityType() == EntityType.character) {
												ThingCharacterOnCharacter action = new ThingCharacterOnCharacter(
													twoActData.verb,
													complement, 				stItem.status,
													twoActData.preposition,
													(Character)supplement, 		twoActData.supplementStatus,
													beforeName, 				beforeStatus,
													afterStatus,				conData.to,
													afterVisibility,			conData.feedback,
													actData.transition,			actData.finale);
												DB.persist(action);
												resetTransitionAndFinale(actData);
											}
											else {
												ThingThingOnCharacter action = new ThingThingOnCharacter(
													twoActData.verb,
													complement, 				stItem.status,
													twoActData.preposition,
													(Thing)supplement, 			twoActData.supplementStatus,
													beforeName, 				beforeStatus,
													afterStatus,				conData.to,
													afterVisibility,			conData.feedback,
													actData.transition,			actData.finale);
												DB.persist(action);
												resetTransitionAndFinale(actData);
											}
											break;
										}

										default: {
											throw new IllegalParseException(
												"consequence: illegal entity name",
												conData.entity);
										}
									}
								}
							}
							else {
								if (supplementWord.getEntityType() == EntityType.character) {
									ThingCharacterOnThing action = new ThingCharacterOnThing(
										twoActData.verb,
										complement, 						stItem.status,
										twoActData.preposition,
										(Character)supplement, 				twoActData.supplementStatus,
										complement.getName(),				stItem.status,
										stItem.status,						null,
										null,				twoActData.feedback,
										actData.transition,					actData.finale);
									DB.persist(action);
									resetTransitionAndFinale(actData);
								}
								else {
									ThingThingOnThing action = new ThingThingOnThing(
										twoActData.verb,
										complement, 						stItem.status,
										twoActData.preposition,
										(Thing)supplement, 					twoActData.supplementStatus,
										complement.getName(),				stItem.status,
										stItem.status,						null,
										null,				twoActData.feedback,
										actData.transition,					actData.finale);
									DB.persist(action);
									resetTransitionAndFinale(actData);
								}
							}
						}
					}
				}
			}
		}
	}

	private void loadCharacterActions(LoadPath loadPath) throws Exception {

		YmlParser parser = new YmlParser(Docs.Characters.class);
		Docs.Characters doc = (Docs.Characters) parser.parse(loadPath, "characters.yml");

		// 3rd pass to insert actions
		for (CharacterRaw chItem : doc.characters) {
			Character complement = CharacterRepository.get().getByName(chItem.name);
			for (CharacterStatusRaw stItem : chItem.statuses) {
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
								for (Consequence conData : actData.consequences) {

									String entityName = Complement.name(conData.entity);
									if (conData.entity == null)
										entityName = complement.getName();
									
									Word entity = WordRepository.get().getByText(entityName);
									switch (entity.getEntityType()) {

										case thing: {
											Boolean afterVisibility = null;
											if ("visible".equals(conData.after))
												afterVisibility = true;
											if ("invisible".equals(conData.after))
												afterVisibility = false;

											String afterStatus = afterVisibility == null ? conData.after : null;

											CharacterOnThing action = new CharacterOnThing(
												oneActData.verb,
												complement, 				stItem.status,
												conData.entity, 			conData.before,
												afterStatus,				conData.to,
												afterVisibility,			conData.feedback,
												actData.transition,			actData.finale);
											DB.persist(action);
											resetTransitionAndFinale(actData);
											break;
										}

										case location: {
											CharacterOnLocation action = new CharacterOnLocation(
												oneActData.verb,
												complement, 				stItem.status,
												conData.entity, 			conData.before,
												conData.after,				conData.feedback,
												actData.transition,			actData.finale);
											DB.persist(action);
											resetTransitionAndFinale(actData);
											break;
										}

										case character: {
											Boolean afterVisibility = null;
											if ("visible".equals(conData.after))
												afterVisibility = true;
											if ("invisible".equals(conData.after))
												afterVisibility = false;

											String beforeName = conData.entity != null ? conData.entity : complement.getName();
											String beforeStatus = conData.before != null ? conData.before : stItem.status;
											String afterStatus = afterVisibility == null ? conData.after : null;

											CharacterOnCharacter action = new CharacterOnCharacter(
												oneActData.verb,
												complement, 				stItem.status,
												beforeName, 				beforeStatus,
												afterStatus,				conData.to,
												afterVisibility, 			conData.feedback,
												actData.transition,			actData.finale);
											DB.persist(action);
											resetTransitionAndFinale(actData);
											break;
										}

										default: {
											throw new IllegalParseException(
												"consequence: illegal entity name",
												conData.entity);
										}
									}
								}
							}
							else {
								CharacterOnCharacter action = new CharacterOnCharacter(
									oneActData.verb,
									complement, 						stItem.status,
									complement.getName(),				stItem.status,
									stItem.status, 						null,
									null,				oneActData.feedback,
									actData.transition,					actData.finale);
								DB.persist(action);
								resetTransitionAndFinale(actData);
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

									String entityName = Complement.name(conData.entity);
									if (conData.entity == null)
										entityName = complement.getName();
									
									Word entity = WordRepository.get().getByText(entityName);
									switch (entity.getEntityType()) {

										case thing: {
											Boolean afterVisibility = null;
											if ("visible".equals(conData.after))
												afterVisibility = true;
											if ("invisible".equals(conData.after))
												afterVisibility = false;

											String beforeName = conData.entity != null ? conData.entity : complement.getName();
											String beforeStatus = conData.before != null ? conData.before : stItem.status;
											String afterStatus = afterVisibility == null ? conData.after : null;

											CharacterThingOnThing action = new CharacterThingOnThing(
												twoActData.verb,
												complement, 				stItem.status,
												twoActData.preposition,
												supplement, 				twoActData.supplementStatus,
												beforeName, 				beforeStatus,
												afterStatus,				conData.to,
												afterVisibility,			conData.feedback,
												actData.transition,			actData.finale);
											DB.persist(action);
											resetTransitionAndFinale(actData);
											break;
										}

										case location: {
											CharacterThingOnLocation action = new CharacterThingOnLocation(
												twoActData.verb,
												complement, 				stItem.status,
												twoActData.preposition,
												supplement, 				twoActData.supplementStatus,
												conData.entity, 			conData.before,
												conData.after,				conData.feedback,
												actData.transition,			actData.finale);
											DB.persist(action);
											resetTransitionAndFinale(actData);
											break;
										}

										case character: {
											Boolean afterVisibility = null;
											if ("visible".equals(conData.after))
												afterVisibility = true;
											if ("invisible".equals(conData.after))
												afterVisibility = false;

											String beforeName = conData.entity != null ? conData.entity : complement.getName();
											String beforeStatus = conData.before != null ? conData.before : stItem.status;
											String afterStatus = afterVisibility == null ? conData.after : null;

											CharacterThingOnCharacter action = new CharacterThingOnCharacter(
												twoActData.verb,
												complement, 				stItem.status,
												twoActData.preposition,
												supplement, 				twoActData.supplementStatus,
												beforeName, 				beforeStatus,
												afterStatus,				conData.to,
												afterVisibility, 			conData.feedback,
												actData.transition,			actData.finale);
											DB.persist(action);
											resetTransitionAndFinale(actData);
											break;
										}

										default: {
											throw new IllegalParseException(
												"consequence: illegal entity name",
												conData.entity);
										}
									}
								}
							}
							else {
								CharacterThingOnCharacter action = new CharacterThingOnCharacter(
									twoActData.verb,
									complement, 						stItem.status,
									twoActData.preposition,
									supplement, 						twoActData.supplementStatus,
									complement.getName(),				stItem.status,
									stItem.status,						null,
									null,				twoActData.feedback,
									actData.transition,					actData.finale);
								DB.persist(action);
								resetTransitionAndFinale(actData);
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

	@Override
	public void dump() {
		Log.skip();
		Log.debug("THING => THING 1-ACTIONS =================================");
		List<ThingOnThing> thingThing1Actions = ThingOnThingRepository.get().findAll();
		thingThing1Actions.forEach(s -> Log.debug(s));
		Log.skip();
		Log.debug("THING, THING => THING 2-ACTIONS =================================");
		List<ThingThingOnThing> thingThing2Actions = ThingThingOnThingRepository.get().findAll();
		thingThing2Actions.forEach(s -> Log.debug(s));
		Log.skip();
		Log.debug("THING => LOCATION 1-ACTIONS ==============================");
		List<ThingOnLocation> thingLocation1Actions = ThingOnLocationRepository.get().findAll();
		thingLocation1Actions.forEach(s -> Log.debug(s));
		Log.skip();
		Log.debug("THING, THING => LOCATION 2-ACTIONS ==============================");
		List<ThingThingOnLocation> thingLocation2Actions = ThingThingOnLocationRepository.get().findAll();
		thingLocation2Actions.forEach(s -> Log.debug(s));
		Log.skip();
		Log.debug("THING => CHARACTER 1-ACTIONS ==============================");
		List<ThingOnCharacter> thingCharacter1Actions = ThingOnCharacterRepository.get().findAll();
		thingCharacter1Actions.forEach(s -> Log.debug(s));
		Log.skip();
		Log.debug("THING, THING => CHARACTER 2-ACTIONS ==============================");
		List<ThingThingOnCharacter> thingCharacter2Actions = ThingThingOnCharacterRepository.get().findAll();
		thingCharacter2Actions.forEach(s -> Log.debug(s));

		Log.skip();
		Log.debug("CHARACTER => THING 1-ACTIONS =================================");
		List<CharacterOnThing> characterThing1Actions = CharacterOnThingRepository.get().findAll();
		characterThing1Actions.forEach(s -> Log.debug(s));
		Log.skip();
		Log.debug("CHARACTER => THING 2-ACTIONS =================================");
		List<CharacterThingOnCharacter> characterThing2Actions = CharacterThingOnCharacterRepository.get().findAll();
		characterThing2Actions.forEach(s -> Log.debug(s));
		Log.skip();
		Log.debug("CHARACTER => LOCATION 1-ACTIONS ==============================");
		List<CharacterOnLocation> characterLocation1Actions = CharacterOnLocationRepository.get().findAll();
		characterLocation1Actions.forEach(s -> Log.debug(s));
		// Log.skip();
		// Log.debug("CHARACTER => LOCATION 2-ACTIONS ==============================");
		// List<ThingThingOnLocation> characterLocation2Actions = ThingThingOnLocationRepository.get().findAll();
		// characterLocation2Actions.forEach(s -> Log.debug(s));
		Log.skip();
		Log.debug("CHARACTER => CHARACTER 1-ACTIONS ==============================");
		List<CharacterOnCharacter> characterCharacter1Actions = CharacterOnCharacterRepository.get().findAll();
		characterCharacter1Actions.forEach(s -> Log.debug(s));
		// Log.skip();
		// Log.debug("CHARACTER => CHARACTER 2-ACTIONS ==============================");
		// List<ThingThingOnCharacter> characterCharacter2Actions = ThingThingOnCharacterRepository.get().findAll();
		// characterCharacter2Actions.forEach(s -> Log.debug(s));
	}
}
