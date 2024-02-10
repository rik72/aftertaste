package io.rik72.aftertaste.ui.views.menu;

import io.rik72.brew.engine.processing.execution.Future;
import io.rik72.brew.game.ui.Terminal;
import javafx.scene.control.MenuItem;

public class AftertasteExit extends MenuItem {
	public AftertasteExit(String text) {
		setText(text);
		setOnAction(e -> {
            if (Terminal.get().isInGame()) {
                Terminal.get().confirm("Exit now? You will loose any unsaved\nprogress.", new Future() {

                    @Override
                    public void onSuccess() {
                        System.exit(0);
                    }

                    @Override
                    public void onFailure() {
                        Terminal.get().hideMenus();
                    }                    
                });
            }
            else {
                System.exit(0);
            }
        });		
	}
}
