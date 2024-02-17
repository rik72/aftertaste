package io.rik72.aftertaste.ui.views;

import io.rik72.aftertaste.App;
import io.rik72.aftertaste.ui.skin.CurrentSkin;
import io.rik72.brew.engine.processing.execution.Future;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ConfirmModal extends Stage {

	public ConfirmModal(String question, Future then) {
		initModality(Modality.APPLICATION_MODAL);
		initStyle((StageStyle.UNDECORATED));
		setResizable(false); 
		initOwner(App.getStage());

		VBox vBox = new VBox();
		vBox.setStyle("-fx-background-color: -aft-color-window-modal-bg;");
		vBox.setPadding(new Insets(20));
		HBox dialogButtonsBox = new HBox();
		dialogButtonsBox.setSpacing(20);

		Region spacerv = new Region();
        VBox.setVgrow(spacerv, Priority.ALWAYS);     
        spacerv.setMinWidth(Region.USE_PREF_SIZE);
		
		Region spacerl = new Region();
        HBox.setHgrow(spacerl, Priority.ALWAYS);     
        spacerl.setMinWidth(Region.USE_PREF_SIZE);
		Button yesButton = new Button("Yes");
		yesButton.setOnAction(e -> {
			close();
			then.onSuccess();
		});
		Button noButton = new Button("No");
		noButton.setOnAction(e -> {
			close();
			then.onFailure();
		});
		Region spacerr = new Region();
        HBox.setHgrow(spacerr, Priority.ALWAYS);     
        spacerr.setMinWidth(Region.USE_PREF_SIZE);

		dialogButtonsBox.getChildren().addAll(spacerl, yesButton, noButton, spacerr);
		Text questionText = new Text(question);
		questionText.setFont(CurrentSkin.getGUIFont());
		questionText.setStyle("-fx-fill: " + CurrentSkin.data.COLOR_WINDOW_TEXT);
		vBox.getChildren().addAll(questionText, spacerv, dialogButtonsBox);
		BorderPane.setMargin(vBox, new Insets(10));

		BorderPane body = new BorderPane(vBox);
		body.setStyle("-fx-background-color: -aft-color-window-modal-border;");

		Scene dialogScene = new Scene(body, 350, 140);
		setScene(dialogScene);
		getScene().getStylesheets().add(ViewHelper.class.getResource("/css/custom.css").toExternalForm());
        getScene().setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				Node node = getScene().focusOwnerProperty().get();
				if (node instanceof Button)
					((Button)node).fire();
			}
		});
	}
}
