package io.rik72.brew.engine.loader.loaders.parsing.raw;

import java.util.List;

public class WordRaw extends Raw {
	public String text;
	public List<String> synonyms;
	public String complement; // ( inventory | location )
	public String supplement; // ( inventory | location )
}