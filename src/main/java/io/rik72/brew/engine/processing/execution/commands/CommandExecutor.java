package io.rik72.brew.engine.processing.execution.commands;

import java.util.Vector;

import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.repositories.WordRepository;
import io.rik72.brew.engine.processing.execution.Executor;
import io.rik72.brew.engine.processing.execution.Results;
import io.rik72.brew.engine.utils.TextUtils;

public class CommandExecutor extends Executor {

	public CommandExecutor(Vector<Word> words, boolean toBeConfirmed) {
		super(words, toBeConfirmed);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Results execute() throws Exception {
		Word commandWord = WordRepository.get().getByText(words.firstElement().getText());
		String className = "Command" + TextUtils.ucFirst(commandWord.getCanonical().getText());
		Class<CommandExecutor> commandClass = (Class<CommandExecutor>) Class.forName("io.rik72.brew.engine.processing.execution.commands." + className);
		CommandExecutor command = commandClass.getDeclaredConstructor(Vector.class, boolean.class).newInstance(words, toBeConfirmed);
		return command.execute();
	}
}
