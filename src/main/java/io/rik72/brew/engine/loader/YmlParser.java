package io.rik72.brew.engine.loader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

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

		Collection<InputStream> inputStreams = FileUtils.findInputStreams(fileName, loadPath);

		List<Mergeable> parsed = new ArrayList<>();
		for (InputStream inputStream : inputStreams) {
			parsed.add(yaml.load(inputStream));
			inputStream.close();
		}

		if (parsed.size() > 0) {
			Mergeable results = parsed.remove(0);
			for (Mergeable toMerge : parsed)
				results.merge(toMerge);
			return results;
		}

		return null;
	}
}
