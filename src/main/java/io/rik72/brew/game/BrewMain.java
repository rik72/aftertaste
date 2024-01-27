package io.rik72.brew.game;

import java.util.Map.Entry;

import io.rik72.brew.engine.finder.Finder;
import io.rik72.brew.engine.finder.LoadPath;
import io.rik72.brew.engine.loader.Loader;
import io.rik72.brew.engine.story.Story;
import io.rik72.brew.engine.story.StoryDescriptor;
import io.rik72.brew.game.ui.Terminal;
import io.rik72.mammoth.delta.Deltas;

public class BrewMain 
{
    public static void start() throws Exception {
        init();
        load();
    }

    private static void init() throws Exception {
        Finder.get().init();
        if (Finder.get().getAvailableStories().size() == 1) {
            Entry<LoadPath, StoryDescriptor> entry = Finder.get().getAvailableStories().entrySet().iterator().next();
            Story.get().init(entry.getKey(), entry.getValue());
            Loader.get().setLoadPath(entry.getKey());
        }

        Terminal.get().init();
    }

    private static void load() throws Exception {
        Loader.get().run();
        Deltas.get().clear();
    }
}
