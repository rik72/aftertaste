package io.rik72.brew.engine.loader;

public interface Loadable extends Dumpable {
	public void load() throws Exception;
	public void register();
}
