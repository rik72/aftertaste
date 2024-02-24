package io.rik72.brew.engine.db.repositories;

import java.util.List;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.CharacterXTextGroup;
import io.rik72.brew.engine.db.entities.TextGroup;
import io.rik72.mammoth.db.DB;
import io.rik72.mammoth.repositories.AbstractRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class CharacterXTextGroupRepository extends AbstractRepository<CharacterXTextGroup> {

	private CharacterXTextGroupRepository() {
		super(CharacterXTextGroup.class);
	}

	public boolean existsByCharacterAndTextGroup(Character character, TextGroup textGroup) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<CharacterXTextGroup> cr = cb.createQuery(entityClass);
		Root<CharacterXTextGroup> root = cr.from(entityClass);
		cr.select(root).where(cb.and(
			cb.equal(root.get("character"), character),
			cb.equal(root.get("textGroup"), textGroup)));
		List<CharacterXTextGroup> results = DB.createQuery(cr).list();
		return !results.isEmpty();
	}

	public List<CharacterXTextGroup> findByCharacter(Character character) {
		return findByField("character", character, "id", OrderBy.ASC);
	}

	///////////////////////////////////////////////////////////////////////////
	private static CharacterXTextGroupRepository instance = new CharacterXTextGroupRepository();
	public static CharacterXTextGroupRepository get() {
		return instance;
	}
}
