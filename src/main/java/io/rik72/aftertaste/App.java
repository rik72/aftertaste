package io.rik72.aftertaste;

import io.rik72.aftertaste.ui.views.StartView;
import io.rik72.aftertaste.ui.views.TerminalView;
import io.rik72.bottlerack.view.ViewManager;
import io.rik72.brew.game.BrewController;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

	public static final int WINDOW_WIDTH = 1024;
	public static final int WINDOW_HEIGHT = 768;

    private static ViewManager viewManager;
    private static Stage stage;
    private static App instance;

    public static Stage getStage() {
        return stage;
    }

    private static void setStage(Stage stage) {
        App.stage = stage;
    }

    private static void setApp(App app) {
        App.instance = app;
    }

    public static Parent getRoot() {
        return getStage().getScene().getRoot();
    }

    @Override
    public void start(Stage stage) {
        setApp(this);
        setStage(stage);
        initStage(stage);
        initViewManager(stage);
        openView("start");
    }

    public static void openView(String view) {
        viewManager.openView(view);
    }

    private void initStage(Stage stage) {
		stage.setTitle("Aftertaste");
		stage.setMinWidth(WINDOW_WIDTH);
		stage.setMaxWidth(WINDOW_WIDTH);
		stage.setMinHeight(WINDOW_HEIGHT);
		stage.setMaxHeight(WINDOW_HEIGHT);
        stage.setScene(new Scene(new BorderPane()));
		stage.getScene().getStylesheets().add(getClass().getResource("/css/custom.css").toExternalForm());
		stage.getScene().getRoot().setStyle("-fx-background-color: -aft-color-window-bg;");
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - WINDOW_WIDTH) / 2);
        stage.setY((primScreenBounds.getHeight() - WINDOW_HEIGHT) / 2);
        stage.show();
    }

    private void initViewManager(Stage stage) {
        viewManager = new ViewManager(stage);
        viewManager.getViews().put("start", new StartView(stage));
        viewManager.getViews().put("terminal", new TerminalView(stage));
    }

    public static void openURL(String url) {
        instance.getHostServices().showDocument(url);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    public static void main() throws Exception {
        BrewController.init();
        launch();
    }
}
