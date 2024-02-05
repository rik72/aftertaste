package io.rik72.brew.engine.db.repositories;

import java.util.List;

import io.rik72.brew.engine.db.entities.CharacterStatus;
import io.rik72.brew.engine.db.entities.CharacterThingOnCharacter;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.entities.abstractions.Status;
import io.rik72.mammoth.db.DB;
import io.rik72.mammoth.repositories.AbstractRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

public class CharacterThingOnCharacterRepository extends AbstractRepository<CharacterThingOnCharacter> {

	private CharacterThingOnCharacterRepository() {
		super(CharacterThingOnCharacter.class);
	}

	public List<CharacterThingOnCharacter> findByParts(Word action, Status complementStatus,
	                                                 Word preposition, Status supplementStatus) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<CharacterThingOnCharacter> cr = cb.createQuery(entityClass);
		Root<CharacterThingOnCharacter> root = cr.from(entityClass);
		Order idAsc = cb.asc(root.get("id"));
		cr.select(root).where(cb.and(
			cb.equal(root.get("action"), action),
			cb.equal(root.get("complementStatus"), complementStatus),
			cb.equal(root.get("preposition"), preposition),
			cb.equal(root.get("supplementStatus"), supplementStatus))).orderBy(idAsc);
		return DB.createQuery(cr).list();
	}

	public List<CharacterThingOnCharacter> findByActionAndAfterStatus(Word action, CharacterStatus afterStatus) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<CharacterThingOnCharacter> cr = cb.createQuery(entityClass);
		Root<CharacterThingOnCharacter> root = cr.from(entityClass);
		cr.select(root).where(cb.and(
			cb.equal(root.get("action"), action),
			cb.equal(root.get("afterStatus"), afterStatus)));
		return DB.createQuery(cr).list();
	}

	///////////////////////////////////////////////////////////////////////////
	private static CharacterThingOnCharacterRepository instance = new CharacterThingOnCharacterRepository();
	public static CharacterThingOnCharacterRepository get() {
		return instance;
	}
}
