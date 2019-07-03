package com.soservice.mina.task;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.soservice.mina.session.BasicSessionManager;
import com.soservice.mina.session.SessionManager;

/**
 * session监控task
 * 
 * @author zhangyou
 * 
 */
public class SessionMonitorTask extends Thread {
	/**
	 * 日志
	 */
	private static Logger logger = Logger.getLogger(SessionMonitorTask.class);
	
	/**
	 * 会话连接管理
	 */
	private SessionManager sessionManager;

	public SessionMonitorTask(SessionManager sessionManager) {
		super();
		this.sessionManager = sessionManager;
	}

	/**
	 * 入口
	 */
	public void run() {
		while (true) {
			try {
				Thread.sleep(10000);
				// 打印监控信息
				ConcurrentMap<String, IoSession> map = sessionManager
						.getSessionMap();
				logger.info("监控信息:本次app客户端连接数" + map.entrySet().size());
//				logger.info("列表如下");
//				for (Entry entry : map.entrySet()) {
//					logger.info("客户端地址" + entry.getKey());
//				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
