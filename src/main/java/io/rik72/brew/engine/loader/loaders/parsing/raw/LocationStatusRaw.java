package io.rik72.brew.engine.loader.loaders.parsing.raw;

import java.util.List;

public class LocationStatusRaw extends Raw {
	public String status;
	public String image;
	public String description;
	public WordRaw word;
	public DirectionsRaw directions;
	public List<PossibilityRaw> possibilities;
	public String transition;
	public String finale;
}