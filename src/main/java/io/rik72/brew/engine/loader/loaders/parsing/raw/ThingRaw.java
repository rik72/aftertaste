package io.rik72.brew.engine.loader.loaders.parsing.raw;

import java.util.List;

public class ThingRaw extends Raw {
	public String name;
	public String location;
	public WordRaw word;
	public boolean visible = true;
	public boolean takeable = false;
	public boolean droppable = true;
	public boolean plural = false;
	public List<ThingStatusRaw> statuses;
}