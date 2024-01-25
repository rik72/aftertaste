package io.rik72.aftertaste.ui.ux;

import io.rik72.aftertaste.ui.scenes.TerminalGUI;
import io.rik72.brew.engine.db.entities.Location;
import io.rik72.brew.engine.processing.execution.Future;
import io.rik72.brew.game.ui.ux.TerminalUX;
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
	public void print(String text, int indent) {
		Text textItem = new Text(text);
		textItem.setFont(gui.normal());
		gui.getTextFlow().getChildren().add(textItem);
		gui.setScrollToBottom();
	}

	@Override
	public void println(String text, int indent) {
		print(text.strip() + "\n", indent);
	}

	@Override
	public void println(String text) {
		println(text, 0);
	}

	@Override
	public void hilightln(String text) {
		Text textItem = new Text(text.strip() + "\n");
		textItem.setFont(gui.bold());
		gui.getTextFlow().getChildren().add(textItem);
		gui.setScrollToBottom();
	}

	@Override
	public void printLongText(String text, int indent) {
		println(text, indent);
	}

	@Override
	public void printLongText(String text) {
		println(text);
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
		// NOP
	}
}
