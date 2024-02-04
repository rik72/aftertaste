package io.rik72.brew.engine.loader.loaders;

import java.util.List;

import io.rik72.amber.logger.Log;
import io.rik72.brew.engine.db.entities.CharacterOnCharacter;
import io.rik72.brew.engine.db.entities.CharacterOnLocation;
import io.rik72.brew.engine.db.entities.CharacterOnThing;
import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.Thing;
import io.rik72.brew.engine.db.entities.ThingOnCharacter;
import io.rik72.brew.engine.db.entities.ThingThingOnCharacter;
import io.rik72.brew.engine.db.entities.ThingOnLocation;
import io.rik72.brew.engine.db.entities.ThingThingOnLocation;
import io.rik72.brew.engine.db.entities.ThingOnThing;
import io.rik72.brew.engine.db.entities.ThingThingOnThing;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.entities.abstractions.Complement;
import io.rik72.brew.engine.db.repositories.CharacterOnCharacterRepository;
import io.rik72.brew.engine.db.repositories.CharacterOnLocationRepository;
import io.rik72.brew.engine.db.repositories.CharacterOnThingRepository;
import io.rik72.brew.engine.db.repositories.CharacterRepository;
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
import io.rik72.brew.engine.loader.YmlParser;
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

public class ConsequenceLoader implements Loadable {

	@Override
	public void load(LoadPath loadPath) {
		loadThingConsequences(loadPath);
		loadCharacterConsequences(loadPath);
	}

	private void loadThingConsequences(LoadPath loadPath) {

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
												afterVisibility,			conData.feedback);
											DB.persist(action);
											break;
										}

										case location: {
											ThingOnLocation action = new ThingOnLocation(
												oneActData.verb,
												complement, 				stItem.status,
												conData.entity, 			conData.before,
												conData.after,				conData.feedback);
											DB.persist(action);
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
												conData.after,			conData.to,
												afterVisibility, 			conData.feedback);
											DB.persist(action);
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
									null,				oneActData.feedback);
								DB.persist(action);
							}
						}
						else {
							TwoAction twoActData = actData.twoAction;
System.out.println(twoActData.supplement);
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

											ThingThingOnThing action = new ThingThingOnThing(
												twoActData.verb,
												complement, 				stItem.status,
												twoActData.preposition,
												supplement, 				twoActData.supplementStatus,
												beforeName, 				beforeStatus,
												afterStatus,				conData.to,
												afterVisibility,			conData.feedback);
											DB.persist(action);
											break;
										}

										case location: {
											ThingThingOnLocation action = new ThingThingOnLocation(
												twoActData.verb,
												complement, 				stItem.status,
												twoActData.preposition,
												supplement, 				twoActData.supplementStatus,
												conData.entity, 			conData.before,
												conData.after,				conData.feedback);
											DB.persist(action);
											break;
										}

										case character: {
											ThingThingOnCharacter action = new ThingThingOnCharacter(
												twoActData.verb,
												complement, 				stItem.status,
												twoActData.preposition,
												supplement, 				twoActData.supplementStatus,
												conData.entity, 			conData.before,
												conData.after,				conData.to,
												conData.feedback);
											DB.persist(action);
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
								ThingThingOnThing action = new ThingThingOnThing(
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

	private void loadCharacterConsequences(LoadPath loadPath) {

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
												afterVisibility,			conData.feedback);
											DB.persist(action);
											break;
										}

										case location: {
											CharacterOnLocation action = new CharacterOnLocation(
												oneActData.verb,
												complement, 				stItem.status,
												conData.entity, 			conData.before,
												conData.after,				conData.feedback);
											DB.persist(action);
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
												afterVisibility, 			conData.feedback);
											DB.persist(action);
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
									null,				oneActData.feedback);
								DB.persist(action);
							}
						}
						// else {
						// 	TwoAction twoActData = actData.twoAction;
						// 	Thing supplement = ThingRepository.get().getByName(twoActData.supplement);
						// 	if (actData.consequences.size() > 0) {
						// 		if (twoActData.feedback != null && twoActData.feedback.length() > 0)
						// 			throw new IllegalParseException(
						// 				"feedback: an action may have a feedback directly set or a consequence list, " +
						// 				"but not both. Consider moving the feedback to one of the consequences. Offending context:",
						// 				actItem.action);
						// 		for (Consequence conData : actData.consequences) {
						// 			if (conData.thingConsequence != null) {
						// 				EntityConsequence entConData = conData.thingConsequence;

						// 				Boolean afterVisibility = null;
						// 				if ("visible".equals(entConData.after))
						// 					afterVisibility = true;
						// 				if ("invisible".equals(entConData.after))
						// 					afterVisibility = false;

						// 				String beforeName = entConData.entity != null ? entConData.entity : complement.getName();
						// 				String beforeStatus = entConData.before != null ? entConData.before : stItem.status;
						// 				String afterStatus = afterVisibility == null ? entConData.after : null;

						// 				ThingTwoAction action = new ThingTwoAction(
						// 					twoActData.verb,
						// 					complement, 				stItem.status,
						// 					twoActData.preposition,
						// 					supplement, 				twoActData.supplementStatus,
						// 					beforeName, 				beforeStatus,
						// 					afterStatus,				entConData.to,
						// 					afterVisibility,			entConData.feedback);
						// 				DB.persist(action);
						// 			} else
						// 			if (conData.locationConsequence != null) {
						// 				EntityConsequence entConData = conData.locationConsequence;

						// 				LocationTwoAction action = new LocationTwoAction(
						// 					twoActData.verb,
						// 					complement, 				stItem.status,
						// 					twoActData.preposition,
						// 					supplement, 				twoActData.supplementStatus,
						// 					entConData.entity, 			entConData.before,
						// 					entConData.after,			entConData.feedback);
						// 				DB.persist(action);
						// 			} else
						// 			if (conData.characterConsequence != null) {
						// 				EntityConsequence entConData = conData.characterConsequence;

						// 				CharacterTwoAction action = new CharacterTwoAction(
						// 					twoActData.verb,
						// 					complement, 				stItem.status,
						// 					twoActData.preposition,
						// 					supplement, 				twoActData.supplementStatus,
						// 					entConData.entity, 			entConData.before,
						// 					entConData.after,			entConData.to,
						// 					entConData.feedback);
						// 				DB.persist(action);
						// 			}
						// 		}
						// 	}
						// 	else {
						// 		ThingTwoAction action = new ThingTwoAction(
						// 			twoActData.verb,
						// 			complement, 						stItem.status,
						// 			twoActData.preposition,
						// 			supplement, 						twoActData.supplementStatus,
						// 			complement.getName(),				stItem.status,
						// 			stItem.status,						null,
						// 			null,				twoActData.feedback);
						// 		DB.persist(action);
						// 	}
						// }
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
		Log.debug("THING => THING 2-ACTIONS =================================");
		List<ThingThingOnThing> thingThing2Actions = ThingThingOnThingRepository.get().findAll();
		thingThing2Actions.forEach(s -> Log.debug(s));
		Log.skip();
		Log.debug("THING => LOCATION 1-ACTIONS ==============================");
		List<ThingOnLocation> thingLocation1Actions = ThingOnLocationRepository.get().findAll();
		thingLocation1Actions.forEach(s -> Log.debug(s));
		Log.skip();
		Log.debug("THING => LOCATION 2-ACTIONS ==============================");
		List<ThingThingOnLocation> thingLocation2Actions = ThingThingOnLocationRepository.get().findAll();
		thingLocation2Actions.forEach(s -> Log.debug(s));
		Log.skip();
		Log.debug("THING => CHARACTER 1-ACTIONS ==============================");
		List<ThingOnCharacter> thingCharacter1Actions = ThingOnCharacterRepository.get().findAll();
		thingCharacter1Actions.forEach(s -> Log.debug(s));
		Log.skip();
		Log.debug("THING => CHARACTER 2-ACTIONS ==============================");
		List<ThingThingOnCharacter> thingCharacter2Actions = ThingThingOnCharacterRepository.get().findAll();
		thingCharacter2Actions.forEach(s -> Log.debug(s));

		Log.skip();
		Log.debug("CHARACTER => THING 1-ACTIONS =================================");
		List<CharacterOnThing> characterThing1Actions = CharacterOnThingRepository.get().findAll();
		characterThing1Actions.forEach(s -> Log.debug(s));
		// Log.skip();
		// Log.debug("CHARACTER => THING 2-ACTIONS =================================");
		// List<CharacterThingOnThing> characterThing2Actions = CharacterThingOnThingRepository.get().findAll();
		// characterThing2Actions.forEach(s -> Log.debug(s));
		Log.skip();
		Log.debug("CHARACTER => LOCATION 1-ACTIONS ==============================");
		List<CharacterOnLocation> characterLocation1Actions = CharacterOnLocationRepository.get().findAll();
		characterLocation1Actions.forEach(s -> Log.debug(s));
		// Log.skip();
		// Log.debug("CHARACTER => LOCATION 2-ACTIONS ==============================");
		// List<CharacterThingOnLocation> characterLocation2Actions = CharacterThingOnLocationRepository.get().findAll();
		// characterLocation2Actions.forEach(s -> Log.debug(s));
		Log.skip();
		Log.debug("CHARACTER => CHARACTER 1-ACTIONS ==============================");
		List<CharacterOnCharacter> characterCharacter1Actions = CharacterOnCharacterRepository.get().findAll();
		characterCharacter1Actions.forEach(s -> Log.debug(s));
		// Log.skip();
		// Log.debug("CHARACTER => CHARACTER 2-ACTIONS ==============================");
		// List<CharacterThingOnCharacter> characterCharacter2Actions = CharacterThingOnCharacterRepository.get().findAll();
		// characterCharacter2Actions.forEach(s -> Log.debug(s));
	}
}
