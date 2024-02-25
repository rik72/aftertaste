package io.rik72.brew.engine.processing.execution.commands;

import io.rik72.brew.engine.processing.execution.base.Future;
import io.rik72.brew.engine.processing.execution.base.Results;
import io.rik72.brew.engine.processing.parsing.mapping.WordMap;
import io.rik72.brew.game.BrewController;
import io.rik72.brew.game.ui.Terminal;
import io.rik72.mammoth.db.DB;

public class CommandQuit extends CommandExecutor {

	public CommandQuit(WordMap wordMap, boolean toBeConfirmed) {
		super(wordMap, toBeConfirmed);
	}

	@Override
	public Results execute() {

		Terminal.get().confirm("Quit without saving?", new Future() {

			@Override
			public void onSuccess() {
				Terminal.get().println("Quitting...");
				try {
					BrewController.clear();
					BrewController.setCurrentStory(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Terminal.get().close();
			}

			@Override
			public void onFailure() {
				Terminal.get().consumeResults(new Results(false, false, ""));
			}
			
		});

		DB.commitTransaction();  // begun at the start of executeInput(...)
		return null;
	}
}
