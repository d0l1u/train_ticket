package com.l9e.transaction.exception;

/**
 * 机器人业务异常
 * @author licheng
 *
 */
public class AccountException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6558379420333896425L;

	public AccountException() {
		super();
	}

	public AccountException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public AccountException(String arg0) {
		super(arg0);
	}

	public AccountException(Throwable arg0) {
		super(arg0);
	}

}
