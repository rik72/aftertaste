package io.rik72.brew.engine.processing.execution.actions.two;

import java.util.Vector;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.entities.abstractions.Complement;
import io.rik72.brew.engine.db.repositories.CharacterRepository;
import io.rik72.brew.engine.db.repositories.ThingRepository;
import io.rik72.brew.engine.processing.execution.Results;
import io.rik72.brew.engine.processing.execution.actions.one.OneActionExecutor;
import io.rik72.brew.engine.utils.TextUtils;

public class TwoActionExecutor extends OneActionExecutor {

	protected Word preposition;
	protected Word sName;
	protected Complement supplement;
	protected boolean supplementIsInInventory;

	public TwoActionExecutor(Vector<Word> words, boolean toBeConfirmed) {
		super(words, toBeConfirmed);
		this.preposition = words.get(2);
		this.sName = words.get(3);
		resolveSupplement();
	}

	protected TwoActionExecutor(Vector<Word> words, boolean toBeConfirmed, Word verb, Character subject, String additionalFeedback,
								Word cName, Complement complement, boolean complementIsInInventory,
								Word preposition,
								Word sName, Complement supplement, boolean supplementIsInInventory) {
		super(words, toBeConfirmed, verb, subject, additionalFeedback, cName, complement, complementIsInInventory);
		this.preposition = preposition;
		this.sName = sName;
		this.supplement = supplement;
		this.supplementIsInInventory = supplementIsInInventory;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Results execute() throws Exception {

		Results results = checkVerb();
		if (results != null)
			return results;

		String className = "TwoAction" + TextUtils.ucFirst(verb.getCanonical().getText());

		Class<? extends TwoActionExecutor> commandClass;
		try {
			commandClass = (Class<TwoActionExecutor>)
				Class.forName("io.rik72.brew.engine.processing.execution.actions.two." + className);
		}
		catch (ClassNotFoundException e) {
			commandClass = TwoActionDo.class;
		}
		TwoActionExecutor action = commandClass.getDeclaredConstructor(
			Vector.class, boolean.class, Word.class, Character.class, String.class,
			Word.class, Complement.class, boolean.class, Word.class, Word.class, Complement.class, boolean.class).newInstance(
				words, toBeConfirmed, verb, subject, additionalFeedback,
				cName, complement, complementIsInInventory,
				preposition,
				sName, supplement, supplementIsInInventory);
		return action.execute();
	}

	protected void resolveSupplement() {

		supplement = ThingRepository.get().getVisibleByLocationAndCanonical(subject.getLocation(), sName.getCanonical().getText());
		if (supplement == null) {
			supplement = ThingRepository.get().getVisibleByLocationAndCanonical(subject.getInventory(), sName.getCanonical().getText());
			if (supplement != null)
				supplementIsInInventory = true;
		}

		if (supplement == null) {
			supplement = CharacterRepository.get().getVisibleByLocationAndCanonical(subject.getLocation(), sName.getCanonical().getText());
		}

		if (supplement == null) {
			if (subject.getLocation().getStatus().getCanonical().equals(sName.getCanonical().getText()))
				supplement = subject.getLocation();
		}
	}

}
