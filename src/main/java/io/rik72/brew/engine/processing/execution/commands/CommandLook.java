package io.rik72.brew.engine.processing.execution.commands;

import java.util.Vector;

import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.processing.execution.Results;
import io.rik72.brew.game.ui.Terminal;

public class CommandLook extends CommandExecutor {

	protected CommandLook(Vector<Word> words, boolean toBeConfirmed) {
		super(words, toBeConfirmed);
	}

	@Override
	public Results execute() throws Exception {
		Terminal.get().showLocation();
		return new Results(true, false, "");
	}
}
