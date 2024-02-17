package io.rik72.aftertaste.ui.views;

import java.io.File;

import io.rik72.aftertaste.App;
import io.rik72.aftertaste.ui.skin.CurrentSkin;
import io.rik72.brew.engine.processing.execution.Future;
import io.rik72.brew.engine.story.Story;
import io.rik72.brew.engine.story.registry.StoryRegistry;
import io.rik72.brew.game.BrewController;
import io.rik72.brew.game.savegames.SaveGame;
import io.rik72.brew.game.ui.Terminal;
import io.rik72.mammoth.delta.Deltas;
import javafx.scene.text.Text;

public class ViewHelper {

	public static void openTerminalView() {
		App.openView("terminal");
	}

	public static void closeTerminalView() {
		App.openView("start");

		Terminal.get().removeTopImage();
		Terminal.get().closeInput();
        Terminal.get().clearTextFlow();
	}

    public static void restart(boolean skipIntro) {

        try {
            BrewController.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        openTerminalView();

        Terminal.get().removeTopImage();
        Terminal.get().closeInput();
        Terminal.get().clearTextFlow();

		if (!skipIntro)
        	Terminal.get().intro();
    }

	public static void applySkin() {
		CurrentSkin.setSkin(Story.get().getSkin(), Story.get().getSkinData());
    	App.getRoot().setStyle(
			"-aft-color-menu-bg: " + CurrentSkin.data.COLOR_MENU_BG + ";" +
			"-aft-color-menu-hilight: " + CurrentSkin.data.COLOR_MENU_HILIGHT + ";" +
			"-aft-color-menu-separator: " + CurrentSkin.data.COLOR_MENU_SEPARATOR + ";" +
			"-aft-color-text-flow-bg: " + CurrentSkin.data.COLOR_TEXT_FLOW_BG + ";" +
			"-aft-color-text-flow-scrollbar: " + CurrentSkin.data.COLOR_TEXT_FLOW_SCROLLBAR + ";" +
			"-aft-color-window-bg: " + CurrentSkin.data.COLOR_WINDOW_BG + ";" +
			"-aft-color-window-location-image-bg: " + CurrentSkin.data.COLOR_WINDOW_LOCATION_IMAGE_BG + ";" +
			"-aft-color-window-modal-bg: " + CurrentSkin.data.COLOR_WINDOW_MODAL_BG + ";" +
			"-aft-color-window-modal-border: " + CurrentSkin.data.COLOR_WINDOW_MODAL_BORDER + ";" +
			"-aft-color-window-text: " + CurrentSkin.data.COLOR_WINDOW_TEXT + ";" +
			"-aft-color-window-button: " + CurrentSkin.data.COLOR_WINDOW_BUTTON + ";" +
			"-aft-color-window-hover: " + CurrentSkin.data.COLOR_WINDOW_HOVER + ";"
		);
	}

	public static void addStoryFolder() throws Exception {
		File file = Terminal.get().chooseDirectory("Add story from folder");
		if (file != null) {
			boolean wasAdded = StoryRegistry.get().addStoryFolder(file.getAbsolutePath());
			if (!wasAdded)
				openAlertModal("This story was already added.", 360, 120);
			else
				openAlertModal("Story added successfully", 360, 120);
		}
	}

	public static void addStoryCan() throws Exception {
		File file = Terminal.get().chooseOpenFile("Add story from .can file", "Story", "*.can");
		if (file != null) {
			boolean wasAdded = StoryRegistry.get().addStoryCan(file.getAbsolutePath());
			if (!wasAdded)
				openAlertModal("This story was already added.", 360, 120);
			else
				openAlertModal("Story added successfully", 360, 120);
		}
	}


	public static void load() throws Exception {
		File file = Terminal.get().chooseOpenFile("Load game", "Savegame", "*.save");
		if (file != null) {
			SaveGame.loadFromFile(file.getPath());
			if (SaveGame.getInstance().checkStoryCompatibility(BrewController.getCurrentStory().getRefId())) {
				restart(true);
				Deltas.set(SaveGame.getInstance().getDeltas());
				Story.get().applyDeltas();
	        	Terminal.get().intro("Game loaded from " + file.getPath());
			}
			else {
				openAlertModal("Incompatible save file " + 
					"('" + SaveGame.getInstance().getStoryDescriptor().getRefId() + "' format vs '" + Story.get().getDescriptor().getRefId() + "'" +
					" required by current story)", 320, 200);
			}
		}
	}

	public static void openAlertModal(String msg, int width , int height) {
		AlertModal modal = createModal(width, height, 20);
		Text msgText = new Text(msg);
		msgText.setFont(CurrentSkin.getGUIFont());
		msgText.setStyle("-fx-fill: " + CurrentSkin.data.COLOR_WINDOW_TEXT);
		modal.getVBox().getChildren().add(msgText);
		modal.show();
	}

    public static AlertModal createModal(int width, int height, int padding) {
		return new AlertModal(width, height, padding);
    }

	public static void openConfirmModal(String question, Future then) {
		new ConfirmModal(question, then).show();		
	}
}
