package io.rik72.brew.engine.story.registry;

import java.util.HashSet;
import java.util.Set;

public class StoryCans extends StoryRepo {

	private static final String USER_PREFS_KEY = "additional.story.cans";

	Set<String> paths = new HashSet<>();

	private StoryCans() {
		super(USER_PREFS_KEY);
	}

	///////////////////////////////////////////////////////////////////////////
	private static StoryCans instance = new StoryCans();
	public static StoryCans get() {
		return instance;
	}

}
