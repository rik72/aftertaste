package io.rik72.aftertaste.ui.scenes;

import io.rik72.bottlerack.scene.AbstractScene;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Test1 extends AbstractScene {

    public Test1(Stage stage) {
		super(stage);
	}

	@Override
	public void show() {

        // Layout
        VBox vPane = new VBox();
        HBox hPane1 = new HBox();
        HBox hPane2 = new HBox();
        vPane.getChildren().addAll(hPane1);
        vPane.getChildren().addAll(hPane2);

        // Elements
        Button btn1 = new Button("Quite long");
        Button btn2 = new Button("Short");
        Button btn3 = new Button("1");
        Button btn4 = new Button("Very very very long");
        Button btn5 = new Button("Medium  ");
        Region spacer = new Region();

        // Disposition
        hPane1.setPadding(new Insets(10));
        hPane1.getChildren().addAll(btn1, btn2, btn3);
        HBox.setMargin(btn1, new Insets(5));
        HBox.setMargin(btn2, new Insets(5));
        HBox.setMargin(btn3, new Insets(5));
        btn1.setMaxHeight(Double.MAX_VALUE);
        btn2.setMaxHeight(Double.MAX_VALUE);
        btn3.setMaxHeight(Double.MAX_VALUE);

        hPane2.setPadding(new Insets(10));
        hPane2.getChildren().addAll(btn4, spacer, btn5);
        HBox.setMargin(btn4, new Insets(5));
        HBox.setMargin(btn5, new Insets(5));
        btn4.setMaxWidth(Double.MAX_VALUE);
        btn5.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Scene scene = new Scene(vPane, 640, 200);
        stage.setScene(scene);
        stage.show();
    }	
}
