package io.rik72.aftertaste.ui.scenes;

import io.rik72.bottlerack.scene.AbstractScene;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginPage extends AbstractScene {

	public LoginPage(Stage stage) {
		super(stage);
	}

	@Override
	public void show() {

		// Elements
		Label emailLabel = new Label("E-mail");
		TextField emailField = new TextField();
		Label passwordLabel = new Label("Password");
		TextField passwordField = new PasswordField();
		Button submitButton = new Button("Submit");
		Button clearButton = new Button("Clear");

		// Layout
		GridPane pane = new GridPane();
		pane.setMinSize(640, 400);
		pane.setPadding(new Insets(40));
		pane.setVgap(10);
		pane.setHgap(10);

		// Build scene
		pane.add(emailLabel, 	0, 0);
		pane.add(emailField, 	1, 0);
		pane.add(passwordLabel,	0, 1);
		pane.add(passwordField,	1, 1);
		pane.add(submitButton,	0, 2);
		pane.add(clearButton,	1, 2);

        Scene scene = new Scene(pane, 640, 400);
        stage.setScene(scene);
		stage.setTitle("Login page");
        stage.show();
	}
}
