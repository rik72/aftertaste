package io.rik72.brew.engine.db.repositories;

import java.util.List;

import io.rik72.brew.engine.db.entities.ThingStatus;
import io.rik72.brew.engine.db.entities.CharacterOnThing;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.entities.abstractions.Status;
import io.rik72.mammoth.db.DB;
import io.rik72.mammoth.repositories.AbstractRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

public class CharacterOnThingRepository extends AbstractRepository<CharacterOnThing> {

	private CharacterOnThingRepository() {
		super(CharacterOnThing.class);
	}

	public List<CharacterOnThing> findByParts(Word action, Status complementStatus) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<CharacterOnThing> cr = cb.createQuery(entityClass);
		Root<CharacterOnThing> root = cr.from(entityClass);
		Order idAsc = cb.asc(root.get("id"));
		cr.select(root).where(cb.and(
			cb.equal(root.get("action"), action),
			cb.equal(root.get("complementStatus"), complementStatus))).orderBy(idAsc);
		return DB.createQuery(cr).list();
	}

	public List<CharacterOnThing> findByActionAndAfterStatus(Word action, ThingStatus afterStatus) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<CharacterOnThing> cr = cb.createQuery(entityClass);
		Root<CharacterOnThing> root = cr.from(entityClass);
		cr.select(root).where(cb.and(
			cb.equal(root.get("action"), action),
			cb.equal(root.get("afterStatus"), afterStatus)));
		return DB.createQuery(cr).list();
	}

	///////////////////////////////////////////////////////////////////////////
	private static CharacterOnThingRepository instance = new CharacterOnThingRepository();
	public static CharacterOnThingRepository get() {
		return instance;
	}
}
