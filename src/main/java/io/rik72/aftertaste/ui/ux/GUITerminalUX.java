package io.rik72.aftertaste.ui.ux;

import java.io.File;

import org.girod.javafx.svgimage.SVGImage;
import org.girod.javafx.svgimage.SVGLoader;

import io.rik72.aftertaste.ui.scenes.TerminalGUI;
import io.rik72.amber.logger.Log;
import io.rik72.brew.engine.db.entities.Location;
import io.rik72.brew.engine.processing.execution.Future;
import io.rik72.brew.game.ui.ux.TerminalUX;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class GUITerminalUX implements TerminalUX {

	private TerminalGUI gui;

	public void setGUI(TerminalGUI gui) {
		this.gui = gui;
	}

	@Override
	public void skip(int lines) {
		for (int i = 0; i < lines; i++)
			println("");
	}

	@Override
	public void pull(int lines) {
		for (int i = 0; i < lines; i++)
			gui.getTextFlow().getChildren().remove(gui.getTextFlow().getChildren().size() - 1);
	}

	@Override
	public void print(String text) {
		Text textItem = new Text(text);
		textItem.setFont(gui.normal());
		gui.getTextFlow().getChildren().add(textItem);
		gui.setScrollToBottom();
	}

	@Override
	public void println(String text) {
		print(text.strip() + "\n");
	}

	@Override
	public void hilightln(String text) {
		Text textItem = new Text(text.strip() + "\n");
		textItem.setFont(gui.bold());
		gui.getTextFlow().getChildren().add(textItem);
		gui.setScrollToBottom();
	}

	@Override
	public void printLongText(String text) {
		println(text);
	}

	@Override
	public void emphasisLongText(String text) {
		Text textItem = new Text(text.strip() + "\n");
		textItem.setFont(gui.italic());
		gui.getTextFlow().getChildren().add(textItem);
		gui.setScrollToBottom();
	}

	@Override
	public void confirm(String question, Future then) {
		gui.openConfirmBoxconfirm(question, then);
	}

	@Override
	public void pressEnterToContinue(Future then) {
		skip(1);
		hilightln("(Press ENTER to continue)");
		skip(1);
		gui.setEnterListener(then);
	}

	@Override
	public void waitForInput() {
		gui.getPromptField().setDisable(false);
		gui.showPromptPane();
		gui.getPromptField().requestFocus();
	}

	@Override
	public void printLocationImage(Location location) {
		SVGImage topImage = loadLocationStatusImage(location.getName(), location.getStatus().getImage());
		if (topImage == null && !"initial".equals(location.getStatus().getImage()))
			topImage = loadLocationStatusImage(location.getName(), "initial");
		if (topImage != null) {
			topImage.scaleTo(480);
			BorderPane pane = new BorderPane(topImage);
			pane.setPadding(new Insets(20));
			gui.setTopImage(pane);
		}
		else {
			gui.setTopImage(null);
		}
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

	@Override
	public File chooseOpenFile() {
		return gui.chooseOpenFile();
	}

	@Override
	public File chooseSaveFile() {
		return gui.chooseSaveFile();
	}
}
