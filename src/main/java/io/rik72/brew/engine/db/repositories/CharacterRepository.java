package io.rik72.brew.engine.db.repositories;

import java.util.ArrayList;
import java.util.List;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.CharacterStatus;
import io.rik72.brew.engine.db.entities.Location;
import io.rik72.brew.engine.story.Story;
import io.rik72.mammoth.db.DB;
import io.rik72.mammoth.repositories.AbstractRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class CharacterRepository extends AbstractRepository<Character> {

	private short mainCharacterId;

	private CharacterRepository() {
		super(Character.class);
	}

	public void setMainCharacterId(short mainCharacterId) {
		this.mainCharacterId = mainCharacterId;
	}

	public Character getMainCharacter() {
		return getById(mainCharacterId);
	}

	public Character getByName(String value) {
		return getByField("name", value);
	}

	public Character getByStatusCanonical(String value) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<Character> cr = cb.createQuery(entityClass);
		Root<Character> root = cr.from(entityClass);
		Join<Character, CharacterStatus> csj = root.join("status", JoinType.INNER);
		cr.select(root).where(cb.equal(csj.get("canonical"), value));
		List<Character> list = DB.createQuery(cr).list();
		return list.size() >= 1 ? list.get(0) : null;
	}

	public List<Character> findByLocation(Location location/* , Boolean visible, Boolean takeable*/) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<Character> cr = cb.createQuery(entityClass);
		Root<Character> root = cr.from(entityClass);

		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.equal(root.get("location"), location));
		predicates.add(cb.notEqual(root.get("id"), Story.get().getMainCharacter().getId()));
		// if (visible != null)
		// 	predicates.add(cb.equal(root.get("visible"), visible));
		// if (takeable != null)
		// 	predicates.add(cb.equal(root.get("takeable"), takeable));

		cr.select(root).where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		return DB.createQuery(cr).list();
	}

	///////////////////////////////////////////////////////////////////////////
	private static CharacterRepository instance = new CharacterRepository();
	public static CharacterRepository get() {
		return instance;
	}
}
