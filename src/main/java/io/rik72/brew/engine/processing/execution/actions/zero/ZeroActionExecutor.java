package io.rik72.brew.engine.processing.execution.actions.zero;

import java.util.List;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.CharacterStatusPossibility;
import io.rik72.brew.engine.db.entities.LocationStatusPossibility;
import io.rik72.brew.engine.db.entities.TextGroup;
import io.rik72.brew.engine.db.entities.ThingStatusPossibility;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.entities.abstractions.Possibility;
import io.rik72.brew.engine.db.repositories.CharacterStatusPossibilityRepository;
import io.rik72.brew.engine.db.repositories.LocationStatusPossibilityRepository;
import io.rik72.brew.engine.db.repositories.ThingStatusPossibilityRepository;
import io.rik72.brew.engine.processing.execution.base.Executor;
import io.rik72.brew.engine.processing.execution.base.Results;
import io.rik72.brew.engine.processing.parsing.mapping.WordMap;
import io.rik72.brew.engine.story.Story;
import io.rik72.brew.engine.utils.TextUtils;
import io.rik72.vati.locale.Translations;

public class ZeroActionExecutor extends Executor {

	protected Word verb;
	protected Character subject;
	protected String additionalFeedback;
	protected String possibilityFeedback;
	protected boolean doable;

	public ZeroActionExecutor(WordMap wordMap, boolean toBeConfirmed) {
		super(wordMap, toBeConfirmed);
		this.verb = wordMap.getVerb();
		this.subject = Story.get().getMainCharacter();
	}

	protected ZeroActionExecutor(WordMap wordMap, boolean toBeConfirmed, Word verb, Character subject, String additionalFeedback) {
		super(wordMap, toBeConfirmed);
		this.verb = verb;
		this.subject = subject;
		this.additionalFeedback = additionalFeedback;
	}

	protected boolean isDoable() {
		return doable;
	}

	protected void setDoable(boolean doable) {
		this.doable = doable;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Results execute() throws Exception {

		setDoable(true);

		Results results = checkVerb();
		if (results != null)
			return results;

		String className = "ZeroAction" + TextUtils.ucFirst(verb.getCanonical().getText());
		Class<ZeroActionExecutor> commandClass = (Class<ZeroActionExecutor>)
			Class.forName("io.rik72.brew.engine.processing.execution.actions.zero." + className);
		ZeroActionExecutor action = commandClass.getDeclaredConstructor(
			WordMap.class, boolean.class, Word.class, Character.class, String.class).newInstance(
				wordMap, toBeConfirmed, verb, subject, additionalFeedback);
		return action.execute();
	}

	protected String doneFeedback() {
		return Translations.get("done_0");
	}

	protected String alreadyDoneFeedback() {
		return Translations.get("already_done_0");
	}

	protected String cantDoThat() {
		return thisCantDoThat();
	}

	private String thisCantDoThat() {
		return Translations.get("you_cant_do_that");
	}

	protected Results checkVerb() {

		Possibility locationConclusion = null;
		List<LocationStatusPossibility> locationPossibilities = LocationStatusPossibilityRepository.get().findByLocation(subject.getLocation());
		for (LocationStatusPossibility possibility : locationPossibilities)
			if (possibility.getAction().getCanonical() == verb.getCanonical())
				locationConclusion = possibility;

		Possibility thingConclusion = null;
		List<ThingStatusPossibility> thinglocationPossibilities = ThingStatusPossibilityRepository.get().findByInventory(subject.getInventory());
		for (ThingStatusPossibility possibility : thinglocationPossibilities)
			if (possibility.getAction().getCanonical() == verb.getCanonical())
				thingConclusion = possibility;

		Possibility characterConclusion = null;
		List<CharacterStatusPossibility> characterPossibilities = CharacterStatusPossibilityRepository.get().findByCharacter(subject);
		for (CharacterStatusPossibility possibility : characterPossibilities)
			if (possibility.getAction().getCanonical() == verb.getCanonical())
				characterConclusion = possibility;

		Possibility conclusion = null;

		if (locationConclusion != null) {
			if (locationConclusion.isImportant())
				return buildPossibilityCheckResults(locationConclusion);
			conclusion = locationConclusion;
		}

		if (thingConclusion != null) {
			if (thingConclusion.isImportant())
				return buildPossibilityCheckResults(thingConclusion);
			if (conclusion == null || (!conclusion.isPossible() && !thingConclusion.isPossible()))
				conclusion = thingConclusion;
		}

		if (characterConclusion != null) {
			if (characterConclusion.isImportant())
				return buildPossibilityCheckResults(characterConclusion);
			if (thingConclusion == null || !thingConclusion.isPossible())
				if (conclusion == null || (!conclusion.isPossible() && !characterConclusion.isPossible()))
					conclusion = characterConclusion;
		}

		if (conclusion != null)
			return buildPossibilityCheckResults(conclusion);
		return null;
	}

	private Results buildPossibilityCheckResults(Possibility possibility) {
		Results results = null;
		if (!possibility.isPossible()) {
			if (possibility.getFeedback() != null && possibility.getFeedback().length() > 0)
				possibilityFeedback = possibility.getFeedback();
			results = buildResults(false, false, thisCantDoThat(), null, null);
		}
		else {
			possibilityFeedback = possibility.getFeedback();
		}
		return results;
	}

	protected Results buildResults(boolean success, boolean refresh, String feedback, TextGroup transition, TextGroup finale) {
		return buildResults(success, refresh, feedback, transition, finale, false, false);
	}

	protected Results buildResults(boolean success, boolean refresh, String feedback, TextGroup transition, TextGroup finale, boolean emphasis) {
		return buildResults(success, refresh, feedback, transition, finale, emphasis, false);
	}

	protected Results buildResults(boolean success, boolean refresh, String feedback, TextGroup transition, TextGroup finale, boolean emphasis, boolean restart) {
		Results results = new Results(success, refresh, feedback, transition, finale, emphasis, restart);
		mergeFeedbacks(results);
		return results;
	}

	protected void mergeFeedbacks(Results results) {
		if (isDoable() && possibilityFeedback != null && possibilityFeedback.length() > 0)
			results.getTexts().add(possibilityFeedback);
		if (results.isSuccess() && additionalFeedback != null && additionalFeedback.length() > 0)
			results.getTexts().add(additionalFeedback);
	}
}
