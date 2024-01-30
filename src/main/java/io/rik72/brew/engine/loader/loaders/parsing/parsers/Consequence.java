package io.rik72.brew.engine.loader.loaders.parsing.parsers;

import io.rik72.brew.engine.loader.loaders.parsing.raw.ConsequenceRaw;

public class Consequence extends Parser {

	public EntityConsequence thingConsequence;
	public EntityConsequence locationConsequence;
	public EntityConsequence characterConsequence;
 
	public Consequence(EntityConsequence thingConsequence, EntityConsequence locationConsequence,
			EntityConsequence characterConsequence) {
		this.thingConsequence = thingConsequence;
		this.locationConsequence = locationConsequence;
		this.characterConsequence = characterConsequence;
	}

	public static Consequence parse(ConsequenceRaw raw) {

		EntityConsequence thingConsequence = EntityConsequence.parse(raw.thing);
		EntityConsequence locationConsequence = EntityConsequence.parse(raw.location);
		EntityConsequence characterConsequence = EntityConsequence.parse(raw.character);

		return new Consequence(thingConsequence, locationConsequence, characterConsequence);
	}
}