package io.rik72.brew.game.ui;

import io.rik72.brew.engine.db.entities.Location;
import io.rik72.brew.engine.processing.execution.Executor;
import io.rik72.brew.engine.processing.execution.Future;
import io.rik72.brew.engine.processing.execution.Results;
import io.rik72.brew.engine.processing.parsing.InputParser;
import io.rik72.brew.engine.story.Story;
import io.rik72.brew.game.ui.base.TerminalBase;
import io.rik72.brew.game.ui.base.TextPlayer;
import io.rik72.brew.game.ui.loader.TerminalLoader;
import io.rik72.mammoth.db.DB;

public class Terminal extends TerminalBase {

	private static String finale;

	private String lastLocationDescription;

	private boolean inGame = false;

	private Terminal() {}

	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}

	public void init() {
		new TerminalLoader().register();
	}

	public void intro() {
		intro(null);
	}

	public void intro(String forcedIntro) {
		setInGame(true);

		TextPlayer player = new TextPlayer();

		String title = Story.get().getTitle();
		String subtitle = Story.get().getSubtitle().toLowerCase().strip();
		player.getHeader().add("=======================================");
		player.getHeader().add(title);
		player.getHeader().add(" ( " + subtitle + " )");
		player.getHeader().add("=======================================");
		if (forcedIntro == null)
			player.getPages().addAll(Story.get().getIntro());
		else
			player.getPages().add(forcedIntro);
		player.setOnFinish(new Future() {
			@Override
			public void onSuccess() {
				Terminal.get().pull(2);
				Terminal.get().hilightln("~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~");
				Terminal.get().showLocation();
				Terminal.get().openInput();
			}			
		});
		player.setFinishAction("begin adventuring...");

		player.start();
	}

	public void finale(final String finale) {
		TextPlayer player = new TextPlayer();

		player.getPages().add(finale);
		player.getFooter().add("* * * * * * * * * * * * * * * * * * * *");
		player.getFooter().add("");
		player.getFooter().add("               T H E    E N D");
		player.getFooter().add("");
		player.getFooter().add("* * * * * * * * * * * * * * * * * * * *");
		player.setOnFinish(new Future() {
			@Override
			public void onSuccess() {
				setInGame(false);
				close();
			}			
		});
		player.setFinishAction("finish game");

		player.start();
	}

	public void showLocation() {
		Location location = Story.get().getMainCharacter().getLocation();
		printLocationImage(location);
		lastLocationDescription = location.getDescription();
		skip(1);
		emphasisLongText(lastLocationDescription);
		skip(1);
	}

	public Results executeInput(String input) throws Exception {

		DB.beginTransaction(); // committed at the end of consumeResults(...)

		Executor executor = InputParser.get().parse(input);
		Results results = null;

		if (executor != null) {
			// boolean confirmed = false;
			// if (executor.isToBeConfirmed()) {
			// 	if (confirm("Did you simply mean \"" + TextUtils.wordsToText(executor.getWords()) + "\" ?")) {
			// 		println(TextUtils.wordsToText(executor.getWords()), 0);
			// 		confirmed = true;
			// 	}
			// }
			// else {
			// 	confirmed = true;
			// }
			// if (confirmed) {
				results = executor.execute();
			// }
		}
		else {
			println("I can't understand.");
		}

		return results;
	}

	public void consumeResults(Results results) {

		if (results != null) {
			if (results.getFeedback() != null && results.getFeedback().length() > 0 || results.getTexts().size() > 0) {
				if (results.getFeedback().length() > 0)
					if (results.isEmphasis())
						emphasisLongText(results.getFeedback());
					else
						printLongText(results.getFeedback());
				if (results.getTexts().size() > 0)
					for (String text : results.getTexts())
						printLongText(text);
			}

			boolean lastLineSkipped = false;
			if (results.isRefresh() && !Story.get().getMainCharacter().getLocation().getDescription().equals(lastLocationDescription)) {
				Terminal.get().showLocation();
				lastLineSkipped = true;
			}
			
			String characterFinale = Story.get().getMainCharacter().getStatus().getFinale();
			String locationFinale = Story.get().getMainCharacter().getLocation().getStatus().getFinale();
			
			finale = null;
			if (characterFinale != null && characterFinale.length() > 0)
				finale = characterFinale;
			else if (locationFinale != null && locationFinale.length() > 0)
				finale = locationFinale;

			if (finale != null) {
				if (!lastLineSkipped)
					skip(1);
				Terminal.get().hilightln("~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~");
				closeInput();
				// dummy - required since the bubbling of last ENTER keypress is not done yet
				pressEnterToContinue(new Future() {
					@Override
					public void onSuccess() {
						pull(2);
						printLongText(
							"It seems your story has come to an end.\n" +
							"Maybe it's not The End, and maybe it's not the end we thought for you from the start, but it is an ending anyway...");
						// start finale text slideshow
						pressEnterToContinue(new Future() {
							@Override
							public void onSuccess() {
								pull(2);
								skip(1);
								finale(finale);
							}
						});
					}
				});
			}
			DB.commitTransaction();  // begun at the start of executeInput(...)
		}
	}

	///////////////////////////////////////////////////////////////////////////
	private static Terminal instance;
	public static Terminal get() {
		if (instance == null)
		 	instance = new Terminal();
		return instance;
	}

}
