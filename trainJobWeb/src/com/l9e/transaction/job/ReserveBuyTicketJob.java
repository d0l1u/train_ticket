package com.l9e.transaction.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.ReserveBuyTicketService;
import com.l9e.util.DateUtil;
import com.l9e.util.hcpjar.HcpTicketQuery;




/**
 * 预约订票
 * @author liuyi02
 */
@Component("reserveBuyTicketJob")
@Deprecated
public class ReserveBuyTicketJob {
	
	
	@Resource
	private ReserveBuyTicketService reserveBuyTicketService;
	
	
	private static final Logger logger = Logger.getLogger(ReserveBuyTicketJob.class);
	
	
	/**
	 * 定时器 1
	 * 激活预售期内订单到 预约订票通知表
	 * 
	 * 每天夜里执行
	 * @author liuyi02
	 */
	public void reserveFirst(){
		String beginTime=DateUtil.dateToString(DateUtil.dateAddDays( new Date(), 60), "yyyy-MM-dd");
		try {
			List<Map<String,String>> list=
				reserveBuyTicketService.selectReserveMap(beginTime);
			int num=list.size();
			if(num>0){
				reserveBuyTicketService.insertIntoNotify(list);
			}
			logger.info("预约订票-插入将日期:"+beginTime+"之前的订单信息插入到预约通知表,插入数据量:"+num+"条");
		} catch (Exception e) {
			logger.info("预约订票-激活预售期内订单到 预约订票通知表异常"+e);
			e.printStackTrace();
		}
		
		/**每天初始话车站编号信息*/
		boolean initBoolean=HcpTicketQuery.initStationNameEveryDay();
		logger.info("预约订票-初始化当天车站编号信息:"+(initBoolean?"成功":"失败"));
	}
	
	
	

	/**
	 *  定时器 2
	 * 7点以后开始执行 查询12306余票情况
	 * @author liuyi02
	 */
	public void reserveTwo(){
		Date d = new Date(); 
		int hours = d.getHours(); 
		logger.info("hours:"+hours);
		/**7点以后激活 通知*/
		if(hours>7){
			List<String> orderIds=reserveBuyTicketService.selectListFromNotify();
			for(String orderId:orderIds){
				/**通知表状态 更新为已请求状态*/
				reserveBuyTicketService.updateNotifyStatus(orderId);
			}
			for(String orderId:orderIds){
				queryTicketNumAndUpdateOrderInfo(orderId);
			}
		}
	}
	
	/**
	 * 查询预约票 票数 并更新订单状态 或者更新通知表结构
	 * 
	 * */
	private void queryTicketNumAndUpdateOrderInfo(String orderId) {
		try {
			Map<String,Object> map=reserveBuyTicketService.queryOrderInfo(orderId);
			String fromStationName=map.get("from_city")+"";
			String toStationName=map.get("to_city")+"";
			String travel_time=map.get("travel_time")+"";
			String trainDate=travel_time.substring(0, travel_time.indexOf(" "));
			logger.info("travel_time转化成发出时间trainDate:"+trainDate);
			String trainNo=map.get("train_no")+"";
			String note=HcpTicketQuery.findNote(fromStationName,toStationName,trainDate,trainNo);
			//String note="17点30分起售";
			//String note="18点起售";
			
			logger.info(orderId+"预售信息:"+note);
			if("".equals(note)||note==null){
				/**订单触发为预订开始*/
				logger.info("预约订票-未查询到放票时间,订单触发为预订开始"+orderId);
				reserveBuyTicketService.updateOrderStatusBegin(orderId);
			}else if(note.contains("起售")){
				try {
					int allMinutes=0;
					String begin_time="";
					if(note.contains("起售")){
						if(note.contains("分")){
							begin_time=note.substring(0,note.indexOf("点"))+":"+note.substring(note.indexOf("点")+1,note.indexOf("分"))+":00";
							allMinutes=	(Integer.parseInt(note.substring(0,note.indexOf("点"))))*60+Integer.parseInt(note.substring(note.indexOf("点")+1,note.indexOf("分")));
						}else{
							begin_time=note.substring(0,note.indexOf("点"))+":00:00";
							allMinutes=	(Integer.parseInt(note.substring(0,note.indexOf("点"))))*60;
						}
					}
					if(begin_time.length()>0&&allMinutes!=0){
						Map<String,String> param=new HashMap<String,String>();
						param.put("order_id", orderId);
						param.put("begin_time", allMinutes+"");
						logger.info("预约订票-查询到放票时间成功,订单号"+orderId+",begin_time:"+begin_time+",allMinutes:"+allMinutes);
						reserveBuyTicketService.updateNotifyInfo(param);
					}else{
						logger.info("预约订票-查询到放票时间,时间解析不出来,订单触发为预订开始:"+orderId+"12306备注信息:"+note);
						reserveBuyTicketService.updateOrderStatusBegin(orderId);
					}
					
				} catch (Exception e) {
					/**订单触发为预订开始*/
					logger.info("预约订票-查询到放票时间,时间解析异常,订单触发为预订开始:"+orderId+"异常信息:"+e);
					reserveBuyTicketService.updateOrderStatusBegin(orderId);
					e.printStackTrace();
				}
			}else{
				/**订单触发为预订开始*/
				logger.info("预约订票-查询到解析不了的放票时间,订单触发为预订开始:"+orderId+"12306备注信息:"+note);
				reserveBuyTicketService.updateOrderStatusBegin(orderId);
			}
		} catch (Exception e) {
			/**订单触发为预订开始*/
			logger.info("预约订票-查询放票时间异常,订单触发为预订开始"+e);
			reserveBuyTicketService.updateOrderStatusBegin(orderId);
			e.printStackTrace();
		}
		
		
	}
	/**
	 *  定时器 3
	 *  开始纷发
	 * @author liuyi02
	 */
	public void reserveThree(){
		Date d = new Date(); 
		int hours = d.getHours(); 
		int minutes=d.getMinutes();
		int allMinutes=hours*60+minutes-5;//可预订时间5分钟后触发请求 当前时间减去5分钟
		/**7点以后激活 通知*/
		if(hours>7&&hours<23){
			List<Map<String,Object>> notifyInfos=reserveBuyTicketService.selectReserveNotifyList(allMinutes+"");
			for(Map<String,Object> notifyInfo:notifyInfos){
				int	beginMinutes=Integer.parseInt(notifyInfo.get("begin_time")+"");
				if(allMinutes>beginMinutes){
					/**订单触发为预订开始*/
					logger.info("预约订票-订单"+notifyInfo.get("order_id")+"匹配到时间,触发预订开始beginMinutes:"+beginMinutes+",nowMinutes"+allMinutes);
					reserveBuyTicketService.updateOrderStatusBegin(notifyInfo.get("order_id")+"");
					reserveBuyTicketService.updateNotifyOutTicketStaus(notifyInfo.get("order_id")+"");
				}
			}
		}
	}



	public static void main(String[] args) {
		/*String beginTime=DateUtil.dateToString(DateUtil.dateAddDays( new Date(), 60), "yyyy-MM-dd");
		System.out.println(beginTime);
		Date date=new Date();
		Date beginDate=DateUtil.stringToDate(DateUtil.dateToString(new Date(), "yyyy-MM-dd")+" "+"6:00:00","yyyy-MM-dd HH:mm:ss");
		
		System.out.println(DateUtil.dateToString(beginDate,"yyyy-MM-dd HH:mm:ss"));
		if(date.getTime()>beginDate.getTime()){
			System.out.println("当前时间大于");
		}else{
			System.out.println("当前时间小于");
		}*/
		
		String note="18点起售";
		//String note="17点30分起售";
		String begin_time="";
		if(note.contains("起售")){
			if(note.contains("分")){
				begin_time=note.substring(0,note.indexOf("点"))+":"+note.substring(note.indexOf("点")+1,note.indexOf("分"))+":00";
				System.out.println(begin_time);
			}else{
				begin_time=note.substring(0,note.indexOf("点"))+":00:00";
				System.out.println(begin_time);
			}
		}
		
	}
	
	
	
	
}
