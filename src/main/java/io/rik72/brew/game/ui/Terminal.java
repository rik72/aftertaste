package io.rik72.brew.game.ui;

import java.io.File;

import io.rik72.aftertaste.ui.ux.TerminalUX;
import io.rik72.brew.engine.db.entities.Location;
import io.rik72.brew.engine.processing.execution.Executor;
import io.rik72.brew.engine.processing.execution.Future;
import io.rik72.brew.engine.processing.execution.Results;
import io.rik72.brew.engine.processing.parsing.InputParser;
import io.rik72.brew.engine.story.Story;
import io.rik72.brew.game.ui.loader.TerminalLoader;

public class Terminal {

	private static String finale;

	private TerminalUX ux;
	private String lastLocationDescription;

	private Terminal() {}

	public void init() {
		new TerminalLoader().register();
	}

	public void intro() {
		TextPlayer player = new TextPlayer();

		String title = Story.get().getTitle();
		String subtitle = Story.get().getSubtitle().toLowerCase().strip();
		player.getHeader().add("=======================================");
		player.getHeader().add(title);
		player.getHeader().add(" ( " + subtitle + " )");
		player.getHeader().add("=======================================");
		player.getPages().addAll(Story.get().getIntro());
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
				System.exit(0);
			}			
		});
		player.setFinishAction("exit");

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

		if (results.getFeedback() != null && results.getFeedback().length() > 0 || results.getTexts().size() > 0) {
			if (results.getFeedback().length() > 0)
				printLongText(results.getFeedback());
			if (results.getTexts().size() > 0)
				for (String text : results.getTexts())
					printLongText(text);
		}

		if (results.isRefresh() && !Story.get().getMainCharacter().getLocation().getDescription().equals(lastLocationDescription))
			Terminal.get().showLocation();
		
		String characterFinale = Story.get().getMainCharacter().getStatus().getFinale();
		String locationFinale = Story.get().getMainCharacter().getLocation().getStatus().getFinale();
		
		finale = null;
		if (characterFinale != null && characterFinale.length() > 0)
			finale = characterFinale;
		else if (locationFinale != null && locationFinale.length() > 0)
			finale = locationFinale;

		if (finale != null) {
			closeInput();
			pull(1);
			// dummy - required since the bubbling of last ENTER keypress is not done yet
			pressEnterToContinue(new Future() {
				@Override
				public void onSuccess() {
					pull(2);
					// start finale text slideshow
					pressEnterToContinue(new Future() {
						@Override
						public void onSuccess() {
							pull(2);
							finale(finale);
						}
					});
				}
			});
		}
	}

	///////////////////////////////////////////////////////////////////////////
	private static Terminal instance;
	public static Terminal get() {
		if (instance == null)
		 	instance = new Terminal();
		return instance;
	}

	///////////////////////////////////////////////////////////////////////////
	public void skip(int lines) {
		ux.skip(lines);
	}

	public void pull(int lines) {
		ux.pull(lines);
	}

	public void print(String text) {
		ux.println(text);
	}

	public void println(String text) {
		ux.println(text);
	}

	public void hilightln(String text) {
		ux.hilightln(text);
	}

	public void printLongText(String text) {
		ux.printLongText(text);
	}

	public void emphasisLongText(String text) {
		ux.emphasisLongText(text);
	}

	public void confirm(String question, Future then) {
		ux.confirm(question, then);
	}

	public void pressEnterToContinue(Future then) {
		ux.pressEnterToContinue(then);
	}

	public void pressEnterTo(Future then, String action) {
		ux.pressEnterTo(then, action);
	}

	public void openInput() {
		ux.openInput();
	}

	public void closeInput() {
		ux.closeInput();
	}

	public void printLocationImage(Location location) {
		ux.printLocationImage(location);
	}

	public File chooseOpenFile() {
		return ux.chooseOpenFile();
	}

	public File chooseSaveFile() {
		return ux.chooseSaveFile();
	}

	public void setUX(TerminalUX ux) {
		this.ux = ux;
	}
}
