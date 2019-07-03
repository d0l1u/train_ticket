package com.l9e.thread;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.l9e.transaction.service.RobotCodeService;
import com.l9e.util.DateUtil;

public class StackData{
	private static final Logger logger = 
		Logger.getLogger(StackData.class);
	
	private static final int QUEUE_SIZE = 30;
	
	private static final LinkedBlockingQueue queue = new LinkedBlockingQueue(QUEUE_SIZE);
	
	private static class StackDataHolder{
		private static final StackData INSTANCE = new StackData();
	}
	
	public static final StackData getInstance(){
		return StackDataHolder.INSTANCE;
	}

	public String getPicId(){
		if(null == queue.peek()){
			return null;
		}else{
			return (String) queue.poll();
		}
	}
	
	public int getQueueSize(){
		return queue.size();
	}
	
	public void insertQueue (RobotCodeService robotService){
		logger.info("==============start insert pic pool============");
		Date date = new Date();
		String datePre = DateUtil.dateToString(date, "yyyyMMdd");		
		Date begin = DateUtil.stringToDate(datePre + "063000", "yyyyMMddHHmmss");//7:00
		Date end = DateUtil.stringToDate(datePre + "233000", "yyyyMMddHHmmss");//23:00
		try {
			if(date.before(begin) || date.after(end)){
				logger.info("7-23点方可启用打码器！");
				//睡眠一分钟
				Thread.sleep(60*1000);
			}
			
			if(queue.size()>=20){
				Thread.sleep(1000);
			}else{
				String qunar_code = robotService.querySystemValue("qunar_play_code");
				String tc_play_code = robotService.querySystemValue("tc_play_code");
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("qunar_channel", qunar_code);
				map.put("tc_channel", tc_play_code);
				map.put("limit", 20);
				List<String> list = robotService.queryPictureIdList(map);
				
				for(String pic_id:list){
//					logger.info("list_pic=====>>>>:"+pic_id);
					if(queue.size()<QUEUE_SIZE){
						int up_result = robotService.updatePicturesStatusHasReturn(pic_id);
						if(up_result==1){
							logger.info("==========save to base pool id："+pic_id);
							queue.offer(pic_id);
						}
					}else{
						break;
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("==============end insert pic pool============");
	}
}
