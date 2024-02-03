package io.rik72.brew.engine.db.repositories;

import java.util.List;

import io.rik72.brew.engine.db.entities.CharacterStatus;
import io.rik72.brew.engine.db.entities.ThingOnCharacter;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.entities.abstractions.Status;
import io.rik72.mammoth.db.DB;
import io.rik72.mammoth.repositories.AbstractRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

public class ThingOnCharacterRepository extends AbstractRepository<ThingOnCharacter> {

	private ThingOnCharacterRepository() {
		super(ThingOnCharacter.class);
	}

	public List<ThingOnCharacter> findByParts(Word action, Status complementStatus) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<ThingOnCharacter> cr = cb.createQuery(entityClass);
		Root<ThingOnCharacter> root = cr.from(entityClass);
		Order idAsc = cb.asc(root.get("id"));
		cr.select(root).where(cb.and(
			cb.equal(root.get("action"), action),
			cb.equal(root.get("complementStatus"), complementStatus))).orderBy(idAsc);
		return DB.createQuery(cr).list();
	}

	public List<ThingOnCharacter> findByActionAndAfterStatus(Word action, CharacterStatus afterStatus) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<ThingOnCharacter> cr = cb.createQuery(entityClass);
		Root<ThingOnCharacter> root = cr.from(entityClass);
		cr.select(root).where(cb.and(
			cb.equal(root.get("action"), action),
			cb.equal(root.get("afterStatus"), afterStatus)));
		return DB.createQuery(cr).list();
	}

	///////////////////////////////////////////////////////////////////////////
	private static ThingOnCharacterRepository instance = new ThingOnCharacterRepository();
	public static ThingOnCharacterRepository get() {
		return instance;
	}
}
