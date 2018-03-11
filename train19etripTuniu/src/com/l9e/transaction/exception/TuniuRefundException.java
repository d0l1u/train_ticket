
package com.l9e.transaction.exception;

/**
 * 途牛退票业务异常
 * @author licheng
 *
 */
public class TuniuRefundException extends TuniuException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2180191774877770627L;

	public TuniuRefundException() {
		super();
	}

	public TuniuRefundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public TuniuRefundException(String arg0) {
		super(arg0);
	}

	public TuniuRefundException(Throwable arg0) {
		super(arg0);
	}

}
