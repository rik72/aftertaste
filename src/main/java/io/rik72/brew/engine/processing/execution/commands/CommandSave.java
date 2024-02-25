package io.rik72.brew.engine.processing.execution.commands;

import java.io.File;
import java.util.Vector;

import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.processing.execution.Future;
import io.rik72.brew.engine.processing.execution.Results;
import io.rik72.brew.game.savegames.SaveGame;
import io.rik72.brew.game.ui.Terminal;
import io.rik72.mammoth.db.DB;
import io.rik72.vati.locale.Translations;

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
