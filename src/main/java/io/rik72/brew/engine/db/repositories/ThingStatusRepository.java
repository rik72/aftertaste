package io.rik72.brew.engine.db.repositories;

import java.util.List;

import io.rik72.brew.engine.db.entities.Thing;
import io.rik72.brew.engine.db.entities.ThingStatus;
import io.rik72.mammoth.db.DB;
import io.rik72.mammoth.repositories.AbstractRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class ThingStatusRepository extends AbstractRepository<ThingStatus> {

	private ThingStatusRepository() {
		super(ThingStatus.class);
	}

	public ThingStatus getByThingAndLabel(Thing thing, String label) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<ThingStatus> cr = cb.createQuery(entityClass);
		Root<ThingStatus> root = cr.from(entityClass);
		cr.select(root).where(cb.and(cb.equal(root.get("thing"), thing), cb.equal(root.get("label"), label)));
		List<ThingStatus> list = DB.createQuery(cr).list();
		return list.size() >= 1 ? list.get(0) : null;
	}

	@Override
	public void deleteAll() {
		List<Thing> allT = ThingRepository.get().findAll();
		for (Thing t : allT) {
			t.unsetStatus();
			DB.persist(t);
		}
		List<ThingStatus> allTS = findAll();
		for (ThingStatus s : allTS) {
			s.unsetThing();
			DB.persist(s);
		}
		super.deleteAll();
	}

	///////////////////////////////////////////////////////////////////////////
	private static ThingStatusRepository instance = new ThingStatusRepository();
	public static ThingStatusRepository get() {
		return instance;
	}
}
