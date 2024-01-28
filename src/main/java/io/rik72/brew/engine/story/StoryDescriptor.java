package io.rik72.brew.engine.story;

import io.rik72.brew.engine.loader.LoadPath;
import io.rik72.brew.engine.loader.YmlParser;
import io.rik72.brew.engine.loader.loaders.docs.Docs;

public class StoryDescriptor {
	private StoryRefId refId;
	private String version;
	private String title;
	private String subtitle;
	
	public StoryDescriptor(StoryRefId refId, String version, String title, String subtitle) {
		this.refId = refId;
		this.version = version;
		this.title = title;
		this.subtitle = subtitle;
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

	public static StoryDescriptor load(LoadPath loadPath) {
		YmlParser parser = new YmlParser(Docs.Story.class);
		Docs.Story doc = (Docs.Story) parser.parse(loadPath.getPath() + "/story.yml");
		
		StoryDescriptor res = new StoryDescriptor(
			new StoryRefId(doc.story.artifactId.strip(), doc.story.saveVersion),
			doc.story.version.strip(), doc.story.title.strip(), doc.story.subtitle.strip());

		return res;
	}
}
