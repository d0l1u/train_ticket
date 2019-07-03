package com.l9e.transaction.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoTrip;
import com.l9e.util.TrainPropUtil;

@Controller
@RequestMapping("/receiveNotify")
public class ReceiveNotifyController extends BaseController{
	
	private static final Logger logger = Logger.getLogger(ReceiveNotifyController.class);
	
	@Resource
	private OrderService orderService;
	
	/**
	 * 接收改签退票结果通知
	 * @param request
	 * @param response
	 */
	@RequestMapping("/refundNotify_no.jhtml")
	public void cpRefundNotify(HttpServletRequest request,
			HttpServletResponse response){ 
		String result = this.getParam(request, "result");//成功
		String orderId = this.getParam(request, "orderid");
		String cpid = this.getParam(request, "cpid");
		String alterdiffmoney = this.getParam(request, "alterdiffmoney");
		String refund12306money = this.getParam(request, "refund12306money");
		String refund12306seq = this.getParam(request, "refund12306seq");
		String status = this.getParam(request, "status");//0、改签通知 1、退票通知
		String refuse_reason = this.getParam(request, "refuse_reason");//拒绝退票原因
		
		logger.info("【接收退票系统通知】参数orderId=" + orderId 
				+ "，cpid=" + cpid + "，alterdiffmoney=" + alterdiffmoney
				+ "，refund12306money=" + refund12306money + "，refund12306seq=" + refund12306seq 
				+ "，status=" + status +", result="+result + ", refuse_reason="+refuse_reason);
		Map<String,String> map = new HashMap<String,String>();
		map.put("order_id", orderId);
		map.put("cp_id", cpid);
		map.put("alter_money", alterdiffmoney);
		map.put("refund_12306_money", refund12306money);
		map.put("refund_12306_seq", refund12306seq);
		map.put("refuse_reason", refuse_reason);
		
		int num = orderService.queryOrderCPCountByNo(orderId);
		try{
			if(num==1){
				if("0".equals(status) && TrainConsts.SUCCESS.equalsIgnoreCase(result)){
					orderService.updateCPAlterInfo(map);
				}else if("1".equals(status) && TrainConsts.SUCCESS.equalsIgnoreCase(result)){
					//订单内车票数
					map.put("refund_status", "11");
					map.put("notify_status", "00");
					//更新退款参数
					orderService.updateRefundInfoSingle(map);
				}else if("1".equals(status) && TrainConsts.FAILURE.equalsIgnoreCase(result)){
					map.put("refund_status", "22");
					map.put("notify_status", "00");
					orderService.updateRefundInfoSingle(map);
				}
				write2Response(response, "success");
			}else{
				orderService.updateCPAlterInfo(map);
				if("1".equals(status) && TrainConsts.SUCCESS.equalsIgnoreCase(result)){
					//查询未改签退款车票数
					int count=orderService.queryOrderCpNoRefundNum(orderId);
					if(count==0){
						List<Map<String,String>> list_info = orderService.queryOrderCpRefundInfoList(orderId);
						Double refund_12306_money = 0.0;
						Double alter_money = 0.0;
						for(Map<String,String> map_info: list_info){
							alter_money += Double.valueOf(map_info.get("alter_money"));
							refund_12306_money += Double.valueOf(map_info.get("refund_12306_money"));
						}
						map.put("refund_12306_money", refund_12306_money+"");
						map.put("alter_money", alter_money+"");
						//订单内车票数
						map.put("refund_status", "11");
						map.put("notify_status", "00");
						//更新退款参数
						orderService.updateRefundInfoSingle(map);
					}
				}else if("1".equals(status) && TrainConsts.FAILURE.equalsIgnoreCase(result)){
					map.put("refund_status", "22");
					map.put("notify_status", "00");
					orderService.updateRefundInfoSingle(map);
				}
				write2Response(response, "success");
			}
		}catch(Exception e){
			logger.error("处理退票结果数据异常！", e);
			write2Response(response, "failed");
		}
		
	}
	/**
	 * 接收出票结果通知
	 * @param request
	 * @param response
	 */
	@RequestMapping("/cpNotify_no.jhtml")
	public void cpNotify(HttpServletRequest request,
			HttpServletResponse response){
		String result = this.getParam(request, "result");
		String orderId = this.getParam(request, "orderid");
		String billNo = this.getParam(request, "billno");
		String buyMoney = this.getParam(request, "buymoney");
		String seatTrains = this.getParam(request, "seattrains");
		String status = this.getParam(request, "status");//00：出票成功 11预定成功
		String seatTypeStr = this.getParam(request, "extseattype");//默认坐席#座位类型,价格#成功坐席
		
		String from_time=this.getParam(request, "from_time");
		String to_time=this.getParam(request, "to_time");//09:11   09:11
		String pay_limit_time=this.getParam(request, "pay_limit_time");
		String out_ticket_time=this.getParam(request, "out_ticket_time");
		
		logger.info("【接收出票系统通知】参数orderId=" + orderId 
				+ "，result=" + result + "，billNo=" + billNo
				+ "，buyMoney=" + buyMoney + "，seatTrains=" + seatTrains + "，status=" + status + "，seatTypeStr="+seatTypeStr
				+ "，from_time=" + from_time + "，to_time=" + to_time
				+ "，pay_limit_time=" + pay_limit_time + "，out_ticket_time=" + out_ticket_time);
		
		//查询订单联程数据
		OrderInfoTrip trip = null;
		if(StringUtils.isNotEmpty(orderId)){
			trip = orderService.queryTripOrderInfoById(orderId);
		}
		
		Map<String, String> paramMap = new HashMap<String, String>(3);//主订单参数
		List<Map<String, String>> cpMapList = new ArrayList<Map<String, String>>();//车票订单参数
		
		if(StringUtils.isEmpty(result) || StringUtils.isEmpty(orderId)){
			//参数为空
			logger.info("exception：参数为空！");
			write2Response(response, "failed");
			
		}else if(TrainConsts.SUCCESS.equalsIgnoreCase(result)){//成功
			if(StringUtils.isEmpty(billNo) || StringUtils.isEmpty(buyMoney) 
					|| StringUtils.isEmpty(status) || StringUtils.isEmpty(seatTrains) 
					|| StringUtils.isEmpty(seatTypeStr)){
				//参数为空
				logger.info("exception：明细参数为空！");
				write2Response(response, "failed");
				return;
			}else if(!"11".equals(status) && !"00".equals(status)){
				logger.info("exception：状态有误！status="+status);
				write2Response(response, "failed");
				return;
			}
			
			Map<String, String> orderMap = null;
			
			
			if(trip == null){//普通订单
				orderMap = new HashMap<String, String>();

				OrderInfo orderInfo = orderService.queryOrderInfoById(orderId);
				orderMap.put("order_status", orderInfo.getOrder_status());
				orderMap.put("seat_type", orderInfo.getSeat_type());
				orderMap.put("ext_seat", orderInfo.getExt_seat());
				orderMap.put("is_pay", orderInfo.getIs_pay());//是否支付：00：已支付；11：未支付；

			}else{//联程订单
				orderMap = new HashMap<String, String>();

				orderMap.put("order_status", trip.getOrder_status());
				orderMap.put("seat_type", trip.getSeat_type());
				orderMap.put("ext_seat", trip.getExt_seat());
				orderMap.put("is_pay", "00");//是否支付：00：已支付；11：未支付；
			}
			
			List<Map<String, String>> cpQueryList = orderService.queryCpInfoList(orderId);
			
			//通知qunar：0、出票成功通知 1、预定成功通知 
			String whenNotify = orderService.queryQunarSysSetting("out_notify_qunar");
			logger.info("【接收出票系统通知】orderId=" + orderId +"whenNotify="+whenNotify+"，备注通知qunar：0、出票成功通知 1、预定成功通知 ");
			String notifyQuanrAtStatus = TrainConsts.OUT_SUCCESS;//出票后什么状态通知qunar
			if(StringUtils.isEmpty(whenNotify)){
				notifyQuanrAtStatus = TrainConsts.OUT_SUCCESS;
			}else if("1".equals(whenNotify)){
				notifyQuanrAtStatus = TrainConsts.BOOK_SUCCESS;
			}else{
				notifyQuanrAtStatus = TrainConsts.OUT_SUCCESS;
			}
			
			//if(StringUtils.isEmpty(orderInfo.getOrder_status())){
			if(StringUtils.isEmpty(orderMap.get("order_status"))){
				logger.info("exception：订单查询异常，订单状态为空！");
				write2Response(response, "failed");
				return;
			}
			
			//选择坐席特殊处理
			String[] seatElements = seatTypeStr.split("#");
			String seatTypeFrom = null;
			if(seatElements.length==3){
				seatTypeFrom = seatElements[2].substring(0, 1);
				
			}else{//无成功坐席，则选取默认坐席
				seatTypeFrom = seatElements[0].substring(0, 1);
			}
			
			Map<String, String> seatMap = new HashMap<String, String>();//用户选的所有坐席对应的支付金额
			
			if(!StringUtils.isEmpty(orderMap.get("seat_type"))){//默认坐席
				seatMap.put("seatType_"+orderMap.get("seat_type"), cpQueryList.get(0).get("pay_money"));
			}
			if(!StringUtils.isEmpty(orderMap.get("ext_seat"))){//被选坐席
				for(String outerStr : orderMap.get("ext_seat").split("\\|")){
					seatMap.put("seatType_"+outerStr.split(",")[0], outerStr.split(",")[1]);
				}
			}
			
			if("11".equals(status)){//预订成功
				//预订成功通知重复通知
				if(!TrainConsts.PAY_SUCCESS.equals(orderMap.get("order_status"))){
					logger.info("【接收出票系统通知】本次预订成功通知为重复通知，orderId=" + orderId);
					write2Response(response, "success");
					return;
				}
				
				paramMap.put("order_id", orderId);
				paramMap.put("buy_money", buyMoney);
				paramMap.put("out_ticket_billno", billNo);
				paramMap.put("order_status", TrainConsts.BOOK_SUCCESS);//预订成功
				paramMap.put("from_time", from_time);//发车时间
				paramMap.put("to_time", to_time);
				paramMap.put("pay_limit_time", pay_limit_time);
				paramMap.put("out_ticket_time", out_ticket_time);
				
				//处理明细数据
				this.cpDataPacking(seatTrains, seatTypeFrom, billNo, orderId, seatMap, cpMapList, response);
				
				//添加日志
				Map<String, String> logMap = new HashMap<String, String>();
				logMap.put("order_id", orderId);
				logMap.put("content", "成功接收出票系统出票结果：预定成功");
				logMap.put("opt_person", "qunar_app");
				
				orderService.updateOrderWithCpNotify(paramMap, cpMapList, logMap, notifyQuanrAtStatus, trip);
				write2Response(response, "success");
				
				if("11".equals(orderMap.get("is_pay"))){//是否支付：00：已支付；11：未支付；
					logger.info("【占座成功】更改book_notify_status:" + orderId);
					orderService.updateBookNotifyPrepare(paramMap.get("order_id"));//更新占座成功通知
				}
				
				/**立即回调出票结果操作*/
				if((notifyQuanrAtStatus.equals(paramMap.get("order_status")) //系统设置的预订成功通知
						|| TrainConsts.OUT_SUCCESS.equals(paramMap.get("order_status"))) //订单的世纪状态：预订成功
						&& "00".equals(orderMap.get("is_pay"))){//00：已支付的
					/**只针对普通票立即回调操作*/
					if(trip == null){
						try {
							long start=System.currentTimeMillis();
							orderService.sendNotifyRequest(orderId,TrainConsts.ORDER_TYPE_COMMON);
							logger.info("立即回调去哪出票结果,"+orderId+",lose time:"+(System.currentTimeMillis()-start));
						} catch (Exception e) {
							logger.info("立即回调去哪出票结果,异常"+e);
							e.printStackTrace();
						}
					}
				}
				
			}else if("00".equals(status)){//出票成功
				
				if(!TrainConsts.PAY_SUCCESS.equals(orderMap.get("order_status"))
						&& !TrainConsts.BOOK_SUCCESS.equals(orderMap.get("order_status"))){
					logger.info("【接收出票系统通知】本次出票成功通知为重复通知，orderId=" + orderId);
					write2Response(response, "success");
					return;
				}
				
				if(trip != null){//联程订单
					if(orderId.contains("_2")){
						try {
							logger.info("联程订单waiting1.5s");
							Thread.sleep(1500);
						} catch (InterruptedException e) {
							e.printStackTrace();
							logger.error("联程订单waiting异常", e);
							write2Response(response, "failed");
							return;
						}
					}
				}
				
				paramMap.put("order_id", orderId);
				paramMap.put("buy_money", buyMoney);
				paramMap.put("out_ticket_billno", billNo);
				paramMap.put("order_status", TrainConsts.OUT_SUCCESS);//出票成功
				paramMap.put("from_time", from_time);//发车时间
				paramMap.put("to_time", to_time);
				paramMap.put("pay_limit_time", pay_limit_time);
				paramMap.put("out_ticket_time", out_ticket_time);
				paramMap.put("is_pay", "00");//已支付
				
				//处理明细数据
				this.cpDataPacking(seatTrains, seatTypeFrom, billNo, orderId, seatMap, cpMapList, response);

				//添加日志
				Map<String, String> logMap = new HashMap<String, String>();
				logMap.put("order_id", orderId);
				logMap.put("content", "成功接收出票系统出票结果：出票成功");
				logMap.put("opt_person", "qunar_app");
				orderService.updateOrderWithCpNotify(paramMap, cpMapList, logMap, notifyQuanrAtStatus, trip);
				write2Response(response, "success");
					
				
				/**立即回调出票结果操作*/
				if(notifyQuanrAtStatus.equals(paramMap.get("order_status")) 
						|| TrainConsts.OUT_SUCCESS.equals(paramMap.get("order_status"))){
					if(trip == null){
						try {
							long start=System.currentTimeMillis();
							orderService.sendNotifyRequest(orderId,TrainConsts.ORDER_TYPE_COMMON);
							logger.info("立即回调去哪出票结果,"+orderId+",lose time:"+(System.currentTimeMillis()-start));
						} catch (Exception e) {
							logger.info("立即回调去哪出票结果,异常"+e);
							e.printStackTrace();
						}
					}
				}
			}
			
		}else if(TrainConsts.FAILURE.equalsIgnoreCase(result)){//失败
			String errorinfo = this.getParam(request, "errorinfo");//错误信息
			String passengers = this.getParam(request, "passengers");//乘客审核信息
			List<Map<String, String>> cpList = orderService.queryCpInfoList(orderId);
			
			if(StringUtils.isEmpty(errorinfo)){
				//参数为空
				logger.info("exception：errorinfo为空！");
				write2Response(response, "failed");
				return;
			}
			if(trip == null){//普通订单
				//阻止重复出票系统通知请求
				OrderInfo orderInfo = orderService.queryOrderInfoById(orderId);
				if(!StringUtils.isEmpty(orderInfo.getOrder_status())
						&& TrainConsts.OUT_FAIL.equals(orderInfo.getOrder_status())){
					logger.info("【接收出票系统通知】本次出票失败通知为重复请求，orderId=" + orderId);
					write2Response(response, "success");
					return;
				}
				paramMap.put("is_pay", orderInfo.getIs_pay()); 
			}else{//联程订单
				if(!StringUtils.isEmpty(trip.getOrder_status())
						&& TrainConsts.OUT_FAIL.equals(trip.getOrder_status())){
					logger.info("【接收出票系统通知】联程订单，本次出票失败通知为重复请求，orderId=" + orderId);
					write2Response(response, "success");
					return;
				}
				paramMap.put("is_pay", "00");//已支付
			}
			
			//乘车人信息错误
			String passenger_reason = "";
			JSONArray pJa = new JSONArray();
			JSONObject jsobj = null;
			//passengers=CP1403021140261036|西红柿|110101198406079315|0#CP1403021140261038|海龟派|110101198406079235|1#CP1403021140261040|想不通|110101198810014951|0
			if(StringUtils.isNotEmpty(passengers)){
				String certType = "1";//身份证
				String certNo = null;
				String name = null;
				String reason = null;
				String cp_id = null;
				
				String[] arrayPassenger = passengers.split("#");
				for(String passenger : arrayPassenger){
					String[] element = passenger.split("\\|");
					if(element == null || element.length<4){
						//参数为空
						logger.info("exception：参数拆分失败！");
						write2Response(response, "failed");
						return;
					}
					cp_id = element[0];
					name = element[1];
					certNo = element[2];
					reason = element[3];
					
					if(StringUtils.isEmpty(reason)){
						//参数为空
						logger.info("exception：参数异常！");
						write2Response(response, "failed");
						return;
					}else if("1".equals(reason) || "2".equals(reason)){//待审核，未通过
						jsobj = new JSONObject(); 
						for(Map<String, String> cp : cpList){
							if(cp.get("cp_id").equals(cp_id)){
								certType = cp.get("qunar_certtype");
//								if("1".equalsIgnoreCase(certType)){//身份证
//									certType = "0";
//								}else if("C".equalsIgnoreCase(certType)){//港澳通行证
//									certType = "7";
//								}else if("G".equalsIgnoreCase(certType)){//台湾通行证
//									certType = "6";
//								}else if("B".equalsIgnoreCase(certType)){//护照
//									certType = "4";
//								}
								
								break;
							}
						}
						jsobj.put("certNo", certNo);
						jsobj.put("certType", certType);
						jsobj.put("name", name);
						jsobj.put("reason", reason);
						pJa.add(jsobj);
					}
				}
				if(pJa != null && pJa.size()>0){
					passenger_reason = pJa.toString();
					logger.info("passenger_reason=" + passenger_reason);
				}
			}
			
			paramMap.put("order_id", orderId); 
			paramMap.put("buy_money", buyMoney);
			paramMap.put("out_ticket_billno", billNo);
			paramMap.put("order_status", TrainConsts.OUT_FAIL);//出票失败
			paramMap.put("out_fail_reason", errorinfo);//出票失败原因
			paramMap.put("passenger_reason", passenger_reason);//乘车人信息错误
			
			
			//添加日志
			Map<String, String> logMap = new HashMap<String, String>();
			if(trip == null){
				logMap.put("order_id", orderId);
				logMap.put("content", "成功接收出票系统出票结果：出票失败");
			}else{
				logMap.put("order_id", trip.getOrder_id());
				logMap.put("content", "成功接收出票系统出票结果：出票失败，"+trip.getOrder_id());
			}
			logMap.put("opt_person", "qunar_app");
			orderService.updateOrderWithCpNotify(paramMap, null, logMap, TrainConsts.OUT_FAIL, trip);
			write2Response(response, "success");
			
			if("11".equals(paramMap.get("is_pay"))){//是否支付：00：已支付；11：未支付；
				logger.info("【占座失败】更改book_notify_status:" + orderId);
				orderService.updateBookNotifyPrepare(paramMap.get("order_id"));//更新占座成功通知
			}
			
		}else{//异常
			logger.info("exception：订单" + orderId + "，接口返回未知状态码！");
			write2Response(response, "failed");
		}
	}
	
	/**
	 * 车票明细数据组装
	 */
	private void cpDataPacking(String seatTrains, String seatTypeFrom, 
			String billNo, String orderId, Map<String, String> seatMap,
			List<Map<String, String>> cpMapList, HttpServletResponse response){
		Map<String, String> cpMap = null;
		//CP0120212|133|12|058号#CP0120213|133|12|059号
		//CP201310111603001001|537|09|17号下铺#CP201310111603001002|515|09|18号上铺
		String[] seatMsgs = seatTrains.split("#");
		for (String seatMsg : seatMsgs) {//CP0120212|133|12|058号
			String[] str = seatMsg.split("\\|");
			if(str == null){
				//参数为空
				logger.info("exception：参数拆分失败！");
				write2Response(response, "failed");
				return;
			}
			String seatType = new String(seatTypeFrom);
			cpMap = new HashMap<String, String>();
			cpMap.put("cp_id", str[0]);
			cpMap.put("train_box", str[2]);//车厢
			cpMap.put("seat_no", str[3]);//座位号
			cpMap.put("out_ticket_billno", billNo);//12306单号
			cpMap.put("seat_type", seatType);
			cpMap.put("qunar_seat_type", TrainPropUtil.getQunarSeatType(seatType));
			cpMap.put("buy_money", str[1]);//成本价格
			
			if("4".equals(seatType)){//高级软卧
				if(str[3].indexOf("上")!=-1){
					seatType = "41";
				}else if(str[3].indexOf("下")!=-1){
					seatType = "42";
				}
			}else if("5".equals(seatType)){//软卧
				if(str[3].indexOf("上")!=-1){
					seatType = "51";
				}else if(str[3].indexOf("下")!=-1){
					seatType = "52";
				}
			}else if("6".equals(seatType)){//硬卧
				if(str[3].indexOf("上")!=-1){
					seatType = "61";
				}else if(str[3].indexOf("中")!=-1){
					seatType = "62";
				}else if(str[3].indexOf("下")!=-1){
					seatType = "63";
				}
			}
			cpMap.put("seat_type", seatType);
			cpMap.put("qunar_seat_type", TrainPropUtil.getQunarSeatType(seatType));
			cpMapList.add(cpMap);
			
			/*if("4".equals(seatType)){//高级软卧
				if(str[3].indexOf("上")!=-1){
					seatType = "41";
				}else if(str[3].indexOf("下")!=-1){
					seatType = "42";
				}
				
				if(seatMap.containsKey("seatType_"+seatType)){
					cpMap.put("seat_type", seatType);
					cpMap.put("qunar_seat_type", TrainPropUtil.getQunarSeatType(seatType));
					
					//报价与qunar报价不同，以qunar为准
					this.differPriceConfirm(seatMap.get("seatType_"+seatType), str[1], orderId, seatType, cpMap);
					
				}else if(seatMap.containsKey("seatType_42")){
					cpMap.put("seat_type", "42");
					cpMap.put("qunar_seat_type", TrainPropUtil.getQunarSeatType("42"));
					cpMap.put("buy_money", seatMap.get("seatType_42"));
					//添加日志
					Map<String, String> log = new HashMap<String, String>();
					log.put("order_id", orderId);
					log.put("content", "坐席调整"+seatType+"("+str[1]+")->42("+seatMap.get("seatType_42")+")");
					log.put("opt_person", "qunar_app");
					orderService.addOrderInfoLog(log);
					logger.info("【接收出票系统通知】order_id="+log.get("order_id")+"，"+log.get("content"));
				}else{
					cpMap.put("seat_type", "41");
					cpMap.put("qunar_seat_type", TrainPropUtil.getQunarSeatType("41"));
					cpMap.put("buy_money", seatMap.get("seatType_41"));
					//添加日志
					Map<String, String> log = new HashMap<String, String>();
					log.put("order_id", orderId);
					log.put("content", "坐席调整"+seatType+"("+str[1]+")->41("+seatMap.get("seatType_41")+")");
					log.put("opt_person", "qunar_app");
					orderService.addOrderInfoLog(log);
					logger.info("【接收出票系统通知】order_id="+log.get("order_id")+"，"+log.get("content"));
				}
				
			}else if("5".equals(seatType)){//软卧
				if(str[3].indexOf("上")!=-1){
					seatType = "51";
				}else if(str[3].indexOf("下")!=-1){
					seatType = "52";
				}
				
				if(seatMap.containsKey("seatType_"+seatType)){
					cpMap.put("seat_type", seatType);
					cpMap.put("qunar_seat_type", TrainPropUtil.getQunarSeatType(seatType));
					
					//报价与qunar报价不同，以qunar为准
					this.differPriceConfirm(seatMap.get("seatType_"+seatType), str[1], orderId, seatType, cpMap);
					
				}else if(seatMap.containsKey("seatType_52")){
					cpMap.put("seat_type", "52");
					cpMap.put("qunar_seat_type", TrainPropUtil.getQunarSeatType("52"));
					cpMap.put("buy_money", seatMap.get("seatType_52"));
					//添加日志
					Map<String, String> log = new HashMap<String, String>();
					log.put("order_id", orderId);
					log.put("content", "坐席调整"+seatType+"("+str[1]+")->52("+seatMap.get("seatType_52")+")");
					log.put("opt_person", "qunar_app");
					orderService.addOrderInfoLog(log);
					logger.info("【接收出票系统通知】order_id="+log.get("order_id")+"，"+log.get("content"));
				}else{
					cpMap.put("seat_type", "51");
					cpMap.put("qunar_seat_type", TrainPropUtil.getQunarSeatType("51"));
					cpMap.put("buy_money", seatMap.get("seatType_51"));
					//添加日志
					Map<String, String> log = new HashMap<String, String>();
					log.put("order_id", orderId);
					log.put("content", "坐席调整"+seatType+"("+str[1]+")->51("+seatMap.get("seatType_51")+")");
					log.put("opt_person", "qunar_app");
					orderService.addOrderInfoLog(log);
					logger.info("【接收出票系统通知】order_id="+log.get("order_id")+"，"+log.get("content"));
				}
				
			}else if("6".equals(seatType)){//硬卧
				if(str[3].indexOf("上")!=-1){
					seatType = "61";
				}else if(str[3].indexOf("中")!=-1){
					seatType = "62";
				}else if(str[3].indexOf("下")!=-1){
					seatType = "63";
				}
				
				if(seatMap.containsKey("seatType_"+seatType)){
					cpMap.put("seat_type", seatType);
					cpMap.put("qunar_seat_type", TrainPropUtil.getQunarSeatType(seatType));
					
					//报价与qunar报价不同，以qunar为准
					this.differPriceConfirm(seatMap.get("seatType_"+seatType), str[1], orderId, seatType, cpMap);
					
				}else if(seatMap.containsKey("seatType_63")){
					cpMap.put("seat_type", "63");
					cpMap.put("qunar_seat_type", TrainPropUtil.getQunarSeatType("63"));
					cpMap.put("buy_money", seatMap.get("seatType_63"));
					//添加日志
					Map<String, String> log = new HashMap<String, String>();
					log.put("order_id", orderId);
					log.put("content", "坐席调整"+seatType+"("+str[1]+")->63("+seatMap.get("seatType_63")+")");
					log.put("opt_person", "qunar_app");
					orderService.addOrderInfoLog(log);
					logger.info("【接收出票系统通知】order_id="+log.get("order_id")+"，"+log.get("content"));
					
				}else if(seatMap.containsKey("seatType_62")){
					cpMap.put("seat_type", "62");
					cpMap.put("qunar_seat_type", TrainPropUtil.getQunarSeatType("62"));
					cpMap.put("buy_money", seatMap.get("seatType_62"));
					//添加日志
					Map<String, String> log = new HashMap<String, String>();
					log.put("order_id", orderId);
					log.put("content", "坐席调整"+seatType+"("+str[1]+")->62("+seatMap.get("seatType_62")+")");
					log.put("opt_person", "qunar_app");
					orderService.addOrderInfoLog(log);
					logger.info("【接收出票系统通知】order_id="+log.get("order_id")+"，"+log.get("content"));

				}else{
					cpMap.put("seat_type", "61");
					cpMap.put("qunar_seat_type", TrainPropUtil.getQunarSeatType("61"));
					cpMap.put("buy_money", seatMap.get("seatType_61"));
					//添加日志
					Map<String, String> log = new HashMap<String, String>();
					log.put("order_id", orderId);
					log.put("content", "坐席调整"+seatType+"("+str[1]+")->61("+seatMap.get("seatType_61")+")");
					log.put("opt_person", "qunar_app");
					orderService.addOrderInfoLog(log);
					logger.info("【接收出票系统通知】order_id="+log.get("order_id")+"，"+log.get("content"));
				}
			}else{//其他非卧铺坐席
				//报价与qunar报价不同，以qunar为准
				this.differPriceConfirm(seatMap.get("seatType_"+seatType), str[1], orderId, seatType, cpMap);
			}

			cpMapList.add(cpMap);
			*/
		}
	}
	
	/**
	 * 报价与qunar报价不同，以qunar为准
	 */
	private void differPriceConfirm(String qunarPrice, String ourPrice, 
			String orderId, String seatType, Map<String, String> cpMap){
		if(!StringUtils.isEmpty(qunarPrice)
				&& Double.parseDouble(qunarPrice) != Double.parseDouble(ourPrice)){
			cpMap.put("buy_money", qunarPrice);
			//添加日志
			Map<String, String> log = new HashMap<String, String>();
			log.put("order_id", orderId);
			log.put("content", "价格调整"+seatType+"("+ourPrice+")->seatType("+qunarPrice+")");
			log.put("opt_person", "qunar_app");
			orderService.addOrderInfoLog(log);
			logger.info("【接收出票系统通知】order_id="+log.get("order_id")+"，"+log.get("content"));
		}
		
	}
	
//	public static void main(String[] args) throws Exception{
//		
//		String str ="jjslw140316204216297_2";
//		if(str.contains("_1")){
//			System.out.println("yes");
//		}else{
//			System.out.println("no");
//		}
//		
//		Map<String, String> map = new HashMap<String,String>();
//		//orderId=sjysz13101115541670e，result=success，billNo=E400824874，buyMoney=1052.000，seatTrains=CP201310111603001001|537|09|17号下铺#CP201310111603001002|515|09|18号上铺，status=00，seatTypeStr=52#51,515
///*		map.put("orderid", "sjysz13101115541670e");
//		map.put("result", "success");
//		map.put("billno", "E400824874");
//		map.put("buymoney", "1052.000");		
//		map.put("seattrains", "CP201310111603001001|537|09|17号下铺#CP201310111603001002|515|09|18号上铺");
//		map.put("status", "00");
//		map.put("extseattype", "52#51,515");*/
//		
//		//orderId=sjysz1310121315312e0，result=failure，billNo=，buyMoney=，seatTrains=null，status=，seatTypeStr=3#无
//		map.put("orderid", "sjysz1310121315312e0");
//		map.put("result", "failure");
//		map.put("extseattype", "3#无");
//		map.put("errorinfo", "2");
//		
//
//		String reqParams = UrlFormatUtil.CreateUrl("", map, "", "UTF-8");
//		System.out.println("reqParams="+reqParams);
//		
//		String rs = HttpUtil.sendByPost("http://192.168.12.98:8088/receiveNotify/cpNotify_no.jhtml", reqParams, "UTF-8");
//		System.out.println(rs);
//	}

}
