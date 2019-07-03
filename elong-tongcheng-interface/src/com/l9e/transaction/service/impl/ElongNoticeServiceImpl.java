package com.l9e.transaction.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.common.Consts;
import com.l9e.transaction.dao.ElongNoticeDao;
import com.l9e.transaction.dao.ElongOrderDao;
import com.l9e.transaction.dao.TongChengChangeDao;
import com.l9e.transaction.service.ElongNoticeService;
import com.l9e.transaction.vo.DBPassengerChangeInfo;
import com.l9e.transaction.vo.ElongOrderInfoCp;
import com.l9e.transaction.vo.ElongOrderLogsVo;
import com.l9e.transaction.vo.ElongOrderNoticeVo;
import com.l9e.util.AmountUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.elong.ElongConsts;
import com.l9e.util.elong.ElongJsonUtil;
import com.l9e.util.elong.ElongMd5Util;
import com.l9e.util.elong.ElongUrlFormatUtil;
import com.l9e.util.elong.StrUtil;
@Service("elongNoticeService")
public class ElongNoticeServiceImpl implements ElongNoticeService{
	private static final Logger logger=Logger.getLogger(ElongNoticeServiceImpl.class);
	
	@Resource
	private ElongNoticeDao elongNoticeDao;
	
	@Resource
	private ElongOrderDao elongOrderDao;
	
	
	@Resource
	private TongChengChangeDao tongChengChangeDao;
	/**
	 * 出票结果通知
	 * 
	 * */
	public void addSendOrdersNotice(ElongOrderNoticeVo orderNoticeVo) {
		/**加载配置信息*/
		String merchantCode=Consts.ELONG_MERCHANTCODE;
		String sign_key =Consts.ELONG_SIGNKEY;
		String process_purchase_result=Consts.ELONG_PROCESSPURCHASERESULT_URL;
		String orderId=orderNoticeVo.getOrder_id();
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
				
				String sendParams=ElongUrlFormatUtil.createGetUrl("", params, "utf-8");
				String result=HttpUtil.sendByPost(process_purchase_result, sendParams, "utf-8");
				logger.info("出票结果通知-"+orderId+"[出票成功],通知返回结果"+result+"tickets"+tickets);
				JSONObject json=JSONObject.fromObject(result);
				String retCode=ElongJsonUtil.getString(json.get("retcode"));
				String retDesc=ElongJsonUtil.getString(json.get("retdesc"));
				if(retCode.equals("200")){
					noticeDbDo("SUCCESS",orderNoticeVo,orderId,retCode+":"+retDesc,orderInfo.get("order_status")+"");
				}else{
					/**通知失败的 加重新通知*/
					if(retCode.equals("444")){
						/**请求参数*/
						Map<String,String> map=getTicketsJson_pj(orderInfo,list);//
						params.put("tickets",map.get("tickets"));
						params.remove("sign");
						String sign_pj=ElongMd5Util.md5_32(params, sign_key, "utf-8");
						params.put("sign",sign_pj);
						String sendParams_pj=ElongUrlFormatUtil.createGetUrl("", params, "utf-8");
						String result_pj=HttpUtil.sendByPost(process_purchase_result, sendParams_pj, "utf-8");
						logger.info("出票结果通知-"+orderId+"[出票成功],通知返回结果_pj:"+result_pj+"tickets_pj:"+tickets);
						JSONObject json_pj=JSONObject.fromObject(result_pj);
						String retCode_pj=ElongJsonUtil.getString(json_pj.get("retcode"));
						String retDesc_pj=ElongJsonUtil.getString(json_pj.get("retdesc"));
						
						ElongOrderLogsVo log=new ElongOrderLogsVo(orderId,"针对金额错误的艺龙结果通知,启用均价通知,均价:"+map.get("price"),"elong_app");
						elongOrderDao.insertElongOrderLogs(log);
						
						if(retCode_pj.equals("200")){
							noticeDbDo("SUCCESS",orderNoticeVo,orderId,retCode_pj+":"+retDesc_pj,orderInfo.get("order_status")+"");
						}else{
							noticeDbDo("FAIL",orderNoticeVo,orderId,retCode_pj+":"+retDesc_pj,orderInfo.get("order_status")+"");
						}
					}else{
						noticeDbDo("FAIL",orderNoticeVo,orderId,retCode+":"+retDesc,orderInfo.get("order_status")+"");
					}
				}
			} catch (Exception e) {
				logger.info("出票结果通知-"+orderId+",发生异常"+e);
				noticeDbDo("EXCEPTION",orderNoticeVo,orderId,"exception:"+e,orderInfo.get("order_status")+"");
				e.printStackTrace();
			}
		}else if((Consts.ELONG_ORDER_FAIL.equals(orderInfo.get("order_status")+""))){
			try {
				logger.info("通知艺龙订单"+orderId+"出票失败");
				/**请求参数*/
				String reason=StrUtil.toString(orderInfo.get("out_fail_reason"));
				String passengerReason=StrUtil.toString(orderInfo.get("passenger_reason"));
				/*2016年4月22日 删掉，-caona 
				 * if("8".equals(reason)||"7".equals(reason)){
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
					log = log==null?"":log;
					String username = "";
					Pattern p1 = Pattern.compile("(?<=！【app出票失败，)[\u4E00-\u9FA5]+(?=联系人尚未通过身份信息核验)|(?<=！【)[\u4E00-\u9FA5]+(?=（)");
					Matcher m1 = p1.matcher(log);
					if(m1.find()) {
						username = m1.group();
					}
					failReasonDesc = username+"_乘客身份信息核验失败";
					reason = "6";
				}else if(Consts.OUT_FAIL_REASON_10.equals(reason)){
					failReasonDesc = "限制高消费";
				}else if(Consts.OUT_FAIL_REASON_12.equals(reason)){
					String log  = elongOrderDao.getFakerLogContentByOrderId(orderId);
					log = log==null?"":log;
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
				String tickets=getErrorTicketsJson(orderInfo,list,Consts.OUT_FAIL_REASON_3.equals(reason));//
				params.put("tickets",tickets);
				
				String sign=ElongMd5Util.md5_32(params, sign_key, "utf-8");
				params.put("sign",sign);
				
				String sendParams=ElongUrlFormatUtil.createGetUrl("", params, "utf-8");
				String result=HttpUtil.sendByPost(process_purchase_result, sendParams, "utf-8");
				logger.info("出票结果通知-"+orderId+"[出票失败:"+failReasonDesc+"],通知返回结果"+result);
				JSONObject json=JSONObject.fromObject(result);
				String retCode=ElongJsonUtil.getString(json.get("retcode"));
				String retDesc=ElongJsonUtil.getString(json.get("retdesc"));
				if(retCode.equals("200")){
					noticeDbDo("SUCCESS",orderNoticeVo,orderId,retCode+":"+retDesc,orderInfo.get("order_status")+"");
				}else{
					noticeDbDo("FAIL",orderNoticeVo,orderId,retCode+":"+retDesc,orderInfo.get("order_status")+"");
				}
			} catch (Exception e) {
				logger.info("出票结果通知-"+orderId+",发生异常"+e);
				noticeDbDo("EXCEPTION",orderNoticeVo,orderId,"exception:"+e,orderInfo.get("order_status")+"");
			}
		}
	}
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
					"elong_app");
			orderNotice.put("notice_status", Consts.NOTICE_OVER);
		}else{
			noticeInfo.put("out_notify_status",orderNoticeVo.getOut_notify_num()==(Consts.OUT_NOTICE_NUM-1)?Consts.NOTICE_FAIL:Consts.NOTICE_ING);
			orderNotice.put("notice_status", orderNoticeVo.getOut_notify_num()==(Consts.OUT_NOTICE_NUM-1)?Consts.NOTICE_FAIL:Consts.NOTICE_ING);
			if("FAIL".equals(noticeStatus)){
				log=new ElongOrderLogsVo(orderId,
						"第"+(orderNoticeVo.getOut_notify_num()+1)+"次通知艺龙出票结果_失败,返回信息["+returnInfo+"]"+",等待重新通知",
						"elong_app");
			}else{//异常处理
				log=new ElongOrderLogsVo(orderId,
						"第"+(orderNoticeVo.getOut_notify_num()+1)+"次通知艺龙出票结果_异常,等待重新通知",
						"elong_app");
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

	/**
	 * 退款结果通知
	 * 
	 * */
	public void sendRefundNotice(Map<String, Object> map) {
		String channel=map.get("channel")+"";
		
		logger.info("发起退票结果通知,channel:"+channel+"&cp_id:"+map.get("cp_id").toString()+",order_id:"+map.get("order_id").toString());
		
		if(Consts.CHANNEL_ELONG.equals(channel)){
			/**加载配置信息*/
			String merchantCode=Consts.ELONG_MERCHANTCODE;
			String sign_key =Consts.ELONG_SIGNKEY;
			String process_return_result=Consts.ELONG_PROCESSRETURNRESULT_URL;
			
			/**接口请求参数*/
			Map<String,String> params=new HashMap<String,String>();
			String order_id=map.get("order_id")+"";
			String cp_id=map.get("cp_id")+"";
			int notify_num=Integer.parseInt(map.get("notify_num")+"");
			
			params.put("merchantCode",merchantCode);
			params.put("orderId",order_id);
			params.put("orderItemId",cp_id);
			try {
				//11、退款完成 22、拒绝退款 
				String refund_status=map.get("refund_status")+"";
				logger.info("order_id:"+order_id+",cp_id:"+cp_id+",refund_status:"+refund_status+"退票结果通知开始");
				if(refund_status.equals("11")){//退款成功参数拼接
					params.put("result", "SUCCESS");
					params.put("amount",map.get("refund_money")+"");//实际退款金额
				}
				if(refund_status.equals("22")){////退款失败参数拼接
					params.put("result", "FAIL");
					String refuse_reason=map.get("refuse_reason")+"";
					params.put("failReason", refuse_reason);
					params.put("failReasonDesc", ElongConsts.getRefundReasonDesc(refuse_reason));
					params.put("comment", map.get("our_remark")+"");
				}
				String sign=ElongMd5Util.md5_32(params, sign_key, "utf-8");
				params.put("sign",sign);
				
				String sendParams=ElongUrlFormatUtil.createGetUrl("", params, "utf-8");
				String result=HttpUtil.sendByPost(process_return_result, sendParams, "utf-8");
				logger.info("艺龙退票结果通知-sendParams:"+sendParams+",result:"+result);
				JSONObject json=JSONObject.fromObject(result);
				String retCode=ElongJsonUtil.getString(json.get("retcode"));
				String retDesc=ElongJsonUtil.getString(json.get("retdesc"));
				if(retCode.equals("200")){
					updateRefundStatus(map,"SUCCESS",notify_num);
					elongOrderDao.updateRefundStream(map);
					/**logs*/
					String content="["+cp_id+"]退票结果通知_成功";
					ElongOrderLogsVo log=new ElongOrderLogsVo(order_id,content,"elong_app");
					elongOrderDao.insertElongOrderLogs(log);
				}else{
					updateRefundStatus(map,"",notify_num);
					elongOrderDao.updateRefundStream(map);
					
					/**logs*/
					String content="["+cp_id+"]退票结果通知_失败[retCode:"+retCode+" retDesc:"+retDesc+"]";
					ElongOrderLogsVo log=new ElongOrderLogsVo(order_id,content,"elong_app");
					elongOrderDao.insertElongOrderLogs(log);
				}
			} catch (Exception e) {
				logger.info("艺龙退票结果通知-异常:order_id:"+order_id+",cp_id:"+cp_id+",异常:"+e);
				/**logs*/
				e.printStackTrace();
				String exceptionInfo=(e+"").replaceAll("'", "").replaceAll("\"", "");
				String content="["+cp_id+"]退票结果通知_异常";
				ElongOrderLogsVo log=new ElongOrderLogsVo(order_id,content,"elong_app");
				elongOrderDao.insertElongOrderLogs(log);
				
				updateRefundStatus(map,"exception",notify_num);
				elongOrderDao.updateRefundStream(map);
			}
		}else if(Consts.CHANNEL_TONGCHENG.equals(channel)){
			/*{"returntype":1,"apiorderid":"TC_20140422084619369","trainorderid":"E216658533","reqtoken":"27172309c08e70a7b741dcc7e7432611","returntickets":[{"ticket_no":"E2610890401070051","passengername":"王二",
				"passporttypeseid":1,"passportseno":"421116198907143795","returnsuccess":true,"returnmoney":"20.05","returntime":"2014-02-13 15:00:05","returnfailid":"","returnfailmsg":""}…],"token":"afe976db398e9e01f9091e6e4c1032be","returnstate":true,"returnmoney":20.05,"returnmsg":"…"，
				"timestamp":1398148112,"sign":"2d2be73480ca44097f684be17fc338f5"}*/
			//11、退款完成 22、拒绝退款 
			int notify_num=Integer.parseInt(map.get("notify_num")+"");
			String url=map.get("callbackurl")+"";
			String partnerid=Consts.TC_PARTNERID;
			String key=Consts.TC_SIGNKEY;
			
			/**接口请求参数*/
			String order_id=map.get("order_id")+"";
			String cp_id=map.get("cp_id")+"";
			String refund_type=map.get("refund_type")+"";
			/*List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
			list=elongNoticeDao.getRefundNoticesListById(order_id);
			boolean isRefund=true;
			//System.out.println(list.size());
			if(list!=null&&list.size()>0){
				for(Map<String,Object> refund:list){
					String refund_status=refund.get("refund_status")+"";
				//System.out.println(refund_status+"+"+refund.get("cp_id"));
					if(!"11".equals(refund_status)&&!"22".equals(refund_status)){
						isRefund=false;
					}
				}
			}*/
			try {
				//String timestamp=(new Date().getTime()/1000)+"";
				String timestampStr;
				if(map.get("verify_time")==null){
					timestampStr=map.get("create_time")+"";
				}else{
					timestampStr=map.get("verify_time")+"";
				}
				String timestamp=(DateUtil.stringToDate(timestampStr, DateUtil.DATE_FMT3).getTime()/1000)+"";
				String token=ElongMd5Util.md5_32(order_id+cp_id+timestamp,"UTF-8").toLowerCase();
				
				String refund_status=map.get("refund_status")+"";
				
				//Map<String, Object> orderInfo=elongOrderDao.queryOrderInfo(order_id);
				//Map<String,Object>	cpInfo=elongNoticeDao.queryCpInfoById(cp_id);
				
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
				}else{
					returntype="1";//线上改签退票 信息查询
					
					trainorderid=orderInfo.get("out_ticket_billno")+"";

					DBPassengerChangeInfo cpInfo=tongChengChangeDao.selectChangePassengerByCpId(cp_id);
					passengername=cpInfo.getUser_name();
					passporttypeseid=cpInfo.getTc_ids_type();
					passportseno=cpInfo.getUser_ids();
					
				}
				
				json.put("returntype", returntype);
				json.put("apiorderid", order_id);
				json.put("trainorderid", trainorderid);
				json.put("reqtoken", map.get("reqtoken")+"");
				json.put("timestamp", timestamp);
				JSONArray arr=new JSONArray();
				JSONObject j1=new JSONObject();
				
				j1.put("ticket_no", cp_id);
				j1.put("passengername", passengername);
				j1.put("passporttypeseid", passporttypeseid);
				j1.put("passportseno",passportseno);
				String returnstate="";
				String returnmoney="";
				if(refund_status.equals("11")){//退款成功参数拼接
					json.put("returnstate", true);
					returnmoney=map.get("refund_money")+"";
					json.put("returnmoney", map.get("refund_money")+"");
					json.put("returnmsg", "");
					j1.put("returnsuccess", true);
					j1.put("returnmoney",map.get("refund_money")+"");//实际退款金额
					j1.put("returntime", DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
					j1.put("returnfailid", "");
					j1.put("returnfailmsg", "");
					
					returnstate="true";
					
				}
				if(refund_status.equals("22")){////退款失败参数拼接
					 //原因：1、已取票2、已过时间 3、来电取消     
					String refuse_reason=map.get("refuse_reason")+"";
					String returnfailid="";
					String returnfailmsg="";
					/*3	未查询到该车票
						9	退票操作异常，请与客服联系
						31	已改签
					32	已退票
					33	已出票，只能在窗口办理退票
					39	不可退票 
					40	订单所在账号被封
					41	无法在线退票_XXX地区处于旅游旺季，无法在线退票
					格式，比如：“无法在线退票_12306官方提示信息”

					*
					*/
					if("9".equals(refuse_reason)){
						returnfailid="9" ;
						returnfailmsg="退票操作异常,请与客服联系";
					}else if("31".equals(refuse_reason)){
						returnfailid="31";
						returnfailmsg="已改签";
					}else if("32".equals(refuse_reason)){
						returnfailid="32";
						returnfailmsg="不可退票";
					}else if("33".equals(refuse_reason)){
						returnfailid="33";
						returnfailmsg="已出票,只能在窗口办理退票";
					}else if("40".equals(refuse_reason)){
						returnfailid="40";
						returnfailmsg="订单所在账号被封";
					}else if("41".equals(refuse_reason)){
						returnfailid="41";
						returnfailmsg=map.get("our_remark")+"";
						if("".equals(returnfailmsg)){
							returnfailmsg="无法在线退票";
						}
					}else{
						returnfailid="9";
						returnfailmsg="退票操作异常,请与客服联系";
					}
					
					json.put("returnstate", false);
					returnmoney="";
					json.put("returnmoney", "");
					json.put("returnmsg", returnfailmsg);
					
					j1.put("returnsuccess", false);
					j1.put("returnmoney","");//实际退款金额
					j1.put("returntime","");
					
					
					j1.put("returnfailid",returnfailid);
					j1.put("returnfailmsg",returnfailmsg);
					
					returnstate="false";
				}
				
				arr.add(j1);
				json.put("returntickets",arr.toString());
				json.put("token", token);
				
				//System.out.println(json.toString());
				//md5(partnerid+returntype+timestamp+apiorderid+trainorderid+token+returnmoney+returnstate+md5(key))
				StringBuffer signParms=new StringBuffer();
				//=md5(partnerid+returntype+timestamp+apiorderid+trainorderid+token+returnmoney+returnstate+md5(key))
				signParms.append(partnerid).append(returntype).append(timestamp).append(order_id).append(trainorderid).append(token)
				.append(returnmoney)
				.append(returnstate+"").append(ElongMd5Util.md5_32(key,"utf-8").toLowerCase());
				
				
				//String sign=ElongMd5Util.md5_32(partnerid+"1"+timestamp+order_id+orderInfo.get("out_ticket_billno")+token+map.get("refund_money")+returnstate+ElongMd5Util.md5_32(key,"utf-8").toLowerCase(),"UTF-8").toLowerCase();
				String sign=ElongMd5Util.md5_32(signParms.toString(),"UTF-8").toLowerCase();
				logger.info("同程退票通知-signParms:"+signParms.toString());
				logger.info("timestamp:"+timestamp);
				json.put("sign", sign);
				
				Map<String,String> params=new HashMap<String,String>();
				params.put("data", json.toString());
				logger.info("同程退票通知-params:"+json.toString());
				String sendParams=ElongUrlFormatUtil.createGetUrl("", params, "utf-8");
				String result=HttpUtil.sendByPost(url, sendParams, "utf-8");
				logger.info("同程退票通知-result:"+result);
				if(result!=null&&"SUCCESS".equals(result)){
					updateRefundStatus(map,"SUCCESS",notify_num);
					elongOrderDao.updateRefundStream(map);
					/**logs*/
					String content="["+cp_id+"]退票结果通知_成功";
					ElongOrderLogsVo log=new ElongOrderLogsVo(order_id,content,"elong_app");
					elongOrderDao.insertElongOrderLogs(log);
				}else{
					updateRefundStatus(map,"",notify_num);
					elongOrderDao.updateRefundStream(map);
					
					/**logs*/
					String content="["+cp_id+"]退票结果通知_失败[result:"+result;
					ElongOrderLogsVo log=new ElongOrderLogsVo(order_id,content,"elong_app");
					elongOrderDao.insertElongOrderLogs(log);
				}
			} catch (Exception e) {
				logger.info("同程退票结果通知-异常:order_id:"+order_id+",cp_id:"+cp_id+",异常:"+e);
				/**logs*/
				e.printStackTrace();
				String content="["+cp_id+"]退票结果通知_异常";
				ElongOrderLogsVo log=new ElongOrderLogsVo(order_id,content,"elong_app");
				elongOrderDao.insertElongOrderLogs(log);
				
				updateRefundStatus(map,"exception",notify_num);
				elongOrderDao.updateRefundStream(map);
			}
			/*returntype	退票回调通知类型 0：表示线下退票 1：表示线上退票
			apiorderid	同程订单号
			trainorderid	火车票取票单号
			reqtoken	（唯一）退票回调特征值(1.当回调内容是客人在线申请退票的退款，该值为在调用退票请求API时，由同程传入；2.当回调内容是客人在线下车站退票的退款，该值由供应商分配。)
			注：当为线下退票时，此值为空
			returntickets	车票退票信息(json字符串数组形式，每张车票包含乘车人信息和退票相关信息，如：
			["ticket_no":" E2610890401070051","passengername":"王二","passporttypeseid":1,"passportseno":"421116198907143795","returnsuccess":true,"returnmoney":"20.05","returntime":"2014-02-13 15:00:05","returnfailid":"","returnfailmsg":""}] 
			注：当为线下退票时，此值为空 
			token	退票信息特征值  注：当为线下退票时，此值为空
			returnstate	退票状态 true:表示成功  false:表示退票失败  
			returnmoney	退款金额（成功需有值） 当为线上退票时，此值为退款总额
			returnmsg	退票后消息描述（当returnstate=false时，需显示退票失败原因等）
			timestamp	请求时间戳，形如：1398148112
			sign	线下退票数字签名
			=md5(partnerid+returntype+timestamp+apiorderid+trainorderid+returnmoney+returnstate+md5(key))
			线上退票数字签名
			=md5(partnerid+returntype+timestamp+apiorderid+trainorderid+token+returnmoney+returnstate+md5(key))
			其中partnerid为同程登录供应商开放平台的账户，key是供应商开放平台分配给同程使用的key。
			md5算法得到的字符串全部为小写*/

				
			/*}else{
				logger.info("同程退票结果通知-错误:order_id:"+order_id+"下有未处理完的退票请求");
			}*/
			
		}
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
	
	@Override
	public void sendOfflineRefund(Map<String, Object> map) {
		Object channel=map.get("channel");
		logger.info("发起线下退款通知,channel:"+channel+"&cp_id:"+map.get("cp_id").toString()+",order_id:"+map.get("order_id").toString());
		if(channel==null||Consts.CHANNEL_ELONG.equals(channel.toString())){
			String order_id=map.get("order_id").toString();
			String cp_id=map.get("cp_id").toString();
			String actual_refund_money=map.get("refund_money").toString();
			String refund_seq=map.get("refund_seq").toString();
			String comment=map.get("our_remark").toString();
			String stream_id=map.get("stream_id").toString();
			int notify_num=Integer.parseInt(map.get("notify_num").toString());
			
			String merchantCode=Consts.ELONG_MERCHANTCODE;
			String sign_key =Consts.ELONG_SIGNKEY;
			String process_refund=Consts.ELONG_PROCESSREFUND_URL;
			
			Map<String,String> params=new HashMap<String,String>();
			params.put("orderId", order_id);
			params.put("orderItemId", cp_id);
			params.put("merchantCode", merchantCode);
			params.put("tradeNo",refund_seq);
			params.put("amount", actual_refund_money);
			params.put("comment",comment);
			String sign=ElongMd5Util.md5_32(params, sign_key, "utf-8");
			params.put("sign", sign);
				try {
					String sendParams=ElongUrlFormatUtil.createGetUrl("", params, "utf-8");
					String result=HttpUtil.sendByPost(process_refund, sendParams, "utf-8");
					logger.info("艺龙线下退款-开始通知,order_id:"+order_id+"&cp_id:"+cp_id+",sendParams:"+sendParams+",result:"+result);
					JSONObject json=JSONObject.fromObject(result);
					String retCode=ElongJsonUtil.getString(json.get("retcode"));
					String retDesc=ElongJsonUtil.getString(json.get("retdesc"));
					if(retCode.equals("200")){
						logger.error("艺龙线下退款-成功,order_id:"+order_id+"&cp_id:"+cp_id);
						params.put("stream_id", stream_id);
						params.put("channel", Consts.CHANNEL_ELONG);
						updateOfflineNoticeStatus("SUCCESS","retCode:"+retCode+"retDesc:"+retDesc,notify_num,params,true);
					}else{
						logger.error("艺龙线下退款-失败,order_id:"+order_id+"&cp_id:"+cp_id+"&retCode:"+retCode+"retDesc:"+retDesc);
						params.put("stream_id", stream_id);
						params.put("channel", Consts.CHANNEL_ELONG);
						updateOfflineNoticeStatus("FAILL","retCode:"+retCode+"retDesc:"+retDesc,notify_num,params,true);
					}
				} catch (Exception e) {
					params.put("stream_id", stream_id);
					params.put("channel", Consts.CHANNEL_ELONG);
					updateOfflineNoticeStatus("Exception","异常",notify_num,params,true);
					logger.error("艺龙线下退款-异常:"+e);
					e.printStackTrace();
				}
		}else if(Consts.CHANNEL_TONGCHENG.equals(channel.toString())){
			int notify_num=Integer.parseInt(map.get("notify_num")+"");
			String url=Consts.TC_REFUND_NOTIFY_URL;
			String partnerid=Consts.TC_PARTNERID;
			String key=Consts.TC_SIGNKEY;
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
			params.put("channel", Consts.CHANNEL_TONGCHENG);
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
				if(cpInfo == null || cpInfo.isEmpty()) {
					/*改签票*/
					cpInfo = elongNoticeDao.queryAlterCpInfoById(cp_id);
				}
				JSONObject json=new JSONObject();
				json.put("returntype", returntype);
				json.put("apiorderid", order_id);
				json.put("trainorderid", orderInfo.get("out_ticket_billno")+"");
				String reqtoken="";
				if("44".equals(map.get("refund_type")+"")){
					reqtoken=map.get("reqtoken")+"";
				}else{
					reqtoken=map.get("refund_seq")+"";
				}
				json.put("reqtoken",reqtoken);
				json.put("token", "");
				
				json.put("returnstate", refund_status);
				json.put("returnmoney", refund_status?actual_refund_money:"");
				json.put("returnmsg", refund_status?"":"退票操作异常，请与客服联系");
				json.put("timestamp", timestamp);
				
				JSONArray arr=new JSONArray();
				JSONObject j1=new JSONObject();
				j1.put("ticket_no", cp_id);
				j1.put("passengername", cpInfo.get("user_name")+"");
				j1.put("passporttypeseid", cpInfo.get("elong_ids_type")+"");
				j1.put("passportseno",cpInfo.get("user_ids")+"");
				
				j1.put("returnsuccess",refund_status);
				j1.put("returnmoney",refund_status?actual_refund_money:"");
				j1.put("returntime",refund_status?DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"):"");
				j1.put("returnfailid",refund_status?"":"9");
				j1.put("returnfailmsg",refund_status?"":"退票操作异常，请与客服联系");
				
				arr.add(j1);
				json.put("returntickets",arr.toString());
				//md5(partnerid+returntype+timestamp+apiorderid+trainorderid+returnmoney+returnstate+md5(key))
				String sign=ElongMd5Util.md5_32(partnerid+returntype+timestamp+order_id+orderInfo.get("out_ticket_billno")+actual_refund_money+refund_status+ElongMd5Util.md5_32(key,"utf-8").toLowerCase(),"UTF-8").toLowerCase();
				json.put("sign", sign);
				Map<String,String> sendParams=new HashMap<String,String>();
				sendParams.put("data", json.toString());
				logger.info("同程线下退款-timestamp:"+timestamp+"params:"+ json.toString());
				String sendParamsStr=ElongUrlFormatUtil.createGetUrl("", sendParams, "utf-8");
				String result=HttpUtil.sendByPost(url, sendParamsStr, "utf-8");
				if(result!=null&&"SUCCESS".equalsIgnoreCase(result)){
					logger.error("同程线下退款-成功,order_id:"+order_id+"&cp_id:"+cp_id);
					updateOfflineNoticeStatus("SUCCESS",result,notify_num,params,refund_status);
				}else{
					logger.error("同程线下退款-失败,order_id:"+order_id+"&cp_id:"+cp_id+"&result:"+result);
					updateOfflineNoticeStatus("FAILL",result,notify_num,params,refund_status);
				}
			} catch (Exception e) {
				updateOfflineNoticeStatus("Exception","异常",notify_num,params,refund_status);
				logger.error("同程线下退款-异常:"+e);
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
		ElongOrderLogsVo log=new ElongOrderLogsVo(params.get("orderId"),context,"elong_app");
		elongOrderDao.insertElongOrderLogs(log);
		
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
