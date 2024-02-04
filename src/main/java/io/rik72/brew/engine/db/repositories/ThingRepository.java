package io.rik72.brew.engine.db.repositories;

import java.util.ArrayList;
import java.util.List;

import io.rik72.brew.engine.db.entities.Location;
import io.rik72.brew.engine.db.entities.Thing;
import io.rik72.brew.engine.db.entities.ThingStatus;
import io.rik72.brew.engine.db.repositories.abstractions.ComplementRepository;
import io.rik72.mammoth.db.DB;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ThingRepository extends ComplementRepository<Thing> {

	private ThingRepository() {
		super(Thing.class);
	}

	public Thing getVisibleByLocationAndCanonical(Location location, String canonical) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<Thing> cr = cb.createQuery(entityClass);
		Root<Thing> root = cr.from(entityClass);
		Join<Thing, ThingStatus> sj = root.join("status", JoinType.INNER);
		cr.select(root).where(cb.and(
			cb.equal(root.get("visible"), true),
			cb.equal(root.get("location"), location),
			cb.equal(sj.get("canonical"), canonical)));
		List<Thing> resList = DB.createQuery(cr).list();
		return resList.size() > 0 ? resList.get(0) : null;
	}

	public List<Thing> findByLocation(Location location) {
		return findByLocation(location, null, null);
	}

	public List<Thing> findByLocation(Location location, Boolean visible) {
		return findByLocation(location, visible, null);
	}

	public List<Thing> findByLocation(Location location, Boolean visible, Boolean takeable) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<Thing> cr = cb.createQuery(entityClass);
		Root<Thing> root = cr.from(entityClass);

		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.equal(root.get("location"), location));
		if (visible != null)
			predicates.add(cb.equal(root.get("visible"), visible));
		if (takeable != null)
			predicates.add(cb.equal(root.get("takeable"), takeable));

		cr.select(root).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		return DB.createQuery(cr).list();
	}

	///////////////////////////////////////////////////////////////////////////
	private static ThingRepository instance = new ThingRepository();
	public static ThingRepository get() {
		return instance;
	}
}
