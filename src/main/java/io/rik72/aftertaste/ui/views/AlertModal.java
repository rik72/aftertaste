package io.rik72.aftertaste.ui.views;

import io.rik72.aftertaste.App;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertModal extends Stage {

	private VBox vBox;

	public AlertModal(int width, int height, int padding) {

		initModality(Modality.APPLICATION_MODAL);
		setResizable(false); 
		initOwner(App.getStage());

		vBox = new VBox();
		vBox.setStyle("-fx-background-color: -aft-color-window-modal-bg;");
		vBox.setPadding(new Insets(padding));
		BorderPane.setMargin(vBox, new Insets(10));

		BorderPane body = new BorderPane(vBox);
		body.setStyle("-fx-background-color: -aft-color-window-modal-border;");

		Scene dialogScene = new Scene(body, width, height);
		setScene(dialogScene);
		getScene().getStylesheets().add(ViewHelper.class.getResource("/css/custom.css").toExternalForm());
	}

	public VBox getVBox() {
		return vBox;
	}
}
