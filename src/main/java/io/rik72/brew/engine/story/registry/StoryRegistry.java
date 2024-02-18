package io.rik72.brew.engine.story.registry;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rik72.brew.engine.loader.LoadPath;
import io.rik72.brew.engine.loader.LoadType;
import io.rik72.brew.engine.story.StoryDescriptor;
import io.rik72.brew.engine.utils.FileUtils;

public class StoryRegistry {

	private static final LoadPath STORY_RESOURCE_LOAD_PATH = new LoadPath("brew/stories", LoadType.RESOURCES);

	private StoryDescriptor embeddedStory;
	private Map<String, StoryDescriptor> storyFolders = new HashMap<>();
	private Map<String, StoryDescriptor> storyCans = new HashMap<>();

	public void init() throws Exception {
		initEmbeddedStory();
		initStoryFolders();
		initStoryCans();
	}

	private void initEmbeddedStory() throws Exception {
		Collection<String> files = FileUtils.findFiles("story.yml", STORY_RESOURCE_LOAD_PATH);
		LoadPath loadPath = new LoadPath(new File((String)files.toArray()[0]).toPath().getParent().toString(), LoadType.RESOURCES);
		embeddedStory = StoryDescriptor.load(loadPath);
	}

	private void initStoryFolders() throws Exception {
		for (String path : StoryFolders.get().getAll())
			initStoryFolder(path);
	}

	private void initStoryCans() throws Exception {
		for (String path : StoryCans.get().getAll())
			initStoryCan(path);
	}

	private void initStoryFolder(String path) throws Exception {
		LoadPath loadPath = new LoadPath(path, LoadType.FOLDER);
		storyFolders.put(path, StoryDescriptor.load(loadPath));
	}

	private void initStoryCan(String path) throws Exception {
		LoadPath loadPath = new LoadPath(path, LoadType.CAN);
		storyCans.put(path, StoryDescriptor.load(loadPath));
	}

	public boolean addStoryFolder(String path) throws Exception {
		boolean wasAdded = StoryFolders.get().add(path);
		if (wasAdded)
			initStoryFolder(path);
		return wasAdded;
	}

	public boolean addStoryCan(String path) throws Exception {
		boolean wasAdded = StoryCans.get().add(path);
		if (wasAdded)
			initStoryCan(path);
		return wasAdded;
	}

	public void removeStoryFolder(String path) throws Exception {
		StoryFolders.get().remove(path);
		storyFolders.remove(path);
	}

	public void removeStoryCan(String path) throws Exception {
		StoryCans.get().remove(path);
		storyCans.remove(path);
	}

	public List<StoryDescriptor> getAll() {
		ArrayList<StoryDescriptor> stories = new ArrayList<StoryDescriptor>();
		stories.add(embeddedStory);
		stories.addAll(storyFolders.values());
		stories.addAll(storyCans.values());
		return stories;
	}

	///////////////////////////////////////////////////////////////////////////
	private static StoryRegistry instance = new StoryRegistry();
	public static StoryRegistry get() {
		return instance;
	}
}
