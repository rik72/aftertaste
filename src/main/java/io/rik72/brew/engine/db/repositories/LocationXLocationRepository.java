package io.rik72.brew.engine.db.repositories;

import java.util.List;

import io.rik72.brew.engine.db.entities.LocationStatus;
import io.rik72.brew.engine.db.entities.LocationXLocation;
import io.rik72.mammoth.repositories.AbstractRepository;

public class LocationXLocationRepository extends AbstractRepository<LocationXLocation> {

	private LocationXLocationRepository() {
		super(LocationXLocation.class);
	}

	public List<LocationXLocation> findByFromStatus(LocationStatus status) {
		return findByField("fromStatus", status);
	}

	///////////////////////////////////////////////////////////////////////////
	private static LocationXLocationRepository instance = new LocationXLocationRepository();
	public static LocationXLocationRepository get() {
		return instance;
	}
}
