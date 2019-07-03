package com.soservice.mina.session;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.mina.core.session.IoSession;

/**
 * 连接会话管理
 * 
 * @author zhangyou
 * 
 */
public abstract class AbstractSessionManager implements SessionManager {
	/**
	 * sessionMap
	 */
	protected ConcurrentMap<String,IoSession> sessionMap=new ConcurrentHashMap<String,IoSession>();
	/**
	 * 返回结果
	 */
	protected HashMap<String,String>  resultMap=new HashMap<String,String> ();
	/**
	 * 销毁连接会话
	 * @param cilent
	 * @param ioSession
	 * @return
	 */
	public void destroyIoSession(String cilent) {
		sessionMap.remove(cilent);
	}

	/**
	 * 取得下个连接会话
	 * @return
	 * @throws Exception 
	 */
	public IoSession getNextIoSession() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 放入连接会话
	 * @param cilent
	 * @param ioSession
	 * @return
	 */
	public void putIoSession(String cilent, IoSession ioSession) {
		sessionMap.put(cilent, ioSession);
	}

	public HashMap<String, String> getResultMap() {
		return resultMap;
	}

	public ConcurrentMap<String, IoSession> getSessionMap() {
		return sessionMap;
	}

	
}
