package io.rik72.aftertaste.ui.scenes;

import io.rik72.bottlerack.scene.AbstractScene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Events extends AbstractScene {

	public Events(Stage stage) {
		super(stage);
	}

	private static int SIZE = 200;

	@Override
	public void show() {
		Button btn = new Button("Click me");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("clicked");
			}
			
		});
		BorderPane pane = new BorderPane(btn);

        Scene scene = new Scene(pane, SIZE, SIZE);
		stage.setScene(scene);
		stage.setTitle("Events");
        stage.show();
	}
}
