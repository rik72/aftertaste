package io.rik72.brew.engine.utils;

import java.io.InputStream;
import java.util.Collection;

import io.rik72.brew.engine.loader.LoadPath;
import io.rik72.brew.engine.utils.files.CanFinder;
import io.rik72.brew.engine.utils.files.FolderFinder;
import io.rik72.brew.engine.utils.files.ResourcesFinder;

public class FileUtils {

	public static Collection<String> findFiles(String filename, LoadPath loadPath) throws Exception {
		switch (loadPath.getLoadType()) {
			case FOLDER: {
				return FolderFinder.get().findFiles(filename, loadPath.getPath());
			}

			case RESOURCES: {
				return ResourcesFinder.get().findFiles(filename, loadPath.getPath());
			}

			case CAN: {
				return CanFinder.get().findFiles(filename, loadPath.getPath());
			}
		}
		return null;
	}

	public static Collection<InputStream> findInputStreams(String filename, LoadPath loadPath) throws Exception {
		switch (loadPath.getLoadType()) {
			case FOLDER: {
				return FolderFinder.get().findInputStreams(filename, loadPath.getPath());
			}

			case RESOURCES: {
				return ResourcesFinder.get().findInputStreams(filename, loadPath.getPath());
			}

			case CAN: {
				return CanFinder.get().findInputStreams(filename, loadPath.getPath());
			}
		}
		return null;
	}
}
