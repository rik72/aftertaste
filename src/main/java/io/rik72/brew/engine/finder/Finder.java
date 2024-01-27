package io.rik72.brew.engine.finder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.rik72.brew.engine.loader.LoadType;
import io.rik72.brew.engine.story.StoryDescriptor;

public class Finder {

	private static final String STORY_ROOT_RESOURCE_FOLDER = "brew/stories";

	private Map<LoadPath, StoryDescriptor> availableStories = new HashMap<>();

	public void init() throws Exception {
		String[] files = getResourceListing(getClass(), STORY_ROOT_RESOURCE_FOLDER);
System.out.println(files.length);
		for (String file : files) {
System.out.println("["+file+"]");
			if (file.length() == 0)
				continue;
			String path = STORY_ROOT_RESOURCE_FOLDER + "/" + file;
			availableStories.put(new LoadPath(path, LoadType.RESOURCES),
				StoryDescriptor.loadDescriptor(
					path + "/story.yml"));
			break;
		}
	}

	public Map<LoadPath, StoryDescriptor> getAvailableStories() {
		return availableStories;
	}

	String[] getResourceListing(Class<?> clazz, String path) throws URISyntaxException, IOException {
		URL dirURL = clazz.getClassLoader().getResource(path);
		if (dirURL != null && dirURL.getProtocol().equals("file")) {
			/* A file path: easy enough */
			return new File(dirURL.toURI()).list();
		} 

		if (dirURL == null) {
			/* 
			* In case of a jar file, we can't actually find a directory.
			* Have to assume the same jar as clazz.
			*/
			String me = clazz.getName().replace(".", "/")+".class";
			dirURL = clazz.getClassLoader().getResource(me);
		}
		
		if (dirURL.getProtocol().equals("jar")) {
			/* A JAR path */
			String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
			JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
			Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
			Set<String> result = new HashSet<String>(); //avoid duplicates in case it is a subdirectory
			while (entries.hasMoreElements()) {
				String name = entries.nextElement().getName();
				if (name.startsWith(path)) { //filter according to the path
System.out.println(name);
					String entry = name.substring(path.length() + 1);
System.out.println(entry);
					int checkSubdir = entry.indexOf("/");
System.out.println(checkSubdir);
					if (checkSubdir >= 0) {
						// if it is a subdirectory, we just return the directory name
						entry = entry.substring(0, checkSubdir);
					}
System.out.println(entry);
					result.add(entry);
				}
			}
			return result.toArray(new String[result.size()]);
		} 
			
		throw new UnsupportedOperationException("Cannot list files for URL "+dirURL);
  	}
	
	private Set<String> listFilesInDir(String dir) throws IOException {
		try (Stream<Path> stream = Files.list(Paths.get(dir))) {
			return stream
			.filter(file -> !Files.isDirectory(file))
			.map(Path::getFileName)
			.map(Path::toString)
			.collect(Collectors.toSet());
		}
	}

	///////////////////////////////////////////////////////////////////////////
	private static Finder instance = new Finder();
	public static Finder get() {
		return instance;
	}
}
