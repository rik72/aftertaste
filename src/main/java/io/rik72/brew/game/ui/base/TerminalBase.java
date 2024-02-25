package io.rik72.brew.game.ui.base;

import java.io.File;

import io.rik72.aftertaste.ui.ux.TerminalUX;
import io.rik72.brew.engine.db.entities.Location;
import io.rik72.brew.engine.processing.execution.base.Future;
import io.rik72.brew.game.ui.Terminal;

public abstract class TerminalBase {

	private TerminalUX ux;

	public void skip(int lines) {
		ux.skip(lines);
	}

	public void pull(int lines) {
		ux.pull(lines);
	}

	public void print(String text) {
		ux.println(text);
	}

	public void printHeaderLn(String text) {
		ux.printHeaderLn(text);
	}

	public void println(String text) {
		ux.println(text);
	}

	public void hilightln(String text) {
		ux.hilightln(text);
	}

	public void printLongText(String text) {
		ux.printLongText(text);
	}

	public void emphasisLongText(String text) {
		ux.emphasisLongText(text);
	}

	public void confirm(String question, Future then) {
		ux.confirm(question, then);
	}

	public void pressEnterToContinue(Future then) {
		ux.pressEnterToContinue(then);
	}

	public void pressEnterTo(Future then, String action) {
		ux.pressEnterTo(then, action);
	}

	public void openInput() {
		ux.openInput();
	}

	public void closeInput() {
		ux.closeInput();
	}

	public void printLocationImage(Location location) {
		ux.printLocationImage(location);
	}

	public void removeTopImage() {
		ux.removeTopImage();
	}

	public File chooseOpenFile(String dialogTitle, String description, String extension) {
		return ux.chooseOpenFile(dialogTitle, description, extension);
	}

	public File chooseSaveFile(String dialogTitle) {
		return ux.chooseSaveFile(dialogTitle);
	}

	public File chooseDirectory(String dialogTitle) {
		return ux.chooseDirectory(dialogTitle);
	}

	public void setUX(TerminalUX ux) {
		this.ux = ux;
	}

	public void showMenus() {
		ux.showMenus();
	}

	public void hideMenus() {
		ux.hideMenus();
	}

	public void clearTextFlow() {
		ux.clearTextFlow();
	}

	public void close() {
		Terminal.get().setInGame(false);
		ux.closeTerminalView();
	}

	public void adjustPromptLabelSize() {
		ux.adjustPromptLabelSize();
	}
}
