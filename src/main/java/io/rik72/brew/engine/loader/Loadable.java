package io.rik72.brew.engine.loader;

import io.rik72.brew.engine.finder.LoadPath;

public interface Loadable extends Dumpable {
	public void load(LoadPath loadPath) throws Exception;
	public void register();
}
