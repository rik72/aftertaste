package io.rik72.brew.engine.story.registry;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import io.rik72.aftertaste.config.UserPreferences;

public abstract class StoryRepo {

	private final String repoPrefsKey;
	//private static final String USER_PREFS_ADDITIONAL_STORY_FOLDERS = "additional.story.folders";

	Set<String> paths = new HashSet<>();

	protected StoryRepo(String repoPrefsKey) {
		this.repoPrefsKey = repoPrefsKey;
		String pathString = getPref();
		if (pathString != null && pathString.length() > 0) {
			String[] pathArray = pathString.split(":");
			paths.addAll(Arrays.asList(pathArray));
		}
	}

	public final Set<String> getAll() {
		return paths;
	}

	public boolean add(String path) {
		boolean wasAdded = paths.add(path);
		if (wasAdded)
			putPref();
		return wasAdded;
	}

	public void remove(String path) {
		boolean wasRemoved = paths.remove(path);
		if (wasRemoved)
			putPref();
	}
	
	private String getPref() {
		return UserPreferences.get(repoPrefsKey);
	}
	
	public void putPref() {
		UserPreferences.put(repoPrefsKey, String.join(":::", paths));
	}
}
