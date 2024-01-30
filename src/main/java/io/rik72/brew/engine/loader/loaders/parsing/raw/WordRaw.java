package io.rik72.brew.engine.loader.loaders.parsing.raw;

import java.util.List;

public class WordRaw extends Raw {
	public String text;
	public List<String> synonyms;
	// 1-actions
	public String complement; // ( inventory | location )
	// 2-actions
	public String supplement; // ( inventory | location )
}