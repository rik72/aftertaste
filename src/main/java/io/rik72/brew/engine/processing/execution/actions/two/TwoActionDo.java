package io.rik72.brew.engine.processing.execution.actions.two;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.Location;
import io.rik72.brew.engine.db.entities.Thing;
import io.rik72.brew.engine.db.entities.ThingThingOnCharacter;
import io.rik72.brew.engine.db.entities.ThingThingOnLocation;
import io.rik72.brew.engine.db.entities.ThingThingOnThing;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.entities.abstractions.Complement;
import io.rik72.brew.engine.db.repositories.LocationRepository;
import io.rik72.brew.engine.db.repositories.ThingThingOnCharacterRepository;
import io.rik72.brew.engine.db.repositories.ThingThingOnLocationRepository;
import io.rik72.brew.engine.db.repositories.ThingThingOnThingRepository;
import io.rik72.brew.engine.processing.execution.Results;
import io.rik72.mammoth.db.DB;

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

		List<ThingThingOnThing> thingActions =
			ThingThingOnThingRepository.get().findByParts(
				verb.getCanonical(), complement.getStatus(), preposition.getCanonical(), supplement.getStatus());
		List<ThingThingOnLocation> locationActions =
			ThingThingOnLocationRepository.get().findByParts(
				verb.getCanonical(), complement.getStatus(), preposition.getCanonical(), supplement.getStatus());
		List<ThingThingOnCharacter> characterActions =
			ThingThingOnCharacterRepository.get().findByParts(
				verb.getCanonical(), complement.getStatus(), preposition.getCanonical(), supplement.getStatus());

		if (!thingActions.isEmpty() || !locationActions.isEmpty() || !characterActions.isEmpty())
			setDoable(true);

		results = checkVerb();
		if (results != null)
			return results;

		results = checkSupplement();
		if (results != null)
			return results;

		boolean refresh = false;

		List<String> texts = new ArrayList<>();
		if (thingActions.size() > 0) {
			for (ThingThingOnThing action : thingActions) {
				Thing before = action.getBeforeStatus().getThing();
				if (before.getStatus() == action.getBeforeStatus()) {
					if (action.getAfterStatus() != null)
						before.setStatus(action.getAfterStatus().getLabel());
					if (action.getToLocation() != null)
						before.setLocation(action.getToLocation());
					if (action.getAfterVisibility() != null)
						before.setVisible(action.getAfterVisibility());
					DB.persist(before);
					if (action.getAfterText() != null)
						texts.add(action.getAfterText());
				}
			}
		}

		if (locationActions.size() > 0) {
			for (ThingThingOnLocation action : locationActions) {
				Location before = action.getBeforeStatus().getLocation();
				if (before.getStatus() == action.getBeforeStatus()) {
					before.setStatus(action.getAfterStatus().getLabel());
					DB.persist(before);
					if (action.getAfterText() != null)
						texts.add(action.getAfterText());
					refresh = true;
				}
			}
		}

		if (characterActions.size() > 0) {
			for (ThingThingOnCharacter action : characterActions) {
				Character before = action.getBeforeStatus().getCharacter();
				if (before.getStatus() == action.getBeforeStatus()) {
					if (action.getAfterStatus() != null)
						before.setStatus(action.getAfterStatus().getLabel());
					if (action.getToLocation() != null)
						before.setLocation(action.getToLocation());
					DB.persist(before);
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
			return new Results(false, false, noSuchThing(cName));
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
			return new Results(false, false, noSuchThing(sName));
		}
		else if (verb.getSupplementPosition() == Word.Position.inventory && !supplementIsInInventory)
			return new Results(false, false, supplementNotInInventory());
		else if (verb.getSupplementPosition() == Word.Position.location && supplementIsInInventory)
			return new Results(false, false, supplementNotInLocation());
		return null;
	}

	protected String noSuchThing(Word word) {
		return "No " + word.getText() + " can be seen here.";
	}

	@Override
	protected String doneFeedback() {
		return "You " + verb.getText() + " the " + cName.getText() + " " + preposition.getText() + " the " + sName.getText() + ".";
	}

	protected String complementNotInInventory() {
		return "You do not possess the " + cName.getText();
	}

	protected String complementNotInLocation() {
		return "You must drop the " + cName.getText() + " first";
	}

	protected String supplementNotInInventory() {
		return "You do not possess the " + sName.getText();
	}

	protected String supplementNotInLocation() {
		return "You must drop the " + sName.getText() + " first";
	}
}
