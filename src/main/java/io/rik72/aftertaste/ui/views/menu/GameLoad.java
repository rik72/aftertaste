package io.rik72.aftertaste.ui.views.menu;

import io.rik72.aftertaste.ui.views.ViewHelper;
import io.rik72.brew.engine.processing.execution.Future;
import io.rik72.brew.game.ui.Terminal;
import javafx.scene.control.MenuItem;

public class GameLoad extends MenuItem {
	public GameLoad(String text) {
		setText(text);
		setOnAction(e -> {
            if (Terminal.get().isInGame()) {
                Terminal.get().confirm("Load saved game? You will loose any\nunsaved progress.", new Future() {

                    @Override
                    public void onSuccess() {
                        Terminal.get().hideMenus();
                        try {
                            ViewHelper.load();
                        } catch (Exception e) {
                            e.printStackTrace();
                            ViewHelper.openErrorModal("An error occurred, impossible\nto load this saved game.", 210, 80);
                        }
                    }

                    @Override
                    public void onFailure() {
                        Terminal.get().hideMenus();
                    }                    
                });
            }
            else {
                Terminal.get().hideMenus();
                try {
                    ViewHelper.load();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ViewHelper.openErrorModal("An error occurred, impossible\nto load this saved game.", 210, 80);
                }
            }
        });
	}
}
