package io.rik72.brew.engine.processing.execution.commands;

import java.io.File;
import java.util.Vector;

import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.processing.execution.Future;
import io.rik72.brew.engine.processing.execution.Results;
import io.rik72.brew.engine.story.Story;
import io.rik72.brew.game.savegames.SaveGame;
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
					File file = Terminal.get().chooseOpenFile("Load game");
                    if (file != null) {
						SaveGame.loadFromFile(file.getPath());
						if (SaveGame.getInstance().checkStoryCompatibility(Story.get().getDescriptor().getRefId())) {
							Story.get().restart();
							Deltas.set(SaveGame.getInstance().getDeltas());
							Story.get().applyDeltas();
							Terminal.get().println("Game loaded.");
							Terminal.get().consumeResults(new Results(true, true, "", false, true));
						}
						else {
							Terminal.get().hilightln("Incompatible save file " + 
								"('" + SaveGame.getInstance().getStoryDescriptor().getRefId() + "' format vs '" + Story.get().getDescriptor().getRefId() + "'" +
								" required by current story)");
							Terminal.get().consumeResults(new Results(false, false, ""));
						}
                    }
				} catch (Exception e) {
					Terminal.get().hilightln("Error in loading game (" + e.getClass().getSimpleName() + ": " + e.getMessage() + ")");
					Terminal.get().consumeResults(new Results(false, false, ""));
				}
			}

			@Override
			public void onFailure() {
				Terminal.get().consumeResults(new Results(false, false, ""));
			}
			
		});

		return null;
	}
}
