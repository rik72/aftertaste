package io.rik72.brew.engine.story.registry;

import java.util.ArrayList;
import java.util.List;

import io.rik72.brew.engine.loader.LoadPath;
import io.rik72.brew.engine.loader.LoadType;
import io.rik72.brew.engine.story.StoryDescriptor;
import io.rik72.brew.engine.utils.FileUtils;

public class StoryRegistry {

	private static final String STORY_ROOT_RESOURCE_FOLDER = "brew/stories";

	private StoryDescriptor embeddedStory;
	private List<StoryDescriptor> userStories = new ArrayList<>();

	public void init() throws Exception {
		initEmbeddedStory();
		initUserStories();
	}

	private void initEmbeddedStory() throws Exception {
		String[] files = FileUtils.getResourceListing(getClass(), STORY_ROOT_RESOURCE_FOLDER);
		String path = STORY_ROOT_RESOURCE_FOLDER + "/" + files[0];
		LoadPath loadPath = new LoadPath(path, LoadType.RESOURCES);
		embeddedStory = StoryDescriptor.load(loadPath);
	}

	private void initUserStories() throws Exception {
		for (String folder : UserStoryFolders.get().getFolders())
			initUserStoryFromFolder(folder);
	}

	private void initUserStoryFromFolder(String folder) throws Exception {
		LoadPath loadPath = new LoadPath(folder, LoadType.FILESYSTEM);
		userStories.add(StoryDescriptor.load(loadPath));
	}

	public void addUserStoryFolder(String folder) throws Exception {
		initUserStoryFromFolder(folder);
		UserStoryFolders.get().addFolder(folder);
	}

	public void removeUserStoryFolder(String folder) throws Exception {
		for (StoryDescriptor descriptor : userStories) {
			if (descriptor.getLoadPath().getPath().equals(folder) && descriptor.getLoadPath().getLoadType() == LoadType.FILESYSTEM) {
				userStories.remove(descriptor);
				UserStoryFolders.get().removeFolder(folder);
				break;
			}
		}
	}

	public StoryDescriptor getEmbeddedStory() {
		return embeddedStory;
	}

	public List<StoryDescriptor> getUserStories() {
		return userStories;
	}

	///////////////////////////////////////////////////////////////////////////
	private static StoryRegistry instance = new StoryRegistry();
	public static StoryRegistry get() {
		return instance;
	}
}
