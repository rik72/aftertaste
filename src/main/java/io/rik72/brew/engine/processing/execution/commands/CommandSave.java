package io.rik72.brew.engine.processing.execution.commands;

import java.io.File;

import io.rik72.brew.engine.processing.execution.base.Future;
import io.rik72.brew.engine.processing.execution.base.Results;
import io.rik72.brew.engine.processing.parsing.mapping.WordMap;
import io.rik72.brew.game.savegames.SaveGame;
import io.rik72.brew.game.ui.Terminal;
import io.rik72.mammoth.db.DB;
import io.rik72.vati.locale.Translations;

public class CommandSave extends CommandExecutor {

	public CommandSave(WordMap wordMap, boolean toBeConfirmed) {
		super(wordMap, toBeConfirmed);
	}

	@Override
	public Results execute() {
		Terminal.get().confirm("Save current game?", new Future() {

			@Override
			public void onSuccess() {
				try {
					File file = Terminal.get().chooseSaveFile("Save game");
                    if (file != null) {
						SaveGame.saveToFile(file.getPath());
						Terminal.get().println(Translations.get("game_saved"));
					}
				} catch (Exception e) {
					Terminal.get().hilightln("Error in saving game (" + e.getClass().getSimpleName() + ": " + e.getMessage() + ")");
				}
				Terminal.get().consumeResults(new Results(true, false, ""));
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
