package io.rik72.brew.engine.processing.execution.actions.one;

import java.util.Vector;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.entities.abstractions.Complement;
import io.rik72.brew.engine.db.repositories.ThingRepository;
import io.rik72.brew.engine.processing.execution.Results;
import io.rik72.brew.engine.processing.execution.actions.zero.ZeroActionExecutor;
import io.rik72.brew.engine.utils.TextUtils;

public class OneActionExecutor extends ZeroActionExecutor {

	protected Word cName;
	protected Complement complement;
	protected boolean complementIsInInventory;

	public OneActionExecutor(Vector<Word> words, boolean toBeConfirmed) {
		super(words, toBeConfirmed);
		this.cName = words.get(1);
		complement = ThingRepository.get().getVisibleByLocationAndCanonical(subject.getLocation(), cName.getCanonical().getText());
		if (complement == null) {
			complement = ThingRepository.get().getVisibleByLocationAndCanonical(subject.getInventory(), cName.getCanonical().getText());
			if (complement != null)
				complementIsInInventory = true;
		}
	}

	protected OneActionExecutor(Vector<Word> words, boolean toBeConfirmed, Word verb, Character subject, String additionalFeedback,
								Word cName, Complement complement, boolean complementIsInInventory) {
		super(words, toBeConfirmed, verb, subject, additionalFeedback);
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
			Vector.class, boolean.class, Word.class, Character.class, String.class, Word.class, Complement.class, boolean.class).newInstance(
				words, toBeConfirmed, verb, subject, additionalFeedback, cName, complement, complementIsInInventory);
		return action.execute();
	}

	protected String noSuchThing() {
		return "No " + cName.getText() + " can be seen here.";
	}
}
