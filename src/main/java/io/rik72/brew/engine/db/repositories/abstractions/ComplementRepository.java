package io.rik72.brew.engine.db.repositories.abstractions;

import io.rik72.brew.engine.db.entities.abstractions.Complement;
import io.rik72.mammoth.repositories.AbstractRepository;

public abstract class ComplementRepository <T extends Complement> extends AbstractRepository<T> {

	protected ComplementRepository(Class<T> entityClass) {
		super(entityClass);
	}

	public T getByName(String value) {
		return (T) getByField("name", Complement.name(value));
	}
}
