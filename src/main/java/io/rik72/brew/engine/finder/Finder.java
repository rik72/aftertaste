package io.rik72.brew.engine.finder;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import io.rik72.brew.engine.loader.LoadPath;
import io.rik72.brew.engine.loader.LoadType;
import io.rik72.brew.engine.story.StoryDescriptor;
import io.rik72.brew.engine.utils.FileUtils;

public class Finder {

	private static final String STORY_ROOT_RESOURCE_FOLDER = "brew/stories";

	private Map<LoadPath, StoryDescriptor> availableStories = new HashMap<>();

	public void init() throws Exception {
		String[] files = FileUtils.getResourceListing(getClass(), STORY_ROOT_RESOURCE_FOLDER);
		for (String file : files) {
			String path = STORY_ROOT_RESOURCE_FOLDER + "/" + file;
			LoadPath loadPath = new LoadPath(path, LoadType.RESOURCES);
			availableStories.put(loadPath, StoryDescriptor.load(loadPath));
			break;
		}
	}

	public Entry<LoadPath, StoryDescriptor> getFirstStory() {
		return availableStories.entrySet().iterator().next();
	}

	public Map<LoadPath, StoryDescriptor> getAvailableStories() {
		return availableStories;
	}

	///////////////////////////////////////////////////////////////////////////
	private static Finder instance = new Finder();
	public static Finder get() {
		return instance;
	}
}
