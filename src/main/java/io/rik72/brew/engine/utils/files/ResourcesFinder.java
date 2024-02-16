package io.rik72.brew.engine.utils.files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ResourcesFinder implements Finder {
	
	@Override
	public Collection<String> findFiles(String fileName, String path) throws Exception {
		return Arrays.asList(findFilesInResources(getClass(), fileName, path));
	}

	@Override
	public Collection<InputStream> findInputStreams(String fileName, String path) throws Exception {
		List<InputStream> inputStreams = new ArrayList<>();
		String[] paths = findFilesInResources(getClass(), fileName, path);
		for (String p : paths)
			inputStreams.add(this.getClass().getClassLoader().getResourceAsStream(p));
		return inputStreams;
	}

	private String[] findFilesInResources(Class<?> clazz, String filename, String root) throws URISyntaxException, IOException {

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
	
	private String[] findFilesInResourcesFileProtocol(Class<?> clazz, String filename, String root, String path) throws URISyntaxException, IOException {
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

	///////////////////////////////////////////////////////////////////////////
	private static ResourcesFinder instance = new ResourcesFinder();
	public static ResourcesFinder get() {
		return instance;
	}
}
