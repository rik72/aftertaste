package io.rik72.brew.engine.processing.execution.commands;

import java.util.Vector;

import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.processing.execution.Future;
import io.rik72.brew.engine.processing.execution.Results;
import io.rik72.brew.engine.story.Story;
import io.rik72.brew.game.ui.Terminal;

public class CommandRestart extends CommandExecutor {

	public CommandRestart(Vector<Word> words, boolean toBeConfirmed) {
		super(words, toBeConfirmed);
	}

	@Override
	public Results execute() {

		Terminal.get().confirm("Restart game?", new Future() {

			@Override
			public void onSuccess() {
				try {
					Story.get().restart();
					Terminal.get().println("Game restarted.");
				} catch (Exception e) {
					Terminal.get().hilightln("Error in restarting game (" + e.getClass().getSimpleName() + ": " + e.getMessage() + ")");
				}
				Terminal.get().consumeResults(new Results(true, true, "", true));
			}

			@Override
			public void onFailure() {
				Terminal.get().consumeResults(new Results(false, false, ""));
			}
			
		});

		return null;
	}
}
