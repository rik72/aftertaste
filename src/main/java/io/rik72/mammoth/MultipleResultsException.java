package io.rik72.mammoth;

public class MultipleResultsException extends IllegalArgumentException {
	private String offendingValue;

	public MultipleResultsException(String msg, String offendingValue) {
		super(msg);
		this.offendingValue = offendingValue;
	}

	public String getOffendingValue() {
		return offendingValue;
	}
}
