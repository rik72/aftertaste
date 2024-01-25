package io.rik72.brew.engine.db.repositories;

import java.util.List;

import io.rik72.brew.engine.db.entities.Location;
import io.rik72.brew.engine.db.entities.LocationStatus;
import io.rik72.mammoth.db.DB;
import io.rik72.mammoth.repositories.AbstractRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class LocationStatusRepository extends AbstractRepository<LocationStatus> {

	private LocationStatusRepository() {
		super(LocationStatus.class);
	}

	public LocationStatus getByLocationAndLabel(Location location, String label) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<LocationStatus> cr = cb.createQuery(entityClass);
		Root<LocationStatus> root = cr.from(entityClass);
		cr.select(root).where(cb.and(cb.equal(root.get("location"), location), cb.equal(root.get("label"), label)));
		List<LocationStatus> list = DB.createQuery(cr).list();
		return list.size() >= 1 ? list.get(0) : null;
	}

	///////////////////////////////////////////////////////////////////////////
	private static LocationStatusRepository instance = new LocationStatusRepository();
	public static LocationStatusRepository get() {
		return instance;
	}
}
