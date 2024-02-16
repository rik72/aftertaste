package io.rik72.brew.engine.story.registry;

import java.util.HashSet;
import java.util.Set;

public class StoryFolders extends StoryRepo {

	private static final String USER_PREFS_KEY = "additional.story.folders";

	Set<String> paths = new HashSet<>();

	private StoryFolders() {
		super(USER_PREFS_KEY);
	}

	///////////////////////////////////////////////////////////////////////////
	private static StoryFolders instance = new StoryFolders();
	public static StoryFolders get() {
		return instance;
	}

}
