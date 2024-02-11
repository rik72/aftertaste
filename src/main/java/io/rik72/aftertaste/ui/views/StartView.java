package io.rik72.aftertaste.ui.views;

import java.net.URL;

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
		URL backgroundImageURL = getClass().getClassLoader().getResource("images/marble.jpg");
		BackgroundImage backgroundImage = new BackgroundImage(new Image(backgroundImageURL.toString(), 720, 480, true, true),
        	BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
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
