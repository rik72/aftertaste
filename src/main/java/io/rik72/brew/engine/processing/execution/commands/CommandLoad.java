package io.rik72.brew.engine.processing.execution.commands;

import java.util.Vector;

import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.processing.execution.Future;
import io.rik72.brew.engine.processing.execution.Results;
import io.rik72.brew.engine.story.Story;
import io.rik72.brew.game.ui.Terminal;
import io.rik72.mammoth.delta.Deltas;

public class CommandLoad extends CommandExecutor {

	public CommandLoad(Vector<Word> words, boolean toBeConfirmed) {
		super(words, toBeConfirmed);
	}

	@Override
	public Results execute() {
		
		Terminal.get().confirm("Load saved game?", new Future() {

			@Override
			public void onSuccess() {
				try {
					Story.get().restart();
					Deltas.loadFromFile("save0001.sav");
					Story.get().applyDeltas();
					Terminal.get().println("Game loaded.", 2);
					Terminal.get().println("...", 2);
				} catch (Exception e) {
					Terminal.get().println("Error in loading game (" + e.getClass().getSimpleName() + ": " + e.getMessage() + ")", 2);
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
