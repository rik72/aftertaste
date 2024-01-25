package io.rik72.brew.engine.processing.execution.commands;

import java.util.Vector;

import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.processing.execution.Future;
import io.rik72.brew.engine.processing.execution.Results;
import io.rik72.brew.game.ui.Terminal;
import io.rik72.mammoth.delta.Deltas;

public class CommandSave extends CommandExecutor {

	public CommandSave(Vector<Word> words, boolean toBeConfirmed) {
		super(words, toBeConfirmed);
	}

	@Override
	public Results execute() {
		Terminal.get().confirm("Save current game?", new Future() {

			@Override
			public void onSuccess() {
				try {
					Deltas.saveToFile("save0001.sav");
					Terminal.get().println("Game saved.", 2);
				} catch (Exception e) {
					Terminal.get().println("Error in saving game (" + e.getClass().getSimpleName() + ": " + e.getMessage() + ")", 2);
				}
				Terminal.get().consumeResults(new Results(true, false, ""));
			}

			@Override
			public void onFailure() {
				Terminal.get().consumeResults(new Results(false, false, ""));
			}
			
		});

		return null;
	}
}
