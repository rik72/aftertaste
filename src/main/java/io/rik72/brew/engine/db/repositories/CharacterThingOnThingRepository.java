package io.rik72.brew.engine.db.repositories;

import java.util.List;

import io.rik72.brew.engine.db.entities.CharacterThingOnThing;
import io.rik72.brew.engine.db.entities.ThingStatus;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.entities.abstractions.Status;
import io.rik72.mammoth.db.DB;
import io.rik72.mammoth.repositories.AbstractRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

public class CharacterThingOnThingRepository extends AbstractRepository<CharacterThingOnThing> {

	private CharacterThingOnThingRepository() {
		super(CharacterThingOnThing.class);
	}

	public List<CharacterThingOnThing> findByParts(Word action, Status complementStatus,
	                                             Word preposition, Status supplementStatus) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<CharacterThingOnThing> cr = cb.createQuery(entityClass);
		Root<CharacterThingOnThing> root = cr.from(entityClass);
		Order idAsc = cb.asc(root.get("id"));
		cr.select(root).where(cb.and(
			cb.equal(root.get("action"), action),
			cb.equal(root.get("complementStatus"), complementStatus),
			cb.equal(root.get("preposition"), preposition),
			cb.equal(root.get("supplementStatus"), supplementStatus))).orderBy(idAsc);
		return DB.createQuery(cr).list();
	}

	public List<CharacterThingOnThing> findByActionAndAfterStatus(Word action, ThingStatus afterStatus) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<CharacterThingOnThing> cr = cb.createQuery(entityClass);
		Root<CharacterThingOnThing> root = cr.from(entityClass);
		cr.select(root).where(cb.and(
			cb.equal(root.get("action"), action),
			cb.equal(root.get("afterStatus"), afterStatus)));
		return DB.createQuery(cr).list();
	}

	///////////////////////////////////////////////////////////////////////////
	private static CharacterThingOnThingRepository instance = new CharacterThingOnThingRepository();
	public static CharacterThingOnThingRepository get() {
		return instance;
	}
}
