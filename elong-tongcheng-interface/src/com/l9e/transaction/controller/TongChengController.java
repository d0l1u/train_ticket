package com.l9e.transaction.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.common.Consts;
import com.l9e.transaction.service.ElongOrderService;
import com.l9e.transaction.service.NoticeService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.TongChengChangeService;
import com.l9e.transaction.vo.DBChangeInfo;
import com.l9e.transaction.vo.DBNoticeVo;
import com.l9e.transaction.vo.DBOrderInfo;
import com.l9e.transaction.vo.DBPassengerChangeInfo;
import com.l9e.transaction.vo.DBPassengerInfo;
import com.l9e.transaction.vo.DBStudentInfo;
import com.l9e.transaction.vo.ElongOrderLogsVo;
import com.l9e.transaction.vo.TongchengChangeLogVO;
import com.l9e.util.AmountUtil;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.UrlFormatUtil;
import com.l9e.util.elong.ElongMd5Util;
import com.l9e.util.elong.StrUtil;
import com.l9e.util.tongcheng.TongChengConsts;


/**
 * 同城 主控制器
 * @author liuyi
 * 同城接口定义 所有票备选无座
 */
@Controller
@RequestMapping("/tc")
public class TongChengController extends BaseController{
	
	private static Logger logger= Logger.getLogger(TongChengController.class);
	
	@Resource
	private OrderService orderService;
	
	@Resource
	private ElongOrderService elongOrderService;
	
	@Resource
	private TongChengChangeService tongChengChangeService;
	
	@Resource
	private NoticeService noticeService;
	
	/**
	 * 同程催单
	 * @param request
	 * @param response
	 */
	@RequestMapping("/remindOrder.jhtml")
	public void tongChengRemindOrder(HttpServletRequest request, 
			HttpServletResponse response) {
		
		try {
			Map<String, String> parameterMap = requestParamMap(request);
			logger.info("同程催单传入参数：" + parameterMap.toString());
			
			String md5key = "kuyou19e";
			if(validateSign(parameterMap, md5key)) {
				String orderId = getParam(request, "orderNo");
				String method = getParam(request, "method");
				String durations = getParam(request, "durations");
				String times = getParam(request, "times");
				String urgency = getParam(request, "urgency");
				String channel = getParam(request, "channel");
				if(StrUtil.isEmpty(orderId) || StrUtil.isEmpty(method) || StrUtil.isEmpty(channel)) {
					logger.info("同程催单，必要参数缺失：order_id=" + orderId + " ,method=" + method + " ,channel=" + channel);
					write2Response(response, "failed");
					return;
				}
				
				/*处理接收到的催单数据*/
				
				write2Response(response, "succeed");
			} else {
				/*验签失败*/
				logger.info("同程催单验签失败，参数：" +parameterMap.toString());
				write2Response(response, "failed");
			}
		} catch (Exception e) {
			logger.info("同程催单接收异常!e:" + e.getMessage());
			write2Response(response, "failed");
		}
	}
	
	/**
	 * 同程短信对接
	 * @param request
	 * @param response
	 */
	@RequestMapping("/recieveMessage.jhtml")
	public void tongChengRecieveMessage(HttpServletRequest request, 
			HttpServletResponse response) {
		
		String jsonStr = getParam(request, "jsonStr");
		if(StrUtil.isEmpty(jsonStr)) {
			logger.info("同程短信接收jsonStr为空");
			write2Response(response, "false");
			return;
		}
		
		logger.info("同程短信接收jsonStr：" + jsonStr);
		/*解析json字符串*/
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode json = mapper.readTree(jsonStr);
			
			JsonNode srcNode = json.path("src");
			JsonNode destNode = json.path("dest");
			JsonNode messageNode = json.path("message");
			JsonNode recvtimeNode = json.path("recvtime");
			
			String src = srcNode.isMissingNode() ? "" : srcNode.getTextValue();
			String dest = destNode.isMissingNode() ? "" : destNode.getTextValue();
			String message = messageNode.isMissingNode() ? "" : messageNode.getTextValue();
			String recvtime = recvtimeNode.isMissingNode() ? "" : recvtimeNode.getTextValue();
			if(StrUtil.isEmpty(src) || StrUtil.isEmpty(dest) || 
					StrUtil.isEmpty(message) || StrUtil.isEmpty(recvtime)) {
				logger.info("接收短信jsonStr参数缺失,jsonStr:" + jsonStr);
				write2Response(response, "false");
			} else {
				logger.info("接收短信成功,jsonStr:" + jsonStr);
				
				/*处理接收到的数据*/
				
				write2Response(response, "true");
			}
		} catch (Exception e) {
			logger.info("短信接收异常, jsonStr: " + jsonStr + " ,e: " + e.getMessage());
			write2Response(response, "false");
			return;
		}
	}
	
	/**
	 * 同城请求主入口(火车票业务)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/main.jhtml")
	public void tongChengMain(HttpServletRequest request, 
			HttpServletResponse response){
		String sysPartnerid=Consts.TC_PARTNERID;
		/**返回json*/
		JSONObject json=new JSONObject();	
		try {
			String jsonStr=getParam(request, "jsonStr");
			logger.info("同城main-传入的 json"+jsonStr);
			if(StrUtil.isEmpty(jsonStr)){
				logger.info("同城main-传入的 json为空对象"+jsonStr);
				json.put("success", false);
				json.put("code", 101);
				json.put("msg", "传入的 json为空对象");
				printJson(response,json.toString());
				return;
			}
			JSONObject webJson=JSONObject.fromObject(jsonStr);
			String partnerid=getJsonValue(webJson,"partnerid");
			String method=getJsonValue(webJson,"method");
			String reqtime=getJsonValue(webJson,"reqtime");
			String sign=getJsonValue(webJson,"sign");
			logger.info("同城main-通用参数:partnerid,"+partnerid+"|method,"+method+"|reqtime,"+reqtime+"|sign,"+sign);
			if(StrUtil.isEmpty(partnerid)||StrUtil.isEmpty(method)||StrUtil.isEmpty(reqtime)||
					StrUtil.isEmpty(sign)){
				logger.info("同城main-通用参数缺失:partnerid,"+partnerid+"|method,"+method+"|reqtime,"+reqtime+"|sign,"+sign);
				json.put("success", false);
				json.put("code", 103);
				json.put("msg", "通用参数缺失");
				printJson(response,json.toString());
				return;
			}
//			logger.info("sysPartnerid," + sysPartnerid + "|!sysPartnerid.equals(partnerid)" + !sysPartnerid.equals(partnerid));
			if(!sysPartnerid.equals(partnerid)){
				json.put("success", false);
				json.put("code", 104);
				json.put("msg", "账户无效");
				printJson(response,json.toString());
				return;
			}
			String key=Consts.TC_SIGNKEY;
			String sysSign=ElongMd5Util.md5_32(partnerid+method+reqtime+(ElongMd5Util.md5_32(key, "UTF-8").toLowerCase()),"UTF-8").toLowerCase();
			if(sysSign.equals(sign)){
				if("query_money".equals(method)){
					queryMoney(webJson,request,response);
				}else if("train_query_unfinished_order_count".equals(method)){
					trainQueryUnfinishedOrderCount(webJson,request,response);
				}else if("train_query".equals(method)){
					trainQuery(webJson, request, response);
				}else if("train_query_remain".equals(method)){
					trainQueryRemain(webJson, request, response);
				}else if("train_order".equals(method)){
					trainOrder(webJson, request, response);
				}else if("train_confirm".equals(method)){
					trainConfirm(webJson, request, response);
				}else if("train_cancel".equals(method)){
					trainCancel(webJson, request, response);
				}else if("train_query_status".equals(method)){
					trainQueryStatus(webJson, request, response);
				}else if("train_query_info".equals(method)){
					trainQueryInfo(webJson, request, response);
				}else if("return_ticket".equals(method)){
					returnTicket(webJson, request, response);
				}else if("train_request_change".equals(method)){
					trainRequestChange(webJson, request, response);
				}else if("train_cancel_change".equals(method)){
					trainCancelChange(webJson, request, response);
				}else if("train_confirm_change".equals(method)){
					trainConfirmChange(webJson, request, response);
				}else if("get_train_info".equals(method)) {
					getTrainInfo(webJson, request, response);
				}else{
					logger.info("同城main-error非法的请求method:"+method);
					json.put("success", false);
					json.put("code",106);
					json.put("msg", "接口不存在");
					printJson(response,json.toString());
				}
			}else{
				logger.info("同城main-身份校验错误:sysSign,"+sysSign+"|sign,"+sign+"|param,"+partnerid+method+reqtime+(ElongMd5Util.md5_32(key, "UTF-8").toLowerCase()));
				json.put("success", false);
				json.put("code", 104);
				json.put("msg", "账户无效,身份校验错误");
				printJson(response,json.toString());
				return;
			}
		} catch (Exception e) {
			logger.info("同城main-请求异常:"+e);
			json.put("success", false);
			json.put("code", 102);
			json.put("msg", "传入的 json 格式错误");
			printJson(response,json.toString());
			e.printStackTrace();
		}
	}
	
	/*
	 * 业务接口方法区
	 */
	
	/**
	 * 查询账户余额 暂不支持
	 */
	private void queryMoney(JSONObject webJson,HttpServletRequest request, 
			HttpServletResponse response) { 
		logger.info("暂不支持查询账户余额 接口");
		JSONObject json=new JSONObject();	
		json.put("success", false);
		json.put("code",106);
		json.put("msg", "接口不存在");
		printJson(response,json.toString());
	} 
	

	/**
	 * 查询授权的未完成订单使用情况 暂不支持
	 */
	private void trainQueryUnfinishedOrderCount(JSONObject webJson,HttpServletRequest request, 
			HttpServletResponse response) { 
		logger.info("暂不支持查询授权的未完成订单使用情况接口");
		JSONObject json=new JSONObject();	
		json.put("success", false);
		json.put("code",106);
		json.put("msg", "接口不存在");
		printJson(response,json.toString());
	} 
	/**
	 * 车票预订查询
	 */
	private void trainQuery(JSONObject webJson,HttpServletRequest request, 
			HttpServletResponse response) { 
		String train_date=
			getJsonValue(webJson, "train_date");
		String from_station=
			getJsonValue(webJson, "from_station");
		String to_station=
			getJsonValue(webJson, "to_station");
		String purpose_codes=
			getJsonValue(webJson, "purpose_codes");
		if(train_date.equals("")||from_station.equals("")||to_station.equals("")||purpose_codes.equals("")){
			logger.info("参数为null");
		}else{
			try {
				/**分发参数*//*
		  		String channel=req.getParameter("channel");
		  		*//**业务参数*//*
		  		String from_station =req.getParameter("from_station");
		  		String arrive_station =req.getParameter("arrive_station");
		  		String travel_time =req.getParameter("travel_time");
		  		String purpose_codes=req.getParameter("purpose_codes");*/
				Map<String,String> paramMap=new HashMap<String, String>();
				String url=Consts.QUERY_PRICE;
				paramMap.put("channel", "tongcheng");
				paramMap.put("from_station", from_station);
				paramMap.put("arrive_station", to_station);
				paramMap.put("travel_time", train_date);
				paramMap.put("purpose_codes", purpose_codes);
				paramMap.put("isNotZW", "no");//非中文查询
				String params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
				logger.info("同程发起预订查询"+paramMap.toString()+"url"+url);
				String result = HttpUtil.sendByPost(url, params, "UTF-8");
				
				logger.info("同程发起预订查询result"+result);
				if(result==null||result.equalsIgnoreCase("STATION_ERROR")||result.equalsIgnoreCase("ERROR")){
					JSONObject jsonReturn=new JSONObject();	
					jsonReturn.put("success", false);
					jsonReturn.put("code",999);
					jsonReturn.put("msg", "查询失败");
					printJson(response,jsonReturn.toString());
				}else{
					JSONObject jsonReturn=new JSONObject();	
					jsonReturn.put("success", true);
					jsonReturn.put("code",100);
					jsonReturn.put("msg", "正常获得结果");
					jsonReturn.put("data", result);
					printJson(response,jsonReturn.toString());
				}
			}catch (Exception e) {
				e.printStackTrace();
				logger.info("同程发起预订查询,系统异常"+e);
				JSONObject json=new JSONObject();	
				json.put("success", false);
				json.put("code",500);
				json.put("msg", "系统异常");
				printJson(response,json.toString());
				e.printStackTrace();
			}
		}
		/*train_date	1~16	string	乘车日期（yyyy-MM-dd）
		from_station	1~16	string	出发站简码
		to_station	1~16	string	到达站简码
		purpose_codes	1~16	string	订票类别，如“ADULT”表示普通票*/

		
	} 
	/**
	 * 余票查询 
	 */
	private void trainQueryRemain(JSONObject webJson,HttpServletRequest request, 
			HttpServletResponse response) { 
		String train_date=
			getJsonValue(webJson, "train_date");
		String from_station=
			getJsonValue(webJson, "from_station");
		String to_station=
			getJsonValue(webJson, "to_station");
		String purpose_codes=
			getJsonValue(webJson, "purpose_codes");
		if(train_date.equals("")||from_station.equals("")||to_station.equals("")||purpose_codes.equals("")){
			logger.info("参数为null");
			JSONObject json=new JSONObject();	
			json.put("success", false);
			json.put("code",107);
			json.put("msg", "业务参数缺失");
			printJson(response,json.toString());
		}else{
			try {
				/**分发参数*//*
		  		String channel=req.getParameter("channel");
		  		*//**业务参数*//*
		  		String from_station =req.getParameter("from_station");
		  		String arrive_station =req.getParameter("arrive_station");
		  		String travel_time =req.getParameter("travel_time");
		  		String purpose_codes=req.getParameter("purpose_codes");*/
				Map<String,String> paramMap=new HashMap<String, String>();
				String url=Consts.QUERY_TICKET;
				paramMap.put("channel", "tongcheng");
				paramMap.put("from_station", from_station);
				paramMap.put("arrive_station", to_station);
				paramMap.put("travel_time", train_date);
				paramMap.put("purpose_codes", purpose_codes);
				paramMap.put("isNotZW", "no");//非中文查询
				String params;
				params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
				logger.info("同程发起余票查询"+paramMap.toString()+"url"+url);
				String result = HttpUtil.sendByPost(url, params, "UTF-8");
				
				logger.info("同程发起余票查询result"+result);
				
				if(result==null||result.equalsIgnoreCase("STATION_ERROR")||result.equalsIgnoreCase("ERROR")){
					JSONObject jsonReturn=new JSONObject();	
					jsonReturn.put("success", false);
					jsonReturn.put("code",999);
					jsonReturn.put("msg", "查询失败");
					printJson(response,jsonReturn.toString());
				}else{
					JSONObject jsonReturn=new JSONObject();	
					jsonReturn.put("success", true);
					jsonReturn.put("code",100);
					jsonReturn.put("msg", "正常获得结果");
					jsonReturn.put("data", result);
					printJson(response,jsonReturn.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("同程发起预订查询,系统异常"+e);
				JSONObject json=new JSONObject();	
				json.put("success", false);
				json.put("code",500);
				json.put("msg", "系统异常");
				printJson(response,json.toString());
				e.printStackTrace();
			}
		}
	} 
	/*
	 * 预留 频繁重复请求解决方案
	 * String key ="tongcheng"+ orderId;
	if(null == MemcachedUtil.getInstance().getAttribute(key)){
		boolean setAttr=MemcachedUtil.getInstance().setAttribute(key,System.currentTimeMillis(), 5*1000);
		logger.info("同城占位-缓存请求编号"+orderId+(setAttr?"成功":"失败"));
	}else{
		long startTime = (Long)MemcachedUtil.getInstance().getAttribute(key);
		returnJson.put("code", "111");
		returnJson.put("success", false);
		returnJson.put("msg", "");
		returnJson.put("tooltip", "请求过于频繁,请稍后再请求");
		printJson(response,returnJson.toString());
		logger.info("艺龙推送退款-5s内缓存检测到,重复提交:"+orderId+",间隔"+(System.currentTimeMillis()-startTime));
		return;
	}*/
	/**
	* 申请分配座位席别
	*/
	private void trainOrder(JSONObject webJson,HttpServletRequest request, 
			HttpServletResponse response){
		String orderId=webJson.getString("orderid");
		String transactionId = orderId;
		Object rtk=webJson.get("reqtoken");//回调请求
		Object cbu=webJson.get("callbackurl");//回调地址
		String tongcheng_get_order=this.queryElongSysValueByName("tongcheng_get_order",60*1000);
		if(rtk==null&&cbu==null){
			try {
				logger.info("同城占位-同步请求开始"+orderId);
				String orderStatus=orderService.queryOrderStatusByOrderId(orderId);
				if(orderStatus==null){
					long start =System.currentTimeMillis();
					//下单步骤、
					DBOrderInfo orderInfo=getOrderInfoFromTc(webJson,"synchronization");
					if("0".equals(tongcheng_get_order)&&"00".equals(orderInfo.getWait_for_order())){
						JSONObject json=new JSONObject();	
						json.put("success", false);
						json.put("code",506);
						json.put("msg", "12306系统错误");
						printJson(response,json.toString());
						logger.info("同城占位-同步接口关闭,order_id:"+orderId);
						return;
					}
					orderService.addOrder(orderInfo);
					logger.info("同城占位-下单,耗时"+(System.currentTimeMillis()-start)+"ms");
					String out=null;
					/*for(int i=0;i<2;i++){*/
					
					out=orderService.sendOutTicket(orderInfo,"book");
					/*	if(out!=null){
							break;
						}
					}*/
					if("SUCCESS".equals(out)){
						ElongOrderLogsVo log=new ElongOrderLogsVo();
						log.setOpt_person("elong_app");
						log.setContent("通知出票系统_成功,耗时"+(System.currentTimeMillis()-start)+"ms");
						log.setOrder_id(orderInfo.getOrder_id());
						logger.info("订单:"+orderInfo.getOrder_id()+"通知出票系统_成功,耗时"+(System.currentTimeMillis()-start)+"ms");
						elongOrderService.insertElongOrderLogs(log);
						
						//insert 通知表  通知出票系统成功  用于出票成功结果异步确认通知
						DBNoticeVo notice=new DBNoticeVo();
						notice.setOrder_id(orderId);
						notice.setChannel(Consts.CHANNEL_TONGCHENG);
						notice.setCp_notify_status(Consts.NOTICE_OVER);
						notice.setBook_notify_url(Consts.TC_BOOKNOTIFYURL);
						notice.setBook_notify_finish_time("finished");
						noticeService.insertNotice(notice);
						/**查询出票结果*/
						String json=getTcSynchronizationInfo(orderId,orderInfo,start);
						if(json!=null){
							logger.info("同程占位-orderId:"+orderId+",同步返回结果:"+json);
							printJson(response,json);
							log=new ElongOrderLogsVo();
							log.setOpt_person("elong_app");
							log.setContent("同步返回同程出票结果[成功],耗时"+(System.currentTimeMillis()-start)+"ms");
							log.setOrder_id(orderInfo.getOrder_id());
							elongOrderService.insertElongOrderLogs(log);
						}else{
							logger.info("同程占位-orderId:"+orderId+",同步返回结果超时");
							/*Map<String,String> map=new HashMap<String,String>();
							map.put("order_id", orderId);
					        map.put("old_order_status1", Consts.ELONG_ORDER_DOWN);//下单成功
					        map.put("old_order_status2", Consts.ELONG_ORDER_ING);//正在出票
					        map.put("order_status", Consts.ELONG_OUT_TIME);//超时订单
					        orderService.updateOrderStatus(map);*/
					        log=new ElongOrderLogsVo();
							log.setOpt_person("elong_app");
							log.setContent("同城占位-orderId:"+orderId+"出票系统返回结果已经超时,转异步通知");
							log.setOrder_id(orderId);
							elongOrderService.insertElongOrderLogs(log);
							JSONObject jsonReturn=new JSONObject();	
							jsonReturn.put("success", false);
							jsonReturn.put("code",950);
							jsonReturn.put("msg", "同步订单已经转为异步");
							printJson(response,jsonReturn.toString());
						}
					}else{
						ElongOrderLogsVo log=new ElongOrderLogsVo();
						log.setOpt_person("elong_app");
						log.setContent("通知出票系统_"+("FAIL".equals(out)?"失败":"异常"));
						log.setOrder_id(orderInfo.getOrder_id());
						elongOrderService.insertElongOrderLogs(log);
						
						Map<String,String> map=new HashMap<String,String>();
						map.put("order_id", orderId);
				        map.put("old_order_status1", Consts.ELONG_ORDER_DOWN);//下单成功
				        map.put("old_order_status2", Consts.ELONG_ORDER_ING);//正在出票
				        map.put("order_status", Consts.ELONG_OUT_TIME);//超时订单  通知出票系统超时
				        orderService.updateOrderStatus(map);
						
						//通知出票系统出票失败
						JSONObject json=new JSONObject();	
						json.put("success", false);
						json.put("code",506);
						json.put("msg", "出票系统操作超时");
						printJson(response,json.toString());
					}
				}else{
					//订单状态错误
					JSONObject json=new JSONObject();	
					json.put("success", false);
					json.put("code",112);
					json.put("msg", "订单状态不正确");
					printJson(response,json.toString());
				}
			} catch (Exception e) {
				JSONObject json=new JSONObject();	
				json.put("success", false);
				json.put("code",506);
				json.put("msg", "收单接口异常");
				printJson(response,json.toString());
				logger.info("同城占位-异常,order_id:"+orderId+",msg:"+e);
				e.printStackTrace();
			}
		}else{
			logger.info("同城占位-异步请求开始"+orderId);
			long start=System.currentTimeMillis();
			JSONObject returnJson=new JSONObject();
			String reqtoken=webJson.getString("reqtoken");
			returnJson.put("reqtoken", reqtoken);
			returnJson.put("transactionid", transactionId);
			try{
				//订单去重
				String orderStatus=orderService.queryOrderStatusByOrderId(orderId);
				if(orderStatus==null){
					DBOrderInfo orderInfo=getOrderInfoFromTc(webJson,"asynchronous");
					if("0".equals(tongcheng_get_order)&&"00".equals(orderInfo.getWait_for_order())){
						returnJson.put("code", "506");
						returnJson.put("success", false);
						returnJson.put("msg","处理失败");
						returnJson.put("tooltip","12306系统异常");
						printJson(response,returnJson.toString());
						logger.info("同城占位-异步接口关闭,order_id:"+orderId);
						return;
					}
					orderService.addOrder(orderInfo);
					returnJson.put("code", "802");
					returnJson.put("success", true);
					returnJson.put("msg", "操作请求已接受");
					returnJson.put("tooltip", "");
					printJson(response,returnJson.toString());
					logger.info("同城占位-成功,order_id:"+orderId+",耗时:"+(System.currentTimeMillis()-start)+"ms");
					
					
					/**通知出票系统出票start*/
					String out=null;
					/*for(int i=0;i<2;i++){*/
					
					out=orderService.sendOutTicket(orderInfo,"book");
					/*	if(out!=null){
							break;
						}
					}*/
					DBNoticeVo notice=new DBNoticeVo();
					notice.setOrder_id(orderInfo.getOrder_id());
					notice.setCp_notify_status("SUCCESS".equals(out)?Consts.NOTICE_OVER:Consts.NOTICE_START);
					notice.setChannel(orderInfo.getChannel());
					notice.setBook_notify_url(orderInfo.getCallbackurl());//预订成功异步回调地址
					noticeService.insertNotice(notice);
					
					ElongOrderLogsVo log=new ElongOrderLogsVo();
					log.setOpt_person("elong_app");
					log.setContent("通知出票系统_"+out);
					log.setOrder_id(orderInfo.getOrder_id());
					elongOrderService.insertElongOrderLogs(log);
					
					
				}else{
					returnJson.put("code", "802");
					returnJson.put("success", true);
					returnJson.put("msg", "操作请求已接受");
					returnJson.put("tooltip", "");
					printJson(response,returnJson.toString());
					logger.info("同城占位-重复退单成功,order_id:"+orderId);
				}
			}catch (Exception e){
				returnJson.put("code", "111");
				returnJson.put("success", false);
				returnJson.put("msg","处理失败");
				returnJson.put("tooltip","系统异常,下单失败");
				printJson(response,returnJson.toString());
				logger.info("同城占位-异常,order_id:"+orderId+",msg:"+e);
				e.printStackTrace();
			}
		}
	} 
	private String getTcSynchronizationInfo(String orderId,DBOrderInfo orderInfo,long start) {
		String jsonStr=null;
		//30S处理时间
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for(int i=0;i<6;i++){
			String orderStatus=orderService.queryOutTicketOrderStatusByOrderId(orderId);
			logger.info("同程获取同步结果,orderStatus:"+orderStatus);
			/**预订成功-等待支付*/
			if("33".equals(orderStatus)||"45".equals(orderStatus)){
				Map<String,Object> outTicketInfo=orderService.queryOutTicketOrderInfo(orderId);
				JSONObject json=new JSONObject();
				json.put("success", true);
				json.put("code",100);
				json.put("msg", "处理或操作成功");
				/*orderid	1~32	string	同程订单号
				transactionid	1~32	string	交易单号
				ordersuccess	1~8	bool	订票是否成功
				orderamount	1~8	string	订单总金额
				checi	1~8	string	车次
				from_station_code	1~8	string	出发站简码
				from_station_name	1~16	string	出发站名称
				to_station_code	1~8	string	到达站简码
				to_station_name	1~16	string	到达站名称
				
				train_date	1~16	string	乘车日期
				start_time	1~8	string	从出发站开车时间
				arrive_time	1~8	string	抵达目的站的时间
				runtime	1~8	string	运行时间
				passengers	1~1024	string	不输入的一样，订票成功了里面的
				cxin 参数会有值。
				help_info	1~256	string	在预订失败的情况下，给出帮助信
				息，可以直接展示给客户看。*/
				json.put("orderid", orderInfo.getOrder_id());
				json.put("transactionid", orderInfo.getOrder_id());
				json.put("ordersuccess",true);
				json.put("orderamount", outTicketInfo.get("buy_money")+"");
				json.put("checi", orderInfo.getTrain_no());
				json.put("from_station_code", orderInfo.getFrom_station_code());
				json.put("from_station_name", orderInfo.getFrom_city());
				json.put("to_station_code", orderInfo.getTo_station_code());
				json.put("to_station_name", orderInfo.getTo_city());
				
				json.put("train_date", orderInfo.getTravel_date());
				
				String start_time=outTicketInfo.get("from_time")+"";
				String arrive_time=outTicketInfo.get("to_time")+"";
				String runtime=DateUtil.minuteDiff(DateUtil.stringToDate(arrive_time, "yyyy-MM-dd HH:mm:ss"),DateUtil.stringToDate(start_time, "yyyy-MM-dd HH:mm:ss"))+"";
				
				//2014-08-09 09:33:00 按照时分返回
				
				start_time=start_time.substring(start_time.indexOf(" ")+1, start_time.indexOf(" ")+6);
				arrive_time=arrive_time.substring(arrive_time.indexOf(" ")+1, arrive_time.indexOf(" ")+6);
				
				
				json.put("start_time",start_time);
				json.put("arrive_time",arrive_time);
				json.put("runtime",runtime);
				json.put("help_info","");
				
				if("Z22".equals(orderInfo.getTrain_no())||"Z21".equals(orderInfo.getTrain_no())){
					json.put("refund_online","1");
				}
				
				JSONArray arr=new JSONArray();
				List<DBPassengerInfo> passengers=orderInfo.getPassengers();
				for(DBPassengerInfo p:passengers){
					Map<String,Object> outTicketCpInfo=orderService.queryOutTicketCpInfo(p.getCp_id());
					JSONObject pJson=new JSONObject();
					pJson.put("passengerid", p.getPassengerid());
					pJson.put("ticket_no", p.getCp_id());
					pJson.put("passengersename", p.getUser_name());
					pJson.put("passportseno", p.getUser_ids());
					pJson.put("passporttypeseid", p.getElong_ids_type());
					pJson.put("passporttypeseidname",TongChengConsts.getPassporttypeseidname(p.getElong_ids_type()));
					pJson.put("piaotype", p.getElong_ticket_type());
					pJson.put("piaotypename",TongChengConsts.getPiaotypename(p.getElong_ticket_type()));
					//to19eSeatMap.put("tc_O", "3");//二等软座	
					//to19eSeatMap.put("tc_M", "2");//一等软座	
					
					//21动卧  22高级动卧  23一等软座  24二等软座
					
					//9:商务座，P:特等座，M:一等座，O:二等座，6:高级软卧，
					//4:软卧，3:硬卧，2:软座，1:硬座 7:一等软座 8:二等软座 A:高级动卧 F:动卧 
					
					String sys_zwcode=outTicketCpInfo.get("seat_type")+"";
					String zwcode=p.getElong_seat_type();
					if("23".equals(sys_zwcode)){//一等座一等软座转换
						zwcode="7";
					}else if("24".equals(sys_zwcode)){//二等座二等软座转换
						zwcode="8";
					}else if("21".equals(sys_zwcode)){//软卧动卧转换
						zwcode="F";
					}else if("22".equals(sys_zwcode)){//高级动卧高级软卧转换
						zwcode="A";
					}
					
					pJson.put("zwcode",zwcode);
					/*if("M".equals(p.getElong_seat_type())||"O".equals(p.getElong_seat_type())){
						String train_no=orderInfo.getTrain_no().toUpperCase();
						if(!train_no.contains("C")&&!train_no.contains("D")&&!train_no.contains("G")){
							pJson.put("zwcode","M".equals(p.getElong_seat_type())?"7":"8");
						}else{
							pJson.put("zwcode",p.getElong_seat_type());
						}
					}else{
						pJson.put("zwcode",p.getElong_seat_type());
					}*/
					pJson.put("zwname", TongChengConsts.getZwname(zwcode));
					pJson.put("cxin",outTicketCpInfo.get("train_box")+"车厢,"+outTicketCpInfo.get("seat_no"));
					pJson.put("price",outTicketCpInfo.get("buy_money")+"");
					arr.add(pJson);
				}
				json.put("passengers",arr.toString());
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
				cxin	string	几车厢几座（在订票成功后才会有值）
				price	string	票价*/
				jsonStr=json.toString();
				break;
			}else if("10".equals(orderStatus)){
				//出票失败订单
				Map<String,Object> outTicketInfo=orderService.queryOutTicketOrderInfo(orderId);
				//1所购买的车次坐席已无票 2身份证件已经实名制购票 3票价和12306不符 4乘车时间异常 5证件错误 6用户要求取消订单 7未通过12306实名认证 8乘客身份信息待核验  
				String error_info=
					outTicketInfo.get("error_info")==null?"1":(outTicketInfo.get("error_info").toString());
				JSONObject json=new JSONObject();
				json.put("success", false);
				int code=309;
				String msg="没有余票";
				List<DBPassengerInfo> passengers=orderInfo.getPassengers();
				if("2".equals(error_info)){
					code=305;
					msg="乘客已经预订过该车次";
				}else if("5".equals(error_info)||"7".equals(error_info)||"8".equals(error_info)){
					code=308;
					for(DBPassengerInfo p:passengers){
						msg=p.getUser_name()+ p.getUser_ids();
						break;
					}
				}else if("10".equals(error_info)){
					code=313;
					msg="订单内有乘客已被法院依法限制高消费，禁止乘坐列车当前坐席类型";
					
				}
				json.put("code",code);
				json.put("msg", msg);
				
				json.put("orderid", orderInfo.getOrder_id());
				json.put("transactionid", orderInfo.getOrder_id());
				json.put("ordersuccess",false);
				json.put("orderamount", orderInfo.getBuy_money());
				json.put("checi", orderInfo.getTrain_no());
				json.put("from_station_code", orderInfo.getFrom_station_code());
				json.put("from_station_name", orderInfo.getFrom_city());
				json.put("to_station_code", orderInfo.getTo_station_code());
				json.put("to_station_name", orderInfo.getTo_city());
				
				json.put("train_date", orderInfo.getTravel_date());
				json.put("start_time", "");
				json.put("arrive_time","");
				json.put("runtime","");
				json.put("help_info",TongChengConsts.getErrorInfo(error_info));
				//1所购买的车次坐席已无票 2身份证件已经实名制购票 3票价和12306不符 4乘车时间异常 5证件错误 6用户要求取消订单 7未通过12306实名认证 8乘客身份信息待核验
				JSONArray arr=new JSONArray();
			
				for(DBPassengerInfo p:passengers){
					JSONObject pJson=new JSONObject();
					pJson.put("passengerid", p.getPassengerid());
					pJson.put("ticket_no", p.getCp_id());
					pJson.put("passengersename", p.getUser_name());
					pJson.put("passportseno", p.getUser_ids());
					pJson.put("passporttypeseid", p.getElong_ids_type());
					pJson.put("passporttypeseidname",TongChengConsts.getPassporttypeseidname(p.getElong_ids_type()));
					pJson.put("piaotype", p.getElong_ticket_type());
					pJson.put("piaotypename",TongChengConsts.getPiaotypename(p.getElong_ticket_type()));
					pJson.put("zwcode",p.getElong_seat_type());
					pJson.put("zwname", TongChengConsts.getZwname(p.getElong_seat_type()));
					pJson.put("cxin","");
					pJson.put("price",p.getPay_money());
					arr.add(pJson);
				}
				json.put("passengers",arr.toString());
				jsonStr=json.toString();
				break;
			}
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		return jsonStr;
	}

	/**
	 * 同城JSONObject
	 * 转化成订单信息的 ：JAVAbean
	 * */
	private DBOrderInfo getOrderInfoFromTc(JSONObject webJson,String type) {
		DBOrderInfo orderInfo=new DBOrderInfo();
		String orderId=webJson.getString("orderid");
		String cheCi=getJsonValue(webJson,"checi");
		String fromStationName=getJsonValue(webJson,"from_station_name");
		String from_station_code=getJsonValue(webJson,"from_station_code");
		String toStationName=getJsonValue(webJson,"to_station_name");
		String to_station_code=getJsonValue(webJson,"to_station_code");
		String trainDate=getJsonValue(webJson,"train_date");
		String wait_for_order=getJsonValue(webJson,"waitfororder");
		if("true".equals(wait_for_order)){
			orderInfo.setWait_for_order("11");
		}else{
			orderInfo.setWait_for_order("00");
		}
		orderInfo.setOrder_id(orderId);
		orderInfo.setTrain_no(cheCi);
		orderInfo.setFrom_city(fromStationName);
		orderInfo.setFrom_station_code(from_station_code);
		orderInfo.setTo_station_code(to_station_code);
		orderInfo.setTo_city(toStationName);
		orderInfo.setTravel_date(trainDate);
		orderInfo.setOrder_name(fromStationName+"/"+toStationName);
		orderInfo.setChannel("tongcheng");//渠道编号
		orderInfo.setOrder_status(Consts.ELONG_ORDER_DOWN);//下单成功
		JSONArray passengersArr=JSONArray.fromObject(webJson.getString("passengers"));
		int num=passengersArr.size();
		orderInfo.setTicket_num(num+"");
		//先预定后支付不涉及价格
		//double pay_money=0;
		String sysSeatType="";
		String channelSeatType="";
		List<DBPassengerInfo> passengers=new ArrayList<DBPassengerInfo>();
		
		List<DBStudentInfo> students=new ArrayList<DBStudentInfo>();
		for(int i=0;i<num;i++){
			DBPassengerInfo p=new DBPassengerInfo();
			JSONObject pJson=passengersArr.getJSONObject(i);
			p.setOrder_id(orderId);
			p.setPassengerid(pJson.getString("passengerid"));
			p.setOut_passengerid(pJson.getString("passengerid"));
			p.setCp_id(CreateIDUtil.createID("tc"));
		//	p.setCp_id(orderId+"_"+pJson.getString("passengerid"));
			p.setUser_name(pJson.getString("passengersename"));
			p.setUser_ids(pJson.getString("passportseno"));
			p.setElong_ids_type(pJson.getString("passporttypeseid"));
			p.setIds_type(TongChengConsts.get19eIdsType(pJson.getString("passporttypeseid")));
			//pJson.getString("passporttypeseidname") 证件名称
			String piaotype=pJson.getString("piaotype");
			p.setElong_ticket_type(piaotype);
			p.setTicket_type(TongChengConsts.get19eTicketType(piaotype));
			//pJson.getString("piaotypename") 票种名称
			p.setElong_seat_type(pJson.getString("zwcode"));
			p.setSeat_type(TongChengConsts.get19eSeatType(pJson.getString("zwcode")));
			if(pJson.containsKey("identitystatusid")){
				p.setIdentityStatusId(pJson.getString("identitystatusid"));
			}
			if(pJson.containsKey("identitystatusmsg")){
				p.setIdentityStatusMsg(pJson.getString("identitystatusmsg"));
			}
			//pJson.getString("zwname") 座位名称
			//p.setBuy_money(pJson.getString("price"));
			//pay_money=pay_money+Double.parseDouble(pJson.getString("price"));
			/**学生票*/
			if("3".equals(piaotype)){
				DBStudentInfo s=new DBStudentInfo();
				/*province_name	string	省份名称：参考附件5.4 的provinces
				province_code	string	省份编号：参考附件5.4 的provinces
				school_code	string	学校代号
				school_name	string	学校名称
				student_no	string	学号
				school_system	string	学制
				enter_year	string	入学年份：yyyy
				preference_from_station_name	string	优惠区间起始地名称【选填】
				preference_from_station_code	string	优惠区间起始地代号
				preference_to_station_name	string	优惠区间到达地名称【选填】
				preference_to_station_code	string	优惠区间到达地代号*/
				s.setOrder_id(orderId);
				s.setCp_id(p.getCp_id());
				s.setProvince_name(getJsonValue(pJson,"province_name"));
				s.setProvince_code(getJsonValue(pJson,"province_code"));
				s.setSchool_code(getJsonValue(pJson,"school_code"));
				s.setSchool_name(getJsonValue(pJson,"school_name"));
				s.setStudent_no(getJsonValue(pJson,"student_no"));
				s.setSchool_system(getJsonValue(pJson,"school_system"));
				s.setEnter_year(getJsonValue(pJson,"enter_year"));
				s.setPreference_from_station_name(getJsonValue(pJson,"preference_from_station_name"));
				s.setPreference_from_station_code(getJsonValue(pJson,"preference_from_station_code"));
				s.setPreference_to_station_name(getJsonValue(pJson,"preference_to_station_name"));
				s.setPreference_to_station_code(getJsonValue(pJson,"preference_to_station_code"));
				s.setChannel("tongcheng");
				students.add(s);
			}
			sysSeatType=p.getSeat_type();
			passengers.add(p);
			channelSeatType=p.getElong_seat_type();
		}
		//orderInfo.setPay_money(pay_money+"");
		//通知出票系统  备选坐席处理
		StringBuffer ext_field1=new StringBuffer();
		
		orderInfo.setSeat_type(sysSeatType);
		orderInfo.setElong_seat_type(channelSeatType);
		ext_field1.append(sysSeatType+"#9,0");
		orderInfo.setExt_field1(ext_field1.toString());
		/*String hasseat=webJson.getString("hasseat");
		 * if("False".equals(hasseat)){//允许出无座
			ext_field1.append("9,0");
		}else{
			ext_field1.append("无");
		}*/
		if("asynchronous".equals(type)){//异步 类型 
			//备选字段二 用于记录 ：请求物证值|出发站编码|目的站编码[异步时填写] 只针对同城渠道 
			orderInfo.setExt_field2(from_station_code+"|"+to_station_code+"|"+webJson.getString("reqtoken"));//请求物证值|出发站编码|目的站编码
			orderInfo.setCallbackurl(webJson.getString("callbackurl"));
		}else{//同步
			orderInfo.setExt_field2(from_station_code+"|"+to_station_code);
		}
		orderInfo.setPassengers(passengers);
		orderInfo.setStudents(students);
		return orderInfo;
	}
	/**
	 * 火车票确认出票
	 */
	private void trainConfirm(JSONObject webJson,HttpServletRequest request, 
			HttpServletResponse response) { 
		/*4.6.2. 业务输出参数
		参数名	长度	类型	备注
		orderid	1~32	string	同程订单号
		ordernumber	1~16	string	取票单号
		changeserial	1~32	string	出票扣款资金变动流水号（出票成功才
		有值）
		4.6.3. 通用输出参数增加可能出现的值
		success 值	code 值	msg 值
		false	401	确认出票的请求时间已超过规定的时间
		false	402	订单不存在
		false	403	账户余额不足*/
		long start=System.currentTimeMillis();
		String order_id=getJsonValue(webJson,"orderid");
		String transactionid=getJsonValue(webJson,"transactionid");
		
		/*通知信息*/
//		DBNoticeVo notice = noticeService.queryNoticeInfoById(order_id);
//		if(notice != null && !"22".equals(notice.getBook_notify_status())) {
//			logger.info("同程-请求确认出票ERROR, 预订结果通知未成功,拒绝支付.order_id : " + order_id);
//			JSONObject json=new JSONObject();
//			json.put("success", false);
//			json.put("code",112);
//			json.put("msg", "订单状态不正确");
//			printJson(response,json.toString());
//			return;
//		}
		
		/*订单信息*/
		DBOrderInfo orderInfo=orderService.queryOrderInfo(order_id);
		String orderStatus=orderInfo.getOrder_status();
		
		if(orderStatus==null){
			logger.info("同程-请求[App]确认出票ERROR,订单不存在order_id:"+order_id);
			JSONObject json=new JSONObject();
			json.put("success", false);
			json.put("code",402);
			json.put("msg", "订单不存在");
			printJson(response,json.toString());
		}else{
			if(Consts.ELONG_ORDER_MAKED.equals(orderStatus)){
				/**以下 支付时间限制暂时去掉*/
				//查询时间
				/*String createTimeStr=orderInfo.getCreate_time();
				Date createTime=DateUtil.stringToDate(createTimeStr,"yyyy-MM-dd HH:mm:ss");
				Date time22_35=DateUtil.stringToDate(DateUtil.dateToString(createTime, "yyyyMMdd")+"223500", "yyyyMMddHHmmss");
				Date time23=DateUtil.stringToDate(DateUtil.dateToString(createTime, "yyyyMMdd")+"230000", "yyyyMMddHHmmss");
				//22:35:00-23:00:00 下 的单子供应商应该给 20 分钟的付款时间
				Date nowTime=new Date();
				if(createTime.before(time23)&&createTime.after(time22_35)){
					//20分钟有效
					long minutes=(nowTime.getTime()-createTime.getTime())/1000*60;
					if(minutes>20){
						logger.info("同程-请求[App]确认出票ERROR,22:35:00-23:00:00 下 的单子供应商应该给 20 分钟的付款时间:"+minutes+"minutes");
						JSONObject json=new JSONObject();
						json.put("success", false);
						json.put("code",401);
						json.put("msg", "确认出票的请求时间已超过规定的时间");
						printJson(response,json.toString());
					}else{
						String	result=orderService.pay(order_id, orderInfo.getBuy_money());
						if(result.equals("SUCCESS")){
							logger.info("App-请求[App]确认出票SUCCESS,order_id:"+order_id);
							ElongOrderLogsVo log=new ElongOrderLogsVo();
							log.setOpt_person("elong_app");
							log.setContent("同程请求支付订单[成功]");
							log.setOrder_id(order_id);
							elongOrderService.insertElongOrderLogs(log);
							//取消火车票成功
							JSONObject json=new JSONObject();
							json.put("success", true);
							json.put("code",100);
							json.put("msg", "处理或操作成功");
							json.put("orderid",order_id);
							json.put("ordernumber",orderInfo.getOut_ticket_billno());
							json.put("changeserial","");
							printJson(response,json.toString());
						}else{
							JSONObject json=new JSONObject();
							json.put("success", true);
							json.put("code",500);
							json.put("msg", "处理或操作失败,请重新操作");
							json.put("orderid",order_id);
							printJson(response,json.toString());
						}
					}
				}else{
					//40分钟有效
					long minutes=(nowTime.getTime()-createTime.getTime())/1000*60;
					if(minutes>40){
						logger.info("同程-请求[App]确认出票ERROR,22:35:00前下 的单子供应商应该给 40 分钟的付款时间:"+minutes+"minutes");
						JSONObject json=new JSONObject();
						json.put("success", false);
						json.put("code",401);
						json.put("msg", "确认出票的请求时间已超过规定的时间");
						printJson(response,json.toString());
					}else{
						String	result=orderService.pay(order_id, orderInfo.getBuy_money());
						if(result.equals("SUCCESS")){
							logger.info("App-请求[App]确认出票SUCCESS,order_id:"+order_id);
							ElongOrderLogsVo log=new ElongOrderLogsVo();
							log.setOpt_person("elong_app");
							log.setContent("同程请求支付订单[成功]");
							log.setOrder_id(order_id);
							elongOrderService.insertElongOrderLogs(log);
							//取消火车票成功
							JSONObject json=new JSONObject();
							json.put("success", true);
							json.put("code",100);
							json.put("msg", "处理或操作成功");
							json.put("orderid",order_id);
							json.put("ordernumber",orderInfo.getOut_ticket_billno());
							json.put("changeserial","");
							printJson(response,json.toString());
						}else{
							JSONObject json=new JSONObject();
							json.put("success", true);
							json.put("code",500);
							json.put("msg", "处理或操作失败,请重新操作");
							json.put("orderid",order_id);
							printJson(response,json.toString());
						}
					}
				}*/
				String	result=orderService.pay(order_id, orderInfo.getBuy_money());
				if(result.equals("SUCCESS")){
					logger.info("App-请求[App]确认出票SUCCESS,order_id:"+order_id);
					ElongOrderLogsVo log=new ElongOrderLogsVo();
					log.setOpt_person("elong_app");
					log.setContent("同程请求支付订单[成功]"+(System.currentTimeMillis()-start));
					log.setOrder_id(order_id);
					elongOrderService.insertElongOrderLogs(log);
					//取消火车票成功
					JSONObject json=new JSONObject();
					json.put("success", true);
					json.put("code",100);
					json.put("msg", "处理或操作成功");
					json.put("orderid",order_id);
					json.put("ordernumber",orderInfo.getOut_ticket_billno());
					json.put("changeserial","");
					printJson(response,json.toString());
				}else{
					
					JSONObject json=new JSONObject();
					json.put("success", false);
					json.put("code",500);
					json.put("msg", "处理或操作失败,请重新操作");
					json.put("orderid",order_id);
					printJson(response,json.toString());
				}
			}else if(Consts.ELONG_ORDER_WAITPAY.equals(orderStatus)){
				JSONObject json=new JSONObject();
				json.put("success", false);
				json.put("code",1102);
				json.put("msg", "申请出票请求已接收，正在处理中");
				json.put("orderid",order_id);
				json.put("ordernumber",orderInfo.getOut_ticket_billno());
				json.put("changeserial","");
				printJson(response,json.toString());
			}else{
				logger.info("同程-请求[App]确认出票ERROR,order_id:"+order_id+"订单状态错误:"+orderStatus);
				JSONObject json=new JSONObject();
				json.put("success", false);
				json.put("code",112);
				json.put("msg", "订单状态不正确");
				printJson(response,json.toString());
			}
		}
		
		/*orderid	1~32	string	同程订单号
		transactionid	1~32	string	交易单号
		4.6.2. 业务输出参数
		参数名	长度	类型	备注
		orderid	1~32	string	同程订单号
		ordernumber	1~16	string	取票单号
		changeserial	1~32	string	出票扣款资金变动流水号（出票成功才
		有值）
		4.6.3. 通用输出参数增加可能出现的值
		success 值	code 值	msg 值
		false	401	确认出票的请求时间已超过规定的时间
		false	402	订单不存在
		false	403	账户余额不足*/
	} 
	/**
	 * 取消火车票订单
	 */
	private void trainCancel(JSONObject webJson,HttpServletRequest request, 
			HttpServletResponse response) {
		String orderid=webJson.getString("orderid");
		String transactionid=webJson.getString("transactionid");
		//更新订单状态param
		Map<String,String> param=new HashMap<String,String>();
		param.put("order_id", orderid);
		
		String orderStatus=orderService.queryOrderStatusByOrderId(orderid);
		try {
			if(orderStatus!=null){
				if(Consts.ELONG_ORDER_CANCELED.equals(orderStatus)){
					logger.info("同程-请求[App]取消订单SUCCESS,已经取消成功"+orderid);
					//取消火车票成功
					JSONObject json=new JSONObject();
					json.put("success", true);
					json.put("code",100);
					json.put("msg", "处理或操作成功");
					json.put("orderid",orderid);
					printJson(response,json.toString());
				}else if(Consts.ELONG_OUT_TIME.equals(orderStatus)){
					logger.info("同程-请求[App]取消订单SUCCESS,已经超时"+orderid);
					//取消火车票成功
					ElongOrderLogsVo log=new ElongOrderLogsVo();
					log.setOpt_person("elong_app");
					log.setContent("同程请求取消订单[成功],超时未支付默认取消成功");
					log.setOrder_id(orderid);
					elongOrderService.insertElongOrderLogs(log);
					
					JSONObject json=new JSONObject();
					json.put("success", true);
					json.put("code",100);
					json.put("msg", "处理或操作成功");
					json.put("orderid",orderid);
					printJson(response,json.toString());
				}else{
//					logger.info("同程-请求[App]取消订单ERROR,状态错误order_id:"+orderid+",orderStatus:"+orderStatus);
//					//订单状态错误
//					JSONObject json=new JSONObject();	
//					json.put("success", false);
//					json.put("code",112);
//					json.put("msg", "订单状态不正确");
//					printJson(response,json.toString());

					logger.info("同程-请求[App]取消订单开始,order_id:"+orderid);
					//同步->出票系统 发起取消预订
					//send
					String result=orderService.cancel(orderid);
					if(result.equals("SUCCESS")){
						logger.info("App-请求[出票Sys]取消订单SUCCESS,order_id:"+orderid);
						
						ElongOrderLogsVo log=new ElongOrderLogsVo();
						log.setOpt_person("elong_app");
						log.setContent("同程请求取消订单[成功]");
						log.setOrder_id(orderid);
						elongOrderService.insertElongOrderLogs(log);
						//取消火车票成功
						JSONObject json=new JSONObject();
						json.put("success", true);
						if(Consts.ELONG_ORDER_ING.equals(orderStatus)) {
							json.put("code",502);
							json.put("msg", "取消请求已接收，占座中");
						} else {
							json.put("code",100);
							json.put("msg", "处理或操作成功");
						}
						json.put("orderid",orderid);
						printJson(response,json.toString());
						
						/**开启取消异步通知*/
						DBNoticeVo notice=new DBNoticeVo();
						notice.setOrder_id(orderid);
						notice.setChannel(Consts.CHANNEL_TONGCHENG);
						notice.setOut_notify_status(Consts.NOTICE_START);
						noticeService.updateNotice(notice);
					}else{
						JSONObject json=new JSONObject();
						json.put("success", true);
						json.put("code",500);
						json.put("msg", "处理或操作失败,请重新操作");
						json.put("orderid",orderid);
						printJson(response,json.toString());
					}
				
				}
			}else{
				//false	402	订单不存在*/
				logger.info("同程-请求[App]取消订单ERROR,订单不存在order_id:"+orderid);
				JSONObject json=new JSONObject();	
				json.put("success", false);
				json.put("code",402);
				json.put("msg", "订单不存在");
				printJson(response,json.toString());
			}
		} catch (Exception e) {
			//false 系统异常
			logger.info("同程-请求[App]取消订单异常,order_id:"+orderid+",msg"+e);
			JSONObject json=new JSONObject();	
			json.put("success", false);
			json.put("code",500);
			json.put("msg", "系统异常");
			printJson(response,json.toString());
			e.printStackTrace();
		}
	} 

	/**
	 * 查询订单状态
	 */
	private void trainQueryStatus(JSONObject webJson,HttpServletRequest request, 
			HttpServletResponse response) { 
		String orderid=webJson.getString("orderid");
		/*参数名	长度	类型	备注
		orderid	1~32	string	同程订单号
		4.8.2. 业务输出参数
		参数名	长度	类型	备注
		orderid	1~32	string	同程订单号
		status	4	int	状态号，详见附注 2
		description	1~64	string	状态描述，详见附注 2
		4.8.3. 通用输出参数增加可能出现的值
		success 值	code 值	msg 值
		false	601	查询订单状态失败
		false	402	订单不存在
		 */
		try {
			String orderStatus=orderService.queryOrderStatus(orderid);
			if(orderStatus!=null){
				/*
				占座失败 1
				占座成功 2
				正在出票 3
				出票成功 4
				出票失败 5
				订单已取消 6
				补单成功 7
				正在退票 8
				退票成功 9
				退票失败 10
				正在改签 11
				改签成功 12
				改签失败 13
				*/
				String status="";
				String description="";
				if(Consts.ELONG_ORDER_FAIL.equals(orderStatus)){
					//占座失败 1
					status="1";
					description="占座失败";
				}else if(Consts.ELONG_ORDER_MAKED.equals(orderStatus)){
					//占座成功 2
					status="2";
					description="占座成功";
				}else if(Consts.ELONG_ORDER_WAITPAY.equals(orderStatus)){
					//正在出票 3
					status="3";
					description="正在出票";
				}else if(Consts.ELONG_ORDER_CANCELED.equals(orderStatus)){
					//订单已取消 6
					status="6";
					description="订单已取消";
				}else if(Consts.ELONG_ORDER_SUCCESS.equals(orderStatus)){
					//订单已取消 6
					status="4";
					description="出票成功";
				}else{
					status="";
					description="";
				}
				
				if(status.equals("")||description.equals("")){
					JSONObject json=new JSONObject();
					json.put("success", false);
					json.put("code",601);
					json.put("msg", "查询订单状态失败");
				}else{
					JSONObject json=new JSONObject();
					json.put("success", true);
					json.put("code",100);
					json.put("msg", "处理或操作成功");
					json.put("orderid", orderid);
					json.put("status",status);
					json.put("description", description);
					printJson(response,json.toString());
					
				}
			}else{
				logger.info("同程-查询订单状态ERROR,订单不存在,order_id:"+orderid);
				JSONObject json=new JSONObject();
				json.put("success", false);
				json.put("code", 402);
				json.put("msg", "订单不存在");
				printJson(response,json.toString());
			}
		} catch (Exception e) {
			logger.info("同程-查询订单状态异常,order_id:"+orderid+",msg:"+e);
			e.printStackTrace();
		}
		
	} 

	/**
	 * 查询订单详情
	 */
	private void trainQueryInfo(JSONObject webJson,HttpServletRequest request, 
			HttpServletResponse response) { 
		String orderid=webJson.getString("orderid");
		String transactionid=webJson.getString("transactionid");
		
		DBOrderInfo orderInfo=orderService.queryOrderInfo(orderid);
		JSONObject json=new JSONObject();
		/*success	bool		true:成功，false:失败
		code	int	4	状态编码
		msg	string	1~25
		6	提示信息*/
		/*transactionid	1~32	string	交易单号
		orderid	1~32	string	同程订单号
		orderstatusname	1~64	string	订单状态名称

		checi	1~8	string	车次，如 D520
		ordernumber	1~16	string	取票单号，如 E518848964
		fromstation	1~32	string	发站，如 苏州
		tostation	1~32	string	到站，如 上海
		traintime	1~32	string	开车时间，如 2014-06-13 15:20:00
		cashchange	1~1024	string	资金变动情况，如
		[{'changeserial':'214665432454475776','c hangemoney':'120.00','changedir':'0',' cha ngetypename':'出票扣款', 'changetype':'2
		'},{'changeserial':'214692313341964288','c hangemoney':'114.00','changedir':'1','cha ngetypename':'线下退票加款', 'changetyp e':'6'}]
		其中属性含义如下： changeserial:资金变动流水号 changemoney:变动金额，单位是元
		
		

					changedir:变动方向（1 表示给同程账户加
		款，0 表示从同程账户扣款） changetypename:表示变动类型名 changetype:表示变动类型编号
		ticketstatus	1~1024	string	车票状态，如
		[{'passengersename':'王伟','ticket_no':'E23
		04545523080033','piaotypename':'成人票
		','status':'已在线退票','statusid':'4'}]
		总共会有以下几种状态：
		1.  已取票，表示客户已在车站窗口或取票机 取得了车票，因此如果有退款，可能会有
		以下三种可能：取了之后退票；取了之后
		改签；还有可能取了票之后再改签，然后 再退票
		2.  已在线改签，表示通过我们双方的系统改 签的
		3.  已线下改签，表示客户直接在窗口改签
		4.  已在线退票，表示通过我们双方的系统退 票的
		5.  已线下退票，表示客户直接在窗口退票
		6.  已出票，表示客户已经支付购票款项
		7.  待出票，表示尚未支付票款
		8.  待核实，表示出现系统故障获取状态出错*/
		try {
			json.put("success", true);
			json.put("code", 100);
			json.put("msg", "处理或操作成功");
			
			json.put("transactionid",orderInfo.getOrder_id() );
			json.put("orderid",orderInfo.getOrder_id() );
			String orderStatus=orderInfo.getOrder_status();
			json.put("checi",orderInfo.getTrain_no() );
			json.put("ordernumber",orderInfo.getOut_ticket_billno() );
			json.put("fromstation",orderInfo.getFrom_city() );
			json.put("tostation", orderInfo.getTo_city());
			json.put("traintime",orderInfo.getFrom_time() );
			json.put("cashchange","" );
			//未发生退票
			String refundCode="1";
			JSONArray arr=new JSONArray();
			List<DBPassengerInfo> passengers=orderInfo.getPassengers();
			
			boolean refundSuccess=false;
			boolean refundIng=false;
			boolean refundFail=false;
			for(DBPassengerInfo p:passengers){
				JSONObject pj=new JSONObject();
				pj.put("passengersename", p.getUser_name());
				pj.put("ticket_no",p.getCp_id() );
				pj.put("piaotypename", TongChengConsts.getPiaotypename(p.getElong_ticket_type()));
				
				Map<String,String> query=new HashMap<String,String>();
				query.put("order_id", orderInfo.getOrder_id());
				query.put("cp_id", p.getCp_id() );
				query.put("refund_type",  Consts.ELONG_REFUNDTYPE_TICKET);
				String ticketRefundStatus=orderService.queryRefundStatus(query);
				//查询是否发生系统的线下退款
				query.put("refund_type",  Consts.ELONG_REFUNDTYPE_AMOUNT);
				String offlineRefundStatus=orderService.queryRefundStatus(query);
				String status="";
				String statisid="";
				if(Consts.ELONG_ORDER_DOWN.equals(orderStatus)||
						Consts.ELONG_ORDER_ING.equals(orderStatus)||
							Consts.ELONG_ORDER_MAKED.equals(orderStatus)||
								Consts.ELONG_ORDER_WAITPAY.equals(orderStatus)){
					status="待出票";
					statisid="7";
					
				}else if(Consts.ELONG_ORDER_CANCELED.equals(orderStatus)){
					status="已取消";
					statisid="9";
				}else if(ticketRefundStatus!=null){
					if(ticketRefundStatus.equals("22")){
						status="已取票";
						statisid="1";
					}else{
						status="已在线退票";
						statisid="4";
					}
				}else if(offlineRefundStatus!=null){
					status="已线下退票";
					statisid="5";
				}else if(Consts.ELONG_ORDER_SUCCESS.equals(orderStatus)){
					status="已出票";
					statisid="6";
				}else{
					status="待核实";
					statisid="8";
				}
				pj.put("status",status);
				pj.put("statusid",statisid);
				pj.put("price", p.getBuy_money());
				String trainBox=p.getTrain_box();
				String seatNo=p.getSeat_no();
				if(trainBox==null&&seatNo==null){
					pj.put("cxin","");
				}else{
					pj.put("cxin",(trainBox==null?"":trainBox+"车厢,")+seatNo==null?"无座":seatNo);
				}
				pj.put("outticketdetail", "");
				arr.add(pj);
				if(ticketRefundStatus!=null||offlineRefundStatus!=null){
					if(ticketRefundStatus.equals("11")||offlineRefundStatus.equals("11")){
						//退票成功
						refundCode="2";
						refundSuccess=true;
					}else if(ticketRefundStatus.equals("22")||offlineRefundStatus.equals("22")){
						//退票失败
						refundCode="3";
						refundFail=true;
					}else{
						//退票中
						refundCode="4";
						refundIng=true;
					}
				}
			}
			String orderstatusname="";
			if(Consts.ELONG_ORDER_MAKED.equals(orderStatus)){
				orderstatusname="占座成功";
			}else if(Consts.ELONG_ORDER_DOWN.equals(orderStatus)||Consts.ELONG_ORDER_ING.equals(orderStatus)||Consts.ELONG_ORDER_WAITPAY.equals(orderStatus)){
				orderstatusname="正在出票";
			}else if(Consts.ELONG_ORDER_FAIL.equals(orderStatus)){
				orderstatusname="出票失败";
			}else if(Consts.ELONG_ORDER_CANCELED.equals(orderStatus)){
				orderstatusname="订单已取消";
			}else if(Consts.ELONG_ORDER_SUCCESS.equals(orderStatus)){
				if(refundSuccess){
					orderstatusname="退票成功";
				}else if(refundIng){
					orderstatusname="正在退票";
				}else if(refundFail){
					orderstatusname="退票失败";
				}else{
					orderstatusname="出票成功";
				}
			}else{
				logger.info("同程-查询订单详情ERROR,非法订单状态orderStatus:"+orderStatus+",refundCode:"+refundCode);
			}
			json.put("orderstatusname",orderstatusname );//订单状态
			json.put("ticketstatus", arr.toString());
			
			
			/**改签变更详情*/
			List<String> list=orderService.queryChangeIdsByOrderId(orderid);
			Map<String,Object> qParam=new HashMap<String,Object>();
			JSONArray arr_c=new JSONArray();
			
			qParam.put("order_id", orderid);
			if(list!=null&&list.size()>0){
				/*[{
				'reqtoken':'FT143R3DFEF00E54',//改签请求特征值
				'changestatus': true,//改签是否成功，true(改签成功) or false(改签失败)
				'fromstation':'苏州', //改签出发站
				'tostation':'上海', //改签到达站
				'traintime':'2014-06-13 15:20:00'，//改签出发时间
				'ticketstatus':
				[{'passengersename':'王伟','ticket_no':'E23
				04545523080034','piaotypename':'成人票
				','status':'已出票','statusid':'6'，'price':'31.5'，'cxin':'16车厢，05号上铺'，'outticketdetail':'于2015-07-19 苏州园区站制票成功'}]//改签车票详情
				}…]*/
				for(String changeId:list){
					qParam.put("change_id", changeId);
					DBChangeInfo c= tongChengChangeService.getChangeInfo(qParam, true);
					JSONObject cj=new JSONObject();
					cj.put("reqtoken", c.getReqtoken());
					//11、改签预订 12、正在改签预订 13、人工改签预订 14、改签成功等待确认 15、改签预订失败 21、改签取消 22、正在取消 23、取消成功 24、取消失败 31、开始支付 32、正在支付 33、人工支付 34、支付成功 35、支付失败 36、补价支付
					boolean changestatus=false;
					String change_status=c.getChange_status();
					if("15".equals(change_status)||"21".equals(change_status)||"22".equals(change_status)||"23".equals(change_status)
							||"24".equals(change_status)||"35".equals(change_status)){
					}else{
						changestatus=true;
					}
					cj.put("changestatus", changestatus);
					cj.put("fromstation", c.getFrom_city());
					cj.put("tostation", c.getTo_city());
					cj.put("traintime",c.getChange_from_time());
					
					
					JSONArray arr_tickets=new JSONArray();
					List<DBPassengerChangeInfo> c_p=c.getcPassengers();
					for(DBPassengerChangeInfo p:c_p){
						JSONObject cpj=new JSONObject();
						
						/*'passengersename':'王伟','ticket_no':'E23
							04545523080034','piaotypename':'成人票
							','status':'已出票','statusid':'6'，'price':'31.5'，'cxin':'16车厢，05号上铺'，'outticketdetail':'于2015-07-19 苏州园区站制票成功'}]//改签车票详情*/
						cpj.put("passengersename", p.getUser_name());
						cpj.put("ticket_no", c.getOut_ticket_billno());
						cpj.put("piaotypename", TongChengConsts.getPiaotypename(p.getTc_ticket_type()));
						
						/*1.  已取票，表示客户已在车站窗口或取票机 取得了车票，因此如果有退款，可能会有
						以下三种可能：取了之后退票；取了之后
						改签；还有可能取了票之后再改签，然后 再退票

						4.  已在线退票，表示通过我们双方的系统退 票的
						5.  已线下退票，表示客户直接在窗口退票
						6.  已出票，表示客户已经支付购票款项
						7.  待出票，表示尚未支付票款*/
						String status="";
						String statusid="";
						if("34".equals(change_status)){
							status="已出票";
							statusid="6";
						}else{
							status="待出票";
							statusid="7";
						}
						
						
						/*else if("11".equals(change_status)||"12".equals(change_status)
							||"13".equals(change_status)||"14".equals(change_status)
							||"31".equals(change_status)||"32".equals(change_status)
							||"33".equals(change_status)||"36".equals(change_status)){
							//11、改签预订 12、正在改签预订 13、人工改签预订 14、改签成功等待确认
							// 31、开始支付 32、正在支付 33、人工支付 34、支付成功 35、支付失败 36、补价支付
							status="待出票";
							statusid="7";
						}*/
						cpj.put("status", status);
						cpj.put("statusid", statusid);
						cpj.put("price", p.getBuy_money());
						if(p.getChange_train_box()!=null){
							cpj.put("cxin", p.getChange_train_box()+"车厢,"+p.getChange_seat_no());
						}else{
							cpj.put("cxin", "");
						}
						cpj.put("outticketdetail", "");
						arr_tickets.add(cpj);
					}
					cj.put("ticketstatus", arr_tickets.toString());
					

					arr_c.add(cj);
				}
				
				
			}
			json.put("changeorderdetail", arr_c);
			
			printJson(response,json.toString());
		} catch (Exception e) {
			json.put("success", false);
			json.put("code", 500);
			json.put("msg", "系统异常");
			printJson(response,json.toString());
			logger.info("同程-查询订单详情异常,msg:"+e);
		}
		
	} 
	

	/**
	 * 线上退票
	 */
	private void returnTicket(JSONObject webJson,HttpServletRequest request, 
			HttpServletResponse response) { 
		/*orderid	1~32	string	同程订单号
		transactionid	1~32	string	供应商交易单号
		ordernumber	1~32	string	取票单号
		reqtoken	1~32	string	请求特征
		callbackurl	1~32	string	异步通知接口回调地址
		tickets	1~1024	string	车票信息（json字符串数组形式，主要包含车票的乘车人信息，乘车人姓名、乘车人证件类型ID和乘车人证件号码，如：[{“ticket_no”:“E2610890401070051”, “passengername”:“王二”,“ passporttypeseid”:1,“passportseno”:“421116198907143795”}]*/
		String orderid=getJsonValue(webJson,"orderid");
		String transactionid=getJsonValue(webJson,"transactionid");
		String ordernumber=getJsonValue(webJson,"ordernumber");
		String reqtoken=getJsonValue(webJson,"reqtoken");
		String callbackurl=getJsonValue(webJson,"callbackurl");
		String tickets=getJsonValue(webJson,"tickets");
		JSONArray arr=JSONArray.fromObject(tickets);
		int size=arr.size();
		/**接口同步返回json字符串*/
		JSONObject json=new JSONObject();	
		json.put("orderid",orderid);
		json.put("ordernumber",ordernumber);
		json.put("reqtoken", reqtoken);
		DBOrderInfo orderInfo=orderService.queryOrderInfo(orderid);
		if(size<=0||orderInfo==null||(!Consts.ELONG_ORDER_SUCCESS.equals(orderInfo.getOrder_status()))){
			logger.info("同程-线上退票ERROR,order_id:"+orderid+"订单不存在或者状态错误");
			json.put("success", false);
			json.put("code",118);
			json.put("msg", "车票不存在或车票状态不正确");
			json.put("tooltip", "");
			printJson(response,json.toString());
			return;
		}
		String tooltip="";
		try {
			boolean returnTicket=true;
			for(int i=0;i<size;i++){
				JSONObject jp=JSONObject.fromObject(arr.get(i));
				String cp_id=jp.getString("ticket_no");
				Object remarkOb=jp.get("remark");
				String remark=remarkOb==null?"":remarkOb.toString();
				
				logger.info("开始退票"+cp_id);
				Map<String,String> paramMap=new HashMap<String, String>();
				paramMap.put("cp_id", cp_id);//新票id
				paramMap.put("order_id", orderid);
				paramMap.put("refund_seq", CreateIDUtil.createID("TCTK"));
				paramMap.put("refund_type", "11");//线上退票 
				String status=orderService.queryRefundStatus(paramMap);//查询退款状态
				paramMap.put("refund_type", "22");//线下退款
				String offLineStatus=orderService.queryRefundStatus(paramMap);//查询退款状态
				paramMap.put("refund_type", "55");//改签退票
				String changeStatus=orderService.queryRefundStatus(paramMap);//查询退款状态
				
				
				if(offLineStatus==null&&status==null&&changeStatus==null&&!"0".equals(remark)){//线上退款
					String buy_money_str=elongOrderService.queryCpPayMoney(paramMap);
					String from_time ="";
					//String from_time_change ="";
					boolean isChangeRefund=false;
					if(buy_money_str==null){
						//改签退票
						buy_money_str=elongOrderService.queryChangeCpPayMoney(paramMap);
						paramMap.put("refund_type", "55");//改签退票
						from_time=elongOrderService.queryChangeFromTime(paramMap);//改签后出发时间
						//from_time=orderInfo.getFrom_time()+":00";
						isChangeRefund=true;
						logger.info("改签退票开始,buy_money_str:"+buy_money_str+",from_time:"+from_time);
					}else{
						paramMap.put("refund_type", "11");//线上退票
						from_time= orderInfo.getFrom_time()+":00";
					}
					
					/**插入新退款记录*/
					String refund_money = "0";
					//String start_time = orderInfo.getFrom_time()+":00";
					//start_time = DateUtil.dateToString(DateUtil.stringToDate(start_time,DateUtil.DATE_FMT1),DateUtil.DATE_FMT1);
					String from_15d = DateUtil.dateAddDaysFmt3(from_time,"-15");
					String from_24 = DateUtil.dateAddDaysFmt3(from_time,"-1");
			        String from_48 = DateUtil.dateAddDaysFmt3(from_time,"-2");
			        double feePercent = 0;
			        
			        /*if(isChangeRefund){
			        	double feePercent_old=0;
			        	double feePercent_change=0;
			        	if(new Date().before(DateUtil.stringToDate(from_15d, "yyyy-MM-dd"))){
			        		feePercent_old = 0.0;
				        }else if(new Date().before(DateUtil.stringToDate(from_48, "yyyy-MM-dd HH:mm:ss"))){
				        	feePercent_old = 0.05;
				        }else if(new Date().before(DateUtil.stringToDate(from_24, "yyyy-MM-dd HH:mm:ss"))){
				        	feePercent_old = 0.1;
				        }else{
				        	feePercent_old = 0.2;
				        }
			        	
			        	
			        	String from_15d_change= DateUtil.dateAddDaysFmt3(from_time_change,"-15");
						String from_24_change = DateUtil.dateAddDaysFmt3(from_time_change,"-1");
				        String from_48_change = DateUtil.dateAddDaysFmt3(from_time_change,"-2");
			        	if(new Date().before(DateUtil.stringToDate(from_15d_change, "yyyy-MM-dd"))){
			        		feePercent_change = 0.0;
				        }else if(new Date().before(DateUtil.stringToDate(from_48_change, "yyyy-MM-dd HH:mm:ss"))){
				        	feePercent_change = 0.05;
				        }else if(new Date().before(DateUtil.stringToDate(from_24_change, "yyyy-MM-dd HH:mm:ss"))){
				        	feePercent_change = 0.1;
				        }else{
				        	feePercent_change = 0.2;
				        }
			        	
			        	if(feePercent_old>=feePercent_change){
			        		feePercent=feePercent_old;
			        	}else{
			        		feePercent=feePercent_change;
			        	}
			        }else{*/
			        
			        /*春运期间，改签票退款均为20%手续费*/
			        //2016年春运自2016年1月24日开始至2016年3月3日
			        boolean isSpringFestival = false;
			        Date startDate = DateUtil.stringToDate("2016-01-24 00:00:00", "yyyy-MM-dd HH:mm:ss");
			        Date endDate = DateUtil.stringToDate("2016-03-03 23:59:59", "yyyy-MM-dd HH:mm:ss");
			        Date changeFromDate = DateUtil.stringToDate(from_time, "yyyy-MM-dd HH:mm:ss");
			        isSpringFestival = changeFromDate.after(startDate) && changeFromDate.before(endDate);
			        /*春运判断结束*/
			        
			        if(isChangeRefund && isSpringFestival) {
			        	feePercent = 0.2;
			        } else {
			        	if(new Date().before(DateUtil.stringToDate(from_15d, "yyyy-MM-dd HH:mm:ss"))){
			        		feePercent = 0.0;
			        		/*改签票退款新代码start*/
			        		if(isChangeRefund) {
			        			//改签票改签后发车时间在15天以外 要判断原票出发时间是否在15天以内
			        			String old_from_time = orderInfo.getFrom_time()+":00";//改签前出发时间
			        			String old_from_15d = DateUtil.dateAddDaysFmt3(old_from_time,"-15");
			        			if(new Date().after(DateUtil.stringToDate(old_from_15d, "yyyy-MM-dd HH:mm:ss"))) {
			        				feePercent = 0.05;
			        			}
			        		}
			        		/*改签票退款新代码end*/
			        	}else if(new Date().before(DateUtil.stringToDate(from_48, "yyyy-MM-dd HH:mm:ss"))){
			        		feePercent = 0.05;
			        	}else if(new Date().before(DateUtil.stringToDate(from_24, "yyyy-MM-dd HH:mm:ss"))){
			        		feePercent = 0.1;
			        	}else{
			        		feePercent = 0.2;
			        	}
			        }
		        	
		        	  
			        
					double total_refund_money = 0;
					double buy_money = Double.parseDouble(buy_money_str);
					double sxf = 0.0;
			        if(feePercent!=0){
			              sxf = AmountUtil.quarterConvert(AmountUtil.mul(buy_money, feePercent));//手续费
			              sxf = sxf > 2 ? sxf : 2;
			        }
					total_refund_money += AmountUtil.ceil(AmountUtil.sub(buy_money, sxf));
					refund_money = String.valueOf(total_refund_money);
					if(buy_money<=2&&feePercent!=0){
						refund_money=String.valueOf(0);
						//退票状态直接置于退款成功状态
						paramMap.put("refund_status", "11");
					}
					paramMap.put("refund_status", "00");//00：等待机器改签
					paramMap.put("refund_money", refund_money);
					paramMap.put("channel", "tongcheng");
					paramMap.put("reqtoken", reqtoken);
					paramMap.put("callbackurl", callbackurl);
					orderService.insertRefundOrder(paramMap);
					
					ElongOrderLogsVo log=new ElongOrderLogsVo();
					log.setOpt_person("elong_app");
					log.setContent("同程申请"+(isChangeRefund?"改签退票":"线上退票")+"_成功["+paramMap.get("cp_id")+"]"+("11".equals(paramMap.get("refund_status"))?"2元及2元以下,直接置于退票成功":""));
					log.setOrder_id(paramMap.get("order_id"));
					elongOrderService.insertElongOrderLogs(log);
				}else if((offLineStatus==null||"22".equals(offLineStatus))&&(status==null||"22".equals(status))&&"0".equals(remark)){
					paramMap.put("refund_type", "44");
					
					paramMap.put("refund_status", "73");//用户生成线下退款(接口推送)
					paramMap.put("channel", "tongcheng");
					paramMap.put("reqtoken", reqtoken);
					paramMap.put("callbackurl", callbackurl);
					orderService.insertRefundOrder(paramMap);
					
					ElongOrderLogsVo log=new ElongOrderLogsVo();
					log.setOpt_person("elong_app");
					log.setContent("同程申请线下退款_成功["+paramMap.get("cp_id")+"]");
					log.setOrder_id(paramMap.get("order_id"));
					elongOrderService.insertElongOrderLogs(log);
				}else{
					tooltip=tooltip+cp_id+",";
					returnTicket=false;
				}
			}
			if(returnTicket){
				json.put("success", true);
				json.put("code",802);
				json.put("msg", "操作请求已接受");
				json.put("tooltip", "");
				printJson(response,json.toString());
			}else{
				json.put("success", false);
				json.put("code",117);
				json.put("msg", "操作请求已接受");
				json.put("tooltip", tooltip+"已经申请线下退票");
				printJson(response,json.toString());
			}
		} catch (Exception e) {
			logger.info("同程线上退票异常"+e);
			e.printStackTrace();
			json.put("success", false);
			json.put("code",500);
			json.put("msg", "系统异常");
			json.put("tooltip", "");
			printJson(response,json.toString());
		}
	} 
	
	/**
	 * 请求改签
	 */
	private void trainRequestChange(JSONObject webJson,HttpServletRequest request, 
			HttpServletResponse response) {
		/*
		 * 1.  改签必须不晚于原票开车前 30分钟方可进行。
		 * 2.  只能对未换取纸质车票的车票进行改签。
		 * 3.  同一订单中相同日期、车次、发站、到站、席别的车票方可批量改签。
		 * 4.  批量改签时，原票不能是卧铺。
		 * 5.  批量改签时，选择的新票座位席别必须一致，并且不能是卧铺。
		 * 6.  一张车票只能办理一次改签，改签后的新票不能再改签，但可退票。
		 */
		/*参数*/
		String orderid = getJsonValue(webJson, "orderid");//同程订单号
		String transactionid = getJsonValue(webJson, "transactionid");//交易单号
		String ordernumber = getJsonValue(webJson, "ordernumber");//取票单号
		String change_checi = getJsonValue(webJson, "change_checi");//改签新车票的车次
		String change_datetime = getJsonValue(webJson, "change_datetime");//改签新车票出发时间，格式 yyyy-MM-dd HH:mm:ss，如：2014-05-30 17:32:00
		String change_zwcode = getJsonValue(webJson, "change_zwcode");//改签新车票的座位席别编码
		String old_zwcode = getJsonValue(webJson, "old_zwcode");//原票的座位席别编码
		String ticketinfo = getJsonValue(webJson, "ticketinfo");//改签车票信息，json数组
		String isasync = getJsonValue(webJson, "isasync");//是否异步 Y、是 N、否
		String callbackurl = getJsonValue(webJson, "callbackurl");//改签占座异步回调地址
		String reqtoken = getJsonValue(webJson, "reqtoken");//请求特征值
		Boolean isChangeTo = getBooleanJsonValue(webJson,"isChangeTo");//true：变更到站  false：改签
		String from_station_name=getJsonValue(webJson, "from_station_name");//出发站名称
		String from_station_code=getJsonValue(webJson, "from_station_code");//出发站简码[可空]
		String to_station_name=getJsonValue(webJson, "to_station_name");//到达站名称
		String to_station_code=getJsonValue(webJson, "to_station_code");//到达站简码[可空]
		String msg = isChangeTo?"变更到站":"改签";
		JSONArray tickets = JSONArray.fromObject(ticketinfo);
		
		logger.info("同程请求"+msg+",orderId : " + orderid + "车票信息 : " + ticketinfo);
		
		JSONObject json = new JSONObject();
		json.put("success", true);
		json.put("code", 100);
		json.put("msg", "请求"+msg+"成功");
		
		TongchengChangeLogVO log=new TongchengChangeLogVO();
		log.setOpt_person("tongcheng_app");
		log.setContent("同程请求"+msg+"失败");
		log.setOrder_id(orderid);
		
		try {
			/*业务参数检查*/
			if("".equals(orderid) || "".equals(transactionid) || 
					"".equals(ordernumber) || "".equals(change_checi) || 
					"".equals(change_datetime) || "".equals(change_zwcode) || 
					"".equals(old_zwcode) || "".equals(ticketinfo) || "".equals(change_datetime)
					|| "".equals(from_station_name)|| "".equals(to_station_name)) {
				json.put("success", false);
				json.put("code", 107);
				json.put("msg", "业务参数缺失");
				printJson(response, json.toString());
				return;
			}
			
			/*查询订单*/
			DBOrderInfo orderInfo = orderService.queryOrderInfo(orderid);
			
			if(orderInfo == null || orderInfo.getOrder_status() == null){
				/*订单不存在*/
				logger.info("同程-请求"+msg+"ERROR,订单不存在order_id:"+orderid);
				json.put("success", false);
				json.put("code",402);
				json.put("msg", "订单不存在");
				printJson(response,json.toString());
				return;
			}
			
			/*检查订单状态*/
			String orderStatus = orderInfo.getOrder_status();
			if(!"33".equals(orderStatus)) {
				/*出票成功才可以改签*/
				logger.info("同程-请求"+msg+"ERROR,订单状态不正确");
				json.put("success", false);
				json.put("code",112);
				json.put("msg", "订单状态不正确");
				printJson(response,json.toString());
				return;
			}
			
			/*查询该订单号下的改签特征值，排除重复请求*/
			List<String> reqtokens = tongChengChangeService.getChangeReqtokens(orderid);
			
			for(String _reqtoken : reqtokens) {
				if(reqtoken.equals(_reqtoken)) {
					logger.info("同程-请求"+msg+"ERROR,该请求已存在");
					json.put("success", false);
					json.put("code",117);
					json.put("msg", "该请求已存在，请勿重复提交");
					printJson(response,json.toString());
					return;
				}
			}
			/*改签时间验证*/
			Date old_from_time = DateUtil.stringToDate(orderInfo.getFrom_time(), DateUtil.DATE_FMT3);
			//变更到站         
			if(isChangeTo){
				if(DateUtil.minuteDiff(old_from_time, new Date()) < 48*60) {
					/*距离发车时间小于48小时*/
					logger.info("同程-请求变更到站ERROR,距离开车时间太近无法变更到站");
					json.put("success", false);
					json.put("code",1002);
					json.put("msg", "距离开车时间太近无法变更到站");
					printJson(response,json.toString());
					return;
				}
			}
			else{
				if(DateUtil.minuteDiff(old_from_time, new Date()) < 30) {
					/*距离发车时间小于30分*/
					logger.info("同程-请求改签ERROR,距离开车时间太近无法改签");
					json.put("success", false);
					json.put("code",1002);
					json.put("msg", "距离开车时间太近无法改签");
					printJson(response,json.toString());
					return;
				}
			}
			/*改签*/
			if(tickets.size() == 0) {
				logger.info("同程-请求"+msg+"ERROR,没有车票信息");
				json.put("success", false);
				json.put("code",108);
				json.put("msg", "没有车票信息");
				printJson(response,json.toString());
				return;
			}
			String seat_type_19e = TongChengConsts.get19eSeatType(old_zwcode);
			String change_seat_type_19e = TongChengConsts.get19eSeatType(change_zwcode);
			if(tickets.size() > 1) {
				/*批量改签*/
				if(seat_type_19e.equals(TongChengConsts.SEAT_TYPE_4) ||
						seat_type_19e.equals(TongChengConsts.SEAT_TYPE_5) ||
						seat_type_19e.equals(TongChengConsts.SEAT_TYPE_6) ||
						change_seat_type_19e.equals(TongChengConsts.SEAT_TYPE_4) ||
						change_seat_type_19e.equals(TongChengConsts.SEAT_TYPE_5) ||
						change_seat_type_19e.equals(TongChengConsts.SEAT_TYPE_6)) {
					/*批量改签原票坐席不能为卧铺*/
					logger.info("同程-请求改签ERROR,批量改签原票或新票坐席不能为卧铺");
					json.put("success", false);
					json.put("code",108);
					json.put("msg", "批量改签原票或新票坐席不能为卧铺");
					printJson(response,json.toString());
					return;
				}
			} 
			
			Map<String, Object> param = new HashMap<String, Object>();//参数
			
			/*组装改签车票信息*/
			DBChangeInfo changeInfo = new DBChangeInfo();
			List<DBPassengerChangeInfo> changePassengers = new ArrayList<DBPassengerChangeInfo>();
	//		Double totalBuyMoney = 0.0;
			for(int i = 0; i < tickets.size(); i++) {
				/*传入的参数数据*/
				JSONObject ticket = tickets.getJSONObject(i);
				DBPassengerChangeInfo cp = tongChengChangeService.getChangeCp(ticket.getString("old_ticket_no"));
				if(cp !=  null) {
					/*每张车票只能改签一次*/
					if(cp.getIs_changed().equals("Y")){
						logger.info("同程-请求改签ERROR,车票已"+msg+"过,车票id:" + cp.getCp_id());
						json.put("success", false);
						json.put("code",108);
						json.put("msg", "车票已"+msg+"过,车票id:" + cp.getCp_id());
						printJson(response,json.toString());
						return;
					}
				} else {
					cp = new DBPassengerChangeInfo();
				}
				String new_cp_id = CreateIDUtil.createID("tc");
				cp.setOrder_id(orderid);//订单id
				cp.setCp_id(ticket.getString("old_ticket_no"));//车票id(原票)
				cp.setNew_cp_id(new_cp_id);//改签后车票id
				cp.setTc_change_seat_type(change_zwcode);//同程改签后新座位席别
				cp.setChange_seat_type(change_seat_type_19e);//19e改签后新座位席别
				cp.setTicket_type(TongChengConsts.get19eTicketType(ticket.getString("piaotype")));//19e车票类型
				cp.setTc_ticket_type(ticket.getString("piaotype"));//同程车票类型
				cp.setTc_ids_type(ticket.getString("passporttypeseid"));//同程证件类别编号
				cp.setIds_type(TongChengConsts.get19eIdsType(ticket.getString("passporttypeseid")));//19e证件类别编号
				cp.setUser_ids(ticket.getString("passportseno"));//证件号码
				cp.setUser_name(ticket.getString("passengersename"));//乘客姓名
				cp.setIs_changed("N");
				
				/*原票信息*/
				DBPassengerInfo p = getCp(cp.getCp_id(), orderInfo);
				if(p != null) {
					cp.setBuy_money(p.getBuy_money());//成本价格
	//				totalBuyMoney += Double.parseDouble(p.getBuy_money());
					cp.setSeat_no(p.getSeat_no());//座位号
					cp.setSeat_type(p.getSeat_type());//座位席别
					cp.setTrain_box(p.getTrain_box());//车厢
				}
				changePassengers.add(cp);
			}
			/*组装改签记录信息*/
			changeInfo.setTravel_time(DateUtil.dateToString(DateUtil.stringToDate(orderInfo.getFrom_time(), "yyyy-MM-dd"), "yyyy-MM-dd"));//乘车日期
			String change_travel_time = DateUtil.dateToString(DateUtil.stringToDate(change_datetime, "yy-MM-dd HH:mm:ss"), "yy-MM-dd");
			changeInfo.setChange_travel_time(change_travel_time);//改签后乘车日期
			changeInfo.setTrain_no(orderInfo.getTrain_no());//车次
			changeInfo.setChange_train_no(change_checi);//改签后车次
			changeInfo.setFrom_time(orderInfo.getFrom_time());//发车时间
			changeInfo.setChange_from_time(change_datetime);//改签后发车时间
			changeInfo.setFrom_city(from_station_name);//出发车站
			changeInfo.setTo_city(to_station_name);//到达车站
			changeInfo.setFrom_station_code(from_station_code);
			changeInfo.setTo_station_code(to_station_code);
			changeInfo.setIschangeto(isChangeTo);
			changeInfo.setOut_ticket_billno(orderInfo.getOut_ticket_billno());//12306单号
			changeInfo.setOrder_id(orderid);
			changeInfo.setIsasync(isasync);
			changeInfo.setCallbackurl(callbackurl);
			changeInfo.setReqtoken(reqtoken);
			changeInfo.setChange_status(TongChengChangeService.TRAIN_REQUEST_CHANGE);
			changeInfo.setcPassengers(changePassengers);//改签、车票关系
			String acc_id = tongChengChangeService.getAccountId(orderid);
			changeInfo.setAccount_id(acc_id);//出票账号id
			changeInfo.setMerchant_id("tongcheng");
			/*改签信息入库*/
			tongChengChangeService.addChangeInfo(changeInfo, true);
			int change_id = changeInfo.getChange_id();
			log.setChange_id(change_id);
			
			/*改签票信息入库*/
	//	elongOrderService.addChangeCp(changePassengers);
			
			JSONArray newTickets = new JSONArray();
			double diffRate= TongChengConsts.getDiffRate(orderInfo.getFrom_time(), new Date());
			//double diffRate_before = TongChengConsts.getDiffRate(orderInfo.getFrom_time(), new Date());
			//double diffRate_change=TongChengConsts.getDiffRate(change_datetime, new Date());
			//diffRate=(diffRate_before>=diffRate_change)?diffRate_before:diffRate_change;
			//logger.info("diffRate about order_id is:"+changeInfo.getOrder_id()+",diffRate_before:"+diffRate_before+",diffRate_change:"+diffRate_change+",diffRate:"+diffRate);
			
			double totalBuyMoney = 0.0;
			double totalChangeBuyMoney = 0.0;
			double totalDiff = 0.0;
			double totalPriceDiff = 0.0;
			double fee = 0.0;
			Integer priceInfoType = null;
			String priceInfo = "";
			String helpInfo = "";
			
			if("Y".equals(isasync)) {
				/*异步*/
				
				json.put("msg", "请求"+msg+"成功，异步回调");
				logger.info("同程请求"+msg+"异步成功,orderId : " + orderid + "车票信息 : " + ticketinfo);
				
				/*将改签信息记录到回调通知表用于异步回调*/
				changeInfo.setChange_notify_count(0);
				changeInfo.setChange_notify_status(TongChengChangeService.CHANGE_NOTIFY_PRE);
				tongChengChangeService.updateChangeInfo(changeInfo, false);
				log.setContent("同程请求"+msg+"异步success");
			} else {
				/*同步*/
				
				/*入库后扫描elong_change_cp表中车票的状态来确定预订结果*/
				long startBook = System.currentTimeMillis();
				param.put("change_id", change_id);
				param.put("change_status_list", new String[]{TongChengChangeService.TRAIN_REQUEST_CHANGE_SUCCESS, TongChengChangeService.TRAIN_REQUEST_CHANGE_FAIL});
				
				/*扫描结果，延迟5秒开始扫描，扫描95秒，共100秒*/
				DBChangeInfo checkChangeInfo = scanResult(param, Consts.TC_SCAN_TIMEOUT, Consts.TC_DELAY);
	//			long interval = 5000L;
	//			long start = 0L;
	//			while(start < 100 * 1000) {
	//				checkChangeInfo = tongChengChangeService.getChangeInfo(param, false);
	//				if(checkChangeInfo != null) break;
	//				try {
	//					Thread.sleep(interval);
	//				} catch (InterruptedException e) {
	//					e.printStackTrace();
	//				}
	//				start += interval;
	//			}
				
	//		放到业务层循环拉库总是返回第一次查询的结果，无法完成功能原因不明(可能是事务的原因,也可能是缓存机制？)
	//		List<DBPassengerChangeInfo> cPassengers = elongOrderService.getChangeResult(param, 30 * 1000L);
				if(checkChangeInfo == null) {
					/*改签预订超时失败*/
					logger.info("同程-请求"+msg+"ERROR,超时失败,已转为异步");
					json.put("success", false);
					json.put("code",506);
					json.put("msg", "超时失败，已转为异步");
					isasync = "Y";
					/*异步*/
					
					/*将改签信息记录到回调通知表用于异步回调*/
					changeInfo.setChange_notify_count(0);
					changeInfo.setIsasync(isasync);
					changeInfo.setChange_notify_status(TongChengChangeService.CHANGE_NOTIFY_PRE);
					tongChengChangeService.updateChangeInfo(changeInfo, false);
					log.setContent("同程同步"+msg+"预订超时失败，已转为异步");
				} else {
					long endBook = System.currentTimeMillis();
					/*响应结果*/
					//新车票数据组装
					/*
					 * [{'piaotype':'1','passportseno':'120101198412093061',
					 *  'new_ticket_no':'E382344856101011A','old_ticket_no':'E382344856105021A',
					 *  'price':'340.0','cxin:''01 车厢,01A 号'}]其中的 new_ticket_no、price 和 cxin 在改签成 功才有值，否则为空字符串
					 * */
					changeInfo = tongChengChangeService.getChangeInfo(param, true);
					int	cp_num=changeInfo.getcPassengers().size();
					for(DBPassengerChangeInfo cPassenger : changeInfo.getcPassengers()) {
						JSONObject newTicket = new JSONObject();
						if(changeInfo.getChange_status().equals(TongChengChangeService.TRAIN_REQUEST_CHANGE_SUCCESS)) {
							/*改签成功*/
							newTicket.put("new_ticket_no", cPassenger.getNew_cp_id());
							newTicket.put("price", cPassenger.getChange_buy_money());
							newTicket.put("cxin", cPassenger.getChange_train_box() + "," + cPassenger.getChange_seat_no());
							totalBuyMoney += Double.parseDouble(cPassenger.getBuy_money());//改签之前总成本价
							totalChangeBuyMoney += Double.parseDouble(cPassenger.getChange_buy_money());//改签之后总成本价
							totalDiff = totalChangeBuyMoney - totalBuyMoney;//总差价
							if(totalDiff < 0.0) {//新票款低于原票款
								priceInfoType = 3;
								priceInfo = "退还票款差额:" + totalDiff + "元";
							} else if(totalDiff == 0.0) {//新票款等于原票款
								priceInfoType = 2;
								priceInfo = "改签票款差价:0.0元";
							} else if(totalDiff > 0.0) {//新票款大于原票款
								priceInfoType = 1;
								priceInfo = "收取新票款:" + totalChangeBuyMoney + "元,退还原票票款:" + totalBuyMoney + "元";
							}
							json.put("pricedifference", totalDiff);
							log.setContent("同程同步改签占座成功!");
						} else if(changeInfo.getChange_status().equals(TongChengChangeService.TRAIN_REQUEST_CHANGE_FAIL)) {
							/*改签失败*/
							newTicket.put("new_ticket_no", "");
							newTicket.put("price", "");
							newTicket.put("cxin", "");
							priceInfoType = 0;
							String code = changeInfo.getFail_reason();
							helpInfo = TongChengConsts.getTcChangeErrorInfo(code);
							if(StrUtil.isEmpty(helpInfo)) {
								code = "506";
								helpInfo = "未知系统异常";
							}
							
							if("9991".equals(code)){
								code="999";
								helpInfo="旅游票,请到车站办理";
							}
							json.put("success", false);
							json.put("code", Integer.valueOf(code));
							json.put("msg", helpInfo);
							
							log.setContent("同程同步改签占座失败!");
						}
						newTicket.put("piaotype", cPassenger.getTc_ticket_type());
						newTicket.put("passportseno", cPassenger.getUser_ids());
						newTicket.put("old_ticket_no", cPassenger.getCp_id());
						newTickets.add(newTicket);
					}
					if(priceInfoType != null) {
						/*改签成功后生成流水号、计算手续费*/
						if(priceInfoType == 3) {//新票款低于原票款
							String ticketPriceDiffChangeSerial = CreateIDUtil.createID("TCC");
							changeInfo.setTicket_price_diff_change_serial(ticketPriceDiffChangeSerial);
							changeInfo.setChange_diff_money(totalDiff + "");
							if(diffRate==0){
								fee=0;
							}else{
								fee = AmountUtil.quarterConvert(Math.abs(AmountUtil.mul(totalDiff, diffRate)));//手续费=退款差额 * 费率
								int less_fee=2*cp_num;
								fee=fee<less_fee?less_fee:fee;
								if(fee>Math.abs(totalDiff)){
									fee=Math.abs(totalDiff);
								}
							}
							totalPriceDiff = AmountUtil.sub(Math.abs(totalDiff), fee);//实际退款=退款差额-手续费
							changeInfo.setFee(fee+"");
							changeInfo.setDiffrate(diffRate+"");
							changeInfo.setTotalpricediff(totalPriceDiff+"");
						} else if(priceInfoType == 1) {//新票款大于原票款
							String oldTicketChangeSerial = CreateIDUtil.createID("TCC");
							String newTicketChangeSerial = CreateIDUtil.createID("TCC");
							changeInfo.setOld_ticket_change_serial(oldTicketChangeSerial);
							changeInfo.setNew_ticket_change_serial(newTicketChangeSerial);
							changeInfo.setChange_refund_money(totalBuyMoney + "");
							changeInfo.setChange_receive_money(totalChangeBuyMoney + "");
						}
						json.put("diffrate", diffRate);
						json.put("totalpricediff", totalPriceDiff);
						json.put("fee", fee);
					}
					
				}
				
			}
			json.put("priceinfotype", priceInfoType);
			json.put("priceinfo", priceInfo);
			json.put("help_info", helpInfo);
			json.put("newtickets", newTickets);
			json.put("orderid", orderid);
			json.put("transactionid", transactionid);
			json.put("reqtoken", reqtoken);
			//增加变更到站增加参数开始
			json.put("from_station_code", from_station_code);
			json.put("from_station_name", from_station_name);
			json.put("to_station_code", to_station_code);
			json.put("to_station_name", to_station_name);
			printJson(response, json.toString());
			if("N".equals(isasync)) {
				tongChengChangeService.updateChangeInfo(changeInfo, false);
			}
	
		} catch (Exception e) {
			logger.info("同程请求"+msg+"异常"+e);
			e.printStackTrace();
			json.put("success", false);
			json.put("code",506);
			json.put("msg", "系统异常");
			printJson(response,json.toString());
			log.setContent("同程请求"+msg+"占座异常!");
		} finally {
			tongChengChangeService.addTongChengChangeLog(log);
		}
	} 
	
	/**
	 * 取消改签
	 */
	private void trainCancelChange(JSONObject webJson,HttpServletRequest request, 
			HttpServletResponse response) { 
		String orderid = getJsonValue(webJson, "orderid");//同程订单号
		String transactionid = getJsonValue(webJson, "transactionid");//交易单号
		//3.2版本//String reqtoken = getJsonValue(webJson, "reqtoken");//请求特征值
		
		logger.info("同程取消改签,orderId : " + orderid);
		
		JSONObject json = new JSONObject();
		json.put("success", true);
		json.put("code", 100);
		json.put("msg", "取消改签成功");
		//3.2版本//json.put("reqtoken", reqtoken);
		
		TongchengChangeLogVO log=new TongchengChangeLogVO();
		log.setOpt_person("tongcheng_app");
		log.setContent("同程请求改签取消失败!");
		log.setOrder_id(orderid);
		
		/*检查业务参数*/
		if("".equals(orderid) || "".equals(transactionid)) {
			json.put("success", false);
			json.put("code", 107);
			json.put("msg", "业务参数缺失");
			printJson(response, json.toString());
			return;
		}
		
		/*查询订单*/
		DBOrderInfo orderInfo = orderService.queryOrderInfo(orderid);
		
		if(orderInfo == null || orderInfo.getOrder_status() == null){
			/*订单不存在*/
			logger.info("同程-取消改签ERROR,订单不存在order_id:"+orderid);
			json.put("success", false);
			json.put("code",402);
			json.put("msg", "订单不存在");
			printJson(response,json.toString());
			return;
		}
		try {
			/*查询改签预订票并更新状态*/
			Map<String, Object> param = new HashMap<String, Object>();//参数
			param.put("order_id", orderid);
			param.put("orderCol", "create_time");
			param.put("sort", "desc");
			param.put("limit", 1);
			param.put("change_status", TongChengChangeService.TRAIN_REQUEST_CHANGE_SUCCESS);
			DBChangeInfo changeInfo = tongChengChangeService.getChangeInfo(param, true);
			if(changeInfo == null) {
				param.remove("change_status");
				param.put("change_status_list", new String[]{TongChengChangeService.TRAIN_CANCEL_CHANGE_ING, TongChengChangeService.TRAIN_CANCEL_CHANGE});
				DBChangeInfo changeChangeInfo = tongChengChangeService.getChangeInfo(param, true);
				if(changeChangeInfo!=null){
					logger.info("同程-取消改签ERROR,订单正在取消order_id:"+orderid);
					json.put("success", false);
					json.put("code",1101);
					json.put("msg", "取消请求已接收，正在处理中");
					printJson(response,json.toString());
					return;
				}
				/*订单状态不正确*/
				logger.info("同程-取消改签ERROR,订单状态不正确order_id:"+orderid);
				json.put("success", false);
				json.put("code",112);
				json.put("msg", "订单状态不正确");
				printJson(response,json.toString());
				return;
			}
			log.setChange_id(changeInfo.getChange_id());
			
			Date currentTime = new Date();
			/*预订成功后的30分钟内才能取消改签*/
			Date bookTime = DateUtil.stringToDate(changeInfo.getBook_ticket_time(), DateUtil.DATE_FMT3);
			long minuteDiff = DateUtil.minuteDiff(currentTime, bookTime);
			if(minuteDiff > 30) {
				logger.info("同程-取消改签ERROR,距离改签车票预订时间超过30分钟");
				json.put("success", false);
				json.put("code",401);
				json.put("msg", "距离改签车票预订时间超过30分钟");
				printJson(response,json.toString());
				return;
			}
			/*将状态为14、预订成功的改签状态都改为21、改签取消*/
			changeInfo.setChange_status(TongChengChangeService.TRAIN_CANCEL_CHANGE);
//		List<DBPassengerChangeInfo> cPassengers = changeInfo.getcPassengers();
//		for(DBPassengerChangeInfo cPassenger : cPassengers) {
//			
//			String change_status = changeInfo.getChange_status();
//			/*if(change_status.equals(TongChengConsts.TRAIN_CONFIRM_CHANGE_OVER)) {
//				logger.info("同程-取消改签ERROR,已改签票订单不能取消");
//				json.put("success", false);
//				json.put("code",1001);
//				json.put("msg", "已改签票订单不能取消");
//				printJson(response,json.toString());
//				return;
//			} else */
//			
//		}
			/*更新改签状态*/
			//3.2版本//changeInfo.setReqtoken(reqtoken);
			tongChengChangeService.updateChangeInfo(changeInfo, false);
			
			/*入库后扫描elong_change_cp表中车票的状态来确定取消结果*/
			param.clear();
			param.put("change_id", changeInfo.getChange_id());
			param.put("change_status_list", new String[]{TongChengChangeService.TRAIN_CANCEL_CHANGE_SUCCESS, TongChengChangeService.TRAIN_CANCEL_CHANGE_FAIL});
			
			/*扫描结果，延迟5秒开始扫描，扫描95秒，共100秒*/
			DBChangeInfo checkChangeInfo = scanResult(param, Consts.TC_SCAN_TIMEOUT, Consts.TC_DELAY);
//			long interval = 5000L;
//			long start = 0L;
//			while(start < 100 * 1000) {
//				checkChangeInfo = tongChengChangeService.getChangeInfo(param, false);
//				if(checkChangeInfo != null) break;
//				try {
//					Thread.sleep(interval);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				start += interval;
//			}
//		不使用原因同请求改签
//		cPassengers = elongOrderService.getChangeResult(param, null);
			if(checkChangeInfo == null) {
				/*改签取消超时失败*/
				logger.info("同程-取消改签ERROR,超时失败");
				json.put("success", false);
				json.put("code",506);
				json.put("msg", "超时失败");
				printJson(response,json.toString());
				
				log.setContent("同程取消改签，超时失败");
				return;
			} else {
				if(checkChangeInfo.getChange_status().equals(TongChengChangeService.TRAIN_CANCEL_CHANGE_SUCCESS)) {
					json.put("orderid", orderid);
					log.setContent("同程取消改签成功！");
				} else if(checkChangeInfo.getChange_status().equals(TongChengChangeService.TRAIN_CANCEL_CHANGE_FAIL)){
					String code = changeInfo.getFail_reason();
					String helpInfo = TongChengConsts.getTcChangeErrorInfo(code);
					if(StrUtil.isEmpty(helpInfo)) {
						code = "506";
						helpInfo = "未知系统异常,取消失败";
					}
					json.put("success", false);
					json.put("code",Integer.valueOf(code));
					json.put("msg", helpInfo);
					log.setContent("同程取消改签失败,[" + helpInfo +"]！");
				}
			}
			printJson(response, json.toString());
		} catch (Exception e) {
			logger.info("同程取消改签异常"+e);
			e.printStackTrace();
			json.put("success", false);
			json.put("code",506);
			json.put("msg", "系统异常");
			printJson(response,json.toString());
			log.setContent("同程取消改签异常!");
		} finally {
			tongChengChangeService.addTongChengChangeLog(log);
		}
	} 
	
	
	/**
	 * 确认改签
	 */
	private void trainConfirmChange(JSONObject webJson,HttpServletRequest request, 
			HttpServletResponse response) {
		String orderid = getJsonValue(webJson, "orderid");//同程订单号
		String transactionid = getJsonValue(webJson, "transactionid");//交易单号
		String isasync = getJsonValue(webJson, "isasync");//是否异步 Y、是 N、否
		String callbackurl = getJsonValue(webJson, "callbackurl");//改签占座异步回调地址
		String reqtoken = getJsonValue(webJson, "reqtoken");//请求特征值
		
		logger.info("同程确认改签,orderId : " + orderid);
		
		JSONObject json = new JSONObject();
		json.put("success", true);
		json.put("code", 100);
		json.put("msg", "确认改签成功");
		
		TongchengChangeLogVO log=new TongchengChangeLogVO();
		log.setOpt_person("tongcheng_app");
		log.setContent("同程请求改签支付失败!");
		log.setOrder_id(orderid);
		
		/*检查业务参数*/
		if("".equals(orderid) || "".equals(transactionid)) {
			json.put("success", false);
			json.put("code", 107);
			json.put("msg", "业务参数缺失");
			printJson(response, json.toString());
			return;
		}
		
		/*查询订单*/
		DBOrderInfo orderInfo = orderService.queryOrderInfo(orderid);
		
		if(orderInfo == null || orderInfo.getOrder_status() == null){
			/*订单不存在*/
			logger.info("同程-确认改签ERROR,订单不存在order_id:"+orderid);
			json.put("success", false);
			json.put("code",402);
			json.put("msg", "订单不存在");
			printJson(response,json.toString());
			return;
		}
		
		try {
			/*查询改签预订票并更新状态*/
			Map<String, Object> param = new HashMap<String, Object>();//参数
			param.put("order_id", orderid);
			param.put("orderCol", "create_time");
			param.put("sort", "desc");
			param.put("limit", 1);
			param.put("change_status", TongChengChangeService.TRAIN_REQUEST_CHANGE_SUCCESS);
//		List<DBPassengerChangeInfo> cPassengers = elongOrderService.findRequestChangeCp(param);
//		DBChangeInfo changeInfo = elongOrderService.getChangeInfo(cPassengers.get(0).getChange_id(), false);
//		changeInfo.setcPassengers(cPassengers);
			DBChangeInfo changeInfo = tongChengChangeService.getChangeInfo(param, true);
			if(changeInfo != null && !changeInfo.getChange_status().equals(TongChengChangeService.TRAIN_REQUEST_CHANGE_SUCCESS)) {
				/*订单不存在*/
				logger.info("同程-确认改签ERROR,没有请求改签信息,order_id:"+orderid);
				json.put("success", false);
				json.put("code",112);
				json.put("msg", "没有请求改签信息");
				printJson(response,json.toString());
				return;
			}
			log.setChange_id(changeInfo.getChange_id());
			List<DBPassengerChangeInfo> cPassengers = changeInfo.getcPassengers();
			/*22:34:59*/
			Calendar time_22_44_59 = Calendar.getInstance();
			time_22_44_59.set(Calendar.HOUR_OF_DAY, 22);
			time_22_44_59.set(Calendar.MINUTE, 44);
			time_22_44_59.set(Calendar.SECOND, 59);
			/*23:30:00*/
			Calendar time_23_30_00 = Calendar.getInstance();
			time_23_30_00.set(Calendar.HOUR_OF_DAY, 23);
			time_23_30_00.set(Calendar.MINUTE, 30);
			time_23_30_00.set(Calendar.SECOND, 00);
			Calendar currentTime = Calendar.getInstance();
			Calendar bookTime = Calendar.getInstance();
			Date book = DateUtil.stringToDate(changeInfo.getBook_ticket_time(), DateUtil.DATE_FMT3);
			bookTime.setTime(book);
			
			JSONArray tickets = new JSONArray();//车票信息
			boolean timeOut = false;
			if(bookTime.before(time_22_44_59)) {//当天22:44:59之前预订
				/*30分钟的付款时间*/
				System.out.println("current : " + DateUtil.dateToString(currentTime.getTime(), "yyyy-MM-dd HH:mm:ss"));
				System.out.println("book : " + DateUtil.dateToString(book, "yyyy-MM-dd HH:mm:ss"));
				if(DateUtil.minuteDiff(currentTime.getTime(), book) > 30) {
					timeOut = true;
				}
				logger.info("当天22:44:59之前预订,30分钟的付款时间,timeout" + timeOut);
			} else if(bookTime.after(time_22_44_59)) {//当天22:44:59之后预订
				/*23:30:00之前付款*/
				if(currentTime.after(time_23_30_00)) {
					timeOut = true;
				}
				logger.info("当天22:44:59之后预订,23:30:00之前付款,timeout" + timeOut);
			}
			if(timeOut) {
				logger.info("同程-确认改签ERROR,确认改签的请求时间已超过规定时间");
				json.put("success", false);
				json.put("code",1003);
				json.put("msg", "确认改签的请求时间已超过规定时间");
				printJson(response,json.toString());
				return;
			}
			/*确认改签*/
			if(StrUtil.isNotEmpty(changeInfo.getChange_refund_money()) && StrUtil.isNotEmpty(changeInfo.getChange_receive_money())) {
				
				Double change_refund_money = Double.parseDouble(changeInfo.getChange_refund_money());
				Double change_receive_money = Double.parseDouble(changeInfo.getChange_receive_money());
				/**
				 * 改签流程已经优化，改签支付：高改低/平改/低改高 三种情况下 改签状态全部更新为31（开始支付）
				 * 其它渠道也需要保持一致,2016-11-16  add by wangsf
				 */
				if(change_receive_money > change_refund_money) {
					changeInfo.setChange_status(TongChengChangeService.TRAIN_CONFIRM_CHANGE);
				} else {
					changeInfo.setChange_status(TongChengChangeService.TRAIN_CONFIRM_CHANGE);
				}
			} else {
				changeInfo.setChange_status(TongChengChangeService.TRAIN_CONFIRM_CHANGE);
			}
			
			changeInfo.setCallbackurl(callbackurl);
			changeInfo.setReqtoken(reqtoken);
			/*更新车票改签状态*/
			tongChengChangeService.updateChangeInfo(changeInfo, false);
//		elongOrderService.updateChangeCp(cPassengers);
			
			if("Y".equals(isasync)) {
				/*异步*/
				json.put("msg", "确认改签成功，异步回调");
				logger.info("同程确认改签异步成功,orderId : " + orderid);
				
				/*将改签信息记录到回调通知表用于异步回调*/
				changeInfo.setChange_notify_count(0);
				changeInfo.setChange_notify_status(TongChengChangeService.CHANGE_NOTIFY_PRE);
				tongChengChangeService.updateChangeInfo(changeInfo, false);
				log.setContent("同程请求改签支付异步success");
			} else {
				/*同步*/
				
				/*入库后扫描elong_change_cp表中车票的状态来确定改签确认结果*/
				param.clear();
				param.put("change_id", changeInfo.getChange_id());
				param.put("change_status_list", new String[]{TongChengChangeService.TRAIN_CONFIRM_CHANGE_SUCCESS, TongChengChangeService.TRAIN_CONFIRM_CHANGE_FAIL});
				
				/*扫描结果，延迟5秒开始扫描，扫描95秒，共100秒*/
				DBChangeInfo checkChangeInfo = scanResult(param, Consts.TC_SCAN_TIMEOUT, Consts.TC_DELAY);
//				long interval = 5000L;
//				long start = 0L;
//				while(start < 100 * 1000) {
//					checkChangeInfo = tongChengChangeService.getChangeInfo(param, false);
//					if(checkChangeInfo != null) break;
//					try {
//						Thread.sleep(interval);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					start += interval;
//				}
//			不使用原因同请求改签
//			cPassengers = elongOrderService.getChangeResult(param, 30 * 1000L);
				if(checkChangeInfo == null) {
					/*改签确认超时失败*/
					logger.info("同程-确认改签ERROR,超时失败");
					json.put("success", false);
					json.put("code",506);
					json.put("msg", "超时失败，已转为异步");
					isasync = "Y";
					/*异步*/
					
					/*将改签信息记录到回调通知表用于异步回调*/
					changeInfo.setChange_notify_count(0);
					changeInfo.setIsasync(isasync);
					changeInfo.setChange_notify_status(TongChengChangeService.CHANGE_NOTIFY_PRE);
					tongChengChangeService.updateChangeInfo(changeInfo, false);
					log.setContent("同程请求改签支付超时失败，已转为异步");
				} else {
					if(checkChangeInfo.getChange_status().equals(TongChengChangeService.TRAIN_CONFIRM_CHANGE_SUCCESS)) {
						
						/*改签确认成功*/
						for(DBPassengerChangeInfo cPassenger : cPassengers) {
							JSONObject ticket = new JSONObject();
							ticket.put("new_ticket_no", cPassenger.getNew_cp_id());
							ticket.put("old_ticket_no", cPassenger.getCp_id());
							ticket.put("cxin", cPassenger.getChange_train_box() + "车厢," + cPassenger.getChange_seat_no());
							tickets.add(ticket);
							cPassenger.setIs_changed("Y");
						}
						json.put("oldticketchangeserial", changeInfo.getOld_ticket_change_serial() == null ? "" : changeInfo.getOld_ticket_change_serial());
						json.put("newticketchangeserial", changeInfo.getNew_ticket_change_serial() == null ? "" : changeInfo.getNew_ticket_change_serial());
						json.put("ticketpricediffchangeserial", changeInfo.getTicket_price_diff_change_serial() == null ? "" : changeInfo.getTicket_price_diff_change_serial());
						
						log.setContent("同程请求改签支付成功!");
					} else if(checkChangeInfo.getChange_status().equals(TongChengChangeService.TRAIN_CONFIRM_CHANGE_FAIL)) {
						String code = changeInfo.getFail_reason();
						String helpInfo = TongChengConsts.getTcChangeErrorInfo(code);
						if(StrUtil.isEmpty(helpInfo)) {
							code = "506";
							helpInfo = "未知系统异常";
						}
						json.put("success", false);
						json.put("code",Integer.valueOf(code));
						json.put("msg", helpInfo);
						
						log.setContent("同程请求改签支付失败!");
					}
				}
			}
			
			json.put("reqtoken", reqtoken);
			json.put("orderid", orderid);
			json.put("newticketcxins", tickets);
			
			printJson(response, json.toString());
			
			if("N".equals(isasync)) {
				tongChengChangeService.updateChangeCp(cPassengers);
			}
		} catch (Exception e) {
			logger.info("同程确认改签异常"+e);
			e.printStackTrace();
			json.put("success", false);
			json.put("code",506);
			json.put("msg", "系统异常");
			printJson(response,json.toString());
			log.setContent("同程请求改签支付异常!");
		} finally {
			tongChengChangeService.addTongChengChangeLog(log);
		}
	} 
	
	
	/**
	 * 车次信息查询
	 */
	private void getTrainInfo(JSONObject webJson, HttpServletRequest request, HttpServletResponse response) {
		String train_date = getJsonValue(webJson, "train_date");//乘车日期
		String from_station = getJsonValue(webJson, "from_station");//出发站简码
		String to_station = getJsonValue(webJson, "to_station");//到达站简码
		String train_no = getJsonValue(webJson, "train_no");//官方系统车次内部编码
		String train_code = getJsonValue(webJson, "train_code");//车次号
		
		JSONObject json=new JSONObject();
		json.put("success", true);
		json.put("code", 100);
		json.put("msg", "车次查询成功");
		/*业务参数检查*/
		if("".equals(train_date) || "".equals(from_station) || 
				"".equals(to_station) || "".equals(train_code)) {
			logger.info("业务参数缺失" + webJson);
			json.put("success", false);
			json.put("code", 107);
			json.put("msg", "业务参数缺失");
			printJson(response, json.toString());
			return;
		}
		
		/*调用内部系统接口*/
		String url = Consts.QUERY_TRAIN_INFO;
		//trainRobotInterface -->/tcQueryTrainInfo
		
		try {
			/*参数*/
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("train_no", train_no);
			paramMap.put("from_station", from_station);
			paramMap.put("to_station", to_station);
			paramMap.put("train_date", train_date);
			paramMap.put("train_code", train_code);
			paramMap.put("channel", Consts.CHANNEL_TONGCHENG);
			
			/*请求robot分发接口*/
			String params;
			params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
			logger.info("同程发起车次查询url:" + url + ",param:"+paramMap.toString());
			String result = HttpUtil.sendByPost(url, params, "UTF-8");
			
			logger.info("同程发起车次查询result"+result);
			
			if("NO_DATAS".equals(result)) {
				/*查询无数据*/
				logger.info("同程车次查询，没有符合条件的车次信息");
				json.put("success", false);
				json.put("code", 201);
				json.put("msg", "没有符合条件的车次信息");
				printJson(response, json.toString());
				return;
			} else if("ERROR".equals(result)) {
				/*查询失败*/
				logger.info("同程车次查询，查询失败");
				json.put("success", false);
				json.put("code", 202);
				json.put("msg", "查询失败");
				printJson(response, json.toString());
				return;
			}
			/*业务输出参数*/
			ObjectMapper mapper = new ObjectMapper();
			JsonNode trainInfoData = mapper.readTree(result);
			train_no = trainInfoData.path("train_no").getTextValue();
			
			JsonNode train_info_list = trainInfoData.path("train_info_list");
			JSONArray data = new JSONArray();
			
			int diffDay = 0;//列车从出发站到达目的站的运行天数
			String lastTime = "00:00";
			boolean isEnabled = false;
			if(train_info_list.size() > 0) {
				for(int i = 0; i < train_info_list.size(); i++) {
					JsonNode train_info = train_info_list.get(i);
					JSONObject j = new JSONObject();
					String arrive_time = train_info.path("arrive_time").getTextValue();
					isEnabled = train_info.path("isEnabled").getBooleanValue();
					if(i == 0) {
						/*第一个节点中包含额外信息*/
						json.put("start_station_name", train_info.path("start_station_name").getTextValue());//始发站站名
						json.put("end_station_name", train_info.path("end_station_name").getTextValue());//终到站站名
					} else {
						if(isEnabled && arrive_time.compareTo(lastTime) < 0) {
							diffDay++;
						}
//						lastTime = arrive_time;
					}
					lastTime = train_info.path("start_time").getTextValue();
					j.put("arrive_time", arrive_time);
					j.put("start_time", train_info.path("start_time").getTextValue());
					j.put("station_name", train_info.path("station_name").getTextValue());
					j.put("station_no", train_info.path("station_no").getTextValue());
					j.put("stopover_time", train_info.path("stopover_time").getTextValue());
					j.put("arrive_days", isEnabled ? diffDay : "----");
					data.add(j);
					
				}
				String trainType = TongChengConsts.getTongChengTrainType(train_code);
				json.put("train_type", trainType);//列车类型
				json.put("train_no", train_no);//12306车次内部编号
				json.put("train_code", train_code);//车次号
				json.put("data", data);//途径站信息
				printJson(response, json.toString());
			} else {
				logger.info("同程车次查询，没有符合条件的车次信息");
				json.put("success", false);
				json.put("code", 201);
				json.put("msg", "没有符合条件的车次信息");
				printJson(response, json.toString());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("同程发起车次查询,系统异常"+e);
			json.put("success", false);
			json.put("code",500);
			json.put("msg", "系统异常");
			printJson(response,json.toString());
			e.printStackTrace();
		}
		
	}
	
	
	/*
	 * 辅助接口方法区
	 * 
	 */
	
	
	/*
	 * 工具方法区
	 */
	
	/**
	 * 请求参数map
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> requestParamMap(HttpServletRequest request) {
		Enumeration parameterNames = request.getParameterNames();
		Map<String, String> map = new HashMap<String, String>();
		while(parameterNames.hasMoreElements()) {
			String parameterName = (String) parameterNames.nextElement();
			map.put(parameterName, getParam(request, parameterName));
		}
		return map;
	}
	
	/**
	 * 辅助功能验签
	 */
	private boolean validateSign(Map<String, String> parameterMap, String md5key) {
		
		/*签名*/
		String sign = parameterMap.get("sign");
		if(StrUtil.isEmpty(sign))
			return false;
		logger.info("同程输入验签sign：" +sign);
		
		
		/*请求参数排序并生成签名*/
		List<String> keyList = new ArrayList<String>(parameterMap.keySet());
		logger.info("排序前参数名集合：" + keyList.toString());
		Collections.sort(keyList);
		logger.info("升序排序后参数名集合：" + keyList.toString());
		
		/*拼接querystring*/
		StringBuilder builder = new StringBuilder();
		for(String key : keyList) {
			if("sign".equals(key))
				continue;
			builder.append(key.toLowerCase())
				.append("=")
				.append(parameterMap.get(key))
				.append("&");
		}
		builder.append("md5key=")
			.append(md5key);
		
		/*计算签名*/
		String signStr = builder.toString();
		logger.info("代加密参数字符串：" + signStr);
		String checkSign = ElongMd5Util.md5_32(signStr, "UTF-8").toLowerCase();
		logger.info("MD5加密签名字符串checkSign：" + checkSign);
		return sign.equals(checkSign);
	}
	
	/**
	 * 
	 * @param json
	 * @param name
	 * @return
	 */
	private String getJsonValue(JSONObject json,String name){
		return json.get(name)==null?"":json.getString(name);
		
	}
	private Boolean getBooleanJsonValue(JSONObject json,String name){
		return json.get(name)==null?false:json.getBoolean(name);
	}
	private DBPassengerInfo getCp(String cp_id, DBOrderInfo orderInfo) {
		for(DBPassengerInfo p : orderInfo.getPassengers()) {
			String cpId = p.getCp_id();
			if(cpId.equals(cp_id)) {
				return p;
			}
		}
		return null;
	}
	
	private DBChangeInfo scanResult(final Map<String, Object> param,final long timeout,final long delay) {
		
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		final SynchronousQueue<DBChangeInfo> queue = new SynchronousQueue<DBChangeInfo>(true);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				long interval = 2800L;
				DBChangeInfo checkChangeInfo = null;
				long start = System.currentTimeMillis();
				while(!Thread.interrupted()) {
					long current = System.currentTimeMillis();
					if((current - start) > (delay + timeout)) break;
					
					checkChangeInfo = tongChengChangeService.getChangeInfo(param, false);
					if(checkChangeInfo != null) {
						try {
							queue.put(checkChangeInfo);
						} catch (InterruptedException e) {
							continue;
						}
						break;
					}
					try {
						Thread.sleep(interval);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		try {
			return queue.poll(timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			return null;
		} 
	}
	
	public static void main(String[] args) {
		/*Date now=new Date();
		int hours=now.getHours();
		System.out.println(hours);*/
//		String ss="V111";
//		String seat_type="O";
//		if("M".equals(seat_type)||"O".equals(seat_type)){
//			String train_no=ss.toUpperCase();
//			System.out.println(train_no);
//			if(!train_no.contains("C")&&!train_no.contains("D")&&!train_no.contains("G")){
//				
//				System.out.println("M".equals(seat_type)?"7":"8");;
//			}else{
//				System.out.println(seat_type);
//			}
//			
//		}else{
//			System.out.println(seat_type);
//		}
		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();
		obj.put("arr", arr);
		obj.put("arr_1", "()");
		System.out.println(obj.toString());
	}

}
