package io.rik72.brew.engine.db.repositories;

import java.util.List;

import io.rik72.brew.engine.db.entities.Location;
import io.rik72.brew.engine.db.entities.LocationStatus;
import io.rik72.brew.engine.db.entities.Thing;
import io.rik72.brew.engine.db.repositories.abstractions.ComplementRepository;
import io.rik72.mammoth.db.DB;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;

public class LocationRepository extends ComplementRepository<Location> {

	private LocationRepository() {
		super(Location.class);
	}

	public Location getByStatusCanonical(String value) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<Location> cr = cb.createQuery(entityClass);
		Root<Location> root = cr.from(entityClass);
		Join<Location, LocationStatus> csj = root.join("status", JoinType.INNER);
		cr.select(root).where(cb.equal(csj.get("canonical"), value));
		List<Location> list = DB.createQuery(cr).list();
		return list.size() >= 1 ? list.get(0) : null;
	}

	@Override
	public void deleteAll() {
		List<Thing> allT = ThingRepository.get().findAll();
		for (Thing t : allT) {
			t.unsetLocation();
			DB.persist(t);
		}
		super.deleteAll();
	}
	
	///////////////////////////////////////////////////////////////////////////
	private static LocationRepository instance = new LocationRepository();
	public static LocationRepository get() {
		return instance;
	}
}
