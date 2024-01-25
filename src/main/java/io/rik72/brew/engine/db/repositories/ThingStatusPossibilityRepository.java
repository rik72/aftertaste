package io.rik72.brew.engine.db.repositories;

import java.util.List;

import io.rik72.brew.engine.db.entities.Location;
import io.rik72.brew.engine.db.entities.Thing;
import io.rik72.brew.engine.db.entities.ThingStatus;
import io.rik72.brew.engine.db.entities.ThingStatusPossibility;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.mammoth.db.DB;
import io.rik72.mammoth.repositories.AbstractRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;

public class ThingStatusPossibilityRepository extends AbstractRepository<ThingStatusPossibility> {

	private ThingStatusPossibilityRepository() {
		super(ThingStatusPossibility.class);
	}

	public List<ThingStatusPossibility> findByThing(Thing thing) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<ThingStatusPossibility> cr = cb.createQuery(entityClass);
		Root<ThingStatusPossibility> root = cr.from(entityClass);
		Join<ThingStatusPossibility, ThingStatus> csj = root.join("status", JoinType.INNER);
		cr.select(root).where(cb.equal(csj.get("character"), thing));
		return DB.createQuery(cr).list();
	}

	public List<ThingStatusPossibility> findByInventory(Location inventory) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<ThingStatusPossibility> cr = cb.createQuery(entityClass);
		Root<ThingStatusPossibility> root = cr.from(entityClass);
		Join<ThingStatusPossibility, ThingStatus> csj = root.join("status", JoinType.INNER);
		Join<ThingStatus, Thing> tj = csj.join("thing", JoinType.INNER);
		cr.select(root).where(cb.and(
			cb.equal(tj.get("location"), inventory),
			cb.equal(root.get("status"), tj.get("status"))));
		return DB.createQuery(cr).list();
	}

	public ThingStatusPossibility getByThingStatusAction(Thing thing, String statusLabel, String action) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<ThingStatusPossibility> cr = cb.createQuery(entityClass);
		Root<ThingStatusPossibility> root = cr.from(entityClass);
		Join<ThingStatusPossibility, Word> wj = root.join("action", JoinType.INNER);
		Join<ThingStatusPossibility, ThingStatus> csj = root.join("status", JoinType.INNER);
		cr.select(root).where(cb.and(
			cb.equal(csj.get("thing"), thing),
			cb.equal(csj.get("label"), statusLabel),
			cb.equal(wj.get("text"), action)));
		List<ThingStatusPossibility> list = DB.createQuery(cr).list();
		return list.size() >= 1 ? list.get(0) : null;
	}

	///////////////////////////////////////////////////////////////////////////
	private static ThingStatusPossibilityRepository instance = new ThingStatusPossibilityRepository();
	public static ThingStatusPossibilityRepository get() {
		return instance;
	}
}
