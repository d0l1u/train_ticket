package com.soservice.mina.factory;

import com.soservice.mina.session.BasicSessionManager;
import com.soservice.mina.session.SessionManager;

/**
 * 连接会话管理工厂
 * 
 * @author zhangyou
 * 
 */
public class SessionManagerFactory {
	/**
	 * 连接会话管理
	 */
	private static final SessionManager sessionManager = new BasicSessionManager();

	/**
	 * 获取连接会话管理
	 */
	public static SessionManager getSessionManager() {
		return sessionManager;
	}

}
