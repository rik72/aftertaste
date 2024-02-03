package io.rik72.brew.engine.db.repositories;

import java.util.List;

import io.rik72.brew.engine.db.entities.CharacterOnCharacter;
import io.rik72.brew.engine.db.entities.CharacterStatus;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.db.entities.abstractions.Status;
import io.rik72.mammoth.db.DB;
import io.rik72.mammoth.repositories.AbstractRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

public class CharacterOnCharacterRepository extends AbstractRepository<CharacterOnCharacter> {

	private CharacterOnCharacterRepository() {
		super(CharacterOnCharacter.class);
	}

	public List<CharacterOnCharacter> findByParts(Word action, Status complementStatus) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<CharacterOnCharacter> cr = cb.createQuery(entityClass);
		Root<CharacterOnCharacter> root = cr.from(entityClass);
		Order idAsc = cb.asc(root.get("id"));
		cr.select(root).where(cb.and(
			cb.equal(root.get("action"), action),
			cb.equal(root.get("complementStatus"), complementStatus))).orderBy(idAsc);
		return DB.createQuery(cr).list();
	}

	public List<CharacterOnCharacter> findByActionAndAfterStatus(Word action, CharacterStatus afterStatus) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<CharacterOnCharacter> cr = cb.createQuery(entityClass);
		Root<CharacterOnCharacter> root = cr.from(entityClass);
		cr.select(root).where(cb.and(
			cb.equal(root.get("action"), action),
			cb.equal(root.get("afterStatus"), afterStatus)));
		return DB.createQuery(cr).list();
	}

	///////////////////////////////////////////////////////////////////////////
	private static CharacterOnCharacterRepository instance = new CharacterOnCharacterRepository();
	public static CharacterOnCharacterRepository get() {
		return instance;
	}
}
