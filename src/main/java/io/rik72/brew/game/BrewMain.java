package io.rik72.brew.game;

import io.rik72.brew.engine.loader.Loader;
import io.rik72.brew.engine.story.Story;
import io.rik72.brew.game.ui.Terminal;
import io.rik72.mammoth.delta.Deltas;

public class BrewMain 
{
    public static void start() throws Exception {
        init();
        load();
    }

    private static void init() {
        Story.get().init();
        Terminal.get().init();
    }

    private static void load() throws Exception {
        Loader.get().run();
        Deltas.get().clear();
    }
}
