package com.l9e.transaction.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.common.Consts;
import com.l9e.transaction.service.ElongNoticeService;
import com.l9e.transaction.service.ElongOrderService;
import com.l9e.transaction.service.NoticeService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.RefundService;
import com.l9e.transaction.vo.DBNoticeVo;
import com.l9e.transaction.vo.DBOrderInfo;
import com.l9e.util.HttpUtil;
import com.l9e.util.elong.ElongUrlFormatUtil;
/**
 * 请求出票系统 出票 退票
 * @author liuyi
 *
 */
@Component("sendJob")
public class SendJob {
	
	private static final Logger logger = Logger.getLogger(SendJob.class);
	@Resource
	private ElongOrderService elongOrderService;
	@Resource
	private ElongNoticeService elongNoticeService;
	
	@Resource
	private NoticeService noticeService;
	
	@Resource
	private OrderService orderService;
	
	@Resource
	private RefundService refundService;
	/**
	 * 发货
	 */
	public void send(){
		List<Map<String,Object>> list=elongNoticeService.querySysNoticeList();
		
		List<Map<String,Object>> sendList=new ArrayList<Map<String,Object>>();
		if(list.size()>0){
			for(Map<String,Object> map:list){
				int count=	elongNoticeService.updateStartNoticeOutSys(map);
				if(count>0){
					sendList.add(map);
					logger.info("艺龙发货通知"+map.get("order_id")+"开始");
				}else{
					logger.info("艺龙发货通知"+map.get("order_id")+"锁定");
					//logger.info("艺龙发货通知"+map.get("order_id")+"开始");
					
				}
			/*	String order_id=map.get("order_id").toString();
				int num=Integer.parseInt(map.get("cp_notify_num")+"");
				Map<String,Object> orderInfo=elongOrderService.queryOrderInfo(order_id);
				List<Map<String, Object>> cpInfoList=elongOrderService.querySendOrderCpsInfo(order_id);
				elongOrderService.addNotifyCpSys(orderInfo, cpInfoList, order_id,num);*/
			}
		}
		
		if(sendList.size()>0){
			for(Map<String,Object> map:sendList){
				long start=System.currentTimeMillis();
				String order_id=map.get("order_id").toString();
				int num=Integer.parseInt(map.get("cp_notify_num")+"");
				Map<String,Object> orderInfo=elongOrderService.queryOrderInfo(order_id);
				List<Map<String, Object>> cpInfoList=elongOrderService.querySendOrderCpsInfo(order_id);
				elongOrderService.addNotifyCpSys(orderInfo, cpInfoList, order_id,num);
				logger.info("艺龙发货通知"+map.get("order_id")+"通知耗时"+(System.currentTimeMillis()-start)+"ms");
			}
		}
	}
	
	/**
	 * 某 渠道的发获通知
	 * */
	public void sendTc(){
		Map<String,String> param=new HashMap<String,String>();
		param.put("row_num", "20");
		//param.put("channel", Consts.CHANNEL_TONGCHENG);
		//数据库查询出的结果
		List<DBNoticeVo> list=noticeService.selectWaitNoticeList(param);
		
		List<DBNoticeVo> sendList=new ArrayList<DBNoticeVo>();
		
		
		if(list.size()>0){
			for(DBNoticeVo vo:list){
				int count=noticeService.updateStartWaitNoticeList(vo);
				if(count>0){
					sendList.add(vo);
				}else{
					logger.info("通知出票系统出票,"+vo.getOrder_id()+"已锁定");
				}
			}
		}
		
		if(sendList.size()>0){
			for(DBNoticeVo vo:sendList){
				long start=System.currentTimeMillis();
				String order_id=vo.getOrder_id();
				DBOrderInfo orderInfo=orderService.queryOrderInfo(order_id);
				orderService.addNotifyCpSys(orderInfo,vo);
				logger.info("通知出票系统出票,"+vo.getOrder_id()+"耗时"+(System.currentTimeMillis()-start));
			}
		}
	}
	
	
	
	/**
	 * 某 渠道的发获通知 顺序通知
	 * */
	public void sendTcSX(){
		Map<String,String> param=new HashMap<String,String>();
		param.put("row_num", "20");
		//param.put("channel", Consts.CHANNEL_TONGCHENG);
		//数据库查询出的结果
		List<DBNoticeVo> list=noticeService.selectWaitNoticeListsx(param);
		
		List<DBNoticeVo> sendList=new ArrayList<DBNoticeVo>();
		
		
		if(list.size()>0){
			for(DBNoticeVo vo:list){
				int count=noticeService.updateStartWaitNoticeList(vo);
				if(count>0){
					sendList.add(vo);
				}else{
					logger.info("通知出票系统出票,顺序通知"+vo.getOrder_id()+"已锁定");
				}
			}
		}
		
		if(sendList.size()>0){
			for(DBNoticeVo vo:sendList){
				long start=System.currentTimeMillis();
				String order_id=vo.getOrder_id();
				DBOrderInfo orderInfo=orderService.queryOrderInfo(order_id);
				orderService.addNotifyCpSys(orderInfo,vo);
				logger.info("通知出票系统出票,顺序通知"+vo.getOrder_id()+"耗时"+(System.currentTimeMillis()-start));
			}
		}
	}
	
	
	/**
	 * 给cp_orderinfo_refund表添加退款数据
	 */
	public void sendRefund(){
		List<Map<String, String>> sendList = refundService.queryCanRefundStreamList();
		if(sendList.size()>0){
			for (Map<String, String> sendMap : sendList) {
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e1) {
//					e1.printStackTrace();
//				}
				Map<String, String> map = new HashMap<String, String>();
		        //通知退票系统成功则订单状态修改为02开始机器改签
		        map.put("order_id", sendMap.get("order_id"));
		        map.put("cp_id",  sendMap.get("cp_id"));
		        map.put("order_status", "03");//人工改签
		        map.put("old_refund_status", sendMap.get("refund_status"));//开始机器改签
		        map.put("refund_type", sendMap.get("refund_type"));
		        logger.info("退票更新退票中状态，参数 ： " + map);
			    int count= refundService.updateOrderRefundStatus(map);
			    logger.info("更新返回结果：" + count + ", order_id: " + sendMap.get("order_id"));
				if(count>0){
					logger.info("通知退票系统，开始~~~订单号："+sendMap.get("order_id")+"，车票号"+sendMap.get("cp_id")+"，类型"+sendMap.get("refund_type"));
					this.notifyRefundSys(sendMap);
				}else{
					logger.info("通知退票系统，锁定~~~订单号："+sendMap.get("order_id")+"，车票号"+sendMap.get("cp_id")+"，类型"+sendMap.get("refund_type"));
				}
			}
		}
	}

	//给退款接口传参
	private void notifyRefundSys(Map<String, String> sendMap){
//		try {
//			Thread.sleep(30000);
//		} catch (InterruptedException e1) {
//			e1.printStackTrace();
//		}
		String order_id = sendMap.get("order_id");
		String cp_id = sendMap.get("cp_id");
		String refund_type=sendMap.get("refund_type");
		String channel= sendMap.get("channel");
		String  notify_refund_back_url=Consts.NOTIFY_REFUND_BACK_URL;//退票系统结果回调接口
		String  notify_refund_interface_url=Consts.NOTIFY_REFUND_INTERFACE_URL;//通知退票系统 退票
		
		try{
			Map<String, String> param = new HashMap<String, String>();
			param.put("order_id", order_id);
			param.put("cp_id", cp_id);
			param.put("refund_type", refund_type);
			Map<String, String> paramMap = new HashMap<String, String>();
			Map<String, String> orderMap = new HashMap<String, String>();
			if("11".equals(sendMap.get("refund_type"))){
				orderMap=refundService.queryRefundCpOrderInfo(param);
				paramMap.put("refundtype","11");//线上退票
			}else{
				//改签退票
				orderMap=refundService.queryChangeRefundCpOrderInfo(param);
				System.out.println("orderMap"+orderMap);
				paramMap.put("refundtype","55");
			}
			Map<String, String> accountMap = refundService.queryAccountOrderInfo(param);
			/** alterfromtime\altertrainno\alterseattype\altertraveltime\refund12306seq */
			System.out.println("orderMap"+accountMap);
			paramMap.put("orderid", order_id);
			paramMap.put("cpid", cp_id);
			paramMap.put("refundseq", sendMap.get("refund_seq"));
			paramMap.put("trainno", orderMap.get("train_no"));//车票总价
			paramMap.put("fromstation", orderMap.get("from_city"));
			paramMap.put("arrivestation", orderMap.get("to_city"));
			paramMap.put("traveltime", orderMap.get("travel_time"));
			paramMap.put("fromtime", orderMap.get("from_time"));
			paramMap.put("buymoney", orderMap.get("buy_money"));
			paramMap.put("refundmoney", sendMap.get("refund_money"));
			paramMap.put("username", orderMap.get("user_name"));
			paramMap.put("tickettype", orderMap.get("ticket_type"));
			paramMap.put("idstype", orderMap.get("ids_type"));
			paramMap.put("userids", orderMap.get("user_ids"));
			paramMap.put("seattype", orderMap.get("seat_type"));
			paramMap.put("trainbox", orderMap.get("train_box"));
			paramMap.put("seatno", orderMap.get("seat_no"));
			paramMap.put("outticketbillno", orderMap.get("out_ticket_billno"));
			paramMap.put("outtickettime", orderMap.get("out_ticket_time"));
			paramMap.put("channel", channel);
			paramMap.put("backurl", notify_refund_back_url);// 回调地址
			paramMap.put("accountname",accountMap.get("acc_username"));
			paramMap.put("accountpwd", accountMap.get("acc_password"));
			paramMap.put("userremark", sendMap.get("user_remark"));
			paramMap.put("refundpercent", sendMap.get("refund_percent"));
			logger.info("paramMap:"+paramMap);
			
			String sendParams=ElongUrlFormatUtil.createGetUrl("", paramMap, "utf-8");
			String result=HttpUtil.sendByPost(notify_refund_interface_url, sendParams, "utf-8");
			logger.info("请求通知退票接口返回：" + result);
			
			if(result!=null&&!"".equals(result)){
				String[] results = result.trim().split("\\|");
				
				if("success".equalsIgnoreCase(results[0]) && results.length == 2
						&& order_id.equals(results[1])){//通知成功
					Map<String, String> map = new HashMap<String, String>();
			        //通知退票系统成功则订单状态修改为02开始机器改签
			        map.put("order_id", order_id);
			        map.put("cp_id", cp_id);
				    map.put("order_status", "02");//开始机器改签
				    map.put("refund_type", refund_type);
				    refundService.updateOrderRefundStatus(map);			        
					logger.info("通知退票系统成功，order_id=" + order_id + "，cp_id=" + cp_id);
				}else{//通知退票系统失败则订单状态修改为03 人工改签
					logger.info("通知退票系统失败，order_id=" + order_id + "，cp_id=" + cp_id);
					Map<String, String> map = new HashMap<String, String>(2);
					map.put("order_id", order_id);
			        map.put("cp_id", cp_id);
			        map.put("refund_type", refund_type);
				    map.put("order_status", "03");//人工改签
				    refundService.updateOrderRefundStatus(map);
				}
			}
		}catch (Exception e){//发生异常则更新超时重发
			e.printStackTrace();
			logger.info("通知退票系统失败，order_id=" + order_id + "，cp_id=" + cp_id);
			Map<String, String> map = new HashMap<String, String>(2);
			map.put("order_id", order_id);
	        map.put("cp_id", cp_id);
		    map.put("order_status", "03");//人工改签
		    map.put("refund_type", refund_type);
		    refundService.updateOrderRefundStatus(map);
			
		}
		logger.info("退款结束~~~订单号："+order_id+"，车票号"+cp_id);
	}
	
	

	
	
	
}
