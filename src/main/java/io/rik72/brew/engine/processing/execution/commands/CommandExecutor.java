package io.rik72.brew.engine.processing.execution.commands;

import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.repositories.WordRepository;
import io.rik72.brew.engine.processing.execution.base.Executor;
import io.rik72.brew.engine.processing.execution.base.Results;
import io.rik72.brew.engine.processing.parsing.mapping.WordMap;
import io.rik72.brew.engine.utils.TextUtils;

public class CommandExecutor extends Executor {

	public CommandExecutor(WordMap wordMap, boolean toBeConfirmed) {
		super(wordMap, toBeConfirmed);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Results execute() throws Exception {
		Word commandWord = WordRepository.get().getByText(wordMap.getVerb().getText());
		String className = "Command" + TextUtils.ucFirst(commandWord.getCanonical().getText());
		Class<CommandExecutor> commandClass = (Class<CommandExecutor>) Class.forName("io.rik72.brew.engine.processing.execution.commands." + className);
		CommandExecutor command = commandClass.getDeclaredConstructor(WordMap.class, boolean.class).newInstance(wordMap, toBeConfirmed);
		return command.execute();
	}
}
