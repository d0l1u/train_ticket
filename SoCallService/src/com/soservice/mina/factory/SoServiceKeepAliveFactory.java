package com.soservice.mina.factory;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

/**
 * 心跳连接工厂
 * 
 * @author zhangyou
 * 
 */
public class SoServiceKeepAliveFactory implements KeepAliveMessageFactory {
	/**
	 * 心跳检测请求内容
	 */
	private static final String HEARTBEATREQUEST = "0x11";
	/**
	 * 心跳检测响应内容
	 */
	private static final String HEARTBEATRESPONSE = "0x12";

	/**
	 * 返回预设请求包
	 */
	public Object getRequest(IoSession iosession) {
		/** 返回预设语句 */
		return HEARTBEATREQUEST;
	}

	/**
	 * 返回预设返回包
	 */
	public Object getResponse(IoSession iosession, Object obj) {
		/** 返回预设语句 */
		return HEARTBEATRESPONSE;
	}

	/**
	 * 检查请求内容
	 */
	public boolean isRequest(IoSession iosession, Object message) {
		/**
		 * 如果是心跳检测请求包
		 */
		if (message.equals(HEARTBEATREQUEST)) {
			return true;
		}
		return false;
	}

	/**
	 * 检查返回内容
	 */
	public boolean isResponse(IoSession iosession, Object message) {
		/**
		 * 如果心跳检测返回包
		 */
		if (message.equals(HEARTBEATRESPONSE)) {
			return true;
		}
		return false;
	}

}
