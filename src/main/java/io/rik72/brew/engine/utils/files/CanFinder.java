package io.rik72.brew.engine.utils.files;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class CanFinder implements Finder {
	
	@Override
	public Collection<String> findFiles(String fileName, String path) throws Exception {
		return findFilesInCan(fileName, path);
	}
	
	@Override
	public Collection<InputStream> findInputStreams(String fileName, String path) throws Exception {
		List<InputStream> inputStreams = new ArrayList<>();
		try (ZipFile zipFile = new ZipFile(path)) {
			for (ZipEntry zipEntry : Collections.list(zipFile.entries()))
				if (zipEntry.getName().endsWith(fileName)) {
					InputStream zipEntryStream = new BufferedInputStream(zipFile.getInputStream(zipEntry));
					// transfers to memory and then to new stream, because zipEntryStream must be closed
					inputStreams.add(new ByteArrayInputStream(streamToString(zipEntryStream).getBytes()));
				}
		}
		return inputStreams;
	}

	private String streamToString(InputStream inputStream) throws Exception {
		StringBuilder textBuilder = new StringBuilder();
		try (
			InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
			Reader reader = new BufferedReader(streamReader)
		) {
			int c = 0;
			while ((c = reader.read()) != -1)
				textBuilder.append((char) c);
		}
		return textBuilder.toString();
	}

	private List<? extends ZipEntry> listFilesInCan(String path) throws IOException {
		try (ZipFile zip = new ZipFile(path)) {
			return Collections.list(zip.entries());
		}
	}

	private Set<String> findFilesInCan(String fileName, String path) throws IOException {
		return listFilesInCan(path).stream()
			.filter(e -> e.getName().endsWith(fileName))
			.map(e -> e.getName())
			.collect(Collectors.toSet());
	}

	///////////////////////////////////////////////////////////////////////////
	private static CanFinder instance = new CanFinder();
	public static CanFinder get() {
		return instance;
	}
}
