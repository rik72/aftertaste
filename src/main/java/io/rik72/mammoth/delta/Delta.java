package io.rik72.mammoth.delta;

import io.rik72.brew.engine.db.entities.abstractions.Complement;

public abstract class Delta {

	protected String name;

	public Delta() {}

	protected  <T extends Complement> Delta(T entity) {
		this.name = entity.getName();
	}	

	public String getName() {
		return name;
	}
}
