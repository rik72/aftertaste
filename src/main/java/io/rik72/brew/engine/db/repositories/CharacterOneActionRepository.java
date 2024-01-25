package io.rik72.brew.engine.db.repositories;

import java.util.List;

import io.rik72.brew.engine.db.entities.CharacterStatus;
import io.rik72.brew.engine.db.entities.CharacterOneAction;
import io.rik72.brew.engine.db.entities.ThingStatus;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.mammoth.db.DB;
import io.rik72.mammoth.repositories.AbstractRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

public class CharacterOneActionRepository extends AbstractRepository<CharacterOneAction> {

	private CharacterOneActionRepository() {
		super(CharacterOneAction.class);
	}

	public List<CharacterOneAction> findByParts(Word action, ThingStatus complementStatus) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<CharacterOneAction> cr = cb.createQuery(entityClass);
		Root<CharacterOneAction> root = cr.from(entityClass);
		Order idAsc = cb.asc(root.get("id"));
		cr.select(root).where(cb.and(
			cb.equal(root.get("action"), action),
			cb.equal(root.get("complementStatus"), complementStatus))).orderBy(idAsc);
		return DB.createQuery(cr).list();
	}

	public List<CharacterOneAction> findByActionAndAfterStatus(Word action, CharacterStatus afterStatus) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<CharacterOneAction> cr = cb.createQuery(entityClass);
		Root<CharacterOneAction> root = cr.from(entityClass);
		cr.select(root).where(cb.and(
			cb.equal(root.get("action"), action),
			cb.equal(root.get("afterStatus"), afterStatus)));
		return DB.createQuery(cr).list();
	}

	///////////////////////////////////////////////////////////////////////////
	private static CharacterOneActionRepository instance = new CharacterOneActionRepository();
	public static CharacterOneActionRepository get() {
		return instance;
	}
}
