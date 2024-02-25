package io.rik72.brew.game.ui;

import io.rik72.aftertaste.config.Config;
import io.rik72.brew.engine.db.entities.Location;
import io.rik72.brew.engine.db.entities.TextGroup;
import io.rik72.brew.engine.db.repositories.CharacterXTextGroupRepository;
import io.rik72.brew.engine.processing.execution.base.Executor;
import io.rik72.brew.engine.processing.execution.base.Future;
import io.rik72.brew.engine.processing.execution.base.Results;
import io.rik72.brew.engine.processing.parsing.InputParser;
import io.rik72.brew.engine.story.Story;
import io.rik72.brew.game.ui.base.TerminalBase;
import io.rik72.brew.game.ui.base.TextPlayer;
import io.rik72.mammoth.db.DB;
import io.rik72.vati.locale.Translations;

public class Terminal extends TerminalBase {

	private static TextGroup finale;

	private String lastLocationDescription;

	private boolean inGame = false;

	private Terminal() {}

	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}

	public void intro() {
		intro(null);
	}

	public void intro(String forcedIntro) {
		setInGame(true);

		TextPlayer player = new TextPlayer();

		String title = Story.get().getDescriptor().getTitle();
		String subtitle = Story.get().getDescriptor().getSubtitle().toLowerCase().strip();

		player.getHeader().add("");
		player.getHeader().add("");
		player.getHeader().add(".");
		player.getHeader().add("...");
		player.getHeader().add("....." );
		player.getHeader().add(".......");
		player.getHeader().add(".........");
		player.getHeader().add("...........");
		player.getHeader().add(".............");
		player.getHeader().add("...............");
		player.getHeader().add(".................");
		player.getHeader().add("...................");
		player.getHeader().add(".....................");
		player.getHeader().add(".. Aftertaste .........");
		player.getHeader().add(".........................");
		player.getHeader().add("........ v" + Config.get().get("application.version") + " ...........");
		player.getHeader().add(".............................");
		player.getHeader().add("...............................");
		player.getHeader().add(".................................");
		player.getHeader().add("...................................");
		player.getHeader().add(".....................................");
		player.getHeader().add(".......................................");
		player.getHeader().add("");
		player.getHeader().add("");
		player.getHeader().add("");
		player.getHeader().add("===============================================");
		player.getHeader().add(title);
		player.getHeader().add(" ( " + subtitle + " )");
		player.getHeader().add("===============================================");
		if (forcedIntro == null)
			player.getPages().addAll(Story.get().getIntro());
		else
			player.getPages().add(forcedIntro);
		player.setOnFinish(new Future() {
			@Override
			public void onSuccess() {
				Terminal.get().pull(1);
				Terminal.get().skip(1);
				Terminal.get().hilightln("\n~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~");
				Terminal.get().showLocation();
				Terminal.get().openInput();
			}			
		});
		player.setFinishAction("begin_adventuring");

		Story.get().getMainCharacter().remember(Story.get().getIntroId());

		player.start();
	}

	public void transition(TextGroup transition) {

		TextPlayer player = new TextPlayer();

		player.getPages().addAll(transition.getTextStrings());

		Story.get().getMainCharacter().remember(transition);

		player.start();
	}

	public void finale(TextGroup finale) {
		TextPlayer player = new TextPlayer();

		player.getPages().addAll(finale.getTextStrings());
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
		player.setFinishAction("finish_game");

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

		DB.commitTransaction(); // for safety
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
			DB.commitTransaction();  // begun at the start of executeInput(...)
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

			if (!checkForFinale(results, lastLineSkipped))
				checkForTransition(results, lastLineSkipped);
			
			DB.commitTransaction();  // begun at the start of executeInput(...)
		}
	}

	private boolean checkForTransition(Results results, boolean lastLineSkipped) {
		TextGroup characterTransition = Story.get().getMainCharacter().getStatus().getTransition();
		TextGroup resultsTransition = results.getTransition();
		TextGroup locationTransition = Story.get().getMainCharacter().getLocation().getStatus().getTransition();

		TextGroup transition = null;
		if (characterTransition != null)
			transition = characterTransition;
		else if (resultsTransition != null)
			transition = resultsTransition;
		else if (locationTransition != null)
			transition = locationTransition;

		if (transition != null && !CharacterXTextGroupRepository.get().existsByCharacterAndTextGroup(
			Story.get().getMainCharacter(), transition)) {
				skip(1);
				transition(transition);
				return true;
		}

		return false;
	}

	private boolean checkForFinale(Results results, boolean lastLineSkipped) {
		TextGroup characterFinale = Story.get().getMainCharacter().getStatus().getFinale();
		TextGroup resultsFinale = results.getFinale();
		TextGroup locationFinale = Story.get().getMainCharacter().getLocation().getStatus().getFinale();
		
		finale = null;
		if (characterFinale != null)
			finale = characterFinale;
		else if (resultsFinale != null)
			finale = resultsFinale;
		else if (locationFinale != null)
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
					printLongText(Translations.get("the_end"));
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
			return true;
		}

		return false;
	}

	///////////////////////////////////////////////////////////////////////////
	private static Terminal instance;
	public static Terminal get() {
		if (instance == null)
		 	instance = new Terminal();
		return instance;
	}

}
