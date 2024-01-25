package io.rik72.mammoth.exceptions;

public abstract class EntityException extends IllegalStateException {
	
	protected EntityException(String msg) {
		super("\n\n    " + msg + "\n");
	}
}
