package io.rik72.brew.engine.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {

	public static String[] getResourceListing(Class<?> clazz, String path) throws URISyntaxException, IOException {
		
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
			String me = clazz.getName().replace(".", "/") + ".class";
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
					String entry = name.substring(path.length() + 1);
					int checkSubdir = entry.indexOf("/");
					if (checkSubdir >= 0) {
						// if it is a subdirectory, we just return the directory name
						entry = entry.substring(0, checkSubdir);
					}
					if (entry.length() > 0)
						result.add(entry);
				}
			}
			jar.close();
			return result.toArray(new String[result.size()]);
		} 
			
		throw new UnsupportedOperationException("Cannot list files for URL " + dirURL);
  	}

	public static String[] findFilesInResources(Class<?> clazz, String filename, String root) throws URISyntaxException, IOException {

		URL dirURL = clazz.getClassLoader().getResource(root);

		if (dirURL != null && dirURL.getProtocol().equals("file")) {
			return findFilesInResourcesFileProtocol(clazz, filename, root, "");
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
			Set<String> resultSet = new HashSet<String>(); //avoid duplicates in case it is a subdirectory
			while (entries.hasMoreElements()) {
				String name = entries.nextElement().getName();
				if (name.endsWith(filename))
					resultSet.add(name);
			}
			jar.close();
			return resultSet.toArray(new String[resultSet.size()]);
		} 
			
		throw new UnsupportedOperationException("Cannot list files for URL "+dirURL);
  	}
	
	public static String[] findFilesInResourcesFileProtocol(Class<?> clazz, String filename, String root, String path) throws URISyntaxException, IOException {
		Set<String> resultSet = new HashSet<String>(); //avoid duplicates in case it is a subdirectory

		String totalPath = root + (path.length() > 0 ? "/" + path : "");
		URL dirURL = clazz.getClassLoader().getResource(totalPath);
		String[] listing = new File(dirURL.toURI()).list();
		for (String file : listing) {
			if (file.equals(filename)) {
				resultSet.add(totalPath + "/" + filename);
				continue;
			}
			File thisFile = new File(clazz.getClassLoader().getResource(totalPath + "/" + file).toURI());
			if (thisFile.isDirectory())
				resultSet.addAll(Arrays.asList(findFilesInResourcesFileProtocol(clazz, filename, root,
					path.length() > 0 ? path + "/" + file : file)));
		}

		return resultSet.toArray(new String[resultSet.size()]);
	}

	public static Set<String> listFilesInDir(String dir) throws IOException {
		try (Stream<Path> stream = Files.list(Paths.get(dir))) {
			return stream
				.filter(file -> !Files.isDirectory(file))
				.map(java.nio.file.Path::getFileName)
				.map(Path::toString)
				.collect(Collectors.toSet());
		}
	}

	public static Set<String> findFilesInDir(String fileName, String path) throws IOException {
		return listFilesInDir(path).stream()
			.filter(file -> file.endsWith(fileName))
			.collect(Collectors.toSet());
	}
}
