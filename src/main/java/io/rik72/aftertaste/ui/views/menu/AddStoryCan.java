package io.rik72.aftertaste.ui.views.menu;

import io.rik72.aftertaste.ui.views.ViewHelper;
import javafx.scene.control.MenuItem;

public class AddStoryCan extends MenuItem {
    
	public AddStoryCan(String text) {
		setText(text);
		setOnAction(e -> {
            try {
                ViewHelper.addStoryCan();
            } catch (Exception ex) {
                ex.printStackTrace();
                ViewHelper.openAlertModal("An error occurred, impossible\nto add this .can file.", 210, 80);
            }
        });
	}
}
