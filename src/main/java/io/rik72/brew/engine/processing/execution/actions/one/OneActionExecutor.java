package io.rik72.brew.engine.processing.execution.actions.one;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.entities.abstractions.Complement;
import io.rik72.brew.engine.db.repositories.CharacterRepository;
import io.rik72.brew.engine.db.repositories.ThingRepository;
import io.rik72.brew.engine.processing.execution.actions.zero.ZeroActionExecutor;
import io.rik72.brew.engine.processing.execution.base.Results;
import io.rik72.brew.engine.processing.parsing.mapping.WordMap;
import io.rik72.brew.engine.utils.TextUtils;
import io.rik72.vati.locale.Translations;

public class OneActionExecutor extends ZeroActionExecutor {

	protected Word cName;
	protected Complement complement;
	protected boolean complementIsInInventory;

	public OneActionExecutor(WordMap wordMap, boolean toBeConfirmed) {
		super(wordMap, toBeConfirmed);
		this.cName = wordMap.getComplement();
		resolveComplement();
	}

	protected OneActionExecutor(WordMap wordMap, boolean toBeConfirmed, Word verb, Character subject, String additionalFeedback,
								Word cName, Complement complement, boolean complementIsInInventory) {
		super(wordMap, toBeConfirmed, verb, subject, additionalFeedback);
		this.cName = cName;
		this.complement = complement;
		this.complementIsInInventory = complementIsInInventory;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Results execute() throws Exception {

		String className = "OneAction" + TextUtils.ucFirst(verb.getCanonical().getText());

		Class<? extends OneActionExecutor> commandClass;
		try {
			commandClass = (Class<OneActionExecutor>)
				Class.forName("io.rik72.brew.engine.processing.execution.actions.one." + className);
		}
		catch (ClassNotFoundException e) {
			commandClass = OneActionDo.class;
		}
		OneActionExecutor action = commandClass.getDeclaredConstructor(
			WordMap.class, boolean.class, Word.class, Character.class, String.class, Word.class, Complement.class, boolean.class).newInstance(
				wordMap, toBeConfirmed, verb, subject, additionalFeedback, cName, complement, complementIsInInventory);
		return action.execute();
	}

	protected void resolveComplement() {

		complement = ThingRepository.get().getVisibleByLocationAndCanonical(subject.getLocation(), cName.getCanonical().getText());
		if (complement == null) {
			complement = ThingRepository.get().getVisibleByLocationAndCanonical(subject.getInventory(), cName.getCanonical().getText());
			if (complement != null)
				complementIsInInventory = true;
		}

		if (complement == null) {
			complement = CharacterRepository.get().getVisibleByLocationAndCanonical(subject.getLocation(), cName.getCanonical().getText());
		}

		if (complement == null) {
			if (subject.getLocation().getStatus().getCanonical().equals(cName.getCanonical().getText()))
				complement = subject.getLocation();
		}
	}

	protected String noSuchComplement() {
		switch (cName.getEntityType()) {
			case character:
				return Translations.get("cant_see_character", cName.getCanonical().getText());
			
			case location:
				return Translations.get("cant_see_location", cName.getCanonical().getText());

			default:
				return Translations.get("cant_see_thing", cName.getCanonical().getText());
		}
	}
}
