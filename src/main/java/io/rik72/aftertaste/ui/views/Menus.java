package io.rik72.aftertaste.ui.views;

import io.rik72.bottlerack.scene.AbstractScene;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Menus extends AbstractScene {

    public Menus(Stage stage) {
		super(stage);
	}

	@Override
	public void show() {

        // menu items
        MenuItem fileNew = new MenuItem("New");
        MenuItem fileSave = new MenuItem("Save");
        SeparatorMenuItem fileSep1 = new SeparatorMenuItem();
        MenuItem fileExit = new MenuItem("Exit");
        SeparatorMenuItem fileSep2 = new SeparatorMenuItem();
        CustomMenuItem fileSlider = new CustomMenuItem(new Slider());

        CheckMenuItem webHtml = new CheckMenuItem("HTML");
        CheckMenuItem webCss = new CheckMenuItem("CSS");
        webHtml.setSelected(true);
        webCss.setSelected(true);

        RadioMenuItem miscMysql = new RadioMenuItem("MySQL");
        RadioMenuItem miscPostgresql = new RadioMenuItem("PostgreSQL");
        ToggleGroup miscToggle = new ToggleGroup();
        miscMysql.setToggleGroup(miscToggle);
        miscPostgresql.setToggleGroup(miscToggle);

        Menu miscTutorials = new Menu("Tutorials");
        MenuItem tutJava = new MenuItem("Java");
        MenuItem tutJavafx = new MenuItem("JavaFX");
        MenuItem tutSwing = new MenuItem("Swing");
        miscTutorials.getItems().addAll(tutJava, tutJavafx, tutSwing);

        // menus
        Menu fileMenu = new Menu("File");
        fileMenu.getItems().addAll(fileNew, fileSave, fileSep1, fileExit);
        fileMenu.getItems().addAll(fileSep2, fileSlider);

        Menu webMenu = new Menu("Web");
        webMenu.getItems().addAll(webHtml, webCss);

        Menu miscMenu = new Menu("Miscellaneous");
        miscMenu.getItems().addAll(miscMysql, miscPostgresql, miscTutorials);

        // menu bar with items
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);
        menuBar.getMenus().add(webMenu);
        menuBar.getMenus().add(miscMenu);

        // layout pane
        BorderPane pane = new BorderPane();
        pane.setTop(menuBar);

        // scene
        Scene scene = new Scene(pane, 640, 400);
        stage.setScene(scene);
        stage.show();
    }	
}
