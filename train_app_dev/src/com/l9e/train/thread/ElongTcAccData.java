package com.l9e.train.thread;

import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l9e.train.service.impl.TrainServiceImpl;
import com.l9e.train.util.DateUtil;

public class ElongTcAccData implements Runnable{

	private Logger logger = LoggerFactory.getLogger(ElongTcAccData.class);	
	
	//翼龙账号
	private static final LinkedBlockingQueue eloQueue = new LinkedBlockingQueue(30);
	//同程账号
	private static final LinkedBlockingQueue tcQueue = new LinkedBlockingQueue(30);
	
	private static class StackDataHolder{
		private static final ElongTcAccData INSTANCE = new ElongTcAccData();
	}
	
	public static final ElongTcAccData getInstance(){
		return StackDataHolder.INSTANCE;
	}

	public int getEloAccId(){
		if(null == eloQueue.peek()){
			return 0;
		}else{
			return (Integer)eloQueue.poll();
		}
	}
	
	public int getTcAccId(){
		if(null == tcQueue.peek()){
			return 0;
		}else{
			return (Integer)tcQueue.poll();
		}
	}
	
	public int getEloQueueSize(){
		return eloQueue.size();
	}
	
	public int getTcQueueSize(){
		return tcQueue.size();
	}
	
	
	public void startPoolLoad(TrainServiceImpl dao){
		try {
			Date date = new Date();
			String datePre = DateUtil.dateToString(date, "yyyyMMdd");		
			Date begin = DateUtil.stringToDate(datePre + "060000", "yyyyMMddHHmmss");//6:00
			Date end = DateUtil.stringToDate(datePre + "230000", "yyyyMMddHHmmss");//23:00
			if(date.before(begin) || date.after(end)){
				logger.info("7-23点方可启用打码器！");
				Thread.sleep(60*1000);//睡眠一分钟
			}
			//elong线程池加载
			logger.info("========start save elong acc pool==========");
			dao.loadAccPool(eloQueue,"elong");
			logger.info("========end save elong acc pool=======size:"+eloQueue.size());
			//tongcheng线程池加载
			logger.info("========start save tongcheng acc pool==========");
			dao.loadAccPool(tcQueue,"tongcheng");
			logger.info("========end save tongcheng acc pool======size:"+tcQueue.size());
		} catch (Exception e) {
			logger.error("thread pool is error!",e);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
