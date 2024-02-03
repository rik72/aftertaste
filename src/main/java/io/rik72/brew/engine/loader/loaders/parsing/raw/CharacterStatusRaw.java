package io.rik72.brew.engine.loader.loaders.parsing.raw;

import java.util.List;

public class CharacterStatusRaw extends Raw {
	public String status;
	public String brief;
	public String description;
	public WordRaw word;
	public List<PossibilityRaw> possibilities;
	public List<ActionRaw> actions;
	public String finale;
}