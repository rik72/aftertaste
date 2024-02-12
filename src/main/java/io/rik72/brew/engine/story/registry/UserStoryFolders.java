package io.rik72.brew.engine.story.registry;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import io.rik72.aftertaste.config.UserPreferences;

public class UserStoryFolders {

	private static final String USER_PREFS_ADDITIONAL_STORY_FOLDERS = "additional.story.folders";

	Set<String> folders = new HashSet<>();

	private UserStoryFolders() {
		String foldersString = getPref();
		if (foldersString != null) {
			String[] folderArray = foldersString.split(":");
			folders.addAll(Arrays.asList(folderArray));
		}
	}

	public final Set<String> getFolders() {
		return folders;
	}

	public boolean addFolder(String folder) {
		boolean wasAdded = folders.add(folder);
		if (wasAdded)
			putPref();
		return wasAdded;
	}

	public void removeFolder(String folder) {
		boolean wasRemoved = folders.remove(folder);
		if (wasRemoved)
			putPref();
	}
	
	private String getPref() {
		return UserPreferences.get(USER_PREFS_ADDITIONAL_STORY_FOLDERS);
	}
	
	public void putPref() {
		UserPreferences.put(USER_PREFS_ADDITIONAL_STORY_FOLDERS, String.join(":", folders));
	}

	///////////////////////////////////////////////////////////////////////////
	private static UserStoryFolders instance = new UserStoryFolders();
	public static UserStoryFolders get() {
		return instance;
	}

}
