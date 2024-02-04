package io.rik72.brew.game.savegames;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import io.rik72.brew.engine.story.Story;
import io.rik72.brew.engine.story.StoryRefId;
import io.rik72.mammoth.delta.Deltas;

public class SaveGame implements Serializable {
	private StoryRefId storyRefId;
	private Deltas deltas;

	public SaveGame() {
	}

	public static void saveToFile(String path) throws Exception {
		FileOutputStream f = new FileOutputStream(new File(path));
		ObjectOutputStream o = new ObjectOutputStream(f);
		o.writeObject(getInstance());
		o.close();
	}

	public static void loadFromFile(String path) throws Exception {
		FileInputStream f = new FileInputStream(new File(path));
		ObjectInputStream i = new ObjectInputStream(f);
		setInstance((SaveGame)i.readObject());
		i.close();
	}

	public StoryRefId getStoryRefId() {
		return storyRefId;
	}

	private void setStoryRefId(StoryRefId storyRefId) {
		this.storyRefId = storyRefId;
	}

	public Deltas getDeltas() {
		return deltas;
	}

	private void setDeltas(Deltas deltas) {
		this.deltas = deltas;
	}

	public boolean checkStoryCompatibility(Story story) {
		return checkStoryCompatibility(story.getRefId());
	}

	public boolean checkStoryCompatibility(StoryRefId storyRefId) {
		return this.storyRefId.equals(storyRefId);
	}

	///////////////////////////////////////////////////////////////////////////
	private static SaveGame instance;
	public static SaveGame getInstance() {
		if (instance == null) {
			instance = new SaveGame();
			instance.setDeltas(Deltas.get());
			instance.setStoryRefId(Story.get().getRefId());
		}
		return instance;
	}

	private static void setInstance(SaveGame instance) {
		SaveGame.instance = instance;
	}
}
