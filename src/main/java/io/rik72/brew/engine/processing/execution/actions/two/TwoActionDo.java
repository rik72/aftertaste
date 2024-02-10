package io.rik72.brew.engine.processing.execution.actions.two;

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
import io.rik72.brew.engine.db.repositories.CharacterThingOnCharacterRepository;
import io.rik72.brew.engine.db.repositories.CharacterThingOnLocationRepository;
import io.rik72.brew.engine.db.repositories.CharacterThingOnThingRepository;
import io.rik72.brew.engine.db.repositories.LocationRepository;
import io.rik72.brew.engine.db.repositories.ThingCharacterOnCharacterRepository;
import io.rik72.brew.engine.db.repositories.ThingCharacterOnLocationRepository;
import io.rik72.brew.engine.db.repositories.ThingCharacterOnThingRepository;
import io.rik72.brew.engine.db.repositories.ThingThingOnCharacterRepository;
import io.rik72.brew.engine.db.repositories.ThingThingOnLocationRepository;
import io.rik72.brew.engine.db.repositories.ThingThingOnThingRepository;
import io.rik72.brew.engine.processing.execution.Results;
import io.rik72.brew.engine.processing.execution.actions.ConsequenceHelper;
import io.rik72.brew.engine.utils.TextUtils;

public class TwoActionDo extends TwoActionExecutor {

	protected TwoActionDo(Vector<Word> words, boolean toBeConfirmed, Word verb, Character subject, String additionalFeedback,
	                      Word cName, Complement complement, boolean complementIsInInventory,
						  Word preposition,
						  Word sName, Complement supplement, boolean supplementIsInInventory) {
		super(words, toBeConfirmed, verb, subject, additionalFeedback,
			cName, complement, complementIsInInventory, preposition, sName, supplement, supplementIsInInventory);
	}

	@Override
	public Results execute() throws Exception {

		Results results = checkComplement();
		if (results != null)
			return results;

		List<ConsequenceOnThing> consequencesOnThings = new ArrayList<>();
		if (cName.getEntityType() == EntityType.thing && sName.getEntityType() == EntityType.thing)
			consequencesOnThings.addAll(
				ThingThingOnThingRepository.get().findByParts(
					verb.getCanonical(), complement.getStatus(), preposition.getCanonical(), supplement.getStatus()));
		else if (cName.getEntityType() == EntityType.character && sName.getEntityType() == EntityType.thing)
			consequencesOnThings.addAll(
				CharacterThingOnThingRepository.get().findByParts(
					verb.getCanonical(), complement.getStatus(), preposition.getCanonical(), supplement.getStatus()));
		else if (cName.getEntityType() == EntityType.thing && sName.getEntityType() == EntityType.character)
			consequencesOnThings.addAll(
				ThingCharacterOnThingRepository.get().findByParts(
					verb.getCanonical(), complement.getStatus(), preposition.getCanonical(), supplement.getStatus()));

		List<ConsequenceOnLocation> consequencesOnLocations = new ArrayList<>();
		if (cName.getEntityType() == EntityType.thing && sName.getEntityType() == EntityType.thing)
			consequencesOnLocations.addAll(
				ThingThingOnLocationRepository.get().findByParts(
					verb.getCanonical(), complement.getStatus(), preposition.getCanonical(), supplement.getStatus()));
		else if (cName.getEntityType() == EntityType.character && sName.getEntityType() == EntityType.thing)
			consequencesOnLocations.addAll(
				CharacterThingOnLocationRepository.get().findByParts(
					verb.getCanonical(), complement.getStatus(), preposition.getCanonical(), supplement.getStatus()));
		else if (cName.getEntityType() == EntityType.thing && sName.getEntityType() == EntityType.character)
			consequencesOnLocations.addAll(
				ThingCharacterOnLocationRepository.get().findByParts(
					verb.getCanonical(), complement.getStatus(), preposition.getCanonical(), supplement.getStatus()));

		List<ConsequenceOnCharacter> consequencesOnCharacters = new ArrayList<>();
		if (cName.getEntityType() == EntityType.thing && sName.getEntityType() == EntityType.thing)
			consequencesOnCharacters.addAll(
				ThingThingOnCharacterRepository.get().findByParts(
					verb.getCanonical(), complement.getStatus(), preposition.getCanonical(), supplement.getStatus()));
		else if (cName.getEntityType() == EntityType.character && sName.getEntityType() == EntityType.thing)
			consequencesOnCharacters.addAll(
				CharacterThingOnCharacterRepository.get().findByParts(
					verb.getCanonical(), complement.getStatus(), preposition.getCanonical(), supplement.getStatus()));
		else if (cName.getEntityType() == EntityType.thing && sName.getEntityType() == EntityType.character)
			consequencesOnCharacters.addAll(
				ThingCharacterOnCharacterRepository.get().findByParts(
					verb.getCanonical(), complement.getStatus(), preposition.getCanonical(), supplement.getStatus()));

		if (!consequencesOnThings.isEmpty() || !consequencesOnLocations.isEmpty() || !consequencesOnCharacters.isEmpty())
			setDoable(true);

		results = checkVerb();
		if (results != null)
			return results;

		results = checkSupplement();
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

	protected Results checkSupplement() {
		if (supplement == null) {
			Location locationSupplement = LocationRepository.get().getByName(sName.getCanonical().getText());
			if (locationSupplement != null && subject.getLocation().getId() == locationSupplement.getId())
				return new Results(false, false, cantDoThat());
			return new Results(false, false, noSuchSupplement());
		}
		else if (verb.getSupplementPosition() == Word.Position.inventory && !supplementIsInInventory)
			return new Results(false, false, supplementNotInInventory());
		else if (verb.getSupplementPosition() == Word.Position.location && supplementIsInInventory)
			return new Results(false, false, supplementNotInLocation());
		return null;
	}

	@Override
	protected String doneFeedback() {
		String complementText = cName.getEntityType() == EntityType.character ?
			TextUtils.ucFirst(cName.getCanonical().getText()) :
			"the " + cName.getCanonical().getText();
		String supplementText = cName.getEntityType() == EntityType.character ?
			TextUtils.ucFirst(sName.getCanonical().getText()) :
			"the " + sName.getCanonical().getText();
		return "You " + verb.getText() + " " + complementText + " " + preposition.getText() + " " + supplementText + ".";
	}

	protected String complementNotInInventory() {
		return "You do not possess the " + cName.getCanonical().getText();
	}

	protected String complementNotInLocation() {
		return "You must drop the " + cName.getCanonical().getText() + " first";
	}

	protected String supplementNotInInventory() {
		return "You do not possess the " + sName.getCanonical().getText();
	}

	protected String supplementNotInLocation() {
		return "You must drop the " + sName.getCanonical().getText() + " first";
	}

	protected String noSuchSupplement() {
		switch (sName.getEntityType()) {
			case character:
				return sName.getCanonical().getText() + " is not here.";
			
			case location:
				return "There " + (supplement.isPlural() ? "is" : "are") + " no " + sName.getCanonical().getText() + " here.";

			default:
				return "You can't see any " + sName.getCanonical().getText() + " here.";
		}
	}
}
