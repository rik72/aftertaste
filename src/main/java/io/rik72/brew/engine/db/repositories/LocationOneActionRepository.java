package io.rik72.brew.engine.db.repositories;

import java.util.List;

import io.rik72.brew.engine.db.entities.LocationOneAction;
import io.rik72.brew.engine.db.entities.ThingStatus;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.mammoth.db.DB;
import io.rik72.mammoth.repositories.AbstractRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

public class LocationOneActionRepository extends AbstractRepository<LocationOneAction> {

	private LocationOneActionRepository() {
		super(LocationOneAction.class);
	}

	public List<LocationOneAction> findByParts(Word action, ThingStatus complementStatus) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<LocationOneAction> cr = cb.createQuery(entityClass);
		Root<LocationOneAction> root = cr.from(entityClass);
		Order idAsc = cb.asc(root.get("id"));
		cr.select(root).where(cb.and(
			cb.equal(root.get("action"), action),
			cb.equal(root.get("complementStatus"), complementStatus))).orderBy(idAsc);
		return DB.createQuery(cr).list();
	}

	///////////////////////////////////////////////////////////////////////////
	private static LocationOneActionRepository instance = new LocationOneActionRepository();
	public static LocationOneActionRepository get() {
		return instance;
	}
}
