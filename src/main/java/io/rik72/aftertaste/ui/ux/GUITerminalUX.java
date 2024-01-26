package io.rik72.aftertaste.ui.ux;

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
	}

	@Override
	public void printLocationImage(Location location) {
		try {
			SVGImage topImage = SVGLoader.load(getClass().getClassLoader().getResource(
				"brew/story/images/" +
				location.getName() + "-" + location.getStatus().getLabel() +
				".svg"));
			topImage.scaleTo(480);
			BorderPane pane = new BorderPane(topImage);
			pane.setPadding(new Insets(20));
			gui.setTopImage(pane);
		} catch (Exception e) {
			Log.error("Could not load image for location '" + location.getName() + "'");
			gui.removeTopImage();
		}
	}
}
