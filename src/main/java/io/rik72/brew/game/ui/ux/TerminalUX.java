package io.rik72.brew.game.ui.ux;

import io.rik72.brew.engine.db.entities.Location;
import io.rik72.brew.engine.processing.execution.Future;

public interface TerminalUX {

	public abstract void skip(int lines);

	public abstract void pull(int lines);

	public abstract void print(String text);

	public abstract void println(String text);

	public abstract void hilightln(String text);

	public abstract void printLongText(String text);

	public abstract void emphasisLongText(String text);

	public abstract void confirm(String question, Future then);

	public abstract void pressEnterToContinue(Future then);

	public abstract void waitForInput();

	public abstract void printLocationImage(Location location);
}