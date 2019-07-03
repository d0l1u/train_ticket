package com.l9e.transaction.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 账号服务线程池持久类
 * @author licheng
 *
 */
@Deprecated
public class AccountThreadPool {

	private static ExecutorService preparedSignPool;
	
	static {
		preparedSignPool = Executors.newFixedThreadPool(50);
	}
	
	private AccountThreadPool(){
	}
	
	/**
	 * 提交一个预登录任务
	 * @param task
	 */
	public static void submitPreparedSignTask(Runnable task) {
		if(!preparedSignPool.isShutdown() && !preparedSignPool.isTerminated()) {
			preparedSignPool.submit(task);
		}
	}
}
