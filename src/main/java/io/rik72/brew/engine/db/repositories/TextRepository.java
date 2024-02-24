package io.rik72.brew.engine.db.repositories;

import java.util.List;

import io.rik72.brew.engine.db.entities.Text;
import io.rik72.brew.engine.db.entities.TextGroup;
import io.rik72.brew.engine.db.entities.Thing;
import io.rik72.mammoth.db.DB;
import io.rik72.mammoth.repositories.AbstractRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

public class TextRepository extends AbstractRepository<Text> {

	private TextRepository() {
		super(Text.class);
	}

	public List<Text> findByGroup(TextGroup group) {
		CriteriaBuilder cb = DB.getCriteriaBuilder();
		CriteriaQuery<Text> cr = cb.createQuery(entityClass);
		Root<Text> root = cr.from(entityClass);
		Order positionAsc = cb.asc(root.get("position"));
		cr.select(root).where(cb.equal(root.get("group"), group)).orderBy(positionAsc);
		return DB.createQuery(cr).list();
	}

	@Override
	public void deleteAll() {
		List<Thing> allT = ThingRepository.get().findAll();
		for (Thing t : allT) {
			t.unsetLocation();
			DB.persist(t);
		}
		super.deleteAll();
	}
	
	///////////////////////////////////////////////////////////////////////////
	private static TextRepository instance = new TextRepository();
	public static TextRepository get() {
		return instance;
	}
}
