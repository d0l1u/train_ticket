package com.soservice.mina.session;

import java.util.HashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.mina.core.session.IoSession;

/**
 * 连接会话管理接口
 * @author zhangyou
 *
 */
public interface SessionManager {
	/**
	 * 取得下个连接会话
	 * @return
	 */
	public IoSession getNextIoSession() throws Exception;
	/**
	 * 放入连接会话
	 * @param cilent
	 * @param ioSession
	 * @return
	 */
	public void putIoSession(String cilent,IoSession ioSession);
	/**
	 * 销毁连接会话
	 * @param cilent
	 * @param ioSession
	 * @return
	 */
	public void destroyIoSession(String cilent);
	/**
	 * 返回结果集合
	 * @return
	 */
	public HashMap<String, String> getResultMap();
	/**
	 * 写入信息
	 * 
	 * @param messegae
	 * @return
	 * @throws Exception
	 */
	public String writeMesseage(String id, String messeage) throws Exception;
    /**
     * 获取会话连接集合
     * @return
     */
	public ConcurrentMap<String, IoSession> getSessionMap(); 

}
