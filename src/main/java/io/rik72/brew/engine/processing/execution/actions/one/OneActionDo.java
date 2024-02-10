package io.rik72.brew.engine.processing.execution.actions.one;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.Location;
import io.rik72.brew.engine.db.entities.Thing;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.entities.Word.EntityType;
import io.rik72.brew.engine.db.entities.abstractions.Complement;
import io.rik72.brew.engine.db.entities.abstractions.ConsequenceOnCharacter;
import io.rik72.brew.engine.db.entities.abstractions.ConsequenceOnLocation;
import io.rik72.brew.engine.db.entities.abstractions.ConsequenceOnThing;
import io.rik72.brew.engine.db.repositories.CharacterOnCharacterRepository;
import io.rik72.brew.engine.db.repositories.CharacterOnLocationRepository;
import io.rik72.brew.engine.db.repositories.CharacterOnThingRepository;
import io.rik72.brew.engine.db.repositories.LocationRepository;
import io.rik72.brew.engine.db.repositories.ThingOnCharacterRepository;
import io.rik72.brew.engine.db.repositories.ThingOnLocationRepository;
import io.rik72.brew.engine.db.repositories.ThingOnThingRepository;
import io.rik72.brew.engine.processing.execution.Results;
import io.rik72.brew.engine.processing.execution.actions.ConsequenceHelper;
import io.rik72.brew.engine.utils.TextUtils;

public class OneActionDo extends OneActionExecutor {

	protected OneActionDo(Vector<Word> words, boolean toBeConfirmed, Word verb, Character subject, String additionalFeedback,
	                      Word cName, Complement complement, boolean complementIsInInventory) {
		super(words, toBeConfirmed, verb, subject, additionalFeedback, cName, complement, complementIsInInventory);
	}

	@Override
	public Results execute() throws Exception {

		Results results = checkComplement();
		if (results != null)
			return results;

		List<ConsequenceOnThing> consequencesOnThings = new ArrayList<>();
		if (cName.getEntityType() == EntityType.thing)
			consequencesOnThings.addAll(
				ThingOnThingRepository.get().findByParts(verb.getCanonical(), complement.getStatus()));
		else if (cName.getEntityType() == EntityType.character)
			consequencesOnThings.addAll(
				CharacterOnThingRepository.get().findByParts(verb.getCanonical(), complement.getStatus()));

		List<ConsequenceOnLocation> consequencesOnLocations = new ArrayList<>();
		if (cName.getEntityType() == EntityType.thing)
			consequencesOnLocations.addAll(
				ThingOnLocationRepository.get().findByParts(verb.getCanonical(), complement.getStatus()));
		else if (cName.getEntityType() == EntityType.character)
			consequencesOnLocations.addAll(
				CharacterOnLocationRepository.get().findByParts(verb.getCanonical(), complement.getStatus()));

		List<ConsequenceOnCharacter> consequencesOnCharacters = new ArrayList<>();
		if (cName.getEntityType() == EntityType.thing)
			consequencesOnCharacters.addAll(
				ThingOnCharacterRepository.get().findByParts(verb.getCanonical(), complement.getStatus()));
		else if (cName.getEntityType() == EntityType.character)
			consequencesOnCharacters.addAll(
				CharacterOnCharacterRepository.get().findByParts(verb.getCanonical(), complement.getStatus()));

		if (!consequencesOnThings.isEmpty() || !consequencesOnLocations.isEmpty() || !consequencesOnCharacters.isEmpty())
			setDoable(true);

		results = checkVerb();
		if (results != null)
			return results;

		boolean refresh = false;
		List<String> texts = new ArrayList<>();

		if (consequencesOnThings.size() > 0) {
			for (ConsequenceOnThing action : consequencesOnThings) {
				Thing before = action.getBeforeStatus().getThing();
				if (before.getStatus() == action.getBeforeStatus()) {
					ConsequenceHelper.applyConsequenceOnThing(action, before);
					if (action.getAfterText() != null)
						texts.add(action.getAfterText());
				}
			}
		}

		if (consequencesOnLocations.size() > 0) {
			for (ConsequenceOnLocation action : consequencesOnLocations) {
				Location before = action.getBeforeStatus().getLocation();
				if (before.getStatus() == action.getBeforeStatus()) {
					ConsequenceHelper.applyConsequenceOnLocation(action, before);
					if (action.getAfterText() != null)
						texts.add(action.getAfterText());
					refresh = true;
				}
			}
		}

		if (consequencesOnCharacters.size() > 0) {
			for (ConsequenceOnCharacter action : consequencesOnCharacters) {
				Character before = action.getBeforeStatus().getCharacter();
				if (before.getStatus() == action.getBeforeStatus()) {
					ConsequenceHelper.applyConsequenceOnCharacter(action, before);
					if (action.getAfterText() != null)
						texts.add(action.getAfterText());
				}
			}
		}

		if (isDoable())
			results = buildResults(true, refresh, doneFeedback());
		else
			results = buildResults(false, refresh, cantDoThat());
		results.getTexts().addAll(texts);
		return results;
	}

	protected Results checkComplement() {
		if (complement == null) {
			Location locationComplement = LocationRepository.get().getByName(cName.getCanonical().getText());
			if (locationComplement != null && subject.getLocation().getId() == locationComplement.getId())
				return new Results(false, false, cantDoThat());
			return new Results(false, false, noSuchComplement());
		}
		else if (verb.getComplementPosition() == Word.Position.inventory && !complementIsInInventory)
			return new Results(false, false, complementNotInInventory());
		else if (verb.getComplementPosition() == Word.Position.location && complementIsInInventory)
			return new Results(false, false, complementNotInLocation());
		return null;
	}

	@Override
	protected String doneFeedback() {
		String complementText = cName.getEntityType() == EntityType.character ?
			TextUtils.ucFirst(cName.getCanonical().getText()) :
			"the " + cName.getCanonical().getText();
		return "You " + verb.getText() + " " + complementText + ".";
	}

	protected String complementNotInInventory() {
		return "You do not possess the " + cName.getCanonical().getText();
	}

	protected String complementNotInLocation() {
		return "You must drop the " + cName.getCanonical().getText() + " first";
	}
}
