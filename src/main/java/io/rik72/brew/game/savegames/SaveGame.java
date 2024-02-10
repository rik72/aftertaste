package io.rik72.brew.game.savegames;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import io.rik72.brew.engine.story.Story;
import io.rik72.brew.engine.story.StoryDescriptor;
import io.rik72.brew.engine.story.StoryRefId;
import io.rik72.mammoth.delta.Deltas;

public class SaveGame implements Serializable {
	private StoryDescriptor storyDescriptor;
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

	public StoryDescriptor getStoryDescriptor() {
		return storyDescriptor;
	}

	public Deltas getDeltas() {
		return deltas;
	}

	public boolean checkStoryCompatibility(StoryRefId storyRefId) {
		return this.storyDescriptor.getRefId().equals(storyRefId);
	}

	///////////////////////////////////////////////////////////////////////////
	private static SaveGame instance;
	public static SaveGame getInstance() {
		if (instance == null) {
			instance = new SaveGame();
			instance.setStoryDescriptor(Story.get().getDescriptor());
			instance.setDeltas(Deltas.get());
		}
		return instance;
	}

	public void setStoryDescriptor(StoryDescriptor storyDescriptor) {
		this.storyDescriptor = storyDescriptor;
	}

	private void setDeltas(Deltas deltas) {
		this.deltas = deltas;
	}

	private static void setInstance(SaveGame instance) {
		SaveGame.instance = instance;
	}
}
