package io.rik72.brew.engine.utils.files;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FolderFinder implements Finder {
	
	@Override
	public Collection<String> findFiles(String fileName, String path) throws Exception {
		return findFilesInDir(fileName, path);
	}
	
	@Override
	public Collection<InputStream> findInputStreams(String fileName, String path) throws Exception {
		List<InputStream> inputStreams = new ArrayList<>();
		Set<String> paths = findFilesInDir(fileName, path);
		for (String p : paths)
			inputStreams.add(new FileInputStream(p));
		return inputStreams;
	}

	private Set<String> listFilesInDir(String dir) throws IOException {
		try (Stream<Path> stream = Files.walk(Paths.get(dir))) {
			return stream
				.filter(file -> !Files.isDirectory(file))
				.map(p -> {
					return p.getParent().toString() + "/" + p.getFileName().toString();
				})
				.collect(Collectors.toSet());
		}
	}

	private Set<String> findFilesInDir(String fileName, String path) throws IOException {
		return listFilesInDir(path).stream()
			.filter(file -> file.endsWith(fileName))
			.collect(Collectors.toSet());
	}

	///////////////////////////////////////////////////////////////////////////
	private static FolderFinder instance = new FolderFinder();
	public static FolderFinder get() {
		return instance;
	}
}
