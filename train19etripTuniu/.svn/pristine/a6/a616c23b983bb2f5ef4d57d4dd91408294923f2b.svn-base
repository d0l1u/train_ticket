package com.l9e.transaction.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TuniuThreadPool {

	private static ExecutorService cpNoticePool;
	private static ExecutorService tuniuCallbackPool;
	
	static {
		cpNoticePool = Executors.newFixedThreadPool(200);
		tuniuCallbackPool = Executors.newFixedThreadPool(300);
	}

	private TuniuThreadPool() {
	}
	
	/**
	 * 提交一个通知出票系统的工作任务
	 * @param request
	 */
	public static void cpNoticeThreadSubmit(SimpleRequest request){
		if(!cpNoticePool.isShutdown() && !cpNoticePool.isTerminated()) {
			cpNoticePool.submit(request);
		}
	}
	
	/**
	 * 提交一个途牛结果回调工作任务
	 * @param request
	 */
	public static void tuniuCallbackSubmit(SimpleRequest request) {
		if(!tuniuCallbackPool.isShutdown() && !tuniuCallbackPool.isTerminated()) {
			tuniuCallbackPool.submit(request);
		}
	}
}
