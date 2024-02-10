package io.rik72.brew.engine.story;

import java.io.Serializable;

import io.rik72.brew.engine.loader.LoadPath;
import io.rik72.brew.engine.loader.YmlParser;
import io.rik72.brew.engine.loader.loaders.parsing.docs.Docs;

public class StoryDescriptor implements Serializable {
	private LoadPath loadPath;
	private StoryRefId refId;
	private String version;
	private String title;
	private String subtitle;
	
	public StoryDescriptor(LoadPath loadPath, StoryRefId refId, String version, String title, String subtitle) {
		this.loadPath = loadPath;
		this.refId = refId;
		this.version = version;
		this.title = title;
		this.subtitle = subtitle;
	}

	public LoadPath getLoadPath() {
		return loadPath;
	}

	public StoryRefId getRefId() {
		return refId;
	}

	public String getVersion() {
		return version;
	}

	public String getTitle() {
		return title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public static StoryDescriptor load(LoadPath loadPath) throws Exception {
		YmlParser parser = new YmlParser(Docs.Story.class);
		Docs.Story doc = (Docs.Story) parser.parse(loadPath, "story.yml");
		
		StoryDescriptor res = new StoryDescriptor(
			loadPath, new StoryRefId(doc.story.artifactId.strip(), doc.story.saveVersion),
			doc.story.version.strip(), doc.story.title.strip(), doc.story.subtitle.strip());

		return res;
	}
}
