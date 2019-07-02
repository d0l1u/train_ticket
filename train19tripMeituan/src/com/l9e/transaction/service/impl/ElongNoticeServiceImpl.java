package com.l9e.transaction.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.common.Consts;
import com.l9e.common.ElongConsts;
import com.l9e.common.TongChengConsts;
import com.l9e.transaction.dao.ChangeDao;
import com.l9e.transaction.dao.ElongNoticeDao;
import com.l9e.transaction.dao.ElongOrderDao;
import com.l9e.transaction.service.ElongNoticeService;
import com.l9e.transaction.vo.ChangePassengerInfo;
import com.l9e.transaction.vo.ElongOrderInfoCp;
import com.l9e.transaction.vo.ElongOrderLogsVo;
import com.l9e.transaction.vo.ElongOrderNoticeVo;
import com.l9e.util.AmountUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.ElongMd5Util;
import com.l9e.util.ElongUrlFormatUtil;
import com.l9e.util.HttpPostJsonUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.StrUtil;
@Service("elongNoticeService")
public class ElongNoticeServiceImpl implements ElongNoticeService{
	private static final Logger logger=Logger.getLogger(ElongNoticeServiceImpl.class);
	
	@Resource
	private ElongNoticeDao elongNoticeDao;
	
	@Resource
	private ElongOrderDao elongOrderDao;
	@Resource 
	private ChangeDao changeDao;
	
	private String getErrorTicketsJson(Map<String, Object> orderInfo,
			List<ElongOrderInfoCp> list, boolean isPriceError) {
		JSONObject cpInfo=new JSONObject();
		//cpInfo.put("ticketNo", elongNoticeVo.getOut_ticket_billno());
		cpInfo.put("orderId", orderInfo.get("order_id"));
		if(isPriceError){
			cpInfo.put("ticketNo", orderInfo.get("out_ticket_billno"));
		}else{
			cpInfo.put("ticketNo", "");
		}
		String ext_field2= orderInfo.get("ext_field2").toString();
		JSONArray arr=new JSONArray();
	/*	"tickets":[{"orderItemId":"${orderItemId}",
			"seatType":"${seatTypeCode}",
			"seatNo":"${seatNo}",
			"price":2000,
			"passengerName":"${passengerName}",
			"certNo":"${certNo}","ticketType":"${ticketType}"}*/
		for(ElongOrderInfoCp cp:list){
			JSONObject json=new JSONObject();
			json.put("orderItemId", cp.getCp_id());
			if(isPriceError){
				json.put("seatType","".equals(ext_field2)?
						cp.getElong_seat_type():
							(seatTypeCheck(cp.getSeat_type(),cp.getElong_seat_type(),ext_field2)));
				String seat_no=cp.getSeat_no();
				String train_box=cp.getTrain_box();
				json.put("seatNo", (StrUtil.isEmpty(train_box)?"":(train_box+"车厢 "))+(StrUtil.isEmpty(seat_no)?"无座":seat_no));
				json.put("price", cp.getBuy_money());
			}else{
				json.put("seatType",cp.getElong_seat_type());
				json.put("seatNo","");
				json.put("price", "");
			}
			json.put("passengerName", cp.getUser_name());
			json.put("certNo", cp.getUser_ids());
			json.put("ticketType", cp.getElong_ticket_type());
			arr.add(json);
		}
		cpInfo.put("tickets", arr);
		return cpInfo.toString();
	}
	/**平均方式通知*/
	private Map<String,String> getTicketsJson_pj(Map<String, Object> orderInfo,
			List<ElongOrderInfoCp> list) {
		Map<String,String> map=new HashMap<String,String>();
		
		JSONObject cpInfo=new JSONObject();
		//cpInfo.put("ticketNo", elongNoticeVo.getOut_ticket_billno());
		cpInfo.put("orderId", orderInfo.get("order_id"));
		cpInfo.put("ticketNo", orderInfo.get("out_ticket_billno"));
		String ext_field2= orderInfo.get("ext_field2").toString();
		JSONArray arr=new JSONArray();
		double total_money = 0; 
		for(ElongOrderInfoCp cp:list){
			total_money += Double.valueOf(cp.getBuy_money());
		}
		String price=AmountUtil.divGiveDown(total_money, list.size())+"";
		for(ElongOrderInfoCp cp:list){
			JSONObject json=new JSONObject();
			json.put("orderItemId", cp.getCp_id());
			json.put("seatType","".equals(ext_field2)?
					cp.getElong_seat_type():
						(seatTypeCheck(cp.getSeat_type(),cp.getElong_seat_type(),ext_field2)));
			String seat_no=cp.getSeat_no();
			String train_box=cp.getTrain_box();
			json.put("seatNo", (StrUtil.isEmpty(train_box)?"":(train_box+"车厢 "))+(StrUtil.isEmpty(seat_no)?"无座":seat_no));
			json.put("price",price);
			json.put("passengerName", cp.getUser_name());
			json.put("certNo", cp.getUser_ids());
			json.put("ticketType", cp.getElong_ticket_type());
			arr.add(json);
		}
		cpInfo.put("tickets", arr);
		map.put("tickets", cpInfo.toString());
		map.put("price", price);
		return map;
	}
	
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
	
	private String seatTypeCheck(String seatType,String elong_seat_type,String ext_field1){
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
		}else if(ElongConsts.SEAT_TYPE_9.equals(seatType)){
			return elong_seat_type;
		}else if(ElongConsts.SEAT_TYPE_5.equals(seatType)){
			/**软卧|动卧 区分*/
			if(ElongConsts.ELONG_SEAT_TYPE_15.equals(elong_seat_type)
					||ext_field1.indexOf(ElongConsts.ELONG_SEAT_TYPE_15+",")>0){
				//一等软座
				return ElongConsts.ELONG_SEAT_TYPE_15;
			}else{
				return ElongConsts.ELONG_SEAT_TYPE_4;
			}
		}else{
			return ElongConsts.getElongSeatType(seatType);
		}
	}
	private void noticeDbDo(String noticeStatus, ElongOrderNoticeVo orderNoticeVo,
			String orderId,String returnInfo,String orderStatus) {
		orderStatus=orderStatus.equals(Consts.ELONG_ORDER_SUCCESS)?"成功":"失败";
		
		/**更新通知表参数*/
		Map<String,Object> noticeInfo=new HashMap<String,Object>();
		noticeInfo.put("order_id", orderId);
		noticeInfo.put("out_notify_time", "out_notify_time");
		noticeInfo.put("out_notify_num", orderNoticeVo.getOut_notify_num()+1);
		/** logsvo*/
		ElongOrderLogsVo log=null;
		/**更新订单表通知状态*/
		Map<String,String> orderNotice=new HashMap<String,String>();
		orderNotice.put("order_id", orderId);
		
		if("SUCCESS".equals(noticeStatus)){
			noticeInfo.put("out_notify_finish_time", "out_notify_finish_time");
			noticeInfo.put("out_notify_status", Consts.NOTICE_OVER);
			log=new ElongOrderLogsVo(orderId,
					"通知艺龙出票结果_成功",
					"meituan_app");
			orderNotice.put("notice_status", Consts.NOTICE_OVER);
		}else{
			noticeInfo.put("out_notify_status",orderNoticeVo.getOut_notify_num()==(Consts.OUT_NOTICE_NUM-1)?Consts.NOTICE_FAIL:Consts.NOTICE_ING);
			orderNotice.put("notice_status", orderNoticeVo.getOut_notify_num()==(Consts.OUT_NOTICE_NUM-1)?Consts.NOTICE_FAIL:Consts.NOTICE_ING);
			if("FAIL".equals(noticeStatus)){
				log=new ElongOrderLogsVo(orderId,
						"第"+(orderNoticeVo.getOut_notify_num()+1)+"次通知艺龙出票结果_失败,返回信息["+returnInfo+"]"+",等待重新通知",
						"meituan_app");
			}else{//异常处理
				log=new ElongOrderLogsVo(orderId,
						"第"+(orderNoticeVo.getOut_notify_num()+1)+"次通知艺龙出票结果_异常,等待重新通知",
						"meituan_app");
			}
		}
		elongNoticeDao.updateSysNotice(noticeInfo);
		elongOrderDao.insertElongOrderLogs(log);
		elongOrderDao.updateOrderNoticeStatus(orderNotice);
	}
	@Override
	public List<ElongOrderNoticeVo> getOrderNoticesList() {
		return elongNoticeDao.getOrderNoticesList();
	}
	@Override
	public List<Map<String, Object>> getRefundNoticesList() {
		return	elongNoticeDao.getRefundNoticesList();
	}

	
	
	private void updateRefundStatus(Map<String, Object> map,String status,int notify_num){
		map.put("notify_time",notify_num==0?"start":null);
		map.put("notify_num",(notify_num+1)+"");
		map.put("notify_status","SUCCESS".equals(status)?Consts.NOTICE_OVER:((notify_num+1==Consts.REFUND_NOTICE_NUM)?Consts.NOTICE_FAIL:Consts.NOTICE_ING));
		map.put("notify_finish_time", "finish");
		
	}
	@Override
	public List<Map<String,Object>> querySysNoticeList() {
		return elongNoticeDao.querySysNoticeList();
	}
	@Override
	public void updateSysNotice(Map<String, Object> sysNoticeInfo) {
		elongNoticeDao.updateSysNotice(sysNoticeInfo);
	}
	@Override
	public List<Map<String, Object>> getOfflineRefundRefundList() {
		return elongOrderDao.getOfflineRefundRefundList();
	}
	
	@Override
	public List<Map<String, Object>> getTcOfflineRefundRefundList() {
		return elongOrderDao.getTcOfflineRefundRefundList();
	}
	
	//美团线下退款和车站退票的退款结果通知
	@Override
	public void sendOfflineRefund(Map<String, Object> map) {
		Object channel=map.get("channel");
		logger.info("发起线下退款通知,channel:"+channel+"&cp_id:"+map.get("cp_id").toString()+",order_id:"+map.get("order_id").toString());
		if(Consts.CHANNEL_MEITUAN.equals(channel.toString())){
			int notify_num=Integer.parseInt(map.get("notify_num")+"");
//			String url=Consts.TC_REFUND_NOTIFY_URL;
//			String url = (String) map.get("callbackurl");
			//http://i.meituan.com/uts/train/106/returnticketNotify
			String url = Consts.MT_URL+"/106/returnticketNotify";
			String key=Consts.MT_KEY;
			String returntype="0";//退票回调通知类型 0：表示线下退票 1：表示线上退票
			
			String actual_refund_money=map.get("refund_money")==null?"":map.get("refund_money").toString();
			/**接口请求参数*/
			String order_id=map.get("order_id")+"";
			String cp_id=map.get("cp_id")+"";
			String stream_id=map.get("stream_id")+"";
			Map<String,String> params=new HashMap<String,String>();
			params.put("orderId", order_id);
			params.put("orderItemId", cp_id);
			params.put("stream_id", stream_id);
			params.put("channel", Consts.CHANNEL_MEITUAN);
			boolean refund_status=!"22".equals(map.get("refund_status"));
			
			try {
				//String timestamp=(new Date().getTime()/1000)+"";
				String timestampStr;
				if(map.get("verify_time")==null){
					timestampStr=map.get("create_time")+"";
				}else{
					timestampStr=map.get("verify_time")+"";
				}
				String timestamp=(DateUtil.stringToDate(timestampStr, DateUtil.DATE_FMT3).getTime()/1000)+"";
				
				//String token=ElongMd5Util.md5_32(order_id+cp_id+timestamp,"UTF-8").toLowerCase();
				//boolean refund_status=!"22".equals(map.get("refund_status"));
				Map<String, Object> orderInfo=elongOrderDao.queryOrderInfo(order_id);
				Map<String,Object>	cpInfo=elongNoticeDao.queryCpInfoById(cp_id);
				JSONObject json=new JSONObject();
				json.put("returntype", returntype);
				json.put("orderid", order_id);
				json.put("order_12306_serial", orderInfo.get("out_ticket_billno")+"");
				String reqtoken="";
				if("44".equals(map.get("refund_type")+"")){
					reqtoken=map.get("reqtoken")+"";
				}else{
					reqtoken=map.get("refund_seq")+"";
				}
				json.put("returnId", map.get("refund_seq"));
				json.put("req_token",reqtoken);
//				json.put("token", "");//退票信息特征值  数据类型为String
				
				json.put("return_state", refund_status);
				json.put("return_money", refund_status?actual_refund_money:"");
				json.put("return_msg", refund_status?"":"退票操作异常，请与客服联系");
				json.put("timestamp", timestamp);
				
				JSONArray arr=new JSONArray();
				JSONObject j1=new JSONObject();
				j1.put("ticket_no", cp_id);
				if(cpInfo!=null){
					j1.put("passenger_name", cpInfo.get("user_name")+"");
					j1.put("certificate_type", cpInfo.get("elong_ids_type")+"");
					j1.put("certificate_no",cpInfo.get("user_ids")+"");
				}else{
					//改签线下退票
					ChangePassengerInfo changeCp = elongNoticeDao.queryChangeCpInfoByNewCp(cp_id);
					if(changeCp!=null){
						j1.put("passenger_name", changeCp.getUser_name());
						j1.put("certificate_type", TongChengConsts.getTcIdsType(changeCp.getIds_type()));
						j1.put("certificate_no", changeCp.getUser_ids());
					}else{
						logger.info("美团通知线下退款，查询乘客为空，orderId"+order_id);
					}
				}
				j1.put("return_success",refund_status);
				j1.put("return_money",refund_status?actual_refund_money:"");
				j1.put("return_time",refund_status?DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"):"");
				j1.put("return_failid",refund_status?"":"9");
				j1.put("returnfail_msg",refund_status?"":"退票操作异常，请与客服联系");
				
				arr.add(j1);
				json.put("returntickets",arr.toString());
				//线下退票数字签名 数据类型为String=md5(returntype+timestamp+orderid+order_12306_serial+returnmoney+returnstate+md5(returnmoney)+key)
				String sign=ElongMd5Util.md5_32(returntype+timestamp+order_id+orderInfo.get("out_ticket_billno")+actual_refund_money+refund_status+ElongMd5Util.md5_32(actual_refund_money,"utf-8").toLowerCase()+key,"UTF-8").toLowerCase();
				json.put("sign", sign);
				Map<String,String> sendParams=new HashMap<String,String>();
				sendParams.put("data", json.toString());
				logger.info("美团线下退款-timestamp:"+timestamp+"params:"+ json.toString());
//				String sendParamsStr=ElongUrlFormatUtil.createGetUrl("", sendParams, "utf-8");
//				String result=HttpUtil.sendByPost(url, sendParamsStr, "utf-8");
				
				//post请求，传参用json格式
				String result = HttpPostJsonUtil.sendJsonPost(url, json.toString(), "utf-8");
//				logger.info("美团线下退票通知-result:"+result);
				if (result != null && StringUtils.isNotEmpty(result)) {
					JSONObject jsonRes = JSONObject.fromObject(result);
					//{"code":100,"msg":"处理或操作成功","success":true}
					if ("100".equals(jsonRes.get("code")) || jsonRes.getInt("code")==100 || jsonRes.getInt("code")==530 || (jsonRes.getBoolean("success"))) {// TODO 可以出票
						logger.error("美团线下退款-成功,order_id:"+order_id+"&cp_id:"+cp_id);
						updateOfflineNoticeStatus("SUCCESS",result,notify_num,params,refund_status);
					} else {
						logger.error("美团线下退款-失败,order_id:"+order_id+"&cp_id:"+cp_id+"&result:"+result);
						updateOfflineNoticeStatus("FAILL",result,notify_num,params,refund_status);
					}
				} else {
					logger.error("美团线下退款-失败,order_id:"+order_id+"&cp_id:"+cp_id+"&result:"+result);
					updateOfflineNoticeStatus("FAILL",result,notify_num,params,refund_status);
				}
				
				/*if(result!=null&&"SUCCESS".equalsIgnoreCase(result)){
					logger.error("美团线下退款-成功,order_id:"+order_id+"&cp_id:"+cp_id);
					updateOfflineNoticeStatus("SUCCESS",result,notify_num,params,refund_status);
				}else{
					logger.error("美团线下退款-失败,order_id:"+order_id+"&cp_id:"+cp_id+"&result:"+result);
					updateOfflineNoticeStatus("FAILL",result,notify_num,params,refund_status);
				}*/
			} catch (Exception e) {
				updateOfflineNoticeStatus("Exception","异常",notify_num,params,refund_status);
				logger.error("美团线下退款-异常:"+e);
				e.printStackTrace();
			}
		}
		
	}
	private void updateOfflineNoticeStatus(String status,String returnInfo,int notify_num,Map<String,String> params,boolean refund_status){
		Map<String,String> map=new HashMap<String,String>();
		map.put("stream_id", params.get("stream_id"));
		map.put("cp_id",params.get("orderItemId")); 
		map.put("order_id", params.get("orderId"));
		map.put("notify_start",notify_num==0?"start":null);
		map.put("notify_status","SUCCESS".equals(status)?Consts.NOTICE_OVER:((notify_num+1==Consts.REFUND_NOTICE_NUM)?Consts.NOTICE_FAIL:Consts.NOTICE_ING));
		map.put("notify_finish_time", "SUCCESS".equals(status)?"finish":null);
		if("SUCCESS".equals(status)&&refund_status){
			map.put("refund_status", "finish");
		}else{
			map.put("refund_status",null);
		}
		elongOrderDao.updateOfflineNoticeStatus(map);
		
		String context="渠道:"+params.get("channel")+"["+params.get("orderItemId")+"]线下退款通知"+("SUCCESS".equals(status)?"_成功":"FAILL".equals(status)?"失败":"异常")+" "+returnInfo;
		//日志操作
		ElongOrderLogsVo log=new ElongOrderLogsVo(params.get("orderId"),context,"meituan_app");
		elongOrderDao.insertElongOrderLogs(log);
		
	}
	
	
	/**
	 * 美团线上退款结果通知
	 * 
	 * */
	public void sendRefundNotice(Map<String, Object> map) {
		String channel=map.get("channel")+"";
		logger.info("发起线上退票结果通知,channel:"+channel+"&cp_id:"+map.get("cp_id").toString()+",order_id:"+map.get("order_id").toString());
		
		if(Consts.CHANNEL_MEITUAN.equals(channel)){
			//11、退款完成 22、拒绝退款 
			int notify_num=Integer.parseInt(map.get("notify_num")+"");
			String url=map.get("callbackurl")+"";
			String key=Consts.MT_KEY;
			
			/**接口请求参数*/
			String order_id=map.get("order_id")+"";
			String cp_id=map.get("cp_id")+"";
			String refund_type=map.get("refund_type")+"";
			try {
				//String timestamp=(new Date().getTime()/1000)+"";
				String timestampStr;
				if(map.get("verify_time")==null){
					timestampStr=map.get("create_time")+"";
				}else{
					timestampStr=map.get("verify_time")+"";
				}
				String timestamp=(DateUtil.stringToDate(timestampStr, DateUtil.DATE_FMT3).getTime()/1000)+"";
//				String token=ElongMd5Util.md5_32(order_id+cp_id+timestamp,"UTF-8").toLowerCase();
				
				String refund_status=map.get("refund_status")+"";
				
				JSONObject json=new JSONObject();
				String returntype="";
				String trainorderid="";
				String passengername="";
				String passporttypeseid="";
				String passportseno="";
				Map<String, Object> orderInfo=elongOrderDao.queryOrderInfo(order_id);
				trainorderid=orderInfo.get("out_ticket_billno")+"";
				if("11".equals(refund_type)){
					returntype="1";//线上退票 信息查询
					Map<String,Object>	cpInfo=elongNoticeDao.queryCpInfoById(cp_id);
					passengername=cpInfo.get("user_name")+"";
					passporttypeseid=cpInfo.get("elong_ids_type")+"";
					passportseno=cpInfo.get("user_ids")+"";
				}else if("55".equals(refund_type)){
					returntype="1";//线上改签退票 信息查询
					trainorderid=orderInfo.get("out_ticket_billno")+"";
					Map<String,Object> changeParam=new HashMap<String, Object>();
					changeParam.put("new_cp_id", cp_id);
					Map<String,Object> changeCpInfo = changeDao.queryChangeCpInfo(changeParam);
					passengername=changeCpInfo.get("user_name").toString();
					passporttypeseid="2";
					passportseno=changeCpInfo.get("user_ids").toString();
				}
				json.put("returnId",map.get("refund_seq"));
				json.put("returntype", returntype);
				json.put("orderid", order_id);
				json.put("order_12306_serial", trainorderid);
				json.put("req_token", map.get("reqtoken")+"");
				json.put("timestamp", timestamp);
				JSONArray arr=new JSONArray();
				JSONObject j1=new JSONObject();
				
				j1.put("ticket_no", cp_id);
				j1.put("passenger_name", passengername);
				j1.put("certificate_type", passporttypeseid);
				j1.put("certificate_no",passportseno);
				String returnstate="";
				String returnmoney="";
				if(refund_status.equals("11")){//退款成功参数拼接
					json.put("return_state", true);
					returnmoney=map.get("refund_money")+"";
					json.put("return_money", map.get("refund_money")+"");
					json.put("return_msg", "");
					j1.put("return_success", true);
					j1.put("return_money",map.get("refund_money")+"");//实际退款金额
					j1.put("return_time", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
					j1.put("return_failid", "");
					j1.put("returnfail_msg", "");
					
					returnstate="true";
				}
				
				if(refund_status.equals("22")){////退款失败参数拼接
					 //原因：1、已取票2、已过时间 3、来电取消     
					String refuse_reason=map.get("refuse_reason")+"";
					String returnfailid="";
					String returnfailmsg="";
//					同程：31、已改签 32、已退票 33、已出票，只能在窗口办理退票 3、未查询到该车票 9、退票操作异常，请与客服联系 40、订单所在账号被封 41、无法在线退票，xxx地区处于旅游旺季
//					美团：0退票操作成功 3未查询到该车票 9退票操作异常，请与客服联系 31已改签 32已退票 33已出票，只能在窗口办理退票 39不可退票
					if("0".equals(refuse_reason)){
						returnfailid="0" ;
						returnfailmsg="退票操作成功";
					}else if("3".equals(refuse_reason)){
						returnfailid="3";
						returnfailmsg="未查询到该车票";
					}else if("39".equals(refuse_reason)){
						returnfailid="39";
						returnfailmsg="不可退票";
					}else if("31".equals(refuse_reason)){
						returnfailid="31";
						returnfailmsg="已改签";
					}else if("32".equals(refuse_reason)){
						returnfailid="32";
						returnfailmsg="已退票";
					}else if("33".equals(refuse_reason)){
						returnfailid="33";
						returnfailmsg="已出票，只能在窗口办理退票";
					}else{
						returnfailid="9";
						returnfailmsg="退票操作异常，请与客服联系";
					}
					
					json.put("return_state", false);
					returnmoney="";
					json.put("return_money", "");
					json.put("return_msg", returnfailmsg);
					
					j1.put("return_success", false);
					j1.put("return_money","");//实际退款金额
					j1.put("return_time","");
					j1.put("return_failid",returnfailid);
					j1.put("returnfail_msg",returnfailmsg);
					
					returnstate="false";
				}
				arr.add(j1);
				json.put("returntickets",arr.toString());
//				json.put("token", token);
				
				//System.out.println(json.toString());
				//线上退票数字签名=md5(returntype+timestamp+orderid+order_12306_serial+returnmoney+returnstate+md5(returnmoney)+key)
				StringBuffer signParms=new StringBuffer();
				signParms.append(returntype).append(timestamp).append(order_id).append(trainorderid)
//				.append(token)
				.append(returnmoney).append(returnstate+"")
				.append(ElongMd5Util.md5_32(returnmoney,"utf-8").toLowerCase()).append(key);
				
				logger.info("美团退票通知-signParms:["+signParms.toString()+"]");
				String sign=ElongMd5Util.md5_32(signParms.toString(),"UTF-8").toLowerCase();
				logger.info("sign:"+sign);
				
				json.put("sign", sign);
				
				Map<String,String> params=new HashMap<String,String>();
				params.put("data", json.toString());
				logger.info("美团退票通知-params:"+json.toString());
//				String sendParams=ElongUrlFormatUtil.createGetUrl("", params, "utf-8");
//				String result=HttpUtil.sendByPost(url, sendParams, "utf-8");
				
				//post请求，传参用json格式
				String result = HttpPostJsonUtil.sendJsonPost(url, json.toString(), "utf-8");
				
				logger.info("美团退票通知-result:"+result);
				if (result != null && StringUtils.isNotEmpty(result)) {
					JSONObject jsonRes = JSONObject.fromObject(result);
					//{"code":100,"msg":"处理或操作成功","success":true}
					if ("100".equals(jsonRes.get("code")) || jsonRes.getInt("code")==100 || (jsonRes.getBoolean("success"))) {// TODO 可以出票
						updateRefundStatus(map,"SUCCESS",notify_num);
						elongOrderDao.updateRefundStream(map);
						/**logs*/
						String content="["+cp_id+"]退票结果通知_成功";
						ElongOrderLogsVo log=new ElongOrderLogsVo(order_id,content,"meituan_app");
						elongOrderDao.insertElongOrderLogs(log);
					} else {
						updateRefundStatus(map,"",notify_num);
						elongOrderDao.updateRefundStream(map);
						/**logs*/
						String content="["+cp_id+"]退票结果通知_失败[result:"+result;
						ElongOrderLogsVo log=new ElongOrderLogsVo(order_id,content,"meituan_app");
						elongOrderDao.insertElongOrderLogs(log);
					}
				} else {
					updateRefundStatus(map,"",notify_num);
					elongOrderDao.updateRefundStream(map);
					/**logs*/
					String content="["+cp_id+"]退票结果通知_失败[result:"+result;
					ElongOrderLogsVo log=new ElongOrderLogsVo(order_id,content,"meituan_app");
					elongOrderDao.insertElongOrderLogs(log);
				}
			} catch (Exception e) {
				logger.info("美团退票结果通知-异常:order_id:"+order_id+",cp_id:"+cp_id+",异常:"+e);
				/**logs*/
				e.printStackTrace();
				String content="["+cp_id+"]退票结果通知_异常";
				ElongOrderLogsVo log=new ElongOrderLogsVo(order_id,content,"meituan_app");
				elongOrderDao.insertElongOrderLogs(log);
				
				updateRefundStatus(map,"exception",notify_num);
				elongOrderDao.updateRefundStream(map);
			}
		}
	}
	
	public static void main(String[] args) {
		System.out.println((new Date().getTime()/1000)+"");
		System.out.println(AmountUtil.divGiveDown(100L, 6L));
		
		
		//tc12345611413783749048TC_20150120_test_0010TC_20150120_test_001048cb958945324b339a9d2c791f3f93310true4555a46a3ec7e3b3e15defb32b8f4bd0
		System.out.println(ElongMd5Util.md5_32("tc123456120150126141900TC_20150120_test_0010E2321234252bdd5edbfb1c9cd6f0ebed9f95db889.000true4555a46a3ec7e3b3e15defb32b8f4bd0","UTF-8").toLowerCase());
	}
	@Override
	public int updateNoticeBegin(ElongOrderNoticeVo orderNoticeVo) {
		return elongNoticeDao.updateNoticeBegin(orderNoticeVo);
	}
	@Override
	public void updateNoticeTime(ElongOrderNoticeVo orderNoticeVo) {
		elongNoticeDao.updateNoticeTime(orderNoticeVo);
	}
	@Override
	public int updateStartOfflineRefundNotice(Map<String,Object> map) {
		return elongNoticeDao.updateStartOfflineRefundNotice(map);
	}
	@Override
	public int updateBeginRefundNotice(Map<String, Object> map) {
		return elongNoticeDao.updateBeginRefundNotice(map);
	}
	@Override
	public int updateStartNoticeOutSys(Map<String, Object> map) {
		return  elongNoticeDao.updateStartNoticeOutSys(map);
	}
	
}
