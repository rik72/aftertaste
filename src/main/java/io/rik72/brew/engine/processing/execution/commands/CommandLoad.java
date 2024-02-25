package io.rik72.brew.engine.processing.execution.commands;

import java.io.File;

import io.rik72.brew.engine.processing.execution.base.Future;
import io.rik72.brew.engine.processing.execution.base.Results;
import io.rik72.brew.engine.processing.parsing.mapping.WordMap;
import io.rik72.brew.engine.story.Story;
import io.rik72.brew.game.savegames.SaveGame;
import io.rik72.brew.game.ui.Terminal;
import io.rik72.mammoth.delta.Deltas;
import io.rik72.vati.locale.Translations;

public class CommandLoad extends CommandExecutor {

	public CommandLoad(WordMap wordMap, boolean toBeConfirmed) {
		super(wordMap, toBeConfirmed);
	}

	@Override
	public Results execute() {
		
		Terminal.get().confirm("Load saved game?", new Future() {

			@Override
			public void onSuccess() {
				try {
					File file = Terminal.get().chooseOpenFile("Load game", "Savegame", "*.save");
                    if (file != null) {
						SaveGame.loadFromFile(file.getPath());
						if (SaveGame.getInstance().checkStoryCompatibility(Story.get().getDescriptor().getRefId())) {
							Story.get().restart();
							Deltas.set(SaveGame.getInstance().getDeltas());
							Story.get().applyDeltas();
							Terminal.get().println(Translations.get("game_loaded"));
							Terminal.get().consumeResults(new Results(true, true, "", null, null, false, true));
						}
						else {
							Terminal.get().hilightln(Translations.get("incompatible_save_file",
								SaveGame.getInstance().getStoryDescriptor().getRefId(),
								Story.get().getDescriptor().getRefId()));
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
