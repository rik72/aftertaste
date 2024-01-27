package io.rik72.brew.engine.story;

import java.io.Serializable;

public class StoryRefId implements Serializable {

	private static final long serialVersionUID = 1L;

	private String artifactId;
	private int saveVersion;

	public StoryRefId() {
	}

	public StoryRefId(String artifactId, int saveVersion) {
		this.artifactId = artifactId;
		this.saveVersion = saveVersion;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public int getSaveVersion() {
		return saveVersion;
	}

	@Override
	public String toString() {
		return artifactId + ":" + saveVersion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((artifactId == null) ? 0 : artifactId.hashCode());
		result = prime * result + saveVersion;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StoryRefId other = (StoryRefId) obj;
		if (artifactId == null) {
			if (other.artifactId != null)
				return false;
		} else if (!artifactId.equals(other.artifactId))
			return false;
		if (saveVersion != other.saveVersion)
			return false;
		return true;
	}
	
}
