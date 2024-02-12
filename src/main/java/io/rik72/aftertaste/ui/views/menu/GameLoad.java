package io.rik72.aftertaste.ui.views.menu;

import io.rik72.aftertaste.ui.views.ViewHelper;
import io.rik72.brew.engine.processing.execution.Future;
import io.rik72.brew.engine.story.StoryDescriptor;
import io.rik72.brew.game.BrewController;
import io.rik72.brew.game.ui.Terminal;
import javafx.scene.control.MenuItem;

public class GameLoad extends MenuItem {
	public GameLoad(String text, StoryDescriptor story) {
		setText(text);
		setOnAction(e -> {
            if (Terminal.get().isInGame()) {
                Terminal.get().confirm("Load saved game? You will loose any\nunsaved progress.", new Future() {

                    @Override
                    public void onSuccess() {
                        Terminal.get().hideMenus();
                        try {
                            BrewController.setCurrentStory(story);
                            ViewHelper.load();
                        } catch (Exception e) {
                            e.printStackTrace();
                            ViewHelper.openAlertModal("An error occurred, impossible\nto load this saved game.", 210, 80);
                        }
                    }

                    @Override
                    public void onFailure() {
                        Terminal.get().hideMenus();
                    }                    
                });
            }
            else {
                try {
                    BrewController.setCurrentStory(story);
                    ViewHelper.load();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ViewHelper.openAlertModal("An error occurred, impossible\nto load this saved game.", 210, 80);
                }
            }
        });
	}
}
