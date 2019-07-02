package com.train.robot.exception;

public class SuccessException extends Exception {
	private static final long serialVersionUID = -7243778425201734115L;

	public SuccessException() {
		super();
	}

	public SuccessException(String message) {
		super(message);
	}

	public SuccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public SuccessException(Throwable cause) {
		super(cause);
	}

	protected SuccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
