package io.rik72.brew.engine.processing.execution.commands;

import io.rik72.brew.engine.processing.execution.base.Results;
import io.rik72.brew.engine.processing.parsing.mapping.WordMap;
import io.rik72.brew.game.ui.Terminal;

public class CommandLook extends CommandExecutor {

	protected CommandLook(WordMap wordMap, boolean toBeConfirmed) {
		super(wordMap, toBeConfirmed);
	}

	@Override
	public Results execute() throws Exception {
		Terminal.get().showLocation();
		return new Results(true, false, "");
	}
}
