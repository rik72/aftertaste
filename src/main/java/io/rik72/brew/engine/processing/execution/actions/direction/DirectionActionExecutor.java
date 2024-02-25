package io.rik72.brew.engine.processing.execution.actions.direction;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.processing.execution.actions.zero.ZeroActionExecutor;
import io.rik72.brew.engine.processing.execution.base.Results;
import io.rik72.brew.engine.processing.parsing.mapping.WordMap;
import io.rik72.brew.engine.utils.TextUtils;
import io.rik72.vati.locale.Translations;

public class DirectionActionExecutor extends ZeroActionExecutor {

	protected Word direction;

	public DirectionActionExecutor(WordMap wordMap, boolean toBeConfirmed) {
		super(wordMap, toBeConfirmed);
		this.direction = wordMap.getComplement();
	}

	protected DirectionActionExecutor(WordMap wordMap, boolean toBeConfirmed, Word verb, Character subject, String additionalFeedback, Word direction) {
		super(wordMap, toBeConfirmed, verb, subject, additionalFeedback);
		this.direction = direction;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Results execute() throws Exception {
		String className = "DirectionAction" + TextUtils.ucFirst(verb.getCanonical().getText());
		Class<DirectionActionExecutor> commandClass = (Class<DirectionActionExecutor>)
			Class.forName("io.rik72.brew.engine.processing.execution.actions.direction." + className);
		DirectionActionExecutor action = commandClass.getDeclaredConstructor(
			WordMap.class, boolean.class, Word.class, Character.class, String.class, Word.class).newInstance(
				wordMap, toBeConfirmed, verb, subject, additionalFeedback, direction);
		return action.execute();
	}

	@Override
	protected String cantDoThat() {
		return Translations.get("you_cant_direction", verb.getCanonical().getText());
	}

	@Override
	protected String doneFeedback() {
		return Translations.get("you_done_direction", verb.getCanonical().getText(), direction.getCanonical().getText());
	}
}
