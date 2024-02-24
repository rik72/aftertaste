package io.rik72.brew.engine.db.repositories;

import java.util.List;

import io.rik72.brew.engine.db.entities.TextGroup;
import io.rik72.brew.engine.db.entities.Thing;
import io.rik72.mammoth.db.DB;
import io.rik72.mammoth.repositories.AbstractRepository;

public class TextGroupRepository extends AbstractRepository<TextGroup> {

	private TextGroupRepository() {
		super(TextGroup.class);
	}

	public TextGroup getByName(String value) {
		return (TextGroup) getByField("name", value);
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
	private static TextGroupRepository instance = new TextGroupRepository();
	public static TextGroupRepository get() {
		return instance;
	}
}
