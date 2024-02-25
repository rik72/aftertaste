package io.rik72.aftertaste.ui.views.menu;

import io.rik72.aftertaste.ui.views.ViewHelper;
import io.rik72.brew.engine.processing.execution.base.Future;
import io.rik72.brew.engine.story.Story;
import io.rik72.brew.engine.story.StoryDescriptor;
import io.rik72.brew.game.ui.Terminal;
import javafx.scene.control.MenuItem;

public class RemoveStory extends MenuItem {
	public RemoveStory(String text, StoryDescriptor story) {
		setText(text);
		setOnAction(e -> {
            if (Terminal.get().isInGame() && Story.get().getDescriptor().getRefId().equals(story.getRefId())) {
                ViewHelper.openAlertModal("A game with this story is in progress,\nit's impossible to remove it now.", 400, 120);
            }
            else {
                Terminal.get().confirm("Remove this story?", new Future() {
                    @Override
                    public void onSuccess() {
                        try {
                            Terminal.get().hideMenus();
                            ViewHelper.removeStory(story);
                            Terminal.get().showMenus();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });		
	}
}
