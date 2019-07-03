package com.l9e.transaction.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.common.Consts;
import com.l9e.transaction.dao.CommonDao;
import com.l9e.transaction.dao.ElongOrderDao;
import com.l9e.transaction.dao.NoticeDao;
import com.l9e.transaction.dao.OrderDao;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.DBNoticeVo;
import com.l9e.transaction.vo.DBOrderInfo;
import com.l9e.transaction.vo.DBPassengerInfo;
import com.l9e.transaction.vo.DBRemedyNoticeVo;
import com.l9e.transaction.vo.DBStudentInfo;
import com.l9e.transaction.vo.ElongOrderInfoCp;
import com.l9e.transaction.vo.ElongOrderLogsVo;
import com.l9e.transaction.vo.SInfo;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.UrlFormatUtil;
import com.l9e.util.elong.ElongConsts;
import com.l9e.util.elong.ElongJsonUtil;
import com.l9e.util.elong.ElongMd5Util;
import com.l9e.util.elong.ElongUrlFormatUtil;
import com.l9e.util.elong.StrUtil;
import com.l9e.util.tongcheng.TongChengConsts;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

	private static final Logger logger = Logger.getLogger(OrderServiceImpl.class);
	@Resource
	private OrderDao orderDao;
	/*@Resource
	private ElongNoticeDao elongNoticeDao;*/
	@Resource
	private ElongOrderDao elongOrderDao;
	@Resource
	private NoticeDao noticeDao;
	
	@Resource
	private CommonDao commonDao;
	
	/**订单插入*/
	@Override
	public void addOrder(DBOrderInfo orderInfo) {
		List<DBPassengerInfo> passengers=orderInfo.getPassengers();
		List<DBStudentInfo> students=orderInfo.getStudents();
		orderDao.addOrderInfo(orderInfo);
		for(DBPassengerInfo p:passengers){
			orderDao.addPassengerInfo(p);
			/*加入315标记表*/
			
		}
		if(students!=null&&students.size()>0){
			for(DBStudentInfo s:students){
				orderDao.addStudentInfo(s);
			}
		}
		/**加入通知表*/
		String channel =orderInfo.getChannel();
		if(Consts.CHANNEL_ELONG.equals(channel)){
			
		}else if(Consts.CHANNEL_TONGCHENG.equals(channel)){
			String callBackUrl=orderInfo.getCallbackurl();
			/*if(callBackUrl!=null){
				//同城异步(插入到通知表 启动通知出票系统项)
				DBNoticeVo notice=new DBNoticeVo();
				notice.setOrder_id(orderInfo.getOrder_id());
				notice.setCp_notify_status(Consts.NOTICE_START);
				notice.setChannel(orderInfo.getChannel());
				notice.setBook_notify_url(callBackUrl);//预订成功异步回调地址
				noticeDao.insertNotice(notice);
			}*/
			ElongOrderLogsVo log=new ElongOrderLogsVo();
			log.setOpt_person("elong_app");
			log.setContent("成功接受同城订单["+orderInfo.getOrder_id()+"]"+(callBackUrl==null?"同步方式":"异步方式"));
			log.setOrder_id(orderInfo.getOrder_id());
			elongOrderDao.insertElongOrderLogs(log);
		}else{
			logger.info("order_id:"+orderInfo.getOrder_id()+",channel type is error:"+orderInfo.getChannel());
		}
	}
	
	
	
	/**
	 * 多渠道通知出票系统出票
	 * */
	@Override
	public void addNotifyCpSys(DBOrderInfo orderInfo,DBNoticeVo vo) {
		String channel=orderInfo.getChannel();
		String order_id=orderInfo.getOrder_id();
		Map<String,String> updateParam=new HashMap<String,String>();
		updateParam.put("order_id", order_id);
		updateParam.put("channel", orderInfo.getChannel());
		updateParam.put("num", vo.getCp_notify_num()+"");
		if(Consts.CHANNEL_TONGCHENG.equals(channel)){
			/**先预订后支付*/
			String result=sendOutTicket(orderInfo,"book");
			if("SUCCESS".equalsIgnoreCase(result)){
				Map<String,String> map = new HashMap<String, String>();
		        //通知出票系统成功则订单状态修改为正在出票  
		        map.put("order_id", order_id);
		        map.put("old_order_status1", Consts.ELONG_ORDER_DOWN);//正在出票
		        map.put("order_status", Consts.ELONG_ORDER_ING);//正在出票
		        orderDao.updateOrderStatus(map);	
			    updateParam.put("notice_status", "SUCCESS");
			    asResultUpdateNotice(updateParam);
			    logger.info("通知出票系统成功，channel:"+orderInfo.getChannel()+",order_id=" + order_id);
			}else if("FAIL".equalsIgnoreCase(result)){
				logger.info("通知出票系统超时，channel:"+orderInfo.getChannel()+",order_id=" + order_id);
				updateParam.put("notice_status", "FAIL");
				asResultUpdateNotice(updateParam);
			}else{
				logger.info("通知出票系统异常，channel:"+orderInfo.getChannel()+",order_id=" + order_id);
				updateParam.put("notice_status", "Exception");
				asResultUpdateNotice(updateParam);
			}
		}else if(Consts.CHANNEL_ELONG.equals(channel)){
			String result=sendOutTicket(orderInfo,"book");
			if("SUCCESS".equalsIgnoreCase(result)){
				Map<String,String> map = new HashMap<String, String>();
		        //通知出票系统成功则订单状态修改为正在出票  
		        map.put("order_id", order_id);
		        map.put("old_order_status1", Consts.ELONG_ORDER_DOWN);//正在出票
		        map.put("order_status", Consts.ELONG_ORDER_ING);//正在出票
		        orderDao.updateOrderStatus(map);	
			    updateParam.put("notice_status", "SUCCESS");
			    asResultUpdateNotice(updateParam);
			    logger.info("通知出票系统成功，channel:"+orderInfo.getChannel()+",order_id=" + order_id);
			}else if("FAIL".equalsIgnoreCase(result)){
				logger.info("通知出票系统超时，channel:"+orderInfo.getChannel()+",order_id=" + order_id);
				updateParam.put("notice_status", "FAIL");
				asResultUpdateNotice(updateParam);
			}else{
				logger.info("通知出票系统异常，channel:"+orderInfo.getChannel()+",order_id=" + order_id);
				updateParam.put("notice_status", "Exception");
				asResultUpdateNotice(updateParam);
			}
		}
	}
	private void asResultUpdateNotice(Map<String,String> param){
		String status=param.get("notice_status");
		String order_id=param.get("order_id");
		int num=Integer.parseInt(param.get("num"));
		String channel=param.get("channel");
		/**通知表更新参数*/
		
		DBNoticeVo noticeInfo=new DBNoticeVo();
		noticeInfo.setOrder_id(order_id);
		noticeInfo.setCp_notify_time("cp_notify_time");
		noticeInfo.setCp_notify_num(num+1);
		noticeInfo.setChannel(channel);
	    
	    ElongOrderLogsVo log=new ElongOrderLogsVo();
		log.setOpt_person("elong_app");
		log.setOrder_id(order_id);
		log.setContent("通知出票系统_"+("SUCCESS".equals(status)?"成功":
			"FAIL".equals(status)?"失败,将启用重发":"异常,将启用重发"
		));
		if("SUCCESS".equals(status)){
			noticeInfo.setCp_notify_status(Consts.NOTICE_OVER);
			noticeInfo.setCp_notify_finish_time("finished");
		}else{
			noticeInfo.setCp_notify_status((num==Consts.CP_NOTICE_NUM-1)?Consts.NOTICE_FAIL:Consts.NOTICE_ING);
		}
		noticeDao.updateNotice(noticeInfo);
		
		elongOrderDao.insertElongOrderLogs(log);
	}
	
	/**
	 * 多渠道出票结果异步请求
	 * */
	@Override
	public void addOrderResultNotice(DBNoticeVo vo) {
		String channel=vo.getChannel();
		if(Consts.CHANNEL_TONGCHENG.equals(channel)){
			//同城渠道的通知
			sendTcOrderResult(vo);
		}else if(Consts.CHANNEL_ELONG.equals(channel)){
			//艺龙渠道的通知
			sendElOrderResult(vo);
		}else{
			logger.info("非法渠道的通知channel:"+channel+",order_id:"+vo.getOrder_id());
		}
	}
	
	/**
	 * 多渠道出票结果异步请求-艺龙
	 * */
	private void sendElOrderResult(DBNoticeVo vo) {
		/**加载配置信息*/
		String merchantCode=Consts.ELONG_MERCHANTCODE;
		String sign_key =Consts.ELONG_SIGNKEY;
		String process_purchase_result=Consts.ELONG_BOOK_RETURN;
		String orderId=vo.getOrder_id();
		Map<String, Object> orderInfo=elongOrderDao.queryOrderInfo(orderId);
		if(Consts.ELONG_ORDER_SUCCESS.equals(orderInfo.get("order_status")+"")||Consts.ELONG_ORDER_MAKED.equals(orderInfo.get("order_status")+"")){
			try {
				/**请求参数*/
				Map<String,String> params=new HashMap<String,String>();
				params.put("merchantCode", merchantCode);
				params.put("orderId", orderId);
				params.put("result", "SUCCESS");
				
				/**查询cp信息*/
				List<ElongOrderInfoCp> list=new ArrayList<ElongOrderInfoCp>();
				list=elongOrderDao.queryOrderCpInfo(orderId);
				
				String tickets=getTicketsJson(orderInfo,list);//
				params.put("tickets",tickets);
				String sign=ElongMd5Util.md5_32(params, sign_key, "utf-8");
				params.put("sign",sign);
				
				logger.info("params:"+params);
				String sendParams=ElongUrlFormatUtil.createGetUrl("", params, "utf-8");
				String result=HttpUtil.sendByPost(process_purchase_result, sendParams, "utf-8");
				logger.info("艺龙先预定，出票结果通知-"+orderId+"[出票成功],通知返回结果"+result+"tickets"+tickets);
				JSONObject json=JSONObject.fromObject(result);
				String retCode=ElongJsonUtil.getString(json.get("retcode"));
				String retDesc=ElongJsonUtil.getString(json.get("retdesc"));
				if(retCode.equals("200")){
					logger.info("艺龙先预定-出票通知SUCCESS，order_id:"+orderId+",result:"+result);
					resultSendUpdateNotice("SUCCESS",vo,result);
					
				}else{
					logger.info("艺龙先预定-出票通知FAIL，order_id:"+orderId+",result:"+result);
					resultSendUpdateNotice("FAIL",vo,result);
				}
			} catch (Exception e) {
				logger.info("艺龙先预定-出票通知_异常，order_id:"+orderId+",msg:"+e);
				resultSendUpdateNotice("EXCEPTION",vo,"");
				e.printStackTrace();
			}
		}else if((Consts.ELONG_ORDER_FAIL.equals(orderInfo.get("order_status")+""))){
			try {
				logger.info("通知艺龙订单"+orderId+"出票失败");
				/**请求参数*/
				String reason=StrUtil.toString(orderInfo.get("out_fail_reason"));
				String passengerReason=StrUtil.toString(orderInfo.get("passenger_reason"));
				/*if("8".equals(reason)||"7".equals(reason)){
					reason="6";
				}*/
				//防止人为选错失败原因
				/*if(TrainConsts.OUT_FAIL_REASON_6.equals(reason)){//passengerReason必填
					if(StringUtils.isEmpty(passengerReason)){
						reason = Consts.OUT_FAIL_REASON_0;
						passengerReason = "";
					}
				}else{
					passengerReason = "";
				}*/
				String failReasonDesc="";
				
				logger.info("reason="+reason + ",orderid = "+orderId);
				if(Consts.OUT_FAIL_REASON_0.equals(reason)){
					failReasonDesc = "其他";
				}else if(Consts.OUT_FAIL_REASON_1.equals(reason)){
					failReasonDesc = "所购买的车次坐席已无票";
				}else if(Consts.OUT_FAIL_REASON_2.equals(reason)){
					failReasonDesc = "身份证件已经实名制购票，不能再次购买同日期同车次的车票";
				}else if(Consts.OUT_FAIL_REASON_3.equals(reason)){
					failReasonDesc = "票价和12306不符";
				}else if(Consts.OUT_FAIL_REASON_4.equals(reason)){
					failReasonDesc = "车次数据与12306不一致";
				}else if(Consts.OUT_FAIL_REASON_5.equals(reason)){
					failReasonDesc = "乘客信息错误";
				}else if(Consts.OUT_FAIL_REASON_6.equals(reason)){
					failReasonDesc = "用户要求取消订单";
				}else if(Consts.OUT_FAIL_REASON_8.equals(reason)){
					String log = elongOrderDao.queryUnVerLogContentByOrderId(orderId);
					log = log == null ? "":log;
					String username = "";
					Pattern p1 = Pattern.compile("(?<=！【app出票失败，)[\u4E00-\u9FA5]+(?=联系人尚未通过身份信息核验)|(?<=！【)[\u4E00-\u9FA5]+(?=（)");
					Matcher m1 = p1.matcher(log);
					if(m1.find()) {
						username = m1.group();
					}
					failReasonDesc =username+ "_乘客身份信息核验失败";
					reason = "6";
				}else if(Consts.OUT_FAIL_REASON_10.equals(reason)){
					failReasonDesc = "限制高消费";
				}else if(Consts.OUT_FAIL_REASON_12.equals(reason)){
					String log = elongOrderDao.getFakerLogContentByOrderId(orderId);
					log = log == null ? "":log;
					String username = "";
					Pattern p1 = Pattern.compile("(?<=！【)[\u4E00-\u9FA5]+(?=的身份信息涉嫌被他人冒用)");
					Matcher m1 = p1.matcher(log);
					if(m1.find()) {
						username = m1.group();
					}
					failReasonDesc = username+"_乘客身份信息被冒用";
					/*艺龙身份冒用reason值扔返回code值6，desc信息返回冒用信息*/
					reason = "6";
				}else{
//					failReasonDesc = "12306乘客身份信息核验失败";
				}
				Map<String,String> params=new HashMap<String,String>();
				/*if(!StringUtils.isEmpty(passengerReason)){
					params.put("passengerReason", passengerReason);
				}*/
				params.put("merchantCode", merchantCode);
				params.put("orderId",orderId);
				params.put("result", "FAIL");
				params.put("failReason", reason);
				params.put("failReasonDesc", failReasonDesc);
				params.put("comment",failReasonDesc);
			
				/**查询cp信息*/
				List<ElongOrderInfoCp> list=new ArrayList<ElongOrderInfoCp>();
				list=elongOrderDao.queryOrderCpInfo(orderId);
				
				//获取出票失败的票价信息
				//String tickets=getErrorTicketsJson(orderInfo,list,Consts.OUT_FAIL_REASON_3.equals(reason));//
				params.put("tickets","");
				
				String sign=ElongMd5Util.md5_32(params, sign_key, "utf-8");
				params.put("sign",sign);
				
				String sendParams=ElongUrlFormatUtil.createGetUrl("", params, "utf-8");
				String result=HttpUtil.sendByPost(process_purchase_result, sendParams, "utf-8");
				logger.info("出票结果通知-"+orderId+"[出票失败:"+failReasonDesc+"],通知返回结果"+result);
				JSONObject json=JSONObject.fromObject(result);
				String retCode=ElongJsonUtil.getString(json.get("retcode"));
				String retDesc=ElongJsonUtil.getString(json.get("retdesc"));
				if(retCode.equals("200")){
					logger.info("艺龙先预定-出票通知SUCCESS，order_id:"+orderId+",result:"+result);
					resultSendUpdateNotice("SUCCESS",vo,result);
				}else{
					logger.info("艺龙先预定-出票通知FAIL，order_id:"+orderId+",result:"+result);
					resultSendUpdateNotice("FAIL",vo,result);
				}
			} catch (Exception e) {
				logger.info("艺龙先预定-出票通知_异常，order_id:"+orderId+",msg:"+e);
				resultSendUpdateNotice("EXCEPTION",vo,"");
				e.printStackTrace();
			}
		}
	}
	
	/**出票结果组装*/
	private String getTicketsJson(Map<String, Object> orderInfo,
			List<ElongOrderInfoCp> list) {
		JSONObject cpInfo=new JSONObject();
		//cpInfo.put("ticketNo", elongNoticeVo.getOut_ticket_billno());
		cpInfo.put("orderId", orderInfo.get("order_id"));
		cpInfo.put("ticketNo", orderInfo.get("out_ticket_billno"));
		String ext_field2= orderInfo.get("ext_field2").toString();
		JSONArray arr=new JSONArray();
		for(ElongOrderInfoCp cp:list){
			JSONObject json=new JSONObject();
			json.put("orderItemId", cp.getCp_id());
			json.put("seatType","".equals(ext_field2)?
					cp.getElong_seat_type():
						(seatTypeCheck(cp.getSeat_type(),cp.getElong_seat_type(),ext_field2)));
			String seat_no=cp.getSeat_no();
			String train_box=cp.getTrain_box();
			json.put("seatNo", (StrUtil.isEmpty(train_box)?"":(train_box+"车厢 "))+(StrUtil.isEmpty(seat_no)?"无座":seat_no));
			json.put("price", cp.getBuy_money());
			json.put("passengerName", cp.getUser_name());
			json.put("certNo", cp.getUser_ids());
			json.put("ticketType", cp.getElong_ticket_type());
			arr.add(json);
		}
		cpInfo.put("tickets", arr);
		return cpInfo.toString();
	}
	
	/**
	 * 多渠道出票结果异步请求-同城
	 * */
	@Deprecated
	private void sendTcOrderResult(DBNoticeVo vo) {
		String order_id=vo.getOrder_id();
		String tc_out_notify_url=Consts.TC_OUTNOTIFYURL;
		String partnerid=Consts.TC_PARTNERID;
		String key=Consts.TC_SIGNKEY;
		String orderStatus=orderDao.queryOrderStatusByOrderId(order_id);
		String tc_cancel_back_url=Consts.TC_CANCELBACKURL;
		
		if(Consts.ELONG_ORDER_CANCELED.equals(orderStatus)){
			/**取消异步通知*/
			/*success	1~8	bool	是否取消成功
			code	1~32	string	消息代码
			成功：100；
			失败错误码参考4.7和4.13
			msg	1~255	string	消息描述
			partnerid	1~16	string	客户端账号（非空，开放平台登录账号）
			reqtime	1~16	string	请求时间，格式：
			yyyyMMddHHmmss（非空）例：
			20140101093518
			sign	1~32	string	数字签名
			=md5(partnerid+reqtime+md5(key))，其中 key 即是由 开放平台分配给同程的。md5 算 法得到的字符串全部为小写
			orderid	1~32	string	同程订单号
			method	1~32	string	操作功能名（非空）*/
			try {
				String reqtime=DateUtil.dateToString(new Date(),"yyyyMMddHHmmss");
				JSONObject backjson =new JSONObject();
				backjson.put("success", true);
				backjson.put("code", "100");
				backjson.put("msg", "处理或操作成功");
				backjson.put("partnerid",partnerid);
				backjson.put("reqtime",reqtime);
				String sign=ElongMd5Util.md5_32(partnerid+reqtime+(ElongMd5Util.md5_32(key, "UTF-8").toLowerCase()),"UTF-8").toLowerCase();
				backjson.put("sign",sign);
				
				backjson.put("orderid",order_id);
				backjson.put("method","train_cancel");
				
				
				Map<String,String> params=new HashMap<String,String>();
				params.put("backjson",backjson.toString());
				String sendParams=ElongUrlFormatUtil.createGetUrl("", params, "utf-8");
				String result=HttpUtil.sendByPost(tc_cancel_back_url, sendParams, "utf-8");
				if(result!=null&&"SUCCESS".equals(result)){
					logger.info("同程-取消通知"+"通知[成功],order_id:"+order_id+",result:"+result);
					resultSendUpdateNotice("SUCCESS",vo,result);
				}else{
					logger.info("同程-取消通知"+"通知[失败],order_id:"+order_id+",result:"+result);
					resultSendUpdateNotice("FAIL",vo,result);
				}
			} catch (Exception e) {
				logger.info("同程-取消通知"+"通知[异常],order_id:"+order_id+",msg:"+e);
				resultSendUpdateNotice("EXCEPTION",vo,"");
				e.printStackTrace();
			}
		}else{
			try {
				/*reqtime	请求时间，格式：yyyyMMddHHmmssfff（非空）例：
				20140101093518059
				sign	数字签名=md5(partnerid+reqtime+md5(key))
				其中 partnerid 为同程登录开放平台的账户，key 是之前开放平台分 配给合作方使用的 key。
				md5 算法得到的字符串全部为小写
				orderid	同程订单号
				transactionid	交易单号
				isSuccess	是否出票成功的标识（Y：出票成功,N：出票失败）*/
				//
				String reqtime=DateUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS");
				Map<String,String> params=new HashMap<String,String>();
				params.put("orderid",order_id);
				params.put("transactionid",order_id);
				params.put("reqtime",reqtime);
				params.put("sign",ElongMd5Util.md5_32(partnerid+reqtime+(ElongMd5Util.md5_32(key, "UTF-8").toLowerCase()),"UTF-8").toLowerCase());
				//isSuccess	是否出票成功的标识（Y：出票成功,N：出票失败）
				params.put("isSuccess",Consts.ELONG_ORDER_SUCCESS.equals(orderStatus)?"Y":"N");
				String sendParams=ElongUrlFormatUtil.createGetUrl("", params, "utf-8");
				logger.info(order_id+",确认出票回调参数："+sendParams);
				String result=HttpUtil.sendByPost(tc_out_notify_url, sendParams, "utf-8");
				if(result!=null&&"SUCCESS".equals(result)){
					logger.info("同程-出票"+(Consts.ELONG_ORDER_SUCCESS.equals(orderStatus)?"成功":"失败")+"通知[成功],order_id:"+order_id+",result:"+result);
					resultSendUpdateNotice("SUCCESS",vo,result);
				}else{
					logger.info("同程-出票"+(Consts.ELONG_ORDER_SUCCESS.equals(orderStatus)?"成功":"失败")+"通知[失败],order_id:"+order_id+",result:"+result);
					resultSendUpdateNotice("FAIL",vo,result);
				}
			} catch (Exception e) {
				logger.info("同程-出票"+(Consts.ELONG_ORDER_SUCCESS.equals(orderStatus)?"成功":"失败")+"通知[异常],order_id:"+order_id+",msg:"+e);
				resultSendUpdateNotice("EXCEPTION",vo,"");
				e.printStackTrace();
			}
		}
	}
	/**
	 * 通知各渠道出票结果 更新通知表
	 * */
	private void resultSendUpdateNotice(String noticeStatus, DBNoticeVo vo,
			String result) {
		String order_id=vo.getOrder_id();
		String channel=vo.getChannel();
		int num=vo.getOut_notify_num();
		DBNoticeVo noticeInfo=new DBNoticeVo();
		noticeInfo.setOrder_id(order_id);
		noticeInfo.setChannel(channel);
		noticeInfo.setOut_notify_time("out_notify_time");
		noticeInfo.setOut_notify_num(num+1);
		/**更新订单 通知状态*/
		Map<String,String>orderNotice=new HashMap<String,String>();
		orderNotice.put("order_id", order_id);
		orderNotice.put("channel", channel);
		/** logsvo*/
		ElongOrderLogsVo log=null;
		if("SUCCESS".equals(noticeStatus)){
			noticeInfo.setOut_notify_finish_time("finished");
			noticeInfo.setOut_notify_status(Consts.NOTICE_OVER);
			log=new ElongOrderLogsVo(order_id,
					"通知"+channel+"出票结果/取消结果_成功",
					"elong_app");
			orderNotice.put("notice_status", Consts.NOTICE_OVER);
		}else{
			String all_notify_status=num==(Consts.TCOUT_NOTICE_NUM-1)?Consts.NOTICE_FAIL:Consts.NOTICE_ING;
			noticeInfo.setOut_notify_status(all_notify_status);
			if(all_notify_status.equals(Consts.NOTICE_FAIL)){
				orderNotice.put("notice_status",  Consts.NOTICE_FAIL);
			}else{
				orderNotice.put("notice_status",  Consts.NOTICE_ING);
			}
			if("FAIL".equals(noticeStatus)){
				log=new ElongOrderLogsVo(order_id,
						"第"+(num+1)+"次通知"+channel+"出票结果/取消结果_失败,返回信息["+result+"]"+",等待重新通知",
						"elong_app");
			}else{//异常处理
				log=new ElongOrderLogsVo(order_id,
						"第"+(num+1)+"次通知"+channel+"出票结果/取消结果_异常,等待重新通知",
						"elong_app");
			}
		}
		noticeDao.updateNotice(noticeInfo);
		elongOrderDao.insertElongOrderLogs(log);
		//更新订单表的通知状态
		/**设置更新状态的单向性 00、准备通知 -->  11、开始通知 -->  22、通知完成 --> 33、通知失败 */
		orderDao.updateOrderNoticeStatus(orderNotice);
	}

	@Override
	public String queryOrderStatusByOrderId(String orderId) {
		return orderDao.queryOrderStatusByOrderId(orderId);
	}




	@Override
	public DBOrderInfo queryOrderInfo(String orderId) {
		DBOrderInfo orderInfo=orderDao.queryOrderInfo(orderId);
		if(orderInfo!=null){
			orderInfo.setPassengers(orderDao.queryOrderCpsInfo(orderId));
		}
		return orderInfo;
	}

	@Override
	public int updateOrderStatus(Map<String, String> map) {
		return orderDao.updateOrderStatus(map);
	}

	@Override
	public String queryOrderStatus(String orderid) {
		//return orderDao.queryOrderStatus(orderid);
		return null;
	}

	@Override
	public String sendOutTicket(DBOrderInfo orderInfo,String type) {
			String order_id=orderInfo.getOrder_id();
			String channel=orderInfo.getChannel();
			try {
				String notify_cp_back_url=Consts.NOTIFY_CP_ALLCHANNEL_BACK_URL;
				String notify_cp_interface_url=Consts.NOTIFY_CP_INTERFACE_URL;
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("orderid", order_id);
				paramMap.put("paymoney", orderInfo.getPay_money());//车票总价
				paramMap.put("trainno", orderInfo.getTrain_no());
				paramMap.put("fromcity", orderInfo.getFrom_city());
				paramMap.put("tocity", orderInfo.getTo_city());
				paramMap.put("ordername",orderInfo.getOrder_name() );
				paramMap.put("traveltime", orderInfo.getTravel_date());
				if(Consts.CHANNEL_ELONG.equals(channel)){
					String fromTime=orderInfo.getFrom_time();
					paramMap.put("fromtime",fromTime);
					paramMap.put("totime",  orderInfo.getTo_time());
					paramMap.put("traveltime", fromTime.substring(0,fromTime.indexOf(" ")));
				}else if(Consts.CHANNEL_TONGCHENG.equals(channel)){
					
				}else{
					
				}
				paramMap.put("seattype", orderInfo.getSeat_type());
				paramMap.put("outtickettype", "11");//
				paramMap.put("channel", orderInfo.getChannel());//渠道编号
				//购买了保险
				//paramMap.put("ext", "level|1");
				//备选坐席问题
				if("book".equals(type)){
					//先预订后支付
					paramMap.put("ispay","11");
				}else if("outTicket".equals(type)){
					//已经支付
					paramMap.put("ispay","00");
				}
				if("11".equals(orderInfo.getWait_for_order())){
					paramMap.put("waitfororder","11");//支持继续出票
				}else{
					paramMap.put("waitfororder","00");//不支持继续出票
				}
				paramMap.put("extseattype", orderInfo.getExt_field1());
				StringBuffer sb = new StringBuffer();
				List<DBPassengerInfo> cpInfoList=orderInfo.getPassengers();
				for (DBPassengerInfo cpInfo : cpInfoList) {
					if(sb.length()>0) {
						sb.append("#");
					}
					sb.append(cpInfo.getCp_id()).append("|").append(cpInfo.getUser_name()).append("|")
					  .append(cpInfo.getTicket_type()).append("|").append(cpInfo.getIds_type()).append("|")
					  .append(cpInfo.getUser_ids()).append("|").append(cpInfo.getSeat_type()).append("|")
					  .append(cpInfo.getPay_money());
				}
				paramMap.put("seattrains", sb.toString());
				paramMap.put("backurl", notify_cp_back_url);
				String params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
				logger.info("通知出票系统：" + order_id+paramMap.toString());
				String result = HttpUtil.sendByPost(notify_cp_interface_url, params, "UTF-8");
				logger.info(order_id+"请求通知出票接口返回：" + result);
				
				if(!StringUtils.isEmpty(result)){
					String[] results = result.trim().split("\\|");
					if("success".equalsIgnoreCase(results[0]) && results.length == 2
							&& orderInfo.getOrder_id().equals(results[1])){//通知成功
						Map<String,String> map = new HashMap<String, String>();
				        //通知出票系统成功则订单状态修改为正在出票  
				        map.put("order_id", order_id);
				        map.put("old_order_status1", Consts.ELONG_ORDER_DOWN);//正在出票
				        map.put("order_status", Consts.ELONG_ORDER_ING);//正在出票
				        orderDao.updateOrderStatus(map);	
					    logger.info("通知出票系统成功，channel:"+orderInfo.getChannel()+",order_id=" + order_id);
					    return "SUCCESS";
					}else{//超时重发
						 logger.info("通知出票系统失败，channel:"+orderInfo.getChannel()+",order_id=" + order_id+"result:"+result);
						return "FAIL";
					}
				}else{
					logger.info("通知出票系统超时，channel:"+orderInfo.getChannel()+",order_id=" + order_id);
					return "FAIL";
				}
			} catch (Exception e) {
				logger.info("通知出票系统异常"+e);
				e.printStackTrace();
				return "EXCEPTION";
			}
	}



	@Override
	public String queryOutTicketOrderStatusByOrderId(String orderId) {
		return orderDao.queryOutTicketOrderStatusByOrderId(orderId);
	}



	@Override
	public Map<String, Object> queryOutTicketOrderInfo(String orderId) {
		return orderDao.queryOutTicketOrderInfo(orderId);
	}



	@Override
	public Map<String, Object> queryOutTicketCpInfo(String cpId) {
		return orderDao.queryOutTicketCpInfo(cpId);
	}


	/**
	 * 多渠道 预订结果通知
	 * */
	@Override
	public void addBookResultNotice(DBNoticeVo vo) {
		String channel=vo.getChannel();
		if(Consts.CHANNEL_TONGCHENG.equals(channel)){
			//同城渠道的通知
			sendTcBookResult(vo);
		}else if(Consts.CHANNEL_ELONG.equals(channel)){
			//艺龙渠道的通知
			sendElBookResult(vo);
		}else{
			logger.info("非法渠道的通知channel:"+channel+",order_id:"+vo.getOrder_id());
		}
	}



	private void sendElBookResult(DBNoticeVo vo) {
		/**加载配置信息*/
		String merchantCode=Consts.ELONG_MERCHANTCODE;
		String sign_key =Consts.ELONG_SIGNKEY;
		String book_result=Consts.ELONG_ORDERRBOOK_URL;
		String orderId=vo.getOrder_id();
		Map<String, Object> orderInfo=elongOrderDao.queryOrderInfo(orderId);
		if(Consts.ELONG_ORDER_MAKED.equals(orderInfo.get("order_status")+"")){
			try {
				/**请求参数*/
				Map<String,String> params=new HashMap<String,String>();
				params.put("merchantCode", merchantCode);
				params.put("orderId", orderId);
				params.put("result", "SUCCESS");
				
				/**查询cp信息*/
				List<ElongOrderInfoCp> list=new ArrayList<ElongOrderInfoCp>();
				list=elongOrderDao.queryOrderCpInfo(orderId);
				
				String tickets=this.getBookTicketsJson(orderInfo,list);//
				params.put("tickets",tickets);
				logger.info(tickets);
				String sign=ElongMd5Util.md5_32(params, sign_key, "utf-8");
				params.put("sign",sign);
				String sendParams=ElongUrlFormatUtil.createGetUrl("", params, "utf-8");
				String result=HttpUtil.sendByPost(book_result, sendParams, "utf-8");
				logger.info("艺龙先预定,预订结果通知sendParams"+params.toString()+"[出票成功],通知返回结果"+result+"tickets"+tickets);
				
				JSONObject json=JSONObject.fromObject(result);
				String retCode=ElongJsonUtil.getString(json.get("retcode"));
				String retDesc=ElongJsonUtil.getString(json.get("retdesc"));
				if(retCode.equals("200")){
					logger.info("艺龙先预定,预订结果_通知成功,order_id:"+orderId+",result:"+result);
					bookResultSendUpdateNotice("SUCCESS",vo,result,true);
				}else{
					logger.info("艺龙先预定,预订结果_通知失败,order_id:"+orderId+",result:"+result);
					bookResultSendUpdateNotice("FAIL",vo,result,true);
				}
			} catch (Exception e) {
				logger.info("艺龙先预定,预订结果_通知异常,order_id:"+orderId+",msg:"+e);
				bookResultSendUpdateNotice("EXCEPTION",vo,"",true);
				e.printStackTrace();
			}
		}else if((Consts.ELONG_ORDER_FAIL.equals(orderInfo.get("order_status")+""))){
			try {
				logger.info("通知艺龙订单"+orderId+"出票失败");
				/**请求参数*/
				String reason=StrUtil.toString(orderInfo.get("out_fail_reason"));
				String passengerReason=StrUtil.toString(orderInfo.get("passenger_reason"));
				/*if("8".equals(reason)||"7".equals(reason)){
					reason="6";
				}*/
				//防止人为选错失败原因
				/*if(TrainConsts.OUT_FAIL_REASON_6.equals(reason)){//passengerReason必填
					if(StringUtils.isEmpty(passengerReason)){
						reason = Consts.OUT_FAIL_REASON_0;
						passengerReason = "";
					}
				}else{
					passengerReason = "";
				}*/
				String failReasonDesc="";
				
				if(Consts.OUT_FAIL_REASON_0.equals(reason)){
					failReasonDesc = "其他";
				}else if(Consts.OUT_FAIL_REASON_1.equals(reason)){
					failReasonDesc = "所购买的车次坐席已无票";
				}else if(Consts.OUT_FAIL_REASON_2.equals(reason)){
					failReasonDesc = "身份证件已经实名制购票，不能再次购买同日期同车次的车票";
				}else if(Consts.OUT_FAIL_REASON_3.equals(reason)){
					failReasonDesc = "票价和12306不符";
				}else if(Consts.OUT_FAIL_REASON_4.equals(reason)){
					failReasonDesc = "车次数据与12306不一致";
				}else if(Consts.OUT_FAIL_REASON_5.equals(reason)){
					failReasonDesc = "乘客信息错误";
				}else if(Consts.OUT_FAIL_REASON_6.equals(reason)){
					failReasonDesc = "用户要求取消订单";
				}else if(Consts.OUT_FAIL_REASON_8.equals(reason)){
					String log = elongOrderDao.queryUnVerLogContentByOrderId(orderId);
					log = log == null?"":log;
					String username = "";
					Pattern p1 = Pattern.compile("(?<=！【app出票失败，)[\u4E00-\u9FA5]+(?=联系人尚未通过身份信息核验)|(?<=！【)[\u4E00-\u9FA5]+(?=（)");
					Matcher m1 = p1.matcher(log);
					if(m1.find()) {
						username = m1.group();
					}
					failReasonDesc = username+"_乘客身份信息核验失败";
				}else if(Consts.OUT_FAIL_REASON_10.equals(reason)){
					failReasonDesc = "限制高消费";
				}else if(Consts.OUT_FAIL_REASON_12.equals(reason)){
					String log = elongOrderDao.getFakerLogContentByOrderId(orderId);
					log = log == null?"":log;
					String username = "";
					Pattern p1 = Pattern.compile("(?<=！【)[\u4E00-\u9FA5]+(?=的身份信息涉嫌被他人冒用)");
					Matcher m1 = p1.matcher(log);
					if(m1.find()) {
						username = m1.group();
					}
					failReasonDesc = username+"_乘客身份信息被冒用";
					/*艺龙身份冒用reason值扔返回code值6，desc信息返回冒用信息*/
					reason = "6";
				}
				
				Map<String,String> params=new HashMap<String,String>();
				/*if(!StringUtils.isEmpty(passengerReason)){
					params.put("passengerReason", passengerReason);
				}*/
				params.put("merchantCode", merchantCode);
				params.put("orderId",orderId);
				params.put("result", "FAIL");
				params.put("failReason", reason);
				params.put("failReasonDesc", failReasonDesc);
				params.put("comment",failReasonDesc);
			
				/**查询cp信息*/
				List<ElongOrderInfoCp> list=new ArrayList<ElongOrderInfoCp>();
				list=elongOrderDao.queryOrderCpInfo(orderId);
				
				//获取出票失败的票价信息
				//String tickets=getErrorTicketsJson(orderInfo,list,Consts.OUT_FAIL_REASON_3.equals(reason));//
				params.put("tickets","");
				
				String sign=ElongMd5Util.md5_32(params, sign_key, "utf-8");
				params.put("sign",sign);
				
				String sendParams=ElongUrlFormatUtil.createGetUrl("", params, "utf-8");
				String result=HttpUtil.sendByPost(book_result, sendParams, "utf-8");
				logger.info("艺龙先预定,预订结果通知sendParams："+params.toString()+"[出票失败:"+failReasonDesc+"],通知返回结果"+result);
				JSONObject json=JSONObject.fromObject(result);
				String retCode=ElongJsonUtil.getString(json.get("retcode"));
				String retDesc=ElongJsonUtil.getString(json.get("retdesc"));
				if(retCode.equals("200")){
					logger.info("艺龙先预定,预订结果_通知成功,order_id:"+orderId+",result:"+result);
					bookResultSendUpdateNotice("SUCCESS",vo,result,false);
				}else{
					logger.info("艺龙先预定,预订结果_通知失败,order_id:"+orderId+",result:"+result);
					bookResultSendUpdateNotice("FAIL",vo,result,false);
				}
			} catch (Exception e) {
				logger.info("艺龙先预定,预订结果_通知异常,order_id:"+orderId+",msg:"+e);
				bookResultSendUpdateNotice("EXCEPTION",vo,"",false);
			}
		}
		
	
	}

	//组装艺龙车票信息方法  预订回调
	private String getBookTicketsJson(Map<String, Object> orderInfo,
			List<ElongOrderInfoCp> list) {
		JSONObject cpInfo=new JSONObject();
		//cpInfo.put("ticketNo", elongNoticeVo.getOut_ticket_billno());
		cpInfo.put("orderId", orderInfo.get("order_id"));
		cpInfo.put("ticketNo", orderInfo.get("out_ticket_billno"));
		
		cpInfo.put("holdingSeatSuccessTime",orderInfo.get("out_ticket_time"));
		cpInfo.put("payTimeDeadLine",orderInfo.get("pay_limit_time"));
		String ext_field2= orderInfo.get("ext_field2").toString();
		JSONArray arr=new JSONArray();
		for(ElongOrderInfoCp cp:list){
			JSONObject json=new JSONObject();
			json.put("orderItemId", cp.getCp_id());
			json.put("seatType","".equals(ext_field2)?
					cp.getElong_seat_type():
						(seatTypeCheck(cp.getSeat_type(),cp.getElong_seat_type(),ext_field2)));
			String seat_no=cp.getSeat_no();
			String train_box=cp.getTrain_box();
			json.put("seatNo", (StrUtil.isEmpty(train_box)?"":(train_box+"车厢 "))+(StrUtil.isEmpty(seat_no)?"无座":seat_no));
			json.put("price", cp.getBuy_money());
			json.put("passengerName", cp.getUser_name());
			json.put("certNo", cp.getUser_ids());
			json.put("ticketType", cp.getElong_ticket_type());
			arr.add(json);
		}
		cpInfo.put("tickets", arr);
		return cpInfo.toString();
	}



	private String seatTypeCheck(String seatType, String elong_seat_type,
			String ext_field1) {
		if(ElongConsts.SEAT_TYPE_2.equals(seatType)){
			/**一等座|一等软座 区分*/
			if(ElongConsts.ELONG_SEAT_TYPE_6.equals(elong_seat_type)
					||ext_field1.indexOf(ElongConsts.ELONG_SEAT_TYPE_6+",")>0){
				//一等软座
				return ElongConsts.ELONG_SEAT_TYPE_6;
			}else{
				return ElongConsts.ELONG_SEAT_TYPE_9;
			}
		}else if(ElongConsts.SEAT_TYPE_3.equals(seatType)){
			/**二等座|二等软座 区分*/
			if(ElongConsts.ELONG_SEAT_TYPE_7.equals(elong_seat_type)
					||ext_field1.indexOf(ElongConsts.ELONG_SEAT_TYPE_7+",")>0){
				//一等软座
				return ElongConsts.ELONG_SEAT_TYPE_7;
			}else{
				return ElongConsts.ELONG_SEAT_TYPE_10;
			}
			
		}else if(ElongConsts.SEAT_TYPE_1.equals(seatType)){
			/**特等座|特等软座 区分*/
			if(ElongConsts.ELONG_SEAT_TYPE_13.equals(elong_seat_type)
					||ext_field1.indexOf(ElongConsts.ELONG_SEAT_TYPE_13+",")>0){
				//一等软座
				return ElongConsts.ELONG_SEAT_TYPE_13;
			}else{
				return ElongConsts.ELONG_SEAT_TYPE_11;
			}
		}else{
			return ElongConsts.getElongSeatType(seatType);
		}
	}



	/**针对同城的  预订结果的异步回调 具体参数协议*/
	private void sendTcBookResult(DBNoticeVo vo){
		String order_id=vo.getOrder_id();
		String book_notify_url=vo.getBook_notify_url();
		DBOrderInfo orderInfo=orderDao.queryOrderInfo(order_id);
		List<DBPassengerInfo> cpInfoList=orderDao.queryOrderCpsInfo(order_id);
		boolean ordersuccess;
		if(Consts.ELONG_ORDER_MAKED.equals(orderInfo.getOrder_status())){
			ordersuccess=true;
		}else if(Consts.ELONG_ORDER_FAIL.equals(orderInfo.getOrder_status())){
			ordersuccess=false;
		}else{
			ordersuccess=false;
		}
		try {
			String[] exts=orderInfo.getExt_field2().split("\\|");
			String from_station_code=exts[0];
			String to_station_code=exts[1];
			String reqtoken="";
			if(exts.length==3){
				reqtoken=exts[2];
			}
			String	error_info=orderInfo.getOut_fail_reason();
			JSONObject j=new JSONObject();
			
			/*success	bool		true:成功，false:失败
			code	int	4	状态编码
			msg	string	1~25
			6	提示信息*/
			if(ordersuccess){
				j.put("success", true);
				j.put("code",100);
				j.put("msg", "处理或操作成功");
			}else{
				j.put("success", false);
				int code=309;
				String msg="没有余票";
				if("2".equals(error_info)){
					code=305;
					msg="乘客已经预订过该车次";
				}else if("5".equals(error_info)||"7".equals(error_info)||"8".equals(error_info)){
					code=308;
					
					String passenger_reson=orderInfo.getPassenger_reason();
					if(passenger_reson!=null){
						//[{"certNo":"310102196403015222","certType":"1","name":"解春芬","reason":"1"},{"certNo":"31010119590907363X","certType":"1","name":"钮来林","reason":"1"}]
						JSONArray arr=JSONArray.fromObject(passenger_reson);
						int size=arr.size();
						StringBuffer sb_msg=new StringBuffer();
						for(int i=0;i<size;i++){
							JSONObject jb=JSONObject.fromObject(arr.getString(i));
							String reason=jb.getString("reason");
							if("1".equals(reason)){
								sb_msg.append(jb.getString("name")).append(jb.getString("certNo"));
							}
						}
						msg=sb_msg.toString();
					}else{
						code=309;
					}
					/*for(DBPassengerInfo p:cpInfoList){
						msg=p.getUser_name()+p.getUser_ids();
						break;
					}*/
				}else if("9".equals(error_info)){
					code=506;
					msg="出票系统故障";
				}else if("10".equals(error_info)){
					code=313;
					msg="订单内有乘客已被法院依法限制高消费，禁止乘坐列车当前坐席类型";
					
				} else if("12".equals(error_info)){
					String content = elongOrderDao.getFakerLogContentByOrderId(order_id);
					String userName = getFakerLogUsername(content);
					//身份信息被冒用
					code = 315;
					msg= userName + "_身份信息涉嫌被他人冒用，请持本人身份证件原件到就近铁路客运车站办理身份核验";
				}
				j.put("code",code);
				j.put("msg", msg);
			}
			
			/*账号使用历史*/
			List<Map<String, String>> accountList = new ArrayList<Map<String,String>>();
			try {
				List<String> usedAccountLogs = elongOrderDao.findUsedAccountLogContentByOrderId(order_id);
				accountList = accountList(usedAccountLogs);
				j.put("accountlist", accountList);
			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.info("账号使用历史结果array: " + accountList.toString());
			/*
			train_date	1~16	string	乘车日期
			start_time	1~8	string	从出发站开车时间
			arrive_time	1~8	string	抵达目的站的时间
			ordernumber	1~8	string	票号
			runtime	1~8	string	运行时间
			passengers	1~1024	string	与输入的一样，订票成功了里面的cxin和ticketid参数会有值。*/
			
			
			
			
			j.put("reqtoken", reqtoken);
			j.put("orderid", orderInfo.getOrder_id());
			j.put("transactionid", orderInfo.getOrder_id());
			j.put("ordersuccess", ordersuccess);
			j.put("orderamount", orderInfo.getBuy_money()==null?"":orderInfo.getBuy_money());
			j.put("checi", orderInfo.getTrain_no());
			j.put("from_station_code", from_station_code);
			j.put("from_station_name", orderInfo.getFrom_city());
			j.put("to_station_code", to_station_code);
			j.put("to_station_name", orderInfo.getTo_city());
			if("12".equals(orderInfo.getOut_fail_reason())){
				j.put("refund_online", "1");
			}
			
			/**乘车日期 格式*/
			if(ordersuccess){
				j.put("train_date", orderInfo.getTravel_date());
				String start_time=orderInfo.getFrom_time();
				String arrive_time=orderInfo.getTo_time();
				String runtime=null;
				if(arrive_time==null){
					String start_query_time=null;
					String arrive_query_time=null;
					
					/*数据库补全*/
					SInfo fromSInfo = commonDao.getSInfo(orderInfo.getTrain_no(), orderInfo.getFrom_city());//出发站信息
					SInfo toSInfo = commonDao.getSInfo(orderInfo.getTrain_no(), orderInfo.getTo_city());//到达站信息
					
					if(fromSInfo == null || toSInfo == null) {
						logger.info("出发，到达时间查询失败");
					} else {
						start_query_time = fromSInfo.getStartTime();
						arrive_query_time = toSInfo.getArriveTime();
						runtime = runtimeMinute(orderInfo, fromSInfo, toSInfo).toString();
					}
					logger.info("to_time db query start_time : " + start_query_time + ", to_time :" + arrive_query_time);
					
//					if(StringUtils.isEmpty(start_query_time) || StringUtils.isEmpty(arrive_query_time)) {
//						/*查询补全*/
//						Map<String,String> paramMap=new HashMap<String, String>();
//						String url=Consts.QUERY_TICKET;
//						paramMap.put("channel", "tongcheng");
//						paramMap.put("from_station", orderInfo.getFrom_city());
//						paramMap.put("arrive_station", orderInfo.getTo_city());
//						paramMap.put("travel_time", orderInfo.getTravel_date());
//						paramMap.put("purpose_codes","ADULT");
//						paramMap.put("isNotZW", "yes");//非中文查询
//						String params;
//						params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
//						logger.info("同程发起余票查询"+paramMap.toString()+"url"+url);
//						String result = HttpUtil.sendByPost(url, params, "UTF-8");
//						if(result==null||result.equalsIgnoreCase("STATION_ERROR")||result.equalsIgnoreCase("ERROR")){
//							logger.info("同程book通知,针对没有to_time采用查询补全_查询失败");
//							throw new RuntimeException("to_time采用查询补全_查询失败");
//						}else{
//							JSONArray arr=JSONArray.fromObject(result);
//							int index=arr.size();
//							for(int i=0;i<index;i++){
//								if(arr.getJSONObject(i).get("train_code").equals(orderInfo.getTrain_no())){
//									runtime=arr.getJSONObject(i).getString("run_time_minute");
//									start_query_time=arr.getJSONObject(i).getString("start_time");
//									arrive_query_time=arr.getJSONObject(i).getString("arrive_time");
//									break;
//								}
//								
//							}
//							if(start_query_time==null||arrive_query_time==null||runtime==null){
//								logger.info("同程book通知,针对没有to_time采用查询补全_查询失败,未匹配到结果");
//								throw new RuntimeException("to_time采用查询补全_查询失败");
//							}else{
//								logger.info("同程book通知,针对没有to_time采用查询补全_查询成功"+start_query_time+"_"+arrive_query_time+"_"+runtime);
//							}
//						}
//					}
					
					// 查询车次到达时间
					if (StringUtils.isEmpty(arrive_query_time)) {
						Map<String, String> paramMap = new HashMap<String, String>();

						/*String fromStation = ""; // 出发车站三字码
						String toStation = ""; // 到达车站三字码
						Map<String, String> map = new HashMap<String, String>();
						map.put("from_city", orderInfo.getFrom_city());
						logger.info("订单中出发城市为：" + orderInfo.getFrom_city());
						map.put("to_city", orderInfo.getTo_city());
						logger.info("订单中到达城市为：" + orderInfo.getTo_city());
						List<DBOrderInfo> dbOrderInfoList = orderDao.queryCity3CList(map);
						logger.info("查询出来的车次三字码列表为：" + dbOrderInfoList);
						DBOrderInfo dbOrderInfo = null;
						if (dbOrderInfoList != null && dbOrderInfoList.size() > 0) {
							for (int i = 0; i < dbOrderInfoList.size(); i++) {
								dbOrderInfo = dbOrderInfoList.get(i);
								logger.info("dbOrderInfo为：" + dbOrderInfo);
								if (dbOrderInfo.getStationName().equals(orderInfo.getFrom_city())) {
									fromStation = dbOrderInfo.getStationCode();
									logger.info("from_station为：" + fromStation);
								} else if (dbOrderInfo.getStationName().equals(orderInfo.getTo_city())) {
									toStation = dbOrderInfo.getStationCode();
									logger.info("to_station为：" + toStation);
								}
							}
						}

						String formatStr = "yyyy-MM-dd";
						SimpleDateFormat format = new SimpleDateFormat(formatStr);
						String travelDateStr = orderInfo.getTravel_date(); //列车乘车日期
						logger.info("订单中的乘车日期为：" + travelDateStr);
						Date travelDate = null;
						String trainDateStr = "";
						try {
							travelDate = format.parse(travelDateStr);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (null != travelDate) {
							trainDateStr = format.format(travelDate);
						}
						logger.info("放到参数中的乘车日期train_date为：" + trainDateStr);
						paramMap.put("train_date", trainDateStr);
						paramMap.put("commond", "queryStation");
						paramMap.put("train_code", orderInfo.getTrain_no()); //车次
						paramMap.put("from_station", fromStation);
						paramMap.put("to_station", toStation);
						/*if (null != result && !"".equals(result)) {
							//2016-11-01|18:56|21:58
							try{
								start_query_time = orderInfo.getTravel_date()+" "+result.split("\\|")[1]+":00";
								arrive_query_time = result.split("\\|")[0]+" "+result.split("\\|")[2]+":00";
								runtime=DateUtil.minuteDiff(DateUtil.stringToDate(arrive_query_time, "yyyy-MM-dd HH:mm:ss"),DateUtil.stringToDate(start_query_time, "yyyy-MM-dd HH:mm:ss"))+"";
								
							}catch(Exception e){
								e.printStackTrace();
								logger.info("同城占座回调查询到达时间异常"+orderInfo.getOrder_id());
							}
						}*/
						
						
						paramMap.put("channel", "tongcheng");
						paramMap.put("from_station", orderInfo.getFrom_city());
						paramMap.put("arrive_station", orderInfo.getTo_city());
						paramMap.put("travel_time", orderInfo.getTravel_date());
						paramMap.put("purpose_codes","ADULT");
						paramMap.put("isNotZW", "yes");//非中文查询
						
						String uri = UrlFormatUtil.CreateUrl("", paramMap,"UTF-8");
						logger.info(orderInfo.getOrder_id()+"调用查询车次到达时间接口中的参数为：" + uri);
						String url = Consts.QUERY_TICKET;// 查询车次到达时间的接口URL
						logger.info(orderInfo.getOrder_id()+"调用查询车次到达时间接口的url为：" + url);
						String result = HttpUtil.sendByPost(url, uri, "UTF-8"); // 调用查询车次到达时间的接口
						
						if(result==null||result.equalsIgnoreCase("STATION_ERROR")||result.equalsIgnoreCase("ERROR")){
							result = HttpUtil.sendByPost(url, uri, "UTF-8");
							if(result==null||result.equalsIgnoreCase("STATION_ERROR")||result.equalsIgnoreCase("ERROR")){
								result = HttpUtil.sendByPost(url, uri, "UTF-8");
							}
						}
						logger.info(orderInfo.getOrder_id()+"调用查询车次到达时间接口最后的返回结果为：" + result);
						if(result==null||result.equalsIgnoreCase("STATION_ERROR")||result.equalsIgnoreCase("ERROR")){
							logger.info("同城book通知,针对没有to_time采用查询补全_查询失败");
							
						}else{
							JSONArray arr = JSONArray.fromObject(result);
							int index = arr.size();
							for (int i = 0; i < index; i++) {
								if (arr.getJSONObject(i).get("train_code").equals(orderInfo.getTrain_no())) {
									runtime = arr.getJSONObject(i).getString("run_time_minute");
									start_query_time = arr.getJSONObject(i).getString("start_time");
									arrive_query_time = arr.getJSONObject(i).getString("arrive_time");
									break;
								}
							}
						}
					}
					
					j.put("start_time",start_query_time);
					j.put("arrive_time",arrive_query_time);
					j.put("runtime", runtime);//运行时间
				}else{
					runtime=DateUtil.minuteDiff(DateUtil.stringToDate(arrive_time, "yyyy-MM-dd HH:mm:ss"),DateUtil.stringToDate(start_time, "yyyy-MM-dd HH:mm:ss"))+"";
					start_time=start_time.substring(start_time.indexOf(" ")+1, start_time.indexOf(" ")+6);
					arrive_time=arrive_time.substring(arrive_time.indexOf(" ")+1, arrive_time.indexOf(" ")+6);
					j.put("start_time",start_time==null?"":start_time);
					j.put("arrive_time",arrive_time==null?"":arrive_time);
					j.put("runtime", runtime);//运行时间
				}
				j.put("ordernumber",orderInfo.getOut_ticket_billno()==null?"":orderInfo.getOut_ticket_billno());
				/**时间 计算方式*/
			}else{
				j.put("train_date", orderInfo.getTravel_date());
				j.put("start_time","");
				j.put("arrive_time","");
				j.put("ordernumber","");
				j.put("runtime", "");//运行时间
			}
			
			JSONArray parr=new JSONArray();
			for(DBPassengerInfo p:cpInfoList){
				/*passengerid	int	乘客的顺序号
				ticket_no	string	票号（此票在本订单中的唯一标识，订票成功后才有值）
				passengersename	string	乘客姓名
				passportseno	string	乘客证件号码
				passporttypeseid	string	证件类型ID
				与名称对应关系:
				1:二代身份证，2:一代身份证，C:港澳通行证，G:台湾通行证，B:护照
				passporttypeseidname	string	证件类型名称
				piaotype	string	票种ID。
				与票种名称对应关系：
				1:成人票，2:儿童票，3:学生票，4:残军票
				piaotypename	string	票种名称
				zwcode	string	座位编码。
				与座位名称对应关系：
				9:商务座，P:特等座，M:一等座，O:二等座，6:高级软卧，
				4:软卧，3:硬卧，2:软座，1:硬座
				注意：当最低的一种座位，无票时，购买选择该座位种类，买下的就是无座(也就说买无座的席别编码就是该车次的最低席别的编码)，另外，当最低席别的票卖完了的时候才可以卖无座的票。
				zwname	string	座位名称
				cxin	string	几车厢几座（在订票成功后才会有值）
				price	string	票价
				reason	int	身份核验状态 0：正常 1：待审核 2：未通过*/

				JSONObject p_json=new JSONObject();
				String seat_no=p.getSeat_no();
				String train_box=p.getTrain_box();
				String cp_id=p.getCp_id();
				//String passengerid=cp_id.substring(cp_id.lastIndexOf("_")+1);
				p_json.put("passengerid",p.getOut_passengerid());
				p_json.put("ticket_no",  ordersuccess?p.getCp_id():"");
				p_json.put("passengersename",p.getUser_name());
				p_json.put("passportseno",p.getUser_ids());
				
				p_json.put("passporttypeseid", p.getElong_ids_type());
				p_json.put("passporttypeseidname",TongChengConsts.getPassporttypeseidname(p.getElong_ids_type()));
				p_json.put("piaotype", p.getElong_ticket_type());
				p_json.put("piaotypename", TongChengConsts.getPiaotypename(p.getElong_ticket_type()));
				
				//系统扩展坐席类型：21动卧  22高级动卧  23一等软座  24二等软座
				//订单请求坐席类型：M一等座 O二等座 4软卧 F动卧 A高级软座  ,7一等软座 8二等软座
				
				//9:商务座，P:特等座，M:一等座，O:二等座，6:高级软卧，
				//4:软卧，3:硬卧，2:软座，1:硬座 7:一等软座 8:二等软座 A:高级动卧 F:动卧 
				
				String zwcode=p.getElong_seat_type();
				if("M".equals(p.getElong_seat_type())){//一等座一等软座转换
					zwcode="23".equals(p.getSeat_type())?"7":"M";
				}else if("O".equals(p.getElong_seat_type())){//二等座二等软座转换
					zwcode="24".equals(p.getSeat_type())?"8":"O";
				}else if("4".equals(p.getElong_seat_type())){//软卧动卧转换
					zwcode="21".equals(p.getSeat_type())?"F":"4";
				}else if("6".equals(p.getElong_seat_type())){//高级动卧高级软卧转换
					zwcode="22".equals(p.getSeat_type())?"A":"6";
				}
				p_json.put("zwcode",zwcode);
				//p_json.put("zwcode", p.getElong_seat_type());
				p_json.put("zwname",TongChengConsts.getZwname(zwcode));
				
				p_json.put("cxin",ordersuccess?((StrUtil.isEmpty(train_box)?"":(train_box+"车厢,"))+(StrUtil.isEmpty(seat_no)?"无座":seat_no)):"");
				p_json.put("price",ordersuccess?p.getBuy_money():"");
				
				//审核未通过
				if(ordersuccess){
					// 0：正常 1：待审核 2：未通过
					p_json.put("reason","0");
				}else{
					if("6".equals(orderInfo.getOut_fail_reason())){
						p_json.put("reason","1");
					}else{
						p_json.put("reason","0");
					}
				}
				parr.add(p_json);
			}
			j.put("passengers", parr.toString());
			Map<String,String> params=new HashMap<String,String>();
			params.put("data", j.toString());
				logger.info("同程book请求参数"+j.toString());
			long start=System.currentTimeMillis();
			String sendParams=ElongUrlFormatUtil.createGetUrl("", params, "utf-8");
		
			String result=HttpUtil.sendByPost(vo.getBook_notify_url(), sendParams, "utf-8");
			if(result!=null){
				logger.info("同程book返回结果,耗时"+(System.currentTimeMillis()-start)+"ms,result:"+result);
				//JSONObject json=JSONObject.fromObject(result);
				if("success".equals(result)){
					logger.info("同程book通知成功,耗时"+(System.currentTimeMillis()-start)+"ms,order_id:"+order_id+",result:"+result);
					bookResultSendUpdateNotice("SUCCESS",vo,result,ordersuccess);
				}else{
					logger.info("同程book通知失败,order_id:"+order_id+",result:"+result);
					bookResultSendUpdateNotice("FAIL",vo,result,ordersuccess);
				}
			}else{
				logger.info("同程book通知失败,order_id:"+order_id+",result返回null");
				bookResultSendUpdateNotice("FAIL",vo,result,ordersuccess);
			}
		}catch (Exception e) {
			logger.info("同程book通知异常,order_id:"+order_id+",msg:"+e);
			bookResultSendUpdateNotice("EXCEPTION",vo,"",ordersuccess);
			e.printStackTrace();
		}
	}

	/**
	 * 通知各渠道出票结果 更新通知表
	 * */
	private void bookResultSendUpdateNotice(String noticeStatus, DBNoticeVo vo,
			String result,boolean ordersuccess) {
		if("tongcheng".equals(vo.getChannel())){
			String order_id=vo.getOrder_id();
			String channel=vo.getChannel();
			int num=vo.getBook_notify_num();
			
			DBNoticeVo noticeInfo=new DBNoticeVo();
			noticeInfo.setOrder_id(order_id);
			noticeInfo.setChannel(channel);
			noticeInfo.setBook_notify_time("out_notify_time");
			noticeInfo.setBook_notify_num(num+1);
			
			/**更新订单 通知状态*/
			Map<String,String>orderNotice=new HashMap<String,String>();
			orderNotice.put("order_id", order_id);
			orderNotice.put("channel", "tongcheng");
			
			/** logsvo*/
			ElongOrderLogsVo log=null;
			if("SUCCESS".equals(noticeStatus)){
				noticeInfo.setBook_notify_finish_time("finished");
				noticeInfo.setBook_notify_status(Consts.NOTICE_OVER);
				log=new ElongOrderLogsVo(order_id,
						"通知"+channel+"预订结果_成功",
						"elong_app");
				orderNotice.put("notice_status", Consts.NOTICE_OVER);
				
			}else{
				String all_notify_status=num==(Consts.TCBOOK_NOTICE_NUM-1)?Consts.NOTICE_FAIL:Consts.NOTICE_ING;
				noticeInfo.setBook_notify_status(all_notify_status);
				if("FAIL".equals(noticeStatus)){
					log=new ElongOrderLogsVo(order_id,
							"第"+(num+1)+"次通知"+channel+"预订结果_失败,返回信息["+result+"]"+",等待重新通知",
							"elong_app");
				}else{//异常处理
					log=new ElongOrderLogsVo(order_id,
							"第"+(num+1)+"次通知"+channel+"预订结果_异常,等待重新通知",
							"elong_app");
				}
				if(all_notify_status.equals(Consts.NOTICE_FAIL)){
					orderNotice.put("notice_status", Consts.NOTICE_FAIL);
					
					/**针对预订结果通知 失败的更新通知失败*/
					orderDao.updateOrderNoticeStatus(orderNotice);
				}else{
					orderNotice.put("notice_status", Consts.NOTICE_ING);
				}
				
			}
			noticeDao.updateNotice(noticeInfo);
			elongOrderDao.insertElongOrderLogs(log);
			//更新订单表的通知状态
			/**设置更新状态的单向性 00、准备通知 -->  11、开始通知 -->  22、通知完成 --> 33、通知失败 */
			//是否设置一个  预订通知 成功与否的状态值
			
			//针对预订失败的通知 有通知成功项
			//orderDao.updateOrderBookNoticeStatus(orderNotice);
			if(!ordersuccess){
				orderDao.updateOrderNoticeStatus(orderNotice);
			};
		}else if("elong".equals(vo.getChannel())){
			String order_id=vo.getOrder_id();
			String channel=vo.getChannel();
			int num=vo.getBook_notify_num();
			
			DBNoticeVo noticeInfo=new DBNoticeVo();
			noticeInfo.setOrder_id(order_id);
			noticeInfo.setChannel(channel);
			noticeInfo.setBook_notify_time("out_notify_time");
			noticeInfo.setBook_notify_num(num+1);
			
			/**更新订单 通知状态*/
			Map<String,String>orderNotice=new HashMap<String,String>();
			orderNotice.put("order_id", order_id);
			orderNotice.put("channel", "elong");
			
			/** logsvo*/
			ElongOrderLogsVo log=null;
			if("SUCCESS".equals(noticeStatus)){
				noticeInfo.setBook_notify_finish_time("finished");
				noticeInfo.setBook_notify_status(Consts.NOTICE_OVER);
				log=new ElongOrderLogsVo(order_id,
						"通知"+channel+"预订结果_成功",
						"elong_app");
				orderNotice.put("notice_status", Consts.NOTICE_OVER);
				
			}else{
				String all_notify_status=num==(Consts.TCBOOK_NOTICE_NUM-1)?Consts.NOTICE_FAIL:Consts.NOTICE_ING;
				noticeInfo.setBook_notify_status(all_notify_status);
				if("FAIL".equals(noticeStatus)){
					log=new ElongOrderLogsVo(order_id,
							"第"+(num+1)+"次通知"+channel+"预订结果_失败,返回信息["+result+"]"+",等待重新通知",
							"elong_app");
				}else{//异常处理
					log=new ElongOrderLogsVo(order_id,
							"第"+(num+1)+"次通知"+channel+"预订结果_异常,等待重新通知",
							"elong_app");
				}
				if(all_notify_status.equals(Consts.NOTICE_FAIL)){
					orderNotice.put("notice_status", Consts.NOTICE_FAIL);
					
					/**针对预订结果通知 失败的更新通知失败*/
					orderDao.updateOrderNoticeStatus(orderNotice);
				}else{
					orderNotice.put("notice_status", Consts.NOTICE_ING);
				}
				
			}
			noticeDao.updateNotice(noticeInfo);
			elongOrderDao.insertElongOrderLogs(log);
			//更新订单表的通知状态
			/**设置更新状态的单向性 00、准备通知 -->  11、开始通知 -->  22、通知完成 --> 33、通知失败 */
			//是否设置一个  预订通知 成功与否的状态值
			
			//针对预订失败的通知 有通知成功项
			//orderDao.updateOrderBookNoticeStatus(orderNotice);
			if(!ordersuccess){
				orderDao.updateOrderNoticeStatus(orderNotice);
			};
		}
		
	}

	@Override
	public void updateOrderInfo(List<Map<String, String>> cpMapList, DBOrderInfo orderInfo) {
		String channel=orderInfo.getChannel();
		//System.out.println(channel+"channel--------------------------");
		ElongOrderLogsVo log=new ElongOrderLogsVo();
		log.setOpt_person("elong_app");
		log.setOrder_id(orderInfo.getOrder_id());
		String status=orderInfo.getOrder_status();
		if(Consts.CHANNEL_ELONG.equals(channel)){
			//艺龙渠道的
			String order_status=orderInfo.getOrder_status();
			if(Consts.ELONG_ORDER_FAIL.equals(order_status)&&"11".equals(orderInfo.getOut_fail_reason())){
				//超时未支付 更新为超时订单
				orderInfo.setOrder_status(Consts.ELONG_OUT_TIME);
			}
			orderDao.updateOrderInfo(orderInfo);
			if(cpMapList!=null){
				for(Map<String,String> cpInfo:cpMapList){
					cpInfo.put("order_id", orderInfo.getOrder_id());
					cpInfo.put("out_ticket_billno",  orderInfo.getOut_ticket_billno());
					orderDao.updateCpOrderInfo(cpInfo);
				}
			}
			DBNoticeVo notice=new DBNoticeVo();
			notice.setOrder_id(orderInfo.getOrder_id());
			notice.setChannel(Consts.CHANNEL_ELONG);
			/*if(Consts.ELONG_ORDER_SUCCESS.equals(order_status)){
				//出发出票结果通知
				notice.setOut_notify_status(Consts.NOTICE_START);
				noticeDao.updateNotice(notice);
			}*/
		/*	else if(Consts.ELONG_ORDER_MAKED.equals(order_status)){
				//同程触发 预订结果通知     预订成功
				String book_notify=noticeDao.selectBookNoticeStatus(orderInfo.getOrder_id());
				if(Consts.NOTICE_OVER.equals(book_notify)){
					//区分二次预订通知以后 不更新通知表
				}else{
					notice.setBook_notify_status(Consts.NOTICE_START);
					noticeDao.updateNotice(notice);
				}
			}*/
			if(Consts.ELONG_ORDER_FAIL.equals(order_status)){
				if(orderInfo.getOrder_status().equals(Consts.ELONG_OUT_TIME)){
					log.setContent("超时订单不给予出票通知"+orderInfo.getOrder_id());
					elongOrderDao.insertElongOrderLogs(log);
				}else{
					//区分预订失败通知   出票失败通知
					String book_notify=noticeDao.selectBookNoticeStatus(orderInfo.getOrder_id());
					if(Consts.NOTICE_OVER.equals(book_notify)){
						notice.setOut_notify_status(Consts.NOTICE_START);
						noticeDao.updateNotice(notice);
					}else{
						notice.setBook_notify_status(Consts.NOTICE_START);
						noticeDao.updateNotice(notice);
					}
				}
			}else{
				logger.info("获取同程渠道的出票结果通知,非法订单状态order_status:"+order_status);
			}
			log.setContent("出票系统返回结果_"+(Consts.ELONG_ORDER_MAKED.equals(status)?"预订成功":
				Consts.ELONG_ORDER_SUCCESS.equals(status)?"出票成功":
					Consts.ELONG_ORDER_FAIL.equals(status)?("出票失败[原因:"+TongChengConsts.getErrorInfo(orderInfo.getOut_fail_reason())+",乘客错误信息:"+orderInfo.getPassenger_reason()):
						"成功")+"");
		
		}else if (Consts.CHANNEL_TONGCHENG.equals(channel)){
			String order_status=orderInfo.getOrder_status();
			if(Consts.ELONG_ORDER_FAIL.equals(order_status)&&"11".equals(orderInfo.getOut_fail_reason())){
				//超时未支付 更新为超时订单
				orderInfo.setOrder_status(Consts.ELONG_OUT_TIME);
			}
			if(Consts.ELONG_ORDER_FAIL.equals(order_status)&&"6".equals(orderInfo.getOut_fail_reason())){
				//用户取消 更新为取消成功订单 
				orderInfo.setOrder_status(Consts.ELONG_ORDER_CANCELED);
			}
			
			orderDao.updateOrderInfo(orderInfo);
			if(cpMapList!=null){
				for(Map<String,String> cpInfo:cpMapList){
					cpInfo.put("order_id", orderInfo.getOrder_id());
					cpInfo.put("out_ticket_billno",  orderInfo.getOut_ticket_billno());
					orderDao.updateCpOrderInfo(cpInfo);
				}
			}
			DBNoticeVo notice=new DBNoticeVo();
			notice.setOrder_id(orderInfo.getOrder_id());
			notice.setChannel(Consts.CHANNEL_TONGCHENG);
			/*if(Consts.ELONG_ORDER_SUCCESS.equals(order_status)){
				//同程触发 出票结果 确定通知
				//出票系统 预订成功时间
				//Map<String, Object> outOrderInfo=orderDao.queryOutTicketOrderInfo(orderInfo.getOrder_id());
				//String bookTime=outOrderInfo.get("out_ticket_time")+"";
				Date now=new Date();
				
				Map<String, Object>  allChannelNotify=orderDao.queryAllChannelNotify(orderInfo.getOrder_id());
				if(allChannelNotify!=null){
					String book_notify_finish_time=allChannelNotify.get("book_notify_finish_time")+"";
					long minutes=DateUtil.minuteDiff(now, DateUtil.stringToDate(book_notify_finish_time, "yyyy-MM-dd HH:mm:ss"));
					if(minutes>45){
						logger.info("预订成功通知时间:"+book_notify_finish_time+",当前时间:"+ DateUtil.dateToString(now, "yyyy-MM-dd HH:mm:ss"));
						log.setContent("检测到预订时间到出票成功时间大于45分钟,启用补单通知:"+orderInfo.getOrder_id());
						elongOrderDao.insertElongOrderLogs(log);
						noticeDao.insertRemedyNotice(orderInfo.getOrder_id());
					}else{
						notice.setOut_notify_status(Consts.NOTICE_START);
						noticeDao.updateNotice(notice);
					}
				}else{
					notice.setOut_notify_status(Consts.NOTICE_START);
					noticeDao.updateNotice(notice);
				}
				//区分 超时支付重预订
			}*/
			/*else if(Consts.ELONG_ORDER_MAKED.equals(order_status)){
				//同程触发 预订结果通知     预订成功
				String book_notify=noticeDao.selectBookNoticeStatus(orderInfo.getOrder_id());
				if(Consts.NOTICE_OVER.equals(book_notify)){
					//区分二次预订通知以后 不更新通知表
				}else{
					notice.setBook_notify_status(Consts.NOTICE_START);
					noticeDao.updateNotice(notice);
				}
			}*/
			if(Consts.ELONG_ORDER_FAIL.equals(order_status)){
				if(orderInfo.getOrder_status().equals(Consts.ELONG_OUT_TIME)){
					log.setContent("超时订单不给予出票通知"+orderInfo.getOrder_id());
					elongOrderDao.insertElongOrderLogs(log);
				}else if(orderInfo.getOrder_status().equals(Consts.ELONG_ORDER_CANCELED)){
					log.setContent("失败原因为[用户取消],启用取消回调"+orderInfo.getOrder_id());
					elongOrderDao.insertElongOrderLogs(log);
				}else{
					//区分预订失败通知   出票失败通知
					String book_notify=noticeDao.selectBookNoticeStatus(orderInfo.getOrder_id());
					if(Consts.NOTICE_OVER.equals(book_notify)){
						//出票失败通知  区分超时补单通知 正常通知
						notice.setOut_notify_status(Consts.NOTICE_START);
						noticeDao.updateNotice(notice);
					}else{
						notice.setBook_notify_status(Consts.NOTICE_START);
						noticeDao.updateNotice(notice);
					}
				}
			}else{
				logger.info("获取同程渠道的出票结果通知,非法订单状态order_status:"+order_status);
			}
			log.setContent("出票系统返回结果_"+(Consts.ELONG_ORDER_MAKED.equals(status)?"预订成功":
				Consts.ELONG_ORDER_SUCCESS.equals(status)?"出票成功":
					Consts.ELONG_ORDER_FAIL.equals(status)?("出票失败[原因:"+TongChengConsts.getErrorInfo(orderInfo.getOut_fail_reason())+",乘客错误信息:"+orderInfo.getPassenger_reason()):
						"成功")+"");
		}
		
		/**插入订单操作 日记*/
		elongOrderDao.insertElongOrderLogs(log);
	}



	@Override
	public String cancel(String orderId) {
		String notify_cp_cancel_url=Consts.NOTIFY_CP_CANCEL_URL;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_id", orderId);
		String params;
		try {
			params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
			String result = HttpUtil.sendByPost(notify_cp_cancel_url, params, "UTF-8");
			logger.info("请求出票系统取消订单:"+orderId+"请求返回结果:" + result);
			
			if(!StringUtils.isEmpty(result)){
				if("success".equalsIgnoreCase(result)){//通知成功
					Map<String,String> map = new HashMap<String, String>();
			        //通知出票系统成功则订单状态修改为正在出票  
			        map.put("order_id", orderId);
			        map.put("old_order_status1", Consts.ELONG_ORDER_MAKED);//预订成功
			        map.put("order_status", Consts.ELONG_ORDER_CANCELED);//取消成功
			        orderDao.updateOrderStatus(map);	
				    logger.info("请求出票系统取消订单,order_id=" + orderId);
				    return "SUCCESS";
				}else{//超时重发
					 logger.info("请求出票系统取消订单,order_id=" + orderId+"result:"+result);
					return "FAIL";
				}
			}else{
				logger.info("请求出票系统取消订单,order_id=" + orderId);
				return "FAIL";
			}
		} catch (Exception e) {
			logger.info("请求出票系统取消订单系统异常"+e);
			e.printStackTrace();
			return "EXCEPTION";
		}
	}

	@Override
	public String pay(String orderId, String payMoney) {
		String notify_cp_PAY_url=Consts.NOTIFY_CP_PAY_URL;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_id", orderId);
		paramMap.put("pay_money", payMoney);//车票总价
		String params;
		try {
			params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
			String result = HttpUtil.sendByPost(notify_cp_PAY_url, params, "UTF-8");
			logger.info("请求出票系统开始支付:"+orderId+"请求返回结果:" + result);
			
			if(!StringUtils.isEmpty(result)){
				if("success".equalsIgnoreCase(result) ){//通知成功
					Map<String,String> map = new HashMap<String, String>();
			        //通知出票系统成功则订单状态修改为正在出票  
			        map.put("order_id", orderId);
			        map.put("old_order_status1", Consts.ELONG_ORDER_MAKED);//预订成功
			        map.put("order_status", Consts.ELONG_ORDER_WAITPAY);//支付中
			        orderDao.updateOrderStatus(map);	
			        orderDao.updateOrderPayMoney(paramMap);
			        logger.info("请求出票系统开始支付,order_id=" + orderId);
				    return "SUCCESS";
				}else{//超时重发
					logger.info("请求出票系统开始支付,order_id=" + orderId+"result:"+result);
					return "FAIL";
				}
			}else{
				logger.info("请求出票系统开始支付,order_id=" + orderId);
				return "FAIL";
			}
		} catch (Exception e) {
			logger.info("请求出票系统开始支付系统异常"+e);
			e.printStackTrace();
			return "EXCEPTION";
		}
	}
	




@Override
public String queryRefundStatus(Map<String, String> paramMap) {
	return orderDao.queryRefundStatus(paramMap);
}



@Override
public void insertRefundOrder(Map<String, String> paramMap) {
	orderDao.insertRefundOrder(paramMap);
}


	/**
	 * 同程补单通知
	 * 
	 * */
	@Override
	public void addOrderRemedyNotice(DBRemedyNoticeVo vo) {
		String order_id=vo.getOrder_id();
		DBOrderInfo orderInfo=this.queryOrderInfo(order_id);
		String partnerid=Consts.TC_PARTNERID;
		String key=Consts.TC_SIGNKEY;
		String tc_remedy_notify_url=Consts.TC_REMEDY_NOTIFY_URL;
		String reqtime=DateUtil.dateToString(new Date(),"yyyyMMddHHmmss");
		String method="";
		String sign=ElongMd5Util.md5_32(partnerid+method+reqtime+(ElongMd5Util.md5_32(key, "UTF-8").toLowerCase()),"UTF-8").toLowerCase();
		boolean remedysuccess=Consts.ELONG_ORDER_SUCCESS.equals(orderInfo.getOrder_status())?true:false;
		/*reqtime	string	1~16	请求时间，格式：
		yyyyMMddHHmmss（非空）例：
		20140101093518
		sign	string	1~32	数字签名
		=md5(partnerid+method+reqti me+md5(key))，其中 key 即是由 开放平台分配给同程的。md5 算 法得到的字符串全部为小写
		orderid	1~32	string	同程原订单号
		transactionid	1~32	string	原交易单号
		remedysuccess	1~8	bool	补单是否成功
		
		remedyfailmsg	1~128	string	补单失败原因（失败才有值）
		orderamount	1~8	string	订单总金额（成功才有值）
		newordernumber	1~16	string	新的取票单号（成功才有值）
		checi	1~8	string	车次
		from_station_code	1~8	string	出发站简码
		from_station_name	1~16	string	出发站名称
		to_station_code	1~8	string	到达站简码
		to_station_name	1~16	string	到达站名称
		train_date	1~16	string	乘车日期
		start_time	1~8	string	从出发站开车时间
		arrive_time	1~8	string	抵达目的站的时间
		runtime	1~8	string	运行时间
		passengers	1~1024	string	补单成功了里面的 cxin 和 price 参
		数会有值。内容详见附录 5.1。
		 */
		try {
			JSONObject json=new JSONObject();
			json.put("reqtime", reqtime);
			json.put("sign", sign);
			json.put("orderid", orderInfo.getOrder_id());
			json.put("transactionid", orderInfo.getOrder_id());
			json.put("remedysuccess", remedysuccess);
			json.put("orderamount", remedysuccess?orderInfo.getBuy_money():"");
			json.put("newordernumber", remedysuccess?orderInfo.getOut_ticket_billno():"");
			json.put("checi",orderInfo.getTrain_no());
			String[] exts=orderInfo.getExt_field2().split("\\|");
			String from_station_code=exts[0];
			String to_station_code=exts[1];
			json.put("from_station_code", from_station_code);
			json.put("from_station_name", orderInfo.getFrom_city());
			json.put("to_station_code", to_station_code);
			json.put("to_station_name",orderInfo.getTo_city());
			json.put("train_date",orderInfo.getTravel_date());
			
			if(remedysuccess){
				String start_time=orderInfo.getFrom_time();
				String arrive_time=orderInfo.getTo_time();
				
				
				
				if(arrive_time==null){
					//针对没有to_time采用查询补全
					Map<String,String> paramMap=new HashMap<String, String>();
					String url=Consts.QUERY_TICKET;
					paramMap.put("channel", "tongcheng");
					paramMap.put("from_station", orderInfo.getFrom_city());
					paramMap.put("arrive_station", orderInfo.getTo_city());
					paramMap.put("travel_time", orderInfo.getTravel_date());
					paramMap.put("purpose_codes","ADULT");
					paramMap.put("isNotZW", "yes");//非中文查询
					String params;
					params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
					logger.info("同程发起余票查询"+paramMap.toString()+"url"+url);
					String result = HttpUtil.sendByPost(url, params, "UTF-8");
					
					String start_query_time=null;
					String arrive_query_time=null;
					String runtime=null;
					if(result==null||result.equalsIgnoreCase("STATION_ERROR")||result.equalsIgnoreCase("ERROR")){
						logger.info("同程book通知,针对没有to_time采用查询补全_查询失败");
						throw new RuntimeException("to_time采用查询补全_查询失败");
					}else{
						JSONArray arr=JSONArray.fromObject(result);
						int index=arr.size();
						for(int i=0;i<index;i++){
							if(arr.getJSONObject(i).get("train_code").equals(orderInfo.getTrain_no())){
								runtime=arr.getJSONObject(i).getString("run_time_minute");
								start_query_time=arr.getJSONObject(i).getString("start_time");
								arrive_query_time=arr.getJSONObject(i).getString("arrive_time");
								break;
							}
						
						}
						if(start_query_time==null||arrive_query_time==null||runtime==null){
							logger.info("同程book通知,针对没有to_time采用查询补全_查询失败,未匹配到结果");
							throw new RuntimeException("to_time采用查询补全_查询失败");
						}else{
							logger.info("同程book通知,针对没有to_time采用查询补全_查询成功"+start_query_time+"_"+arrive_query_time+"_"+runtime);
							json.put("start_time",start_query_time);
							json.put("arrive_time",arrive_query_time);
							json.put("runtime", runtime);//运行时间
						}
					}
				
				}else{
					String runtime=DateUtil.minuteDiff(DateUtil.stringToDate(arrive_time, "yyyy-MM-dd HH:mm:ss"),DateUtil.stringToDate(start_time, "yyyy-MM-dd HH:mm:ss"))+"";
					start_time=start_time.substring(start_time.indexOf(" ")+1, start_time.indexOf(" ")+6);
					arrive_time=arrive_time.substring(arrive_time.indexOf(" ")+1, arrive_time.indexOf(" ")+6);
					/*json.put("start_time",start_time==null?"":start_time);
					json.put("arrive_time",arrive_time==null?"":arrive_time);*/
					json.put("runtime", runtime);
					
					String start_time_str=	DateUtil.dateToString(DateUtil.stringToDate(orderInfo.getFrom_time(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
					String arrive_time_str=	DateUtil.dateToString(DateUtil.stringToDate(orderInfo.getTo_time(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
					
					json.put("start_time",start_time_str);
					json.put("arrive_time",arrive_time_str);
				}
				json.put("remedyfailmsg", "");
			}else{
				json.put("start_time","");
				json.put("arrive_time","");
				json.put("runtime", "");
				json.put("remedyfailmsg", "");
			}
			List<DBPassengerInfo> cpInfoList=orderInfo.getPassengers();
			JSONArray parr=new JSONArray();
			for(DBPassengerInfo p:cpInfoList){
				/*passengerid	int	乘客的顺序号
				ticket_no	string	票号（此票在本订单中的唯一标识，订票/补单成功后才
				有值）
				passengersename	string	乘客姓名
				passportseno	string	乘客证件号码
				passporttypeseid	string	证件类型 ID
				不名称对应关系:
				1:二代身份证，2:一代身份证，C:港澳通行证，G:台湾通 行证，B:护照
				passporttypeseidname	string	证件类型名称
				piaotype	string	票种 ID。
				不票种名称对应关系：
				1:成人票，2:儿童票，3:学生票，4:残军票
				piaotypename	string	票种名称
				zwcode	string	座位编码。
				与座位名称对应关系：
				9:商务座，P:特等座，M:一等座，O:二等座，6:高级软卧，
				4:软卧，3:硬卧，2:软座，1:硬座 注意：当最低的一种座位，无票时，购买选择该座位种类， 买下的就是无座(也就说买无座的席别编码就是该车次的 最低席别的编码)，另外，当最低席别的票卖完了的时候 才可以卖无座的票。
				zwname	string	座位名称
				cxin	string	几车厢几座（在订票成功后才会有值，
				如：‘15车厢，20号上铺’）
				price	string	票价*/


				JSONObject p_json=new JSONObject();
				String seat_no=p.getSeat_no();
				String train_box=p.getTrain_box();
				//String cp_id=p.getCp_id();
				p_json.put("passengerid",p.getOut_passengerid());
				p_json.put("ticket_no",  remedysuccess?p.getCp_id():"");
				p_json.put("passengersename",p.getUser_name());
				p_json.put("passportseno",p.getUser_ids());
				
				p_json.put("passporttypeseid", p.getElong_ids_type());
				p_json.put("passporttypeseidname",TongChengConsts.getPassporttypeseidname(p.getElong_ids_type()));
				p_json.put("piaotype", p.getElong_ticket_type());
				p_json.put("piaotypename", TongChengConsts.getPiaotypename(p.getElong_ticket_type()));
				
				
				String zwcode=p.getElong_seat_type();
				if("M".equals(p.getElong_seat_type())){//一等座一等软座转换
					zwcode="23".equals(p.getSeat_type())?"7":"M";
				}else if("O".equals(p.getElong_seat_type())){//二等座二等软座转换
					zwcode="24".equals(p.getSeat_type())?"8":"O";
				}else if("4".equals(p.getElong_seat_type())){//软卧动卧转换
					zwcode="21".equals(p.getSeat_type())?"F":"4";
				}else if("6".equals(p.getElong_seat_type())){//高级动卧高级软卧转换
					zwcode="22".equals(p.getSeat_type())?"A":"6";
				}
				p_json.put("zwcode",zwcode);
				p_json.put("zwname",TongChengConsts.getZwname(zwcode));
				
				if(remedysuccess){
					p_json.put("cxin",remedysuccess?((StrUtil.isEmpty(train_box)?"":(train_box+"车厢,"))+(StrUtil.isEmpty(seat_no)?"无座":seat_no)):"");
					p_json.put("price",remedysuccess?p.getBuy_money():"");
				}else{
					p_json.put("cxin","");
					p_json.put("price","");
					
				}
				parr.add(p_json);
			}
			json.put("passengers", parr.toString());
			Map<String,String> params=new HashMap<String,String>();
			params.put("remedyinfo",json.toString());
			logger.info("同程补单通知,请求参数remedyinfo="+json.toString());
			String sendParams=ElongUrlFormatUtil.createGetUrl("", params, "utf-8");
			String result=HttpUtil.sendByPost(tc_remedy_notify_url, sendParams, "utf-8");
			if(result!=null){
				logger.info("同程补单通知,result:"+result);
				if("SUCCESS".equalsIgnoreCase(result)){
					logger.info("同程补单通知成功,order_id:"+order_id+",result:"+result);
					orderRemedySendUpdateNotice("SUCCESS",vo,result);
				}else{
					logger.info("同程补单通知失败,order_id:"+order_id+",result:"+result);
					orderRemedySendUpdateNotice("FAIL",vo,result);
				}
			}else{
				logger.info("同程补单通知失败,order_id:"+order_id+",result返回null");
				orderRemedySendUpdateNotice("FAIL",vo,result);
			}
		} catch (Exception e) {
			logger.info("同程补单通知异常,order_id:"+order_id+",msg"+e);
			orderRemedySendUpdateNotice("Exception",vo,"");
		}
		
		
		
	}

	private void orderRemedySendUpdateNotice(String noticeStatus,
			DBRemedyNoticeVo vo, String result) {
		String order_id=vo.getOrder_id();
		int num=vo.getNotify_num();
		
		DBRemedyNoticeVo noticeInfo=new DBRemedyNoticeVo();
		noticeInfo.setOrder_id(vo.getOrder_id());
		noticeInfo.setNotify_time("notice");
		noticeInfo.setNotify_num(num+1);
		/**更新订单 通知状态*/
		Map<String,String>orderNotice=new HashMap<String,String>();
		orderNotice.put("order_id", order_id);
		orderNotice.put("channel", "tongcheng");
		/** logsvo*/
		ElongOrderLogsVo log=null;
		if("SUCCESS".equals(noticeStatus)){
			noticeInfo.setNotify_finish_time("finished");
			noticeInfo.setNotify_status(Consts.NOTICE_OVER);
			log=new ElongOrderLogsVo(order_id,
					"同程补单结果通知_成功",
					"elong_app");
			orderNotice.put("notice_status", Consts.NOTICE_OVER);
		}else{
			String notify_status=num==(Consts.TCBUDANG_NOTICE_NUM-1)?Consts.NOTICE_FAIL:Consts.NOTICE_ING;
			noticeInfo.setNotify_status(notify_status);
			if(notify_status.equals(Consts.NOTICE_FAIL)){
				orderNotice.put("notice_status", Consts.NOTICE_FAIL);
			}else{
				orderNotice.put("notice_status", Consts.NOTICE_ING);
			}
			if("FAIL".equals(noticeStatus)){
				log=new ElongOrderLogsVo(order_id,
						"第"+(num+1)+"次同程补单结果通知_失败,返回信息["+result+"]"+",等待重新通知",
						"elong_app");
			}else{//异常处理
				log=new ElongOrderLogsVo(order_id,
						"第"+(num+1)+"次同程补单结果通知__异常,等待重新通知",
						"elong_app");
			}
		}
		
		noticeDao.updateRemedyNotice(noticeInfo);
		elongOrderDao.insertElongOrderLogs(log);
		//更新订单表的通知状态
		/**设置更新状态的单向性 00、准备通知 -->  11、开始通知 -->  22、通知完成 --> 33、通知失败 */
		orderDao.updateOrderNoticeStatus(orderNotice);
	}

	



	@Override
	public Map<String, Object> queryAllChannelNotify(String order_id) {
		return orderDao.queryAllChannelNotify(order_id);
	}



	@Override
	public String querySeatNo(Map<String, String> cpInfo) {
		return orderDao.querySeatNo(cpInfo);
	}



	@Override
	public List<String> queryChangeIdsByOrderId(String order_id) {
		return orderDao.queryChangeIdsByOrderId(order_id);
	}
	
	/**
	 * 获取身份信息冒用乘客姓名
	 * @param content
	 * @return
	 */
	private String getFakerLogUsername(String content) {
		content = content == null ? "" : content;
		String username = "";
		Pattern p1 = Pattern.compile("(?<=！【)[\u4E00-\u9FA5]+(?=的身份信息涉嫌被他人冒用)");
		Matcher m1 = p1.matcher(content);
		if(m1.find()) {
			username = m1.group();
		}
		if("".equals(username)) {
			//备用方案
			Pattern p2 = Pattern.compile("(?<=请您持本人和)[\u4E00-\u9FA5]+(?=的身份证件原件)");
			Matcher m2 = p2.matcher(content);
			if(m2.find()) {
				username = m2.group();
			}
		}
		if("".equals(username)) {
			//备用方案
			Pattern p3 = Pattern.compile("(?<=添加联系人)[\u4E00-\u9FA5]+(?=失败！)");
			Matcher m3 = p3.matcher(content);
			if(m3.find()) {
				username = m3.group();
			}
		}
		return username;
	}
	
	/**
	 * 获取使用账号集合
	 * @param logs
	 * @return
	 */
	private List<Map<String, String>> accountList(List<String> logs) {
		//预定车票分配的帐号ID：749746分配的帐号：LSX1986042179
		//预定车票分配的帐号ID：1082929分配的帐号：FLKpfqVlAI
		List<Map<String, String>> array = new ArrayList<Map<String,String>>();
		try {
			Pattern pid = Pattern.compile("(?<=预定车票分配的帐号ID：)\\d+");
			Pattern pname = Pattern.compile("(?<=分配的帐号：).+$");
			Matcher mid = null;
			Matcher mName = null;
			
			if(logs != null && logs.size() > 0) {
				for(int i = 0; i < logs.size(); i++) {
					Map<String, String> account = new HashMap<String, String>();
					String log = logs.get(i);
					if(i == logs.size() - 1) {
						//最后一个成功
						mName = pname.matcher(log);
						if(mName.find()) {
							account.put("accountname", mName.group());
						}
						account.put("accountstatusid", "2");
						account.put("accountstatusname", "可用");
					} else {
						//之前的失败
						mid = pid.matcher(log);
						Integer accountId = null;
						if(mid.find()) {
							try {
								accountId = Integer.valueOf(mid.group());
							} catch (NumberFormatException e) {
								logger.info("账号id解析错误");
								continue;
							}
						}
						Map<String, String> accountInfo = orderDao.queryUsedAccountInfo(accountId);
						String username = accountInfo.get("username");
						String status = accountInfo.get("status");
						String stop = accountInfo.get("stop");
						account.put("accountname", username);
						if(StringUtils.equals(status, "22")) {
							if(StringUtils.equals(stop, "2")) {
								account.put("accountstatusid", "4");
								account.put("accountstatusname", "该账号当天取消三次，不可使用");
							} else {
								account.put("accountstatusid", "5");
								account.put("accountstatusname", "其他");
							}
						} else if(StringUtils.equals(status, "66") || StringUtils.equals(status, "00")){
							account.put("accountstatusid", "2");
							account.put("accountstatusname", "可用");
						} else {
							account.put("accountstatusid", "5");
							account.put("accountstatusname", "其他");
						}
					}
					array.add(account);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("使用账号处理异常," + logs.toString());
		}
		
		return array;
	}
	
	private Long runtimeMinute(DBOrderInfo order, SInfo fromSInfo, SInfo toSInfo) {
		
		String travelDate = order.getTravel_date();//乘车日期
		
		Date fromDate = DateUtil.stringToDate(travelDate + " " + fromSInfo.getStartTime(), "yyyy-MM-dd HH:mm");
		
		Date toDate = DateUtil.stringToDate(travelDate + " " + toSInfo.getArriveTime(), "yyyy-MM-dd HH:mm");
		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTime(toDate);
		toCalendar.add(Calendar.DATE, toSInfo.getCost());
		
		toDate = toCalendar.getTime();
		Long diffMinute = DateUtil.minuteDiff(toDate, fromDate);
		return diffMinute;
	}
}
