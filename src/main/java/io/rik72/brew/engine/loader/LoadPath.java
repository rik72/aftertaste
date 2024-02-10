package io.rik72.brew.engine.loader;

import java.io.Serializable;

public class LoadPath implements Serializable {
	private String path;
	private LoadType loadType;
	
	public LoadPath(String path, LoadType loadType) {
		this.path = path;
		this.loadType = loadType;
	}

	public String getPath() {
		return path;
	}
	
	public LoadType getLoadType() {
		return loadType;
	}
}
