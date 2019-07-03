package com.l9e.transaction.exception;

/**
 * 机器人业务异常
 * @author licheng
 *
 */
public class OrderException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6558379420333896425L;

	public OrderException() {
		super();
	}

	public OrderException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public OrderException(String arg0) {
		super(arg0);
	}

	public OrderException(Throwable arg0) {
		super(arg0);
	}

}
