package io.rik72.brew.game.ui;

import java.util.ArrayList;
import java.util.List;

import io.rik72.brew.engine.processing.execution.Future;

public class TextPlayer {

	private List<String> header = new ArrayList<>();
	private List<String> pages = new ArrayList<>();
	private Future onFinish;
	private String defaultAction = "continue";
	private String finishAction = "continue";

	public List<String> getHeader() {
		return header;
	}

	public List<String> getPages() {
		return pages;
	}

	public void setOnFinish(Future onFinish) {
		this.onFinish = onFinish;
	}

	public void setFinishAction(String finishAction) {
		this.finishAction = finishAction;
	}

	public void play() {

		Terminal.get().skip(1);
		for (String line : header)
			Terminal.get().println(line);

		if (pages.size() > 0)
			Terminal.get().pressEnterToContinue(new Future() {
				@Override
				public void onSuccess() {
					Terminal.get().pull(2);
					next();
				}
			});
	}

	public void next() {

		Terminal.get().printLongText(pages.remove(0));

		Terminal.get().pressEnterTo(new Future() {
			@Override
			public void onSuccess() {
				if (pages.size() > 0) {
					Terminal.get().pull(2);
					next();
				}
				else {
					if (onFinish != null)
						onFinish.onSuccess();
				}
			}
		}, pages.size() > 0 ? defaultAction : finishAction);
	}
}