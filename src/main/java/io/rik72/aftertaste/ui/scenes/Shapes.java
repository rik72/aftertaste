package io.rik72.aftertaste.ui.scenes;

import io.rik72.bottlerack.scene.AbstractScene;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class Shapes extends AbstractScene {

	public Shapes(Stage stage) {
		super(stage);
	}

	private static int SIZE = 800;

	@Override
	public void show() {

		Group gp = new Group();
		for (int i = 0; i < SIZE; i += 10) {
			Line hLine = new Line(0, i, SIZE, i);
			hLine.setStroke(Color.LIGHTGRAY);
			Line vLine = new Line(i, 0, i, SIZE);
			vLine.setStroke(Color.LIGHTGRAY);
			gp.getChildren().addAll(hLine, vLine);
		}

        Scene scene = new Scene(gp, SIZE, SIZE);
		stage.setScene(scene);
        stage.setMaxWidth(SIZE);
		stage.setMaxHeight(SIZE);
		stage.setMinWidth(SIZE);
		stage.setMinHeight(SIZE);
		stage.setTitle("Shapes");
        stage.show();
	}
}
