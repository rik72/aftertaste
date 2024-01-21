package io.rik72.aftertaste;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
// import javafx.scene.layout.BorderPane;
// import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
// import javafx.scene.layout.VBox;
// import javafx.scene.Scene;
// import javafx.scene.control.Label;
// import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;

    @Override
    public void start(Stage stage) {
        // var javaVersion = SystemInfo.javaVersion();
        // var javafxVersion = SystemInfo.javafxVersion();

        // var label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        // var scene = new Scene(new StackPane(label), 640, 480);
        // stage.setScene(scene);
        // stage.show();

        btn1 = new Button("Button 1");
        btn2 = new Button("Button 2");
        btn3 = new Button("Button 3");
        btn4 = new Button("Button 4");
        btn5 = new Button("Button 5");

        HBox pane = new HBox();
        pane.getChildren().addAll(btn1, btn2, btn3, btn4, btn5);
        pane.setPadding(new Insets(10));
        HBox.setMargin(btn1, new Insets(1));
        HBox.setMargin(btn2, new Insets(2));
        HBox.setMargin(btn3, new Insets(3));
        HBox.setMargin(btn4, new Insets(4));
        HBox.setMargin(btn5, new Insets(5));

        Scene scene = new Scene(pane, 640, 200);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}