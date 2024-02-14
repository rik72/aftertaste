package io.rik72.aftertaste.ui.views;

import java.net.URL;

import io.rik72.aftertaste.App;
import io.rik72.aftertaste.ui.views.menu.TopMenu;
import io.rik72.bottlerack.view.AbstractView;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class StartView extends AbstractView {

	public StartView(Stage stage) {
		super(stage);
	}

	@Override
	protected Parent create() {

		BorderPane pane = new BorderPane();
		// URL backgroundImageURL = getClass().getClassLoader().getResource("images/adventure-awaits.jpg");
		URL backgroundImageURL = getClass().getClassLoader().getResource("images/marble-logo.jpg");
		BackgroundImage backgroundImage = new BackgroundImage(
			new Image(backgroundImageURL.toString(), App.WINDOW_WIDTH * 1.1, App.WINDOW_HEIGHT * 1.1, true, true),
        	BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
		pane.setBackground(new Background(backgroundImage));

		TopMenu topMenu = new TopMenu();
		pane.setTop(topMenu);

		return pane;
	}

	@Override
	public void onOpen() {
		// NOP
	}

	@Override
	public void onClose() {
		// NOP
	}
	
}
