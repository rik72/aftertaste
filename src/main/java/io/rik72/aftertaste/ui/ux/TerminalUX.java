package io.rik72.aftertaste.ui.ux;

import java.io.File;

import org.girod.javafx.svgimage.SVGImage;
import org.girod.javafx.svgimage.SVGLoader;

import io.rik72.aftertaste.App;
import io.rik72.aftertaste.ui.Defaults;
import io.rik72.aftertaste.ui.views.TerminalView;
import io.rik72.aftertaste.ui.views.ViewHelper;
import io.rik72.amber.logger.Log;
import io.rik72.brew.engine.db.entities.Location;
import io.rik72.brew.engine.processing.execution.Future;
import io.rik72.brew.game.ui.Terminal;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class TerminalUX {

	private TerminalView gui;

	public TerminalUX(TerminalView gui) {
		this.gui = gui;
	}

	public void skip(int lines) {
		for (int i = 0; i < lines; i++)
			println("");
	}

	public void pull(int lines) {
		for (int i = 0; i < lines; i++)
			gui.getTextFlow().getChildren().remove(gui.getTextFlow().getChildren().size() - 1);
	}

	public void print(String text) {
		Text textItem = new Text(text);
		textItem.setFont(Defaults.FONT_NORMAL);
		textItem.setFill(Color.GRAY);
		textItem.setLineSpacing(3);
		gui.getTextFlow().getChildren().add(textItem);
		gui.setScrollToBottom();
	}

	public void println(String text) {
		print(text.strip() + "\n");
	}

	public void hilightln(String text) {
		Text textItem = new Text(text.strip() + "\n");
		textItem.setFont(Defaults.FONT_BOLD);
		textItem.setFill(Color.WHITE);
		textItem.setLineSpacing(3);
		gui.getTextFlow().getChildren().add(textItem);
		gui.setScrollToBottom();
	}

	public void printLongText(String text) {
		println(text);
	}

	public void emphasisLongText(String text) {
		Text textItem = new Text(text.strip() + "\n");
		textItem.setFont(Defaults.FONT_ITALIC);
		textItem.setFill(Color.GREEN);
		textItem.setLineSpacing(3);
		gui.getTextFlow().getChildren().add(textItem);
		gui.setScrollToBottom();
	}

	public void confirm(String question, Future then) {
		ViewHelper.openConfirmModal(question, then);
	}

	public void pressEnterTo(Future then, String action) {
		skip(1);
		hilightln("(Press ENTER to " + action + ")");
		skip(1);
		gui.setEnterListener(then);
	}

	public void closeTerminalView() {
		App.openView("start");

		Terminal.get().removeTopImage();
		Terminal.get().closeInput();
        Terminal.get().clearTextFlow();
	}

	public void pressEnterToContinue(Future then) {
		pressEnterTo(then, "continue");
	}

	public void openInput() {
		gui.getPromptField().setDisable(false);
		gui.showPromptPane();
		gui.getPromptField().requestFocus();
	}

	public void closeInput() {
		gui.getPromptField().setText("");
		gui.getPromptField().setDisable(true);
		gui.hidePromptPane();
	}

	public void printLocationImage(Location location) {
		SVGImage topImage = loadLocationStatusImage(location.getName().substring(1), location.getStatus().getImage());
		if (topImage == null && !"initial".equals(location.getStatus().getImage()))
			topImage = loadLocationStatusImage(location.getName(), "initial");
		if (topImage != null) {
			BorderPane pane = new BorderPane(topImage);
			pane.setPadding(new Insets(20));
			pane.setMinHeight(360);
			gui.setTopImage(pane);
		}
		else {
			gui.removeTopImage();
		}
	}

	public void removeTopImage() {
		gui.removeTopImage();
	}

	public void showMenus() {
		gui.showMenus();
	}

	public void hideMenus() {
		gui.hideMenus();
	}

	private SVGImage loadLocationStatusImage(String location, String image) {
		try {
			SVGImage res = SVGLoader.load(getClass().getClassLoader().getResource(
				"brew/stories/test/images/" +
				location + "-" + image +
				".svg"));
			return res;
		} catch (Exception e) {
			Log.error("Could not load image '" + image + "' for location '" + location + "'");
			gui.removeTopImage();
		}

		return null;
	}

	public void clearTextFlow() {
		gui.getTextFlow().getChildren().clear();
	}

	public File chooseOpenFile(String dialogTitle) {
		return gui.chooseOpenFile(dialogTitle);
	}

	public File chooseSaveFile(String dialogTitle) {
		return gui.chooseSaveFile(dialogTitle);
	}

	public File chooseDirectory(String dialogTitle) {
		return gui.chooseDirectory(dialogTitle);
	}
}
