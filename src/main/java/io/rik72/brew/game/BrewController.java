package io.rik72.brew.game;

import java.util.Map.Entry;

import io.rik72.brew.engine.cleaner.Cleaner;
import io.rik72.brew.engine.finder.Finder;
import io.rik72.brew.engine.loader.LoadPath;
import io.rik72.brew.engine.loader.Loader;
import io.rik72.brew.engine.story.Story;
import io.rik72.brew.engine.story.StoryDescriptor;
import io.rik72.brew.game.ui.Terminal;
import io.rik72.mammoth.delta.Deltas;

public class BrewController 
{
    private static Entry<LoadPath, StoryDescriptor> currentStory;
 
    public static Entry<LoadPath, StoryDescriptor> getCurrentStory() {
        return currentStory;
    }

    public static void setCurrentStory(Entry<LoadPath, StoryDescriptor> currentStory) {
        BrewController.currentStory = currentStory;
    }
   
    public static void init() throws Exception {
        Finder.get().init();
        Story.get().init();
        Terminal.get().init();
    }

    public static void clear() throws Exception {
        Cleaner.get().run();
    }

    public static void load() throws Exception {
        clear();
        Loader.get().setLoadPath(currentStory.getKey());
        Loader.get().run();
        Deltas.get().clear();
    }
}
