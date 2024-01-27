package io.rik72.brew.engine.loader;

import java.util.Vector;

import io.rik72.amber.logger.Log;
import io.rik72.brew.engine.finder.LoadPath;
import io.rik72.mammoth.db.DB;

public class Loader implements Dumpable {

	private LoadPath loadPath;
	private Vector<Loadable> loadables = new Vector<>();

	private Loader() {}

	public void register(Loadable loadable) {
		loadables.add(loadable);
	}

	public void run() throws Exception {

        try {
            DB.beginTransaction();
			for (Loadable loadable : loadables) {
				loadable.load(loadPath);
				loadable.dump();
			}
            DB.commitTransaction();
        } catch (Exception e) {
            DB.rollbackTransaction();
            throw e;
        }
	}

	public void setLoadPath(LoadPath loadPath) {
		this.loadPath = loadPath;
	}

	///////////////////////////////////////////////////////////////////////////
	private static Loader instance = new Loader();
	public static Loader get() {
		return instance;
	}

	@Override
	public void dump() throws Exception {
		for (Loadable loadable : loadables) {
			loadable.dump();
		}
		Log.skip();
	}
}
