package io.rik72.brew.engine.loader;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import io.rik72.amber.logger.Log;
import io.rik72.brew.engine.loader.loaders.parsing.docs.Docs.Mergeable;
import io.rik72.brew.engine.utils.FileUtils;

public class YmlParser {

	private Yaml yaml;

	public YmlParser(Class<? extends Mergeable> theClass) {
		yaml = new Yaml(new Constructor(theClass, new LoaderOptions()));
	}

	public YmlParser() {
		yaml = new Yaml();
	}

	public Mergeable parse(LoadPath loadPath, String fileName) throws Exception {

		List<InputStream> inputStreams = new ArrayList<>();
		List<Mergeable> parsed = new ArrayList<>();

		if (loadPath.getLoadType() == LoadType.RESOURCES) {
			String[] paths = FileUtils.findFilesInResources(getClass(), fileName, loadPath.getPath());
			Log.skip();
			Log.debug(" >>>> " + Arrays.asList(paths));
			for (String path : paths)
				inputStreams.add(this.getClass().getClassLoader().getResourceAsStream(path));
		}
		else {
			Set<String> paths = FileUtils.findFilesInDir(fileName, loadPath.getPath());
			Log.skip();
			Log.debug(" >>>> " + paths);
			for (String path : paths)
				inputStreams.add(new FileInputStream(path));
		}

		for (InputStream inputStream : inputStreams)
			parsed.add(yaml.load(inputStream));
		Mergeable results = parsed.remove(0);
		for (Mergeable toMerge : parsed)
			results.merge(toMerge);
		return results;
	}
}
