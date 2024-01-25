package io.rik72.bottlerack.scene;

import javafx.stage.Stage;

public abstract class AbstractScene {
	protected Stage stage;

	public AbstractScene(Stage stage) {
		this.stage = stage;
	}

	public abstract void show();

	public Stage getStage() {
		return stage;
	}
}
