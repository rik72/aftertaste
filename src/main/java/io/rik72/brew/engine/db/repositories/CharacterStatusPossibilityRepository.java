package io.rik72.brew.engine.db.repositories;

import java.util.List;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.CharacterStatus;
import io.rik72.brew.engine.db.entities.CharacterStatusPossibility;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.mammoth.db.DB;
import io.rik72.mammoth.repositories.AbstractRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;

public class CharacterStatusPossibilityRepository extends AbstractRepository<CharacterStatusPossibility> {

	private CharacterStatusPossibilityRepository() {
		super(CharacterStatusPossibility.class);
	}

	public List<CharacterStatusPossibility> findByCharacter(Character character) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<CharacterStatusPossibility> cr = cb.createQuery(entityClass);
		Root<CharacterStatusPossibility> root = cr.from(entityClass);
		Join<CharacterStatusPossibility, CharacterStatus> csj = root.join("status", JoinType.INNER);
		Join<CharacterStatus, Character> cj = csj.join("character", JoinType.INNER);
		cr.select(root).where(cb.and(
			cb.equal(csj.get("character"), character),
			cb.equal(root.get("status"), cj.get("status"))));
		return DB.createQuery(cr).list();
	}

	public CharacterStatusPossibility getByCharacterStatusAction(Character character, String statusLabel, String action) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<CharacterStatusPossibility> cr = cb.createQuery(entityClass);
		Root<CharacterStatusPossibility> root = cr.from(entityClass);
		Join<CharacterStatusPossibility, Word> wj = root.join("action", JoinType.INNER);
		Join<CharacterStatusPossibility, CharacterStatus> csj = root.join("status", JoinType.INNER);
		cr.select(root).where(cb.and(
			cb.equal(csj.get("character"), character),
			cb.equal(csj.get("label"), statusLabel),
			cb.equal(wj.get("text"), action)));
		List<CharacterStatusPossibility> list = DB.createQuery(cr).list();
		return list.size() >= 1 ? list.get(0) : null;
	}

	///////////////////////////////////////////////////////////////////////////
	private static CharacterStatusPossibilityRepository instance = new CharacterStatusPossibilityRepository();
	public static CharacterStatusPossibilityRepository get() {
		return instance;
	}
}
