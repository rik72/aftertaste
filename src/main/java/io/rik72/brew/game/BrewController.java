package io.rik72.brew.game;

import io.rik72.brew.engine.cleaner.Cleaner;
import io.rik72.brew.engine.loader.Loader;
import io.rik72.brew.engine.story.Story;
import io.rik72.brew.engine.story.StoryDescriptor;
import io.rik72.brew.engine.story.registry.StoryRegistry;
import io.rik72.mammoth.delta.Deltas;

public class BrewController 
{
    private static StoryDescriptor currentStory;
 
    public static StoryDescriptor getCurrentStory() {
        return currentStory;
    }

    public static void setCurrentStory(StoryDescriptor currentStory) {
        BrewController.currentStory = currentStory;
    }
   
    public static void init() throws Exception {
        StoryRegistry.get().init();
        Story.get().init();
    }

    public static void clear() throws Exception {
        Cleaner.get().run();
    }

    public static void load() throws Exception {
        clear();
        Loader.get().setLoadPath(currentStory.getLoadPath());
        Loader.get().run();
        Deltas.get().clear();
    }
}
