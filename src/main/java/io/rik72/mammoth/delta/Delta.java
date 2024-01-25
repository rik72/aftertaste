package io.rik72.mammoth.delta;

import io.rik72.mammoth.entities.AbstractEntity;

public abstract class Delta {

	protected short id;

	public Delta() {}

	protected  <T extends AbstractEntity> Delta(T entity) {
		this.id = entity.getId();
	}	

	public short getId() {
		return id;
	}
}
