package io.rik72.bottlerack.view;

import java.util.HashMap;
import java.util.Map;

import javafx.stage.Stage;

public class ViewManager {
	private Map<String, AbstractView> views = new HashMap<>();
	private AbstractView openView;
	private Stage stage;

	public ViewManager(Stage stage) {
		this.stage = stage;
	}

	public Map<String, AbstractView> getViews() {
		return views;
	}

	public void openView(String label) {
		if (openView != null)
			openView.onClose();
		openView = views.get(label);
		openView.onOpen();
		stage.getScene().setRoot(openView.getRoot());
//        ViewHelper.applySkin();
	}
}
