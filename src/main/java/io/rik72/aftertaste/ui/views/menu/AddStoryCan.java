package io.rik72.aftertaste.ui.views.menu;

import io.rik72.aftertaste.ui.views.ViewHelper;
import io.rik72.brew.game.ui.Terminal;
import javafx.scene.control.MenuItem;

public class AddStoryCan extends MenuItem {
    
	public AddStoryCan(String text) {
		setText(text);
		setOnAction(e -> {
            try {
                Terminal.get().hideMenus();
                ViewHelper.addStoryCan();
                Terminal.get().showMenus();
            } catch (Exception ex) {
                ex.printStackTrace();
                ViewHelper.openAlertModal("An error occurred, impossible\nto add this .can file.", 210, 80);
            }
        });
	}
}
