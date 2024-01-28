package io.rik72.brew.engine.loader;

public interface Loadable extends Dumpable {
	public void load(LoadPath loadPath) throws Exception;
	public void register();
}
