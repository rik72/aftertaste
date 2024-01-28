package io.rik72.brew.engine.loader;

import java.io.InputStream;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class YmlParser {

	private Yaml yaml;

	public YmlParser(Class<?> theClass) {
		yaml = new Yaml(new Constructor(theClass, new LoaderOptions()));
	}

	public YmlParser() {
		yaml = new Yaml();
	}

	public Object parse(String path) {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
		return yaml.load(inputStream);
	}

	public Object parse(LoadPath loadPath, String fileName) {
		if (loadPath.getLoadType() == LoadType.RESOURCES) {
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(loadPath.getPath() + "/" + fileName);
			return yaml.load(inputStream);
		}
		return null;
	}
}
