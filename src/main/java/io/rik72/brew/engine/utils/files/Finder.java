package io.rik72.brew.engine.utils.files;

import java.io.InputStream;
import java.util.Collection;

public interface Finder {
	public Collection<String> findFiles(String fileName, String path) throws Exception;
	public Collection<InputStream> findInputStreams(String fileName, String path) throws Exception;
}
