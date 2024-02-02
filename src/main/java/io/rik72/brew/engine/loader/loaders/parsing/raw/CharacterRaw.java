package io.rik72.brew.engine.loader.loaders.parsing.raw;

import java.util.List;

public class CharacterRaw extends Raw {
	public String name;
	public boolean main;
	public String startLocation;
	public WordRaw word;
	public boolean visible = true;
	public List<CharacterStatusRaw> statuses;
}