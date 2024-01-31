package io.rik72.brew.engine.loader.loaders;

import java.util.List;

import io.rik72.amber.logger.Log;
import io.rik72.brew.engine.db.entities.Location;
import io.rik72.brew.engine.db.entities.LocationStatus;
import io.rik72.brew.engine.db.entities.LocationStatusPossibility;
import io.rik72.brew.engine.db.entities.LocationXLocation;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.repositories.LocationRepository;
import io.rik72.brew.engine.db.repositories.LocationStatusPossibilityRepository;
import io.rik72.brew.engine.db.repositories.LocationStatusRepository;
import io.rik72.brew.engine.db.repositories.LocationXLocationRepository;
import io.rik72.brew.engine.loader.LoadPath;
import io.rik72.brew.engine.loader.Loadable;
import io.rik72.brew.engine.loader.Loader;
import io.rik72.brew.engine.loader.YmlParser;
import io.rik72.brew.engine.loader.loaders.parsing.docs.Docs;
import io.rik72.brew.engine.loader.loaders.parsing.docs.Helpers;
import io.rik72.brew.engine.loader.loaders.parsing.parsers.Directions;
import io.rik72.brew.engine.loader.loaders.parsing.parsers.Parser;
import io.rik72.brew.engine.loader.loaders.parsing.parsers.Possibility;
import io.rik72.brew.engine.loader.loaders.parsing.parsers.exceptions.IllegalParseException;
import io.rik72.brew.engine.loader.loaders.parsing.raw.LocationRaw;
import io.rik72.brew.engine.loader.loaders.parsing.raw.LocationStatusRaw;
import io.rik72.brew.engine.loader.loaders.parsing.raw.PossibilityRaw;
import io.rik72.mammoth.db.DB;

public class LocationLoader implements Loadable {

	@Override
	public void load(LoadPath loadPath) throws Exception {

		YmlParser parser = new YmlParser(Docs.Locations.class);
		Docs.Locations doc = (Docs.Locations) parser.parse(loadPath, "locations.yml");

		for (LocationRaw locItem : doc.locations) {
			Parser.checkNotEmpty("location name", locItem.name);
			DB.persist(new Location(locItem.name));
		}

		// insert words for locations
		for (LocationRaw locItem : doc.locations) {
			Helpers.loadWord(locItem.word, Word.Type.NAME);
			Parser.checkNotEmpty("location word", locItem.word);
			Parser.checkNotEmpty("location status list", locItem.statuses);
			for (LocationStatusRaw stItem : locItem.statuses) {
				Helpers.loadWord(stItem.word, Word.Type.NAME);
			}
		}

		// second pass for statuses
		for (LocationRaw locItem : doc.locations) {
			Location location = LocationRepository.get().getByName(locItem.name);
			String lastDescription = null;
			boolean firstStatus = true;
			for (LocationStatusRaw stItem : locItem.statuses) {
				Parser.checkNotEmpty("location status label", stItem.status);
				if (stItem.description == null) {
					if (lastDescription == null && "initial".equals(stItem.status))
						throw new Exception("Initial description of location \"" + location.getName() + "\" cannot be null");
				}
				else {
					lastDescription = stItem.description.strip();
				}
				if (stItem.image == null)
					stItem.image = stItem.status;
				LocationStatus status = new LocationStatus(location.getName(), stItem.status, stItem.image, lastDescription,
					stItem.word != null ? stItem.word.text : locItem.word.text, stItem.finale != null ? stItem.finale.strip() : null);
				DB.persist(status);
				if (firstStatus && !"initial".equals(stItem.status))
					throw new IllegalParseException("location status list: first item must be the 'initial' status");
				firstStatus = false;
				if (stItem.directions != null) {
					Directions directions = Directions.parse(stItem.directions);
					if (directions.east != null)
						addConnectedLocation(status, directions.east.location, "east", directions.east.verbs);
					if (directions.north != null)
						addConnectedLocation(status, directions.north.location, "north", directions.north.verbs);
					if (directions.northeast != null)
						addConnectedLocation(status, directions.northeast.location, "northeast", directions.northeast.verbs);
					if (directions.northwest != null)
						addConnectedLocation(status, directions.northwest.location, "northwest", directions.northwest.verbs);
					if (directions.south != null)
						addConnectedLocation(status, directions.south.location, "south", directions.south.verbs);
					if (directions.southeast != null)
						addConnectedLocation(status, directions.southeast.location, "southeast", directions.southeast.verbs);
					if (directions.southwest != null)
						addConnectedLocation(status, directions.southwest.location, "southwest", directions.southwest.verbs);
					if (directions.west != null)
						addConnectedLocation(status, directions.west.location, "west", directions.west.verbs);
					if (directions.up != null)
						addConnectedLocation(status, directions.up.location, "up", directions.up.verbs);
					if (directions.down != null)
						addConnectedLocation(status, directions.down.location, "down", directions.down.verbs);
				}
				if (stItem.possibilities != null) for (PossibilityRaw pDoc : stItem.possibilities) {
					Possibility pItem = Possibility.parse(pDoc);
					LocationStatusPossibility possibility;
					if (pItem.inherit != null) {
						LocationStatusPossibility parent =
							LocationStatusPossibilityRepository.get().getByLocationStatusAction(
								location, pItem.inherit, pItem.verb);
						if (parent == null)
							throw new IllegalParseException("inherit reference to unknown status", pItem.inherit);
						possibility = new LocationStatusPossibility(
							locItem.name, stItem.status, parent.getAction().getText(),
							parent.isPossible(), parent.isImportant(), parent.getFeedback());
						if (pItem.possible != null)
							possibility.setPossible(pItem.possible);
						if (pItem.important != null)
							possibility.setImportant(pItem.important);
						if (pItem.feedback != null)
							possibility.setFeedback(pItem.feedback);
					}
					else {
						possibility = new LocationStatusPossibility(
							locItem.name, stItem.status, pItem.verb, pItem.possible, pItem.important, pItem.feedback);
					}
					DB.persist(possibility);
				}
			}
			location.setStatus("initial");
			DB.persist(location);
		}
	}

	private void addConnectedLocation(LocationStatus fromStatus, String toLocationName, String direction, List<String> verbs) {
		if (verbs != null)
			for (String action : verbs) {
				LocationXLocation locationXlocation = new LocationXLocation(fromStatus, toLocationName, direction, action);
				DB.persist(locationXlocation);
			}
		else {
			LocationXLocation locationXlocation = new LocationXLocation(fromStatus, toLocationName, direction, null);
			DB.persist(locationXlocation);
		}
	}

	@Override
	public void register() {
		Loader.get().register(this);
	}

	public void reload() {

		YmlParser parser = new YmlParser(Docs.Locations.class);
		Docs.Locations doc = (Docs.Locations) parser.parse("brew/stories/test/locations.yml");

		for (LocationRaw locItem : doc.locations) {
			Location location = LocationRepository.get().getByName(locItem.name);
			location.setStatus("initial");
			DB.persist(location);
		}
	}

	@Override
	public void dump() {
		Log.skip();
		Log.debug("LOCATIONS ==============================================");
		List<Location> locations = LocationRepository.get().findAll();
		locations.forEach(s -> Log.debug(s));
		Log.skip();
		Log.debug("LOCATION STATUSES ======================================");
		List<LocationStatus> statuses = LocationStatusRepository.get().findAll();
		statuses.forEach(s -> Log.debug(s));
		Log.skip();
		Log.debug("LOCATION X LOCATION ====================================");
		List<LocationXLocation> lxls = LocationXLocationRepository.get().findAll();
		lxls.forEach(s -> Log.debug(s));
		Log.skip();
		Log.debug("LOCATION STATUS POSSIBILITIES ==========================");
		List<LocationStatusPossibility> possibilities = LocationStatusPossibilityRepository.get().findAll();
		possibilities.forEach(s -> Log.debug(s));
	}
}
