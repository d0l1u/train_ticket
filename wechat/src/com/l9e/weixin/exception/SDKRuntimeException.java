package com.l9e.weixin.exception;

import org.apache.log4j.Logger;

import com.l9e.weixin.pojo.WxPayHelper;

public class SDKRuntimeException extends Exception {
	private static final Logger logger = Logger.getLogger(SDKRuntimeException.class);
	private static final long serialVersionUID = 1L;

	public SDKRuntimeException(String str) {
	        super(str);
	        logger.error("发生错误：" + str);
	 }
}
