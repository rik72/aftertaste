package io.rik72.aftertaste;

import io.rik72.aftertaste.ui.Defaults;
import io.rik72.aftertaste.ui.views.StartView;
import io.rik72.aftertaste.ui.views.TerminalView;
import io.rik72.bottlerack.view.ViewManager;
import io.rik72.brew.game.BrewController;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static ViewManager viewManager;

    @Override
    public void start(Stage stage) {
        initStage(stage);
        initViewManager(stage);
        openView("start");
    }

    public static void openView(String view) {
        viewManager.openView(view);
    }

    private void initStage(Stage stage) {
		stage.setTitle("Aftertaste");
		stage.setMinWidth(Defaults.WINDOW_WIDTH);
		stage.setMaxWidth(Defaults.WINDOW_WIDTH);
		stage.setMinHeight(Defaults.WINDOW_HEIGHT);
		stage.setMaxHeight(Defaults.WINDOW_HEIGHT);
        stage.setScene(new Scene(new BorderPane()));
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - Defaults.WINDOW_WIDTH) / 2);
        stage.setY((primScreenBounds.getHeight() - Defaults.WINDOW_HEIGHT) / 2);
        stage.show();
    }

    private void initViewManager(Stage stage) {
        viewManager = new ViewManager(stage);
        viewManager.getViews().put("start", new StartView(stage));
        viewManager.getViews().put("terminal", new TerminalView(stage));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) throws Exception {
        BrewController.init();
        launch();
    }
}
