package io.rik72.aftertaste.ui.views;

import java.net.URL;

import io.rik72.aftertaste.App;
import io.rik72.aftertaste.ui.Defaults;
import io.rik72.bottlerack.view.AbstractView;
import io.rik72.brew.engine.finder.Finder;
import io.rik72.brew.engine.story.StoryDescriptor;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class StartView extends AbstractView {

	public StartView(Stage stage) {
		super(stage);
	}

	@Override
	protected Parent create() {
		
		StoryDescriptor defaultStory = Finder.get().getFirstStory().getValue();
		Button startDefaultStoryButton = new Button("Play a new game of \n\"" + defaultStory.getTitle() + "\"");
		startDefaultStoryButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				App.openView("terminal");
			}
			
		});
		startDefaultStoryButton.setTextAlignment(TextAlignment.CENTER);
		startDefaultStoryButton.setFont(Defaults.FONT_NORMAL);

		BorderPane pane = new BorderPane();
		pane.setCenter(startDefaultStoryButton);
		pane.setPadding(new Insets(300, 0, 0, 0));
		URL backgroundImageURL = getClass().getClassLoader().getResource("images/adventure-awaits.jpg");
		BackgroundImage backgroundImage = new BackgroundImage(new Image(backgroundImageURL.toString(), 720, 480, true, true),
        	BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		pane.setBackground(new Background(backgroundImage));
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
