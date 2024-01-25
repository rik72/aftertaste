package io.rik72.brew.engine.processing.execution.actions.direction;

import java.util.Vector;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.processing.execution.Results;
import io.rik72.brew.engine.processing.execution.actions.zero.ZeroActionExecutor;
import io.rik72.brew.engine.utils.TextUtils;

public class DirectionActionExecutor extends ZeroActionExecutor {

	protected Word direction;

	public DirectionActionExecutor(Vector<Word> words, boolean toBeConfirmed) {
		super(words, toBeConfirmed);
		this.direction = words.get(1);
	}

	protected DirectionActionExecutor(Vector<Word> words, boolean toBeConfirmed, Word verb, Character subject, String additionalFeedback, Word direction) {
		super(words, toBeConfirmed, verb, subject, additionalFeedback);
		this.direction = direction;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Results execute() throws Exception {
		String className = "DirectionAction" + TextUtils.ucFirst(verb.getCanonical().getText());
		Class<DirectionActionExecutor> commandClass = (Class<DirectionActionExecutor>)
			Class.forName("io.rik72.brew.engine.processing.execution.actions.direction." + className);
		DirectionActionExecutor action = commandClass.getDeclaredConstructor(
			Vector.class, boolean.class, Word.class, Character.class, String.class, Word.class).newInstance(
				words, toBeConfirmed, verb, subject, additionalFeedback, direction);
		return action.execute();
	}

	@Override
	protected String cantDoThat() {
		return "You can't " + verb.getText() + " that way.";
	}

	@Override
	protected String doneFeedback() {
		return "You " + verb.getText() + " " + direction.getCanonical().getText() + ".";
	}
}
