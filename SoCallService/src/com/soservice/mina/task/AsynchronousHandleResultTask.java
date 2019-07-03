package com.soservice.mina.task;

import com.soservice.mina.session.SessionManager;

/**
 * 异步处理app返回内容task
 * 
 * @author zhangyou
 * 
 */
public class AsynchronousHandleResultTask extends Thread {
	/**
	 * 会话连接管理
	 */
	private SessionManager sessionManager;
	private String message;

	public AsynchronousHandleResultTask(SessionManager sessionManager,String message) {
		super();
		this.sessionManager = sessionManager;
		this.message=message;
	}
	/**
	 * 执行异步任务
	 */
	public void run() {
		String[]  messages=message.split("@@");
		sessionManager.getResultMap().put(messages[0], message);
	}

}
