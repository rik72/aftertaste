package io.rik72.aftertaste.ui.views.menu;

import io.rik72.aftertaste.ui.views.ViewHelper;
import io.rik72.brew.game.ui.Terminal;
import javafx.scene.control.MenuItem;

public class AddStory extends MenuItem {
	public AddStory(String text) {
		setText(text);
		setOnAction(e -> {
            Terminal.get().hideMenus();
            try {
                ViewHelper.addStoryFolder();
            } catch (Exception ex) {
                ex.printStackTrace();
                ViewHelper.openAlertModal("An error occurred, impossible\nto add this story folder.", 210, 80);
            }
            Terminal.get().showMenus();
        });
	}
}
