package io.rik72.brew.game;

import io.rik72.brew.engine.finder.Finder;
import io.rik72.brew.engine.loader.LoadPath;
import io.rik72.brew.engine.loader.Loader;
import io.rik72.brew.engine.story.Story;
import io.rik72.brew.game.ui.Terminal;
import io.rik72.mammoth.delta.Deltas;

public class BrewController 
{
    public static void init() throws Exception {
        Finder.get().init();
        Story.get().init();
        Terminal.get().init();
    }

    public static void load(LoadPath loadPath) throws Exception {
        Loader.get().setLoadPath(loadPath);
        Loader.get().run();
        Deltas.get().clear();
    }
}
