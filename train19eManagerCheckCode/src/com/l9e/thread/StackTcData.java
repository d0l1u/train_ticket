package com.l9e.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.l9e.transaction.service.RobotCodeService;
import com.l9e.util.DateUtil;

public class StackTcData{
	private static final Logger logger = 
		Logger.getLogger(StackTcData.class);
	
	private static final int QUEUE_SIZE = 30;
	
	private static final LinkedBlockingQueue queue = new LinkedBlockingQueue(QUEUE_SIZE);
	
	private static class StackDataHolder{
		private static final StackTcData INSTANCE = new StackTcData();
	}
	
	public static final StackTcData getInstance(){
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
		logger.info("==============start insert tongcheng pic pool============");
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
				List<String> listAll = null;
				List<String> list = null;
				//系统设置--是否分离同城打码  00：同程打自己码；11：我们+同程打码 22：同程给我们打码 33：打码兔打码 44：我们+打码兔打码
				String tc_play_code = robotService.querySystemValue("tc_play_code");
				String code_type_channel = robotService.querySystemValue("code_type_channel");
				
				if("22".equals(tc_play_code)){
					listAll = new ArrayList<String>();
					Map<String,Object> map_19e = new HashMap<String,Object>();
					map_19e.put("channel", "tongcheng");
					map_19e.put("limit", 10);
					list = robotService.queryChannelPictureIdList(map_19e);
					listAll.addAll(list);
					
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("channel", "19e");
					map.put("limit", 10);
					list = robotService.queryChannelPictureIdList(map);
					listAll.addAll(list);
				}else if("33".equals(code_type_channel) || "44".equals(code_type_channel)){//33：打码兔打码 44人工+打码兔打码
					listAll = new ArrayList<String>();
					Map<String,Object> map_damatu = new HashMap<String,Object>();
					map_damatu.put("channel", "damatu");
					map_damatu.put("limit", 20);
					listAll = robotService.queryChannelPictureIdList(map_damatu);
				}else{
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("channel", "tongcheng");
					map.put("limit", 20);
					listAll = robotService.queryChannelPictureIdList(map);
				}
				for(String pic_id:listAll){
//					logger.info("list_pic=====>>>>:"+pic_id);
					if(queue.size()<QUEUE_SIZE){
						int up_result = robotService.updatePicturesStatusHasReturn(pic_id);
						if(up_result==1){
							logger.info("==========save to tongcheng pool id："+pic_id);
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
		logger.info("==============end insert tongcheng pic pool============");
	}
	
	/*public static void main(String[] args) {
		List<String> listAll = null;
		List<String> list = null;
		
		listAll = new ArrayList<String>();
		list = new ArrayList<String>();
		
		list.add("tongcheng1");
		list.add("tongcheng2");
		list.add("tongcheng3");
		list.add("tongcheng4");
		listAll.addAll(list);
		
		list = new ArrayList<String>();
		list.add("19e1");
		list.add("19e2");
		list.add("19e3");
		list.add("19e4");
		listAll.addAll(list);
		
		
		for(String str:listAll){
			System.out.println(str);
		}
	}*/
}
