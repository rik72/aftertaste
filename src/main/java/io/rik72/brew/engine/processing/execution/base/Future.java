package io.rik72.brew.engine.processing.execution.base;

public abstract class Future {

	public abstract void onSuccess();

	public void onFailure() {
		throw new UnsupportedOperationException("Unimplemented method 'onFailure'");
	}
}
