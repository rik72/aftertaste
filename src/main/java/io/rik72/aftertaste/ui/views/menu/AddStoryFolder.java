package io.rik72.aftertaste.ui.views.menu;

import io.rik72.aftertaste.ui.views.ViewHelper;
import io.rik72.brew.game.ui.Terminal;
import javafx.scene.control.MenuItem;

public class AddStoryFolder extends MenuItem {
    
	public AddStoryFolder(String text) {
		setText(text);
		setOnAction(e -> {
            try {
                Terminal.get().hideMenus();
                ViewHelper.addStoryFolder();
                Terminal.get().showMenus();
            } catch (Exception ex) {
                ex.printStackTrace();
                ViewHelper.openAlertModal("An error occurred, impossible\nto add this story folder.", 210, 80);
            }
        });
	}
}
