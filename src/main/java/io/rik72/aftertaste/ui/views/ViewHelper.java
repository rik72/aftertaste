package io.rik72.aftertaste.ui.views;

import java.io.File;

import io.rik72.aftertaste.App;
import io.rik72.brew.engine.processing.execution.Future;
import io.rik72.brew.engine.story.Story;
import io.rik72.brew.game.BrewController;
import io.rik72.brew.game.savegames.SaveGame;
import io.rik72.brew.game.ui.Terminal;
import io.rik72.mammoth.delta.Deltas;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewHelper {

    public static void restart(boolean skipIntro) {

        App.openView("terminal");

        Terminal.get().removeTopImage();
        Terminal.get().closeInput();
        Terminal.get().clearTextFlow();

        try {
            BrewController.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

		if (!skipIntro)
        	Terminal.get().intro();
    }

	public static void load() throws Exception {
		File file = Terminal.get().chooseOpenFile();
		if (file != null) {
			SaveGame.loadFromFile(file.getPath());
			if (SaveGame.getInstance().checkStoryCompatibility(BrewController.getCurrentStory().getValue().getRefId())) {
				restart(true);
				Deltas.set(SaveGame.getInstance().getDeltas());
				Story.get().applyDeltas();
	        	Terminal.get().intro("Game loaded from " + file.getPath());
			}
			else {
				openErrorModal("Incompatible save file " + 
					"('" + SaveGame.getInstance().getStoryDescriptor().getRefId() + "' format vs '" + Story.get().getRefId() + "'" +
					" required by current story)", 320, 200);
			}
		}
	}

	public static void openErrorModal(String msg, int width , int height) {
		Stage modal = createModal(width, height, 20);
		VBox vBox = (VBox) modal.getScene().getRoot();
		vBox.getChildren().add(new Text(msg));
		modal.show();
	}

    public static Stage createModal(int width, int height, int padding) {
		Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.setResizable(false); 
		dialog.initOwner(App.getStage());
		VBox dialogVbox = new VBox();
		dialogVbox.setPadding(new Insets(padding));
		Scene dialogScene = new Scene(dialogVbox, width, height);
		dialog.setScene(dialogScene);
        return dialog;
    }

	public static void openConfirmModal(String question, Future then) {
		Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.setResizable(false); 
		dialog.initOwner(App.getStage());

		VBox dialogVbox = new VBox();
		dialogVbox.setPadding(new Insets(20));
		HBox dialogButtonsBox = new HBox();
		dialogButtonsBox.setSpacing(20);

		Region spacerv = new Region();
        VBox.setVgrow(spacerv, Priority.ALWAYS);     
        spacerv.setMinWidth(Region.USE_PREF_SIZE);
		
		Button yesButton = new Button("Yes");
		yesButton.setOnAction(e -> {
			dialog.close();
			then.onSuccess();
		});
		Region spacerl = new Region();
        HBox.setHgrow(spacerl, Priority.ALWAYS);     
        spacerl.setMinWidth(Region.USE_PREF_SIZE);
		Region spacerr = new Region();
        HBox.setHgrow(spacerr, Priority.ALWAYS);     
        spacerr.setMinWidth(Region.USE_PREF_SIZE);
		Button noButton = new Button("No");
		noButton.setOnAction(e -> {
			dialog.close();
			then.onFailure();
		});

		dialogButtonsBox.getChildren().addAll(spacerl, yesButton, noButton, spacerr);
		dialogVbox.getChildren().addAll(new Text(question), spacerv, dialogButtonsBox);

		Scene dialogScene = new Scene(dialogVbox, 240, 120);
		dialog.setScene(dialogScene);

        dialogScene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				Node node = dialogScene.focusOwnerProperty().get();
				if (node instanceof Button)
					((Button)node).fire();
			}
		});		
		dialog.show();		
	}
}
