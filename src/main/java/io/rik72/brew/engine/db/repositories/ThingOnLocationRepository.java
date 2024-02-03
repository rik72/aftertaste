package io.rik72.brew.engine.db.repositories;

import java.util.List;

import io.rik72.brew.engine.db.entities.ThingOnLocation;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.entities.abstractions.Status;
import io.rik72.mammoth.db.DB;
import io.rik72.mammoth.repositories.AbstractRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

public class ThingOnLocationRepository extends AbstractRepository<ThingOnLocation> {

	private ThingOnLocationRepository() {
		super(ThingOnLocation.class);
	}

	public List<ThingOnLocation> findByParts(Word action, Status complementStatus) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<ThingOnLocation> cr = cb.createQuery(entityClass);
		Root<ThingOnLocation> root = cr.from(entityClass);
		Order idAsc = cb.asc(root.get("id"));
		cr.select(root).where(cb.and(
			cb.equal(root.get("action"), action),
			cb.equal(root.get("complementStatus"), complementStatus))).orderBy(idAsc);
		return DB.createQuery(cr).list();
	}

	///////////////////////////////////////////////////////////////////////////
	private static ThingOnLocationRepository instance = new ThingOnLocationRepository();
	public static ThingOnLocationRepository get() {
		return instance;
	}
}
