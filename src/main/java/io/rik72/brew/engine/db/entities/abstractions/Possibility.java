package io.rik72.brew.engine.db.entities.abstractions;

import io.rik72.brew.engine.db.entities.Word;

public interface Possibility {
	public Word getAction();
	public void setAction(String action);
	public boolean isPossible();
	public void setPossible(boolean possible);
	public boolean isImportant();
	public void setImportant(boolean important);
	public String getFeedback();
	public void setFeedback(String feedback);
}
