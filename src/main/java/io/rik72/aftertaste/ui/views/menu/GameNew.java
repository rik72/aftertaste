package io.rik72.aftertaste.ui.views.menu;

import io.rik72.aftertaste.ui.views.ViewHelper;
import io.rik72.brew.engine.processing.execution.base.Future;
import io.rik72.brew.engine.story.StoryDescriptor;
import io.rik72.brew.game.BrewController;
import io.rik72.brew.game.ui.Terminal;
import javafx.scene.control.MenuItem;

public class GameNew extends MenuItem {
	public GameNew(String text, StoryDescriptor story) {
		setText(text);
		setOnAction(e -> {
            if (Terminal.get().isInGame()) {
                Terminal.get().confirm("Start new game without saving?", new Future() {

                    @Override
                    public void onSuccess() {
                        Terminal.get().hideMenus();
                        BrewController.setCurrentStory(story);
                        ViewHelper.restart(false);
                    }

                    @Override
                    public void onFailure() {
                        Terminal.get().hideMenus();
                    }                    
                });
            }
            else {
                BrewController.setCurrentStory(story);
                ViewHelper.restart(false);
            }
        });		
	}
}
