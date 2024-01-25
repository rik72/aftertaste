package io.rik72.aftertaste;

import io.rik72.aftertaste.ui.scenes.*;
import io.rik72.aftertaste.ui.ux.GUITerminalUX;
import io.rik72.brew.game.BrewMain;
import io.rik72.brew.game.ui.Terminal;
// import io.rik72.aftertaste.scenes.Test1Scene;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        TerminalGUI scene = new TerminalGUI(stage);
        scene.show();

        try {
        
            ((GUITerminalUX)Terminal.get().getUx()).setGUI(scene);
            Terminal.get().intro();

		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public static void main(String[] args) throws Exception {
        BrewMain.start();
        launch();
    }
}
