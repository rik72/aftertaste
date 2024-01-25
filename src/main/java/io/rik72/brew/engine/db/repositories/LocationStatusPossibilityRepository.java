package io.rik72.brew.engine.db.repositories;

import java.util.List;

import io.rik72.brew.engine.db.entities.Location;
import io.rik72.brew.engine.db.entities.LocationStatus;
import io.rik72.brew.engine.db.entities.LocationStatusPossibility;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.mammoth.db.DB;
import io.rik72.mammoth.repositories.AbstractRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;

public class LocationStatusPossibilityRepository extends AbstractRepository<LocationStatusPossibility> {

	private LocationStatusPossibilityRepository() {
		super(LocationStatusPossibility.class);
	}

	public List<LocationStatusPossibility> findByLocation(Location location) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<LocationStatusPossibility> cr = cb.createQuery(entityClass);
		Root<LocationStatusPossibility> root = cr.from(entityClass);
		Join<LocationStatusPossibility, LocationStatus> csj = root.join("status", JoinType.INNER);
		Join<LocationStatus, Location> lj = csj.join("location", JoinType.INNER);
		cr.select(root).where(cb.and(
				cb.equal(csj.get("location"), location),
				cb.equal(root.get("status"), lj.get("status"))));
		// Root<LocationStatus> root = cr.from(LocationStatus.class);
		// Join<LocationStatus, LocationStatusPossibility> csj = root.join("status", JoinType.INNER);
		return DB.createQuery(cr).list();
	}

	public LocationStatusPossibility getByLocationStatusAction(Location location, String statusLabel, String action) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<LocationStatusPossibility> cr = cb.createQuery(entityClass);
		Root<LocationStatusPossibility> root = cr.from(entityClass);
		Join<LocationStatusPossibility, Word> wj = root.join("action", JoinType.INNER);
		Join<LocationStatusPossibility, LocationStatus> csj = root.join("status", JoinType.INNER);
		cr.select(root).where(cb.and(
			cb.equal(csj.get("location"), location),
			cb.equal(csj.get("label"), statusLabel),
			cb.equal(wj.get("text"), action)));
		List<LocationStatusPossibility> list = DB.createQuery(cr).list();
		return list.size() >= 1 ? list.get(0) : null;
	}

	///////////////////////////////////////////////////////////////////////////
	private static LocationStatusPossibilityRepository instance = new LocationStatusPossibilityRepository();
	public static LocationStatusPossibilityRepository get() {
		return instance;
	}
}
