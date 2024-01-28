package io.rik72.bottlerack.view;

import javafx.scene.Parent;
import javafx.stage.Stage;

public abstract class AbstractView {
	protected Stage stage;
	protected Parent root;
	
	public AbstractView(Stage stage) {
		this.stage = stage;
		root = create();
	}

	public Parent getRoot() {
		return root;
	}

	protected abstract Parent create();

	public abstract void onOpen();

	public abstract void onClose();
}
