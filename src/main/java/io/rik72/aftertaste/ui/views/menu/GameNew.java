package io.rik72.aftertaste.ui.views.menu;

import io.rik72.aftertaste.ui.views.ViewHelper;
import io.rik72.brew.engine.processing.execution.Future;
import io.rik72.brew.game.ui.Terminal;
import javafx.scene.control.MenuItem;

public class GameNew extends MenuItem {
	public GameNew(String text) {
		setText(text);
		setOnAction(e -> {
            if (Terminal.get().isInGame()) {
                Terminal.get().confirm("Start new game without saving?", new Future() {

                    @Override
                    public void onSuccess() {
                        Terminal.get().hideMenus();
                        ViewHelper.restart(false);
                    }

                    @Override
                    public void onFailure() {
                        Terminal.get().hideMenus();
                    }                    
                });
            }
            else {
                ViewHelper.restart(false);
            }
        });		
	}
}
