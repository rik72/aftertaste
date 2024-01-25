package io.rik72.brew.engine.loader.loaders.docs.types.raw;

import java.util.List;

public class LocationStatusRaw extends Raw {
	public String status;
	public String description;
	public WordRaw word;
	public DirectionsRaw directions;
	public List<PossibilityRaw> possibilities;
}