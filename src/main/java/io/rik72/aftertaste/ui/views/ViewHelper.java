package io.rik72.aftertaste.ui.views;

import java.io.File;

import io.rik72.aftertaste.App;
import io.rik72.aftertaste.ui.Defaults;
import io.rik72.brew.engine.processing.execution.Future;
import io.rik72.brew.engine.story.Story;
import io.rik72.brew.engine.story.registry.StoryRegistry;
import io.rik72.brew.game.BrewController;
import io.rik72.brew.game.savegames.SaveGame;
import io.rik72.brew.game.ui.Terminal;
import io.rik72.mammoth.delta.Deltas;
import javafx.scene.paint.Color;
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

        openTerminalView();

        Terminal.get().removeTopImage();
        Terminal.get().closeInput();
        Terminal.get().clearTextFlow();

        try {
            BrewController.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

		applySkin();

		if (!skipIntro)
        	Terminal.get().intro();
    }

	private static void applySkin() {
    	App.getRoot().setStyle(
			"-aft-color-menu-bg: " + Defaults.COLOR_MENU_BG + ";" + 
    		"-aft-color-menu-hilight: " + Defaults.COLOR_MENU_HILIGHT + ";" + 
    		"-aft-color-menu-separator: " + Defaults.COLOR_MENU_SEPARATOR + ";" + 
    		"-aft-color-windows-bg: " + Defaults.COLOR_WINDOWS_BG + ";" + 
    		"-aft-color-windows-text: " + Defaults.COLOR_WINDOWS_TEXT + ";" + 
    		"-aft-color-windows-button: " + Defaults.COLOR_WINDOWS_BUTTON + ";" + 
    		"-aft-color-windows-hover: " + Defaults.COLOR_WINDOWS_HOVER + ";"
		);
	}

	public static void addStoryFolder() throws Exception {
		File file = Terminal.get().chooseDirectory("Add story folder");
		if (file != null) {
			boolean wasAdded = StoryRegistry.get().addUserStoryFolder(file.getAbsolutePath());
			if (!wasAdded)
				openAlertModal("This story was already added.", 360, 120);
			else
				openAlertModal("Story added successfully", 360, 120);
		}
	}


	public static void load() throws Exception {
		File file = Terminal.get().chooseOpenFile("Load game");
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
					"('" + SaveGame.getInstance().getStoryDescriptor().getRefId() + "' format vs '" + Story.get().getRefId() + "'" +
					" required by current story)", 320, 200);
			}
		}
	}

	public static void openAlertModal(String msg, int width , int height) {
		AlertModal modal = createModal(width, height, 20);
		Text msgText = new Text(msg);
		msgText.setFont(Defaults.FONT_NORMAL);
		msgText.setFill(Color.web(Defaults.COLOR_WINDOWS_TEXT));
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
