package io.rik72.brew.engine.loader.loaders.parsing.parsers;

import io.rik72.brew.engine.loader.loaders.parsing.raw.DirectionsRaw;

public class Directions extends Parser {

	public Direction north;
	public Direction northeast;
	public Direction east;
	public Direction southeast;
	public Direction south;
	public Direction southwest;
	public Direction west;
	public Direction northwest;
	public Direction up;
	public Direction down;

	public Directions(Direction north, Direction northeast, Direction east, Direction southeast, Direction south,
			Direction southwest, Direction west, Direction northwest, Direction up, Direction down) {
		this.north = north;
		this.northeast = northeast;
		this.east = east;
		this.southeast = southeast;
		this.south = south;
		this.southwest = southwest;
		this.west = west;
		this.northwest = northwest;
		this.up = up;
		this.down = down;
	}

	public static Directions parse(DirectionsRaw raw) {

		Direction north 	= Direction.parse(raw.north);
		Direction northeast = Direction.parse(raw.northeast);
		Direction east 		= Direction.parse(raw.east);
		Direction southeast = Direction.parse(raw.southeast);
		Direction south 	= Direction.parse(raw.south);
		Direction southwest = Direction.parse(raw.southwest);
		Direction west 		= Direction.parse(raw.west);
		Direction northwest = Direction.parse(raw.northwest);
		Direction up 		= Direction.parse(raw.up);
		Direction down 		= Direction.parse(raw.down);
		
		return new Directions(north, northeast, east, southeast, south, southwest, west, northwest, up, down);
	}
}