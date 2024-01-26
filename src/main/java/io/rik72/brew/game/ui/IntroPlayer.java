package io.rik72.brew.game.ui;

import java.util.List;

import io.rik72.brew.engine.processing.execution.Future;
import io.rik72.brew.engine.story.Story;

public class IntroPlayer {
	public void play() {
		String title = Story.get().getTitle();
		String subtitle = Story.get().getSubtitle().toLowerCase().replace("\n", "");

		Terminal.get().skip(1);
		Terminal.get().println("=======================================");
		Terminal.get().println(title);
		Terminal.get().println(" ( " + subtitle + " )");
		Terminal.get().println("=======================================");

		Terminal.get().pressEnterToContinue(new Future() {
			@Override
			public void onSuccess() {
				Terminal.get().pull(2);
				intro(Story.get().getIntro());
			}
		});
	}

	public void intro(List<String> chunks) {

		Terminal.get().printLongText(chunks.remove(0));

		Terminal.get().pressEnterToContinue(new Future() {
			@Override
			public void onSuccess() {
				if (chunks.size() > 0) {
					Terminal.get().pull(2);
					intro(chunks);
				}
				else {
					Terminal.get().pull(2);
					Terminal.get().hilightln("~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~");
		            Terminal.get().showLocation();
					Terminal.get().waitForInput();
				}
			}
		});
	}
}