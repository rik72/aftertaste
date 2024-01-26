package io.rik72.brew.engine.processing.execution.actions.one;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.Location;
import io.rik72.brew.engine.db.entities.Thing;
import io.rik72.brew.engine.db.entities.CharacterOneAction;
import io.rik72.brew.engine.db.entities.LocationOneAction;
import io.rik72.brew.engine.db.entities.ThingOneAction;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.repositories.LocationRepository;
import io.rik72.brew.engine.db.repositories.CharacterOneActionRepository;
import io.rik72.brew.engine.db.repositories.LocationOneActionRepository;
import io.rik72.brew.engine.db.repositories.ThingOneActionRepository;
import io.rik72.brew.engine.processing.execution.Results;
import io.rik72.mammoth.db.DB;

public class OneActionDo extends OneActionExecutor {

	protected OneActionDo(Vector<Word> words, boolean toBeConfirmed, Word verb, Character subject, String additionalFeedback,
	                      Word cName, Thing complement, boolean complementIsInInventory) {
		super(words, toBeConfirmed, verb, subject, additionalFeedback, cName, complement, complementIsInInventory);
	}

	@Override
	public Results execute() throws Exception {

		Results results = checkComplement();
		if (results != null)
			return results;

		List<ThingOneAction> thingActions =
			ThingOneActionRepository.get().findByParts(verb.getCanonical(), complement.getStatus());
		List<LocationOneAction> locationActions =
			LocationOneActionRepository.get().findByParts(verb.getCanonical(), complement.getStatus());
		List<CharacterOneAction> characterActions =
			CharacterOneActionRepository.get().findByParts(verb.getCanonical(), complement.getStatus());

		if (!thingActions.isEmpty() || !locationActions.isEmpty() || !characterActions.isEmpty())
			setDoable(true);

		results = checkVerb();
		if (results != null)
			return results;

		boolean refresh = false;

		List<String> texts = new ArrayList<>();
		if (thingActions.size() > 0) {
			for (ThingOneAction action : thingActions) {
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
			for (LocationOneAction action : locationActions) {
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
			for (CharacterOneAction action : characterActions) {
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
			return new Results(false, false, noSuchThing());
		}
		else if (verb.getComplement() == Word.Position.inventory && !complementIsInInventory)
			return new Results(false, false, complementNotInInventory());
		else if (verb.getComplement() == Word.Position.location && complementIsInInventory)
			return new Results(false, false, complementNotInLocation());
		return null;
	}

	@Override
	protected String doneFeedback() {
		return "You " + verb.getText() + " the " + cName.getText() + ".";
	}

	protected String complementNotInInventory() {
		return "You do not possess the " + cName.getText();
	}

	protected String complementNotInLocation() {
		return "You must drop the " + cName.getText() + " first";
	}
}
