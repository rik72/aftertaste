package io.rik72.brew.engine.processing.execution.commands;

import java.util.Vector;

import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.processing.execution.Future;
import io.rik72.brew.engine.processing.execution.Results;
import io.rik72.brew.game.BrewController;
import io.rik72.brew.game.ui.Terminal;
import io.rik72.mammoth.db.DB;

public class CommandQuit extends CommandExecutor {

	public CommandQuit(Vector<Word> words, boolean toBeConfirmed) {
		super(words, toBeConfirmed);
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
					// TODO Auto-generated catch block
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
