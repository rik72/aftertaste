package io.rik72.brew.engine.db.repositories;

import java.util.List;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.CharacterStatus;
import io.rik72.mammoth.db.DB;
import io.rik72.mammoth.repositories.AbstractRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class CharacterStatusRepository extends AbstractRepository<CharacterStatus> {

	private CharacterStatusRepository() {
		super(CharacterStatus.class);
	}

	public CharacterStatus getByCharacterAndLabel(Character character, String label) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<CharacterStatus> cr = cb.createQuery(entityClass);
		Root<CharacterStatus> root = cr.from(entityClass);
		cr.select(root).where(cb.and(cb.equal(
				root.get("character"), character),
				cb.equal(root.get("label"), label)));
		List<CharacterStatus> list = DB.createQuery(cr).list();
		return list.size() >= 1 ? list.get(0) : null;
	}

	@Override
	public void deleteAll() {
		List<Character> allC = CharacterRepository.get().findAll();
		for (Character c : allC) {
			c.unsetStatus();
			DB.persist(c);
		}
		List<CharacterStatus> allCS = findAll();
		for (CharacterStatus s : allCS) {
			s.unsetCharacter();
			DB.persist(s);
		}
		super.deleteAll();
	}

	///////////////////////////////////////////////////////////////////////////
	private static CharacterStatusRepository instance = new CharacterStatusRepository();
	public static CharacterStatusRepository get() {
		return instance;
	}
}
