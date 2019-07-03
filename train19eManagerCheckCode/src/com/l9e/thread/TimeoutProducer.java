package com.l9e.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.l9e.producerConsumer.distinct.DistinctProducer;
import com.l9e.transaction.service.RobotCodeService;
import com.l9e.transaction.vo.Picture;

//生产者：生产qunar、19e、tc等所有渠道的码
public class TimeoutProducer extends DistinctProducer<Picture> {
	
	private Logger logger=Logger.getLogger(this.getClass());
	
	@Override
	public String getObjectKeyId(Picture orderbill) {
		return orderbill.getPic_id();
	}

	@Override
	public List<Picture> getProducts(RobotCodeService robotService) {
		logger.info("get Timeout list start:"+robotService);
		//查询需要发送的类
		List<Picture> list_qunar = new ArrayList<Picture>();
		//系统设置--是否启用qunar打码
		String qunar_code = robotService.querySystemValue("qunar_play_code");
		/*List<Picture> list_tc = new ArrayList<Picture>();
		//系统设置--是否分离同城打码     是否进行同程打码 00：同程打码；11：19e打码
		String tc_play_code = robotService.querySystemValue("tc_play_code");
		if("00".equals(tc_play_code)){
			//获取集合                              
			list_tc = robotService.queryQunarPictureList("tongcheng");
			logger.info("end get tongcheng Timeout list:"+list_tc.size());
			
			if(null == list_tc || list_tc.size() == 0){
				logger.info("tongcheng no pic wait play!");
				return null;
			}
			for(Picture pic : list_tc){
				//批量更新码为处理状态
				robotService.updatePicturesStatus(pic.getPic_id());
				//打码数据放入队列----同程打码队列start
//				mqServic.sendMqMsg(TrainConsts.ROBOT_TC, pic.getPic_id());
			}
		}*/
		
		if("00".equals(qunar_code)){//是否进行去哪订单打码 00：开启去哪打码；11：停止去哪打码 22：去哪公共打码 33：打码兔打码
			try {
				//生产图片ID
				StackData data = new StackData();
				data.insertQueue(robotService);
				
				
				//集中打码(排除qunar所有渠道)
				/*if("11".equals(tc_play_code)){//我们打码
					//获取集合
					List<Picture> list_pic = robotService.queryQunarPictureList("tongcheng");
					Map<String,String> map = new HashMap<String,String>();
					map.put("old_channel", "tongcheng");
					map.put("new_channel", "19e");
					for(Picture pic : list_pic){
						//批量更新待打码图片渠道为19e
						map.put("pic_id", pic.getPic_id());
						robotService.updatePicturesChannel(map);
					}
					//logger.info("切换mq打码");
				}else if("33".equals(tc_play_code)){//33打码兔打码
					//获取集合
					List<Picture> list_pic = robotService.queryQunarPictureList("tongcheng");
					Map<String,String> map = new HashMap<String,String>();
					map.put("old_channel", "tongcheng");
					map.put("new_channel", "damatu");
					for(Picture pic : list_pic){
						//批量更新待打码图片渠道为damatu
						map.put("pic_id", pic.getPic_id());
						robotService.updatePicturesChannel(map);
					}
				}else{
					//分离生产同城pic pool
					StackTcData dataTc = new StackTcData();
					dataTc.insertQueue(robotService);
				}*/
				
				
				if("11".equals(qunar_code)){//停止qunar打码
					//获取集合
					List<Picture> list_pic = robotService.queryQunarPictureList("qunar");
					Map<String,String> map = new HashMap<String,String>();
					map.put("old_channel", "qunar");
					map.put("new_channel", "19e");
					for(Picture pic : list_pic){
						//批量更新待打码图片渠道为19e
						map.put("pic_id", pic.getPic_id());
						robotService.updatePicturesChannel(map);
					}
					return null;
				}else if("33".equals(qunar_code)){//33打码兔打码
					//获取集合
					List<Picture> list_pic = robotService.queryQunarPictureList("qunar");
					Map<String,String> map = new HashMap<String,String>();
					map.put("old_channel", "qunar");
					map.put("new_channel", "damatu");
					for(Picture pic : list_pic){
						//批量更新待打码图片渠道为damatu
						map.put("pic_id", pic.getPic_id());
						robotService.updatePicturesChannel(map);
					}
					return null;
				}
				//获取集合                              
				list_qunar = robotService.queryQunarPictureList("qunar");
				logger.info("end get Timeout list:"+list_qunar.size());
				
				if(null == list_qunar || list_qunar.size() == 0){
					logger.info("qunar no pic wait play!");
					return null;
				}
				for(Picture pic : list_qunar){
					pic.setQunar_code(qunar_code);
					//批量更新码为处理状态
					robotService.updatePicturesStatus(pic.getPic_id());
				}
			} catch (Exception e) {
				logger.error("getProducts Repate exception"+e);
			}
		}
		
//		list_qunar.addAll(list_tc);
//		list_tc.addAll(list_qunar);
		return list_qunar;
	}

}
