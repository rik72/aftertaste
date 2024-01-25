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
}
