package io.rik72.mammoth.exceptions;

import io.rik72.brew.engine.utils.TextUtils;

public class EntityNotFoundException extends EntityException {
	
	public EntityNotFoundException(String entityLabel, String entity) {
		this(entityLabel, entity, null, null);
	}
	
	public EntityNotFoundException(String entityLabel, String entity, String forEntityLabel, String forEntity) {
		super(entityLabel + " " + TextUtils.quote(entity) + " not found" +
			(forEntityLabel != null && forEntityLabel.length() > 0 && forEntity != null && forEntity.length() > 0 ?
				" for " + forEntityLabel + " " + TextUtils.quote(forEntity) : ""));
	}
}
