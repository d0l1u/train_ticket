package com.l9e.transaction.controller;

import java.util.ArrayList;
import java.util.Date;
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
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.common.Consts;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.ElongOrderService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.DBOrderInfo;
import com.l9e.transaction.vo.ElongOrderInfo;
import com.l9e.transaction.vo.ElongOrderInfoCp;
import com.l9e.transaction.vo.ElongOrderLogsVo;
import com.l9e.transaction.vo.ElongPassengerInfo;
import com.l9e.transaction.vo.ElongRefundVo;
import com.l9e.util.AmountUtil;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.elong.ElongConsts;
import com.l9e.util.elong.ElongMd5Util;
import com.l9e.util.elong.StrUtil;

/**
 * 获取出票结果、退款结果主控制器
 * @author liuyi
 *
 */
@Controller
@RequestMapping("/ticket")
public class ElongController extends BaseController{
	private static Logger logger= Logger.getLogger(ElongController.class);
	
	@Resource
	private ElongOrderService elongOrderService;
	

	@Resource
	private OrderService orderService;
	
	/**
	 * 订单详情查询
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryOrder.jhtml")
	public void queryOrder(HttpServletRequest request, 
			HttpServletResponse response){  
		/*
		{
			“retcode”:”200”,
			“retdesc”:”成功”,
			“orderDetail”: {
			"arrStation": "北京南",                        始发站名
			    "contactName": "自动化",                      联系人姓名
			    "dptStation": "杭州",                          到达站名
			    "orderDate": "2014-08-07 10:52:05",            下单时间
			"orderId": "20140807212402185",              订单号
			“ticketNo”:”E1234567”                        12306订单号
			    "passengers": [                               乘客信息列表
			        {
			            "certNo": "370724198704210751",     证件号
			            "certType": "1",                      证件类型
			            "name": "张玉朋",                    姓名
			            "orderItemId": "20140807212402186",  票item号
			            "ticketType": "2"                     票类型
						  “seatNo”:”01车厢05号”             坐席号
			“price”：105                       单张票的价格
			        },
			        {
			            "certNo": "46000319871214184X",
			            "certType": "1",
			            "name": "李月华",
			            "orderItemId": "20140807212402187",
			            "ticketType": "2"
						  “price”: 100                      单张票的价格
			“seatNo”:”01车厢06号”             坐席号
			        }
			    ],
			    "seatType": "10",                             坐席类型
			    "ticketPrice": 629,                            票价
			    "trainEndTime": "2014-08-09 16:17:00",        到站时间
			    "trainNo": "G41",                             车次
			"trainStartTime": "2014-08-09 09:33:00",        发车时间
			“totalPrice”:205                              订单总价
			“payTimeDeadLine”:”yyyy-MM-dd hh:mm:ss”  用户支付截止时间
			“holdingSeatSuccessTime”:”yyyy-MM-dd hh:mm:ss”   占座成功时间
			“orderStatus”: 4                               订单状态码（见4.5）
			“orderStatusDesc”: “占座中”                    订单状态描述（见4.5）
			“failureReason”:3                             订单失败码
			“failureReasonDesc”:”身份核验未通过”           订单失败原因
			}
			}
*/
		long start=System.currentTimeMillis();
		String merchantId=getParam(request, "merchantId");
		String timeStamp=getParam(request, "timeStamp");
		String orderId=getParam(request, "orderId");
		String sign=getParam(request, "sign");
		String sys_merchant_id=Consts.SYS_MERCHANT_ID;//SYS_MERCHANT_ID
		String sys_sign_key =Consts.SYS_SIGN_KEY;//SYS_SIGN_KEY
		
		if(StrUtil.isEmpty(merchantId)||StrUtil.isEmpty(timeStamp)||StrUtil.isEmpty(orderId)
				||StrUtil.isEmpty(sign)){
			logger.info("艺龙查询订单-用户身份校验error:必要参数为空!merchantId:"+merchantId+",timeStamp:"+timeStamp
					+",orderId:"+orderId+",sign:"+sign);
			printJson(response, returnJson("401","参数校验失败").toString());
			return;
		}
		
		if (!sys_merchant_id.equals(merchantId)) {
			logger.info("艺龙查询订单-用户身份校验error:不存在该用户!merchantId:"+merchantId+"sys_merchant_id:"+sys_merchant_id);
			printJson(response, returnJson("402","账号不存在").toString());
			return;
		}
		
		Map<String,String> params=new HashMap<String,String>();
		params.put("merchantId", merchantId);
		params.put("timeStamp", timeStamp);
		params.put("orderId", orderId);
		String sys_sign=ElongMd5Util.md5_32(ElongMd5Util.getParam(params, sys_sign_key), "UTF-8");
		if(sys_sign.equals(sign)){
			int orderCount=elongOrderService.getOrderCount(orderId);
			if(orderCount!=0){
				try {
					Map<String, Object> orderInfo=elongOrderService.queryOrderInfo(orderId);
					/**查询cp信息*/
					List<ElongOrderInfoCp> list=new ArrayList<ElongOrderInfoCp>();
					list=elongOrderService.queryOrderCpInfo(orderId);
					JSONObject j=new JSONObject();
					j.put("retcode", "200");
					j.put("retdesc", "成功");
					JSONObject orderJson=new JSONObject();
					orderJson.put("arrStation", this.toString(orderInfo.get("from_city")));
					orderJson.put("contactName","");
					orderJson.put("dptStation",this.toString(orderInfo.get("to_city")));
					orderJson.put("orderDate",this.toString(orderInfo.get("order_time")));
					orderJson.put("orderId",this.toString(orderInfo.get("order_id")));
					orderJson.put("ticketNo",this.toString(orderInfo.get("out_ticket_billno")));
					orderJson.put("seatType",this.toString(orderInfo.get("elong_seat_type")));
					orderJson.put("ticketPrice",this.toString(orderInfo.get("buy_money")));
					orderJson.put("trainEndTime",this.toString(orderInfo.get("from_time")));
					
					orderJson.put("trainNo",this.toString(orderInfo.get("train_no")));
					orderJson.put("trainStartTime",this.toString(orderInfo.get("to_time")));
					//orderJson.put("totalPrice",this.toString(orderInfo.get("buy_money")));
					orderJson.put("payTimeDeadLine",this.toString(orderInfo.get("pay_limit_time")));
					orderJson.put("holdingSeatSuccessTime",this.toString(orderInfo.get("out_ticket_time")));
					
/*	4	占座中
					7	占座成功
					8	占座失败
					9	出票成功
					11	出票失败
					12	出票中*/
					String status=this.toString(orderInfo.get("order_status"));
					String pay_money =this.toString(orderInfo.get("pay_money"));
					
					String orderStatus="";
					String orderStatusDesc="";
					if(Consts.ELONG_ORDER_DOWN.equals(status)||Consts.ELONG_ORDER_ING.equals(status)){
						orderStatus="4";
						orderStatusDesc="占座中";
					}else if(Consts.ELONG_ORDER_MAKED.equals(status)){
						orderStatus="7";
						orderStatusDesc="占座成功";
					}else if(Consts.ELONG_ORDER_FAIL.equals(status)&&"".equals(pay_money)){
						orderStatus="8";
						orderStatusDesc="占座失败";
						
					}else if(Consts.ELONG_ORDER_SUCCESS.equals(status)){
						orderStatus="9";
						orderStatusDesc="出票成功";
					}else if(Consts.ELONG_ORDER_FAIL.equals(status)&&!"".equals(pay_money)){
						orderStatus="11";
						orderStatusDesc="出票失败";
					}else if(Consts.ELONG_OUT_TIME.equals(status)||Consts.ELONG_ORDER_CANCELED.equals(status)){
						orderStatus="11";
						orderStatusDesc="出票失败";
					}else{
						orderStatus="12";
						orderStatusDesc="出票中";
					}
					orderJson.put("orderStatus",orderStatus);
					orderJson.put("orderStatusDesc",orderStatusDesc);
					
					String failureReasonDesc="";
					String reason=this.toString(orderInfo.get("out_fail_reason"));
					if("".equals(reason)){
						failureReasonDesc="";
					}else{
						/*if("8".equals(reason)||"7".equals(reason)){
							reason="6";
						}*/
						if(Consts.OUT_FAIL_REASON_0.equals(reason)){
							failureReasonDesc = "其他";
						}else if(Consts.OUT_FAIL_REASON_1.equals(reason)){
							failureReasonDesc = "所购买的车次坐席已无票";
						}else if(Consts.OUT_FAIL_REASON_2.equals(reason)){
							failureReasonDesc = "身份证件已经实名制购票，不能再次购买同日期同车次的车票";
						}else if(Consts.OUT_FAIL_REASON_3.equals(reason)){
							failureReasonDesc = "票价和12306不符";
						}else if(Consts.OUT_FAIL_REASON_4.equals(reason)){
							failureReasonDesc = "车次数据与12306不一致";
						}else if(Consts.OUT_FAIL_REASON_5.equals(reason)){
							failureReasonDesc = "乘客信息错误";
						}else if(Consts.OUT_FAIL_REASON_6.equals(reason)){
							failureReasonDesc = "用户取消订单";
						}else if(Consts.OUT_FAIL_REASON_8.equals(reason)){
							reason="6";
							failureReasonDesc = "12306乘客身份信息核验失败";
						}
					}
					orderJson.put("failureReason",reason);
					orderJson.put("failureReasonDesc",failureReasonDesc);
					
					String tickets=getTicketsJson(orderInfo,list);//
					orderJson.put("passengers",tickets);
					
					j.put("orderDetail", orderJson.toString());
					
					
					logger.info("艺龙查询订单:"+orderId+",返回结果:"+j.toString());
					printJson(response,j.toString());
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("艺龙查询订单-系统异常:"+e);
					printJson(response, returnJson("400","系统错误").toString());
				}
			}else{
					printJson(response, returnJson("452","此订单不存在").toString());
				}
		}else{
			logger.info("艺龙查询订单-校验不通过,加密前串:"+ElongMd5Util.getParam(params, sys_sign_key)+",加密后sys_sign:"+sys_sign+"_sign"+sign);
			printJson(response, returnJson("403","签名校验失败").toString());
		}
	}
	
	private String getTicketsJson(Map<String, Object> orderInfo,
			List<ElongOrderInfoCp> list) {
		String ext_field2= orderInfo.get("ext_field2").toString();
		JSONArray arr=new JSONArray();
		for(ElongOrderInfoCp cp:list){
			JSONObject json=new JSONObject();
			json.put("certNo", cp.getUser_ids());
			json.put("certType", cp.getElong_ids_type());
			json.put("name", cp.getUser_name());
			json.put("orderItemId", cp.getCp_id());
			json.put("ticketType", cp.getElong_ticket_type());
			json.put("seatType","".equals(ext_field2)?
					cp.getElong_seat_type():
						(seatTypeCheck(cp.getSeat_type(),cp.getElong_seat_type(),ext_field2)));
			String seat_no=cp.getSeat_no();
			String train_box=cp.getTrain_box();
			json.put("price", cp.getBuy_money());
			if(StrUtil.isEmpty(train_box)&&StrUtil.isEmpty(seat_no)){
				json.put("seatNo", "");
				
			}else{
				json.put("seatNo", (StrUtil.isEmpty(train_box)?"":(train_box+"车厢 "))+(StrUtil.isEmpty(seat_no)?"无座":seat_no));
			}
			arr.add(json);
		}
		return arr.toString();
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
	/**
	 * 获取艺龙先预订后支付订单
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/book.jhtml")
	public void getBookOrders(HttpServletRequest request, 
			HttpServletResponse response){   
		long start=System.currentTimeMillis();
		String merchantId=getParam(request, "merchantId");
		String timeStamp=getParam(request, "timeStamp");
		String orderId=getParam(request, "orderId");
		String paramJson=getParam(request, "paramJson");
		String sign=getParam(request, "sign");
		String sys_merchant_id=Consts.SYS_MERCHANT_ID;//SYS_MERCHANT_ID
		String sys_sign_key =Consts.SYS_SIGN_KEY;//SYS_SIGN_KEY
		logger.info("获取参数列表："+getFullURL(request));
		if(StrUtil.isEmpty(merchantId)||StrUtil.isEmpty(timeStamp)||StrUtil.isEmpty(orderId)
				||StrUtil.isEmpty(paramJson)||StrUtil.isEmpty(sign)){
			logger.info("艺龙推送订单-用户身份校验error:必要参数为空!merchantId:"+merchantId+",timeStamp:"+timeStamp
					+",orderId:"+orderId+",paramJson:"+paramJson+",sign:"+sign);
			printJson(response, returnJson("401","参数校验失败").toString());
			return;
		}
		
		if (!sys_merchant_id.equals(merchantId)) {
			logger.info("艺龙推送订单-用户身份校验error:不存在该用户!merchantId:"+merchantId+"sys_merchant_id:"+sys_merchant_id);
			printJson(response, returnJson("402","账号不存在").toString());
			return;
		}
		
		Map<String,String> params=new HashMap<String,String>();
		params.put("merchantId", merchantId);
		params.put("timeStamp", timeStamp);
		params.put("orderId", orderId);
		params.put("paramJson", paramJson);
		String sys_sign=ElongMd5Util.md5_32(ElongMd5Util.getParam(params, sys_sign_key), "UTF-8");
		if(sys_sign.equals(sign)){
			ObjectMapper mapper = new ObjectMapper();
			ElongOrderInfo orderInfo = null;
			try {
				orderInfo = mapper.readValue(paramJson, ElongOrderInfo.class);
				//过滤重复下单
				int orderCount=elongOrderService.getOrderCount(orderId);
				if(orderCount==0){
					List<ElongPassengerInfo> passengers=orderInfo.getPassengers();
					//orderInfo.setSumTicketPrice((Double.parseDouble(orderInfo.getTicketPrice())*passengers.size()+""));
					orderInfo.setSysSeatType(ElongConsts.get19eSeatType(orderInfo.getSeatType()));
					orderInfo.setOrder_status(Consts.ELONG_ORDER_DOWN);
					orderInfo.setOrder_name(orderInfo.getDptStation()+"/"+orderInfo.getArrStation());
					orderInfo.setTicket_num(passengers.size()+"");
					orderInfo.setChannel("elong");
					orderInfo.setOrder_type("11");//先预订后支付类型
					
					StringBuffer ext_field1=new StringBuffer();
					String acceptStand=orderInfo.getAcceptStand();
					ext_field1.append(ElongConsts.get19eSeatType(orderInfo.getSeatType())+"#");
					
					StringBuffer ext_field2=new StringBuffer();//elong备选坐席
					ext_field2.append("");
					/**备选座处理开始*/
					if(ElongConsts.ACCEPTSTAND.equals(acceptStand)){
						ext_field1.append("9,0|");
						ext_field2.append("0,0|");
					}
					
					String[] extSeat=orderInfo.getExtSeats();
					if (extSeat!=null&&extSeat.length>0){
						for(String es:extSeat){
							/*系统备选字符串*/
							ext_field1.append(ElongConsts.get19eSeatType(es)+",0|");
							/**艺龙 备选字符串 用于备选结果返回*/
							ext_field2.append(es+",0|");
						}
					}else{
						/** 兼容原接口 acceptStand==null */
						if(ElongConsts.NOTACCEPTSTAND.equals(acceptStand)||acceptStand==null){
							ext_field1.append("无|");
						}
					}
					String ext_all=ext_field1.toString();
					if(ext_all.contains("|")){
						ext_all=ext_all.substring(0,ext_all.lastIndexOf("|"));
					}
					//System.out.println(ext_all);
					orderInfo.setExt_field1(ext_all);
					orderInfo.setExt_field2(ext_field2.toString());
					/**************/
					
					for(ElongPassengerInfo p:passengers){
						p.setSysCertType(ElongConsts.get19eIdsType(p.getCertType(), p.getCertNo()));
						p.setSysTicketType(ElongConsts.get19eTicketType(p.getTicketType()));
						//p.setPay_money(orderInfo.getTicketPrice());
						p.setOrder_id(orderInfo.getOrderId());
						p.setSeat_type(ElongConsts.get19eSeatType(orderInfo.getSeatType()));
						p.setElong_seat_type(orderInfo.getSeatType());
						p.setTelephone(orderInfo.getContactMobile());
					}
					long step1=System.currentTimeMillis();
					/**订单入库操作*/
					elongOrderService.addOrder(orderInfo);
					printJson(response, returnJson("200","操作成功").toString());
					long step2=System.currentTimeMillis();
					
					ElongOrderLogsVo log=new ElongOrderLogsVo();
					log.setOpt_person("elong_app");
					log.setContent("成功接受艺龙订单["+orderInfo.getOrderId()+"]"+" 坐席类型["+(ElongConsts.getSeatTypeMessage(orderInfo.getSeatType()))+"] 备选坐席:["+orderInfo.getExt_field1()+"]");
					log.setOrder_id(orderInfo.getOrderId());
					
					elongOrderService.insertElongOrderLogs(log);
					
					long step3=System.currentTimeMillis();
					String resultStr=elongOrderService.send(orderInfo,"book");
					long step4=System.currentTimeMillis();
					elongOrderService.initNotice(resultStr,"book",orderInfo.getOrderId());
					long step5=System.currentTimeMillis();
					
					long step6=System.currentTimeMillis();
					logger.info("艺龙推送订单-下单成功"+orderInfo.getOrderId()+"耗时:"+(System.currentTimeMillis()-start)+"ms"
							+"step1:"+(step1-start)+",step2:"+(step2-step1)+",step3:"+(step3-step2)+",step4:"+(step4-step3)+
							",step5:"+(step5-step4)+",step6:"+(step6-step5));
				}else{
					Map<String, Object> orderMap=elongOrderService.queryOrderInfo(orderInfo.getOrderId());
					String order_status=orderMap.get("order_status").toString();
					if(Consts.ELONG_ORDER_BACKING.equals(order_status)){
						logger.info("艺龙推送订单-下单失败"+orderInfo.getOrderId()+",订单回库处理中");
						printJson(response, returnJson("-1","订单回库处理中").toString());
					}else if(Consts.ELONG_ORDER_BACKFAIL.equals(order_status)){
						logger.info("艺龙推送订单-下单失败"+orderInfo.getOrderId()+",订单回库失败");
						printJson(response, returnJson("-1","订单回库处理失败").toString());
					}else{
						logger.info("艺龙推送订单-重复下单"+orderInfo.getOrderId());
						printJson(response, returnJson("200","成功[重复下单]").toString());
					}
				}
			} catch (Exception e) {
				printJson(response, returnJson("400","系统错误").toString());
				logger.info("艺龙推送订单-"+orderId+"接收订单异常"+e);
				e.printStackTrace();
			}
		}else{
			logger.info("艺龙推送订单-校验不通过,加密前串:"+ElongMd5Util.getParam(params, sys_sign_key)+",加密后sys_sign:"+sys_sign+"_sign"+sign);
			printJson(response, returnJson("403","签名校验失败").toString());
		}
	}
	
	
	/**
	 * 获取艺龙推送订单信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getOrders.jhtml")
	public void getOrders(HttpServletRequest request, 
			HttpServletResponse response){   
		long start=System.currentTimeMillis();
		String merchantId=getParam(request, "merchantId");
		String timeStamp=getParam(request, "timeStamp");
		String orderId=getParam(request, "orderId");
		String paramJson=getParam(request, "paramJson");
		String sign=getParam(request, "sign");
		String sys_merchant_id=Consts.SYS_MERCHANT_ID;//SYS_MERCHANT_ID
		String sys_sign_key =Consts.SYS_SIGN_KEY;//SYS_SIGN_KEY
		logger.info("获取参数列表："+getFullURL(request));
		if(StrUtil.isEmpty(merchantId)||StrUtil.isEmpty(timeStamp)||StrUtil.isEmpty(orderId)
				||StrUtil.isEmpty(paramJson)||StrUtil.isEmpty(sign)){
			logger.info("艺龙推送订单-用户身份校验error:必要参数为空!merchantId:"+merchantId+",timeStamp:"+timeStamp
					+",orderId:"+orderId+",paramJson:"+paramJson+",sign:"+sign);
			printJson(response, returnJson("-1","必要参数不能为空").toString());
			return;
		}
		
		if (!sys_merchant_id.equals(merchantId)) {
			logger.info("艺龙推送订单-用户身份校验error:不存在该用户!merchantId:"+merchantId+"sys_merchant_id:"+sys_merchant_id);
			printJson(response, returnJson("-1","merchantId错误,不存在该用户").toString());
			return;
		}
		
		Map<String,String> params=new HashMap<String,String>();
		params.put("merchantId", merchantId);
		params.put("timeStamp", timeStamp);
		params.put("orderId", orderId);
		params.put("paramJson", paramJson);
		String sys_sign=ElongMd5Util.md5_32(ElongMd5Util.getParam(params, sys_sign_key), "UTF-8");
		if(sys_sign.equals(sign)){
			ObjectMapper mapper = new ObjectMapper();
			mapper.getDeserializationConfig().set(                  
				    org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			ElongOrderInfo orderInfo = null;
			try {
				orderInfo = mapper.readValue(paramJson, ElongOrderInfo.class);
				//过滤重复下单
				int orderCount=elongOrderService.getOrderCount(orderId);
				if(orderCount==0){
					List<ElongPassengerInfo> passengers=orderInfo.getPassengers();
					orderInfo.setSumTicketPrice((Double.parseDouble(orderInfo.getTicketPrice())*passengers.size()+""));
					orderInfo.setSysSeatType(ElongConsts.get19eSeatType(orderInfo.getSeatType()));
					orderInfo.setOrder_status(Consts.ELONG_ORDER_DOWN);
					orderInfo.setOrder_name(orderInfo.getDptStation()+"/"+orderInfo.getArrStation());
					orderInfo.setTicket_num(passengers.size()+"");
					StringBuffer ext_field1=new StringBuffer();
					String acceptStand=orderInfo.getAcceptStand();
					ext_field1.append(ElongConsts.get19eSeatType(orderInfo.getSeatType())+"#");
					orderInfo.setChannel("elong");
					orderInfo.setOrder_type("22");
					StringBuffer ext_field2=new StringBuffer();//elong备选坐席
					ext_field2.append("");
					/**备选座处理开始*/
					if(ElongConsts.ACCEPTSTAND.equals(acceptStand)){
						ext_field1.append("9,0|");
						ext_field2.append("0,0|");
					}
					
					String[] extSeat=orderInfo.getExtSeats();
					if (extSeat!=null&&extSeat.length>0){
						for(String es:extSeat){
							/*系统备选字符串*/
							ext_field1.append(ElongConsts.get19eSeatType(es)+",0|");
							/**艺龙 备选字符串 用于备选结果返回*/
							ext_field2.append(es+",0|");
						}
					}else{
						/** 兼容原接口 acceptStand==null */
						if(ElongConsts.NOTACCEPTSTAND.equals(acceptStand)||acceptStand==null){
							ext_field1.append("无|");
						}
					}
					String ext_all=ext_field1.toString();
					if(ext_all.contains("|")){
						ext_all=ext_all.substring(0,ext_all.lastIndexOf("|"));
					}
					//System.out.println(ext_all);
					orderInfo.setExt_field1(ext_all);
					orderInfo.setExt_field2(ext_field2.toString());
					/**************/
					
					for(ElongPassengerInfo p:passengers){
						p.setSysCertType(ElongConsts.get19eIdsType(p.getCertType(), p.getCertNo()));
						p.setSysTicketType(ElongConsts.get19eTicketType(p.getTicketType()));
						p.setPay_money(orderInfo.getTicketPrice());
						p.setOrder_id(orderInfo.getOrderId());
						p.setSeat_type(ElongConsts.get19eSeatType(orderInfo.getSeatType()));
						p.setElong_seat_type(orderInfo.getSeatType());
						p.setTelephone(orderInfo.getContactMobile());
					}
					/**订单入库操作*/
					
					long step1=System.currentTimeMillis();
					/**订单入库操作*/
					elongOrderService.addOrder(orderInfo);
					printJson(response, returnJson("0","操作成功").toString());
					long step2=System.currentTimeMillis();
					
					ElongOrderLogsVo log=new ElongOrderLogsVo();
					log.setOpt_person("elong_app");
					log.setContent("成功接受艺龙订单["+orderInfo.getOrderId()+"]"+" 坐席类型["+(ElongConsts.getSeatTypeMessage(orderInfo.getSeatType()))+"] 备选坐席:["+orderInfo.getExt_field1()+"]");
					log.setOrder_id(orderInfo.getOrderId());
					
					elongOrderService.insertElongOrderLogs(log);
					
					long step3=System.currentTimeMillis();
					String resultStr=elongOrderService.send(orderInfo,"outTicket");
					long step4=System.currentTimeMillis();
					elongOrderService.initNotice(resultStr,"outTicket",orderInfo.getOrderId());
					long step5=System.currentTimeMillis();
					
					long step6=System.currentTimeMillis();
					logger.info("艺龙推送订单-下单成功"+orderInfo.getOrderId()+"耗时:"+(System.currentTimeMillis()-start)+"ms"
							+"step1:"+(step1-start)+",step2:"+(step2-step1)+",step3:"+(step3-step2)+",step4:"+(step4-step3)+
							",step5:"+(step5-step4)+",step6:"+(step6-step5));
				}else{
					Map<String, Object> orderMap=elongOrderService.queryOrderInfo(orderInfo.getOrderId());
					String order_status=orderMap.get("order_status").toString();
					if(Consts.ELONG_ORDER_BACKING.equals(order_status)){
						logger.info("艺龙推送订单-下单失败"+orderInfo.getOrderId()+",订单回库处理中");
						printJson(response, returnJson("-1","订单回库处理中").toString());
					}else if(Consts.ELONG_ORDER_BACKFAIL.equals(order_status)){
						logger.info("艺龙推送订单-下单失败"+orderInfo.getOrderId()+",订单回库失败");
						printJson(response, returnJson("-1","订单回库处理失败").toString());
					}else{
						logger.info("艺龙推送订单-重复下单"+orderInfo.getOrderId());
						printJson(response, returnJson("0","操作成功[重复订单]").toString());
					}
				}
			} catch (Exception e) {
				printJson(response, returnJson("500","系统错误").toString());
				logger.info("艺龙推送订单-"+orderId+"接收订单异常"+e);
				e.printStackTrace();
			}
		}else{
			logger.info("艺龙推送订单-校验不通过,加密前串:"+ElongMd5Util.getParam(params, sys_sign_key)+",加密后sys_sign:"+sys_sign+"_sign"+sign);
			printJson(response, returnJson("-1","校验不通过").toString());
		}
	}
	/**
	 * 获取艺龙推送退款 订单信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/refund.jhtml")
	public void refund(HttpServletRequest request, 
			HttpServletResponse response){  
		String merchantId=getParam(request, "merchantId");
		String timeStamp=getParam(request, "timeStamp");
		String orderId=getParam(request, "orderId");
		String orderItemId=getParam(request, "orderItemId");
		String paramJson=getParam(request, "paramJson");
		String sign=getParam(request, "sign");
		//Map<String, String> merchantSetting = commonService.queryMerchantInfo(merchantId);
		String sys_merchant_id=Consts.SYS_MERCHANT_ID;//SYS_MERCHANT_ID
		String sys_sign_key =Consts.SYS_SIGN_KEY;//SYS_SIGN_KEY
		
		if(StrUtil.isEmpty(merchantId)||StrUtil.isEmpty(timeStamp)||StrUtil.isEmpty(orderId)
				||StrUtil.isEmpty(orderItemId)||StrUtil.isEmpty(paramJson)||StrUtil.isEmpty(sign)){
			logger.info("艺龙推送退款-error:必要参数为空!merchantId:"+merchantId+",timeStamp:"+timeStamp
					+",orderId:"+orderId+",paramJson:"+paramJson+",sign:"+sign);
			
			printJson(response, returnJson("-1","必要参数不能为空").toString());
			return;
		}
		
		if (!sys_merchant_id.equals(merchantId)) {
			logger.info("艺龙推送退款-用户身份校验error:不存在该用户!merchantId:"+merchantId+"sys_merchant_id:"+sys_merchant_id);
			printJson(response, returnJson("-1","不存在该用户").toString());
			return;
		}
		Map<String,String> params=new HashMap<String,String>();
		params.put("merchantId", merchantId);
		params.put("timeStamp", timeStamp);
		params.put("orderId", orderId);
		params.put("orderItemId", orderItemId);
		params.put("paramJson", paramJson);
		String sys_sign=ElongMd5Util.md5_32(ElongMd5Util.getParam(params, sys_sign_key), "UTF-8");
		
		String key ="elong"+ orderId+orderItemId;
		if(null == MemcachedUtil.getInstance().getAttribute(key)){
			boolean setAttr=MemcachedUtil.getInstance().setAttribute(key,System.currentTimeMillis(), 5*1000);
			logger.info("艺龙推送退款-缓存请求编号"+orderId+"/"+orderItemId+(setAttr?"成功":"失败"));
		}else{
			long start = (Long)MemcachedUtil.getInstance().getAttribute(key);
			logger.info("艺龙推送退款-5s内缓存检测到,重复提交:"+orderId+"/"+orderItemId+",间隔"+(System.currentTimeMillis()-start));
			printJson(response, returnJson("0","操作成功:重复提交").toString());
			return;
		}
		if(sign.equals(sys_sign)){
			try {
				//ObjectMapper mapper = new ObjectMapper();
				//ElongRefundVo refundInfo = null;/**可以不用解析*/
				/**查询订单信息*/
				Map<String, Object> orderInfo=elongOrderService.queryOrderInfo(orderId);
				
				if(orderInfo!=null&&(Consts.ELONG_ORDER_SUCCESS.equals(orderInfo.get("order_status")+"")
						||Consts.ELONG_ORDER_MAKED.equals(orderInfo.get("order_status")+""))){
					String orderStatus=orderInfo.get("order_status").toString();//订单状态
					Map<String,String> paramMap=new HashMap<String, String>();
					paramMap.put("cp_id", orderItemId);
					paramMap.put("order_id", orderId);
					paramMap.put("refund_seq", CreateIDUtil.createID("TK"));
					paramMap.put("refund_type", "11");
					String status=elongOrderService.queryRefundStatus(paramMap);//查询退款状态
					if(status==null){
						/**插入新退款记录*/
						String refund_money = "0";
						String start_time = orderInfo.get("from_time")+":00";
						//System.out.println("start_time"+start_time+"___from_time"+ orderInfo.get("from_time"));
						start_time = DateUtil.dateToString(DateUtil.stringToDate(start_time,DateUtil.DATE_FMT1),DateUtil.DATE_FMT1);
						String from_15d = DateUtil.dateAddDaysFmt3(orderInfo.get("from_time")+":00","-15");
						String from_24 = DateUtil.dateAddDaysFmt3(orderInfo.get("from_time")+":00","-1");
		                String from_48 = DateUtil.dateAddDaysFmt3(orderInfo.get("from_time")+":00","-2");
		                //String refund_percent = null;
		                double feePercent = 0;
		                /*if("2015-02-04".compareTo(start_time)<0 && "2015-03-15".compareTo(start_time)>0){
		                    feePercent = 0.2;
		                }else{*/
	                    if(new Date().before(DateUtil.stringToDate(from_15d, "yyyy-MM-dd HH:mm:ss"))){
	                         feePercent = 0.0;
	                    }else if(new Date().before(DateUtil.stringToDate(from_48, "yyyy-MM-dd HH:mm:ss"))){
	                         feePercent = 0.05;
	                    }else if(new Date().before(DateUtil.stringToDate(from_24, "yyyy-MM-dd HH:mm:ss"))){
	                         feePercent = 0.1;
	                    }else{
	                         feePercent = 0.2;
	                    }
		               // }
						/*String from_24 = DateUtil.dateAddDaysFmt3(orderInfo.get("from_time")+":00","-1");
						String from_48 = DateUtil.dateAddDaysFmt3(orderInfo.get("from_time")+":00","-2");
						double feePercent = 0;
						if(new Date().before(DateUtil.stringToDate(from_48, "yyyy-MM-dd HH:mm:ss"))){
								feePercent = 0.05;
						}else if(new Date().before(DateUtil.stringToDate(from_24, "yyyy-MM-dd HH:mm:ss"))){
							feePercent = 0.1;
						}else{
							feePercent = 0.2;
						}*/
						double total_refund_money = 0;
						double buy_money = Double.parseDouble(elongOrderService.queryCpPayMoney(paramMap));
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
						if(Consts.ELONG_ORDER_SUCCESS.equals(orderStatus)){
							paramMap.put("refund_status", "00");//00：等待机器改签
						}
						if(Consts.ELONG_ORDER_MAKED.equals(orderStatus)){
							paramMap.put("refund_status", "55");// 针对预订成功未支付成功的 初始状态为预退款状态
						}
						paramMap.put("refund_money", refund_money);
						paramMap.put("channel", "elong");
						elongOrderService.insertRefundOrder(paramMap);
						logger.info("艺龙推送退款-申请退款成功order_id:"+orderId+"&cp_id:"+orderItemId+("success".equals(paramMap.get("refund_status"))?"2元及2元以下,直接置于退票成功":""));
						printJson(response, returnJson("0","操作成功").toString());
					}else{
						if("22".equals(status)){
							logger.info("艺龙推送退款-原订单"+orderId+"/"+orderItemId+"为拒绝状态,重置退票状态");
							elongOrderService.updateRefundStatus(paramMap);
							ElongOrderLogsVo log=new ElongOrderLogsVo();
							log.setOpt_person("elong_app");
							log.setContent("艺龙重复退款申请成功["+paramMap.get("order_id")+"/"+paramMap.get("cp_id")+"]针对已拒绝退款车票");
							log.setOrder_id(paramMap.get("order_id"));
							elongOrderService.insertElongOrderLogs(log);
						}else{
							logger.info("艺龙推送退款-重复提交退款请求order_id:"+orderId+"&cp_id:"+orderItemId);
						}
						printJson(response, returnJson("0","操作成功:重复提交").toString());
					}
				}else{
					logger.info("艺龙推送退款-订单表没有该订单，无法申请退款操作"+orderId);
					printJson(response, returnJson("-1","不存在该订单,或者订单不为出票成功状态").toString());
				}
			}catch (Exception e) {
				printJson(response, returnJson("500","系统错误").toString());
				logger.error("艺龙推送退款-接收订单异常"+e);
				e.printStackTrace();
			}
		}else{
			logger.info("艺龙推送退款-校验不通过,加密前串:"+ElongMd5Util.getParam(params, sys_sign_key)+",sys_sign:"+sys_sign+",sign:"+sign);
			printJson(response, returnJson("-1","校验不通过").toString());
		}
	}
	/**
	 * 获取艺龙线下退款申请
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/offlineRefund.jhtml")
	public void offlineRefund(HttpServletRequest request, 
			HttpServletResponse response){  
		String merchantId=getParam(request, "merchantId");
		String timeStamp=getParam(request, "timeStamp");
		String orderId=getParam(request, "orderId");
		String orderItemId=getParam(request, "orderItemId");
		String type=getParam(request, "type");
		String note=getParam(request, "note");
		String sign=getParam(request, "sign");
		String sys_merchant_id=Consts.SYS_MERCHANT_ID;//SYS_MERCHANT_ID
		String sys_sign_key =Consts.SYS_SIGN_KEY;//SYS_SIGN_KEY
		if(StrUtil.isEmpty(merchantId)||StrUtil.isEmpty(timeStamp)||StrUtil.isEmpty(orderId)
				||StrUtil.isEmpty(orderItemId)||StrUtil.isEmpty(sign)||StrUtil.isEmpty(type)){
			logger.info("艺龙offline退款-error:必要参数为空!merchantId:"+merchantId+",timeStamp:"+timeStamp
					+",orderId:"+orderId+",orderItemId:"+orderItemId+"sign:"+sign+"type:"+type);
			printJson(response, returnJson("-1","必要参数不能为空").toString());
			return;
		}
		if (!sys_merchant_id.equals(merchantId)) {
			logger.info("艺龙offline退款-用户身份校验error:不存在该用户!merchantId:"+merchantId+"sys_merchant_id:"+sys_merchant_id);
			printJson(response, returnJson("-1","不存在该用户").toString());
			return;
		}
		String key ="elong"+ orderId+orderItemId;
		if(null == MemcachedUtil.getInstance().getAttribute(key)){
			boolean setAttr=MemcachedUtil.getInstance().setAttribute(key,System.currentTimeMillis(), 5*1000);
			logger.info("艺龙offline退款-缓存请求编号"+orderId+"/"+orderItemId+(setAttr?"成功":"失败"));
		}else{
			long start = (Long)MemcachedUtil.getInstance().getAttribute(key);
			logger.info("艺龙offline退款-5s内缓存检测到,重复提交:"+orderId+"/"+orderItemId+",间隔"+(System.currentTimeMillis()-start));
			printJson(response, returnJson("-1","您操作过于频繁,请稍后再处理...").toString());
			return;
		}
		try {
			Map<String,String> params=new HashMap<String,String>();
			params.put("merchantId", merchantId);
			params.put("timeStamp", timeStamp);
			params.put("orderId", orderId);
			params.put("orderItemId", orderItemId);
			params.put("type", type);
			params.put("note", note);
			String sys_sign=ElongMd5Util.md5_32(ElongMd5Util.getParam(params, sys_sign_key), "UTF-8");
			if(sign.equals(sys_sign)){
				//查询是否发生过线上退票
				Map<String,String> query=new HashMap<String,String>();
				query.put("order_id", orderId);
				query.put("cp_id", orderItemId);
				query.put("refund_type",  Consts.ELONG_REFUNDTYPE_TICKET);
				String ticketRefundStatus=elongOrderService.queryRefundStatus(query);
				//查询是否发生系统的线下退款
				query.put("refund_type",  Consts.ELONG_REFUNDTYPE_AMOUNT);
				String offlineRefundStatus=elongOrderService.queryRefundStatus(query);
				if((ticketRefundStatus==null||Consts.ELONG_REFUNDSTATUS_FAIL.equals(ticketRefundStatus))&&offlineRefundStatus==null){
					params.put("refund_status", Consts.ELONG_USER_OFFLINEREFUND);
					params.put("opt_person", "elong_app");
					params.put("refund_type", Consts.ELONG_REFUNDTYPE_AMOUNT);
					//type	类型	是	string	是	1-	退票2-	改签
					params.put("user_remark", ("1".equals(type)?"(正常退票)":"(改签退票)")+note);
					params.put("our_remark", "艺龙接口申请线下退款");
					params.put("refund_seq", CreateIDUtil.createID("TKE"));
					params.put("channel", "elong");
					elongOrderService.addOfflineRefund(params);
					printJson(response, returnJson("0","操作成功").toString());
				}else{
					if(ticketRefundStatus!=null&&(!Consts.ELONG_REFUNDSTATUS_FAIL.equals(ticketRefundStatus))){
						logger.info("艺龙offline退款-error:"+orderId+"/"+orderItemId+",已经发生线上退票");
						printJson(response, returnJson("-1","该订单已经线上退票处理中,请人工处理").toString());
					}else{
						logger.info("艺龙offline退款-error:"+orderId+"/"+orderItemId+",系统发生过线下退款");
						printJson(response, returnJson("-1","该订单已经发生过历史线下退款,建议转人工处理").toString());
					}
				}
			}else{
				logger.info("艺龙offline退款-校验不通过,加密前串:"+ElongMd5Util.getParam(params, sys_sign_key)+",sys_sign:"+sys_sign+",sign:"+sign);
				printJson(response, returnJson("-1","校验不通过").toString());
			}
		} catch (Exception e) {
			logger.info("艺龙offline退款-异常:"+e);
			printJson(response, returnJson("500","系统错误").toString());
			e.printStackTrace();
		}
	}
	
	/**
	 * 艺龙支付请求 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/payOrder.jhtml")
	public void payOrder(HttpServletRequest request, 
			HttpServletResponse response){   
		
		/*merchantId	供应商id	是	string	是	分配给艺龙的id
		timeStamp	推送时间戳	是	string	是	发送请求的时间戳
		orderId	订单号	是	string	是
			下单订单号
		ticketPrice	支付金额	是	BigDecimal	是	用户支付总额。
		sign	签名	是	string	否	签名*/
		String merchantId=getParam(request, "merchantId");
		String timeStamp=getParam(request, "timeStamp");
		String orderId=getParam(request, "orderId");
		String ticketPrice=getParam(request, "ticketPrice");
		String result =getParam(request, "result");
		String sign=getParam(request, "sign");
		String sys_merchant_id=Consts.SYS_MERCHANT_ID;//SYS_MERCHANT_ID
		String sys_sign_key =Consts.SYS_SIGN_KEY;//SYS_SIGN_KEY
		
		if(StrUtil.isEmpty(merchantId)||StrUtil.isEmpty(timeStamp)||StrUtil.isEmpty(orderId)
				||StrUtil.isEmpty(ticketPrice)||StrUtil.isEmpty(sign)){
			logger.info("艺龙支付请求-用户身份校验error:必要参数为空!merchantId:"+merchantId+",timeStamp:"+timeStamp
					+",orderId:"+orderId+",ticketPrice:"+ticketPrice+",sign:"+sign);
			printJson(response, returnJson("401","参数校验失败").toString());
			return;
		}
		
		if (!sys_merchant_id.equals(merchantId)) {
			logger.info("艺龙支付请求 -用户身份校验error:不存在该用户!merchantId:"+merchantId+"sys_merchant_id:"+sys_merchant_id);
			printJson(response, returnJson("402","账号不存在").toString());
			return;
		}
		
		Map<String,String> params=new HashMap<String,String>();
		params.put("merchantId", merchantId);
		params.put("timeStamp", timeStamp);
		params.put("orderId", orderId);
		params.put("result", result);
		params.put("ticketPrice", ticketPrice);
		String sys_sign=ElongMd5Util.md5_32(ElongMd5Util.getParam(params, sys_sign_key), "UTF-8");
		if(sys_sign.equals(sign)){
			try {
				DBOrderInfo orderInfo=orderService.queryOrderInfo(orderId);
				if(orderInfo==null){
					logger.info("艺龙支付请求-请求[App]确认出票ERROR,订单不存在order_id:"+orderId);
					printJson(response, returnJson("452","此订单不存在").toString());
				}else{
					String orderStatus=orderInfo.getOrder_status();
					String buy_money=orderInfo.getBuy_money();
					/*if(Double.parseDouble(buy_money)!=Double.parseDouble(ticketPrice)){
						logger.info("艺龙支付请求 -支付金额错误，ticketPrice"+ticketPrice+",buy_money"+buy_money);
						printJson(response, returnJson("200","操作成功").toString());
						return;
					}*/
					//"SUCCESS"  "FAIL"
					if("SUCCESS".equals(result)){
						if(Consts.ELONG_ORDER_MAKED.equals(orderStatus)){
							String	payResult=orderService.pay(orderId, ticketPrice);
							if("SUCCESS".equals(payResult)){
								logger.info("艺龙通知支付[成功]确认支付出票SUCCESS,order_id:"+orderId);
								ElongOrderLogsVo log=new ElongOrderLogsVo();
								log.setOpt_person("elong_app");
								log.setContent("艺龙请求支付订单[成功]");
								log.setOrder_id(orderId);
								elongOrderService.insertElongOrderLogs(log);
								//支付成功
								printJson(response, returnJson("200","操作成功").toString());
							}else{
								logger.info("App-请求[App]确认出票FAIL,order_id:"+orderId+"result:"+result);
								printJson(response, returnJson("400","系统错误,请重新操作").toString());
							}
						}else if(Consts.ELONG_ORDER_WAITPAY.equals(orderStatus)||Consts.ELONG_ORDER_SUCCESS.equals(orderStatus)){
							printJson(response, returnJson("200","操作成功").toString());
						}else{
							logger.info("艺龙支付请求-请求[App]确认出票ERROR,order_id:"+orderId+"订单状态错误:"+orderStatus);
							printJson(response, returnJson("452","此订单不存在:订单状态错误").toString());
						}
					}else{
						//请求支付失败 发起取消订单操作
						if(Consts.ELONG_ORDER_MAKED.equals(orderStatus)){
							logger.info("艺龙通知支付[失败] 取消订单开始,order_id:"+orderId);
							//同步->出票系统 发起取消预订
							//send
							String cancelResult=orderService.cancel(orderId);
							if(cancelResult.equals("SUCCESS")){
								logger.info("App-请求[出票Sys]取消订单SUCCESS,order_id:"+orderId);
								
								ElongOrderLogsVo log=new ElongOrderLogsVo();
								log.setOpt_person("elong_app");
								log.setContent("艺龙请求取消订单[成功]");
								log.setOrder_id(orderId);
								elongOrderService.insertElongOrderLogs(log);
								//取消火车票成功
								printJson(response, returnJson("200","操作成功").toString());
							}else{
								printJson(response, returnJson("400","系统错误,请重新操作").toString());
							}
						}else if(Consts.ELONG_ORDER_CANCELED.equals(orderStatus)){
							logger.info("艺龙通知支付[失败] SUCCESS,已经取消成功"+orderId);
							//取消火车票成功
							printJson(response, returnJson("200","操作成功").toString());
						}else if(Consts.ELONG_OUT_TIME.equals(orderStatus)){
							logger.info("艺龙通知支付[失败] SUCCESS,已经超时"+orderId);
							//取消火车票成功
							ElongOrderLogsVo log=new ElongOrderLogsVo();
							log.setOpt_person("elong_app");
							log.setContent("艺龙通知支付[失败],超时未支付默认取消成功");
							log.setOrder_id(orderId);
							elongOrderService.insertElongOrderLogs(log);
							
							printJson(response, returnJson("200","操作成功").toString());
						}
						else{
							logger.info("艺龙通知支付[失败] ERROR,状态错误order_id:"+orderId+",orderStatus:"+orderStatus);
							//订单状态错误
							printJson(response, returnJson("452","此订单不存在:订单状态错误").toString());
						}
						
						
					}
				}
			} catch (Exception e) {
				printJson(response, returnJson("400","系统错误").toString());
				logger.info("艺龙支付请求 -"+orderId+"处理发送异常"+e);
			}
		}else{
			logger.info("艺龙支付请求 -校验不通过,加密前串:"+ElongMd5Util.getParam(params, sys_sign_key)+",加密后sys_sign:"+sys_sign+"_sign"+sign);
			printJson(response, returnJson("403","签名校验失败").toString());
		}
	}
	
	/**
	 * 艺龙取消订单请求 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cancelOrder.jhtml")
	public void cancelOrder(HttpServletRequest request, 
			HttpServletResponse response){   
		
		/*merchantId	供应商id	是	string	是	分配给艺龙的id
		timeStamp	推送时间戳	是	string	是	发送请求的时间戳
		orderId	订单号	是	string	是
			下单订单号
		ticketPrice	支付金额	是	BigDecimal	是	用户支付总额。
		sign	签名	是	string	否	签名*/
		String merchantId=getParam(request, "merchantId");
		String timeStamp=getParam(request, "timeStamp");
		String orderId=getParam(request, "orderId");
		String sign=getParam(request, "sign");
		String sys_merchant_id=Consts.SYS_MERCHANT_ID;//SYS_MERCHANT_ID
		String sys_sign_key =Consts.SYS_SIGN_KEY;//SYS_SIGN_KEY
		
		if(StrUtil.isEmpty(merchantId)||StrUtil.isEmpty(timeStamp)||StrUtil.isEmpty(orderId)
				||StrUtil.isEmpty(sign)){
			logger.info("艺龙取消请求-用户身份校验error:必要参数为空!merchantId:"+merchantId+",timeStamp:"+timeStamp
					+",orderId:"+orderId+",sign:"+sign);
			printJson(response, returnJson("401","参数校验失败").toString());
			return;
		}
		
		if (!sys_merchant_id.equals(merchantId)) {
			logger.info("艺龙取消请求 -用户身份校验error:不存在该用户!merchantId:"+merchantId+"sys_merchant_id:"+sys_merchant_id);
			printJson(response, returnJson("402","账号不存在").toString());
			return;
		}
		
		Map<String,String> params=new HashMap<String,String>();
		params.put("merchantId", merchantId);
		params.put("timeStamp", timeStamp);
		params.put("orderId", orderId);
		String sys_sign=ElongMd5Util.md5_32(ElongMd5Util.getParam(params, sys_sign_key), "UTF-8");
		if(sys_sign.equals(sign)){
			String orderStatus=orderService.queryOrderStatusByOrderId(orderId);
			try {
				if(orderStatus!=null){
					if(Consts.ELONG_ORDER_MAKED.equals(orderStatus)||Consts.ELONG_ORDER_WAITPAY.equals(orderStatus)){
						logger.info("艺龙-请求[App]取消订单开始,order_id:"+orderId);
						//同步->出票系统 发起取消预订
						//send
						String result=orderService.cancel(orderId);
						if(result.equals("SUCCESS")){
							logger.info("App-请求[出票Sys]取消订单SUCCESS,order_id:"+orderId);
							
							ElongOrderLogsVo log=new ElongOrderLogsVo();
							log.setOpt_person("elong_app");
							log.setContent("艺龙请求取消订单[成功],原订单状态："+orderStatus);
							log.setOrder_id(orderId);
							elongOrderService.insertElongOrderLogs(log);
							//取消火车票成功
							printJson(response, returnJson("200","操作成功").toString());
						}else{
							printJson(response, returnJson("400","系统错误,请重新操作").toString());
						}
					}else if(Consts.ELONG_ORDER_CANCELED.equals(orderStatus)){
						logger.info("艺龙-请求[App]取消订单SUCCESS,已经取消成功"+orderId);
						//取消火车票成功
						printJson(response, returnJson("200","操作成功").toString());
					}else if(Consts.ELONG_OUT_TIME.equals(orderStatus)||Consts.ELONG_ORDER_FAIL.equals(orderStatus)){
						logger.info("艺龙-请求[App]取消订单SUCCESS,已经超时"+orderId);
						//取消火车票成功
						ElongOrderLogsVo log=new ElongOrderLogsVo();
						log.setOpt_person("elong_app");
						log.setContent("艺龙请求取消订单[成功],超时/失败默认取消成功:"+orderStatus);
						log.setOrder_id(orderId);
						elongOrderService.insertElongOrderLogs(log);
						
						printJson(response, returnJson("200","操作成功").toString());
					}
					else{
						logger.info("艺龙-请求[App]取消订单ERROR,状态错误order_id:"+orderId+",orderStatus:"+orderStatus);
						//订单状态错误
						printJson(response, returnJson("452","此订单不存在:订单状态错误").toString());
					}
				}else{
					logger.info("艺龙-请求[App]取消订单ERROR,订单不存在order_id:"+orderId);
					printJson(response, returnJson("452","此订单不存在").toString());
				}
			} catch (Exception e) {
				//false 系统异常
				logger.info("艺龙-请求[App]取消订单异常,order_id:"+orderId+",msg"+e);
				printJson(response, returnJson("400","系统错误").toString());
				e.printStackTrace();
			}
		}else{
				logger.info("艺龙取消请求 -校验不通过,加密前串:"+ElongMd5Util.getParam(params, sys_sign_key)+",加密后sys_sign:"+sys_sign+"_sign"+sign);
				printJson(response, returnJson("403","签名校验失败").toString());
			}
	}
	
	
	
	
	/**
	 * 出票结果通知
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/orderNotice.jhtml")
	public void orderNotice(HttpServletRequest request, 
			HttpServletResponse response) {
		try {
			String result = this.getParam(request, "result");
			String orderId = this.getParam(request, "orderid");
			String billNo = this.getParam(request, "billno");
			String buyMoney = this.getParam(request, "buymoney");
			String seatTrains = this.getParam(request, "seattrains");
			String status = this.getParam(request, "status");//00：出票成功 11预定成功
			String level = this.getParam(request, "level");//vip用户不为空
			logger.info("[接收出票结果通知接口]参数orderId=" + orderId 
					+ ",result=" + result + ",billNo=" + billNo
					+ ",buyMoney=" + buyMoney + ",seatTrains=" + seatTrains + ",status=" + status+",level="+level);
			Map<String, Object> paramMap = new HashMap<String, Object>();//主订单参数
			List<Map<String, String>> cpMapList = new ArrayList<Map<String, String>>();//车票订单参数
			
			/**获取通知类型   出票成功通知  或者预订成功通知   0、出票成功通知 1、预定成功通知*/
			String noticeType=this.queryElongSysValueByName("out_notify_elong").trim();
			if(StringUtils.isEmpty(result) || StringUtils.isEmpty(orderId)){
				//参数为空
				logger.info("exception:参数为空！");
				write2Response(response, "failed");
			}else if(TrainConsts.SUCCESS.equalsIgnoreCase(result)){//成功
				if(StringUtils.isEmpty(billNo) || StringUtils.isEmpty(buyMoney) 
						|| StringUtils.isEmpty(status) || StringUtils.isEmpty(seatTrains)){
					//参数为空
					logger.info("exception:明细参数为空！");
					write2Response(response, "failed");
					return;
				}else if(!"11".equals(status) && !"00".equals(status)){
					logger.info("exception:状态有误！status="+status);
					write2Response(response, "failed");
					return;
				}
				//查询订单信息
				Map<String,Object> orderInfo=elongOrderService.queryOrderInfo(orderId);
				
				if(StringUtils.isEmpty(orderInfo.get("order_status").toString())){
					logger.info("exception:订单查询异常，订单状态为空！");
					write2Response(response, "failed");
					return;
				}
				if("11".equals(status)){//预订成功
					if(Consts.ELONG_ORDER_MAKED.equals(orderInfo.get("order_status"))){
						logger.info("[接收出票结果通知接口]本次出票成功通知为重复通知,orderId=" + orderId);
						write2Response(response, "success");
						return;
					}
					paramMap.put("order_id", orderId);
					paramMap.put("buy_money", buyMoney);
					paramMap.put("out_ticket_billno", billNo);
					paramMap.put("order_status",Consts.ELONG_ORDER_MAKED);//预订成功
					//明细数据处理
					this.detailDataPacking(seatTrains, cpMapList, response);
					
					/**更新订单信息*/
					elongOrderService.updateOrderInfo(paramMap,cpMapList,Consts.MAKED_NOTICE.equals(noticeType)?true:false);
					
					write2Response(response, "success");
				}
				if("00".equals(status)){//出票成功    //
					/**重复通知处理*/
					if(Consts.ELONG_ORDER_SUCCESS.equals(orderInfo.get("order_status"))){
						logger.info("[接收出票结果通知接口]本次出票成功通知为重复通知,orderId=" + orderId);
						write2Response(response, "success");
						return;
					}
					paramMap.put("order_id", orderId);
					paramMap.put("buy_money", buyMoney);
					paramMap.put("out_ticket_time", "now");//保证出票时间值非空，数据库进行处理
					paramMap.put("out_ticket_billno", billNo);
					paramMap.put("order_status",Consts.ELONG_ORDER_SUCCESS);//出票成功
					
					//明细数据处理
					this.detailDataPacking(seatTrains, cpMapList, response);
					
					/**出票成功通知 预备*/
					boolean isNotNotice=Consts.SUCCESS_NOTICE.equals(noticeType)?true:false;
					String out_notify_status=elongOrderService.queryNoticeStatusByOrderId(orderId);
					//00、准备通知  11、开始通知  22、通知完成  33、通知失败
					if(!(Consts.NOTICE_START.equals(out_notify_status)||
							Consts.NOTICE_ING.equals(out_notify_status)||
							Consts.NOTICE_OVER.equals(out_notify_status))){
						isNotNotice=true;
					}
					
					/**更新订单信息*/
					elongOrderService.updateOrderInfo(paramMap,cpMapList,isNotNotice);
					
					/**更新预备退票 退票状态*/
					if(cpMapList!=null){
						for(Map<String,String> cpInfo:cpMapList){
							cpInfo.put("order_id",orderId);
							cpInfo.put("refund_type","11");
							String cpStatus=elongOrderService.queryRefundStatus(cpInfo);//查询退款状态
							if("55".equals(cpStatus)){
								//预备退票 状态更新为 开始机器退票状态
								elongOrderService.updateRefundStatus(cpInfo);
								ElongOrderLogsVo log=new ElongOrderLogsVo();
								log.setOpt_person("elong_app");
								log.setContent("车票号["+cpInfo.get("cp_id")+"]预退票状态激活为机器退改签");
								log.setOrder_id(orderId);
								elongOrderService.insertElongOrderLogs(log);
							}
						}
					}
					
					write2Response(response, "success");
				}
			}else if(TrainConsts.FAILURE.equalsIgnoreCase(result)){//失败
				//查询订单信息
				Map<String,Object> orderInfo=elongOrderService.queryOrderInfo(orderId);
				List<Map<String, Object>> cpList = elongOrderService.querySendOrderCpsInfo(orderId);
				if(!StringUtils.isEmpty(orderInfo.get("order_status").toString())
						&& Consts.ELONG_ORDER_FAIL.equals(orderInfo.get("order_status"))){
					logger.info("[接收出票结果通知接口]本次出票失败通知为重复请求，orderId=" + orderId);
					write2Response(response, "success");
					return;
				}
				String passengers = this.getParam(request, "passengers");//乘客审核信息
				String errorinfo = this.getParam(request, "errorinfo");//错误信息
				logger.info("接收出票结果通知接口 参数orderId=" + orderId + ",errorinfo=" + errorinfo );
				//乘车人信息错误
				String passenger_reason = "";
				JSONArray pJa = new JSONArray();
				JSONObject jsobj = null;
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
							for(Map<String, Object> cp : cpList){
								if(cp.get("cp_id").equals(cp_id)){
									certType = cp.get("elong_ids_type").toString();
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
				//错误信息：1所购买的车次坐席已无票 2身份证件已经实名制购票 3票价和12306不符 4乘车时间异常 5证件错误 6用户要求取消订单 7未通过12306实名认证 8乘客身份信息待核验  【qunar】错误信息：0、其他 1、所购买的车次坐席已无票 2、身份证件已经实名制购票，不能再次购买同日期同车次的车票 3、qunar票价和12306不符 4、车次数据与12306不一致 5、乘客信息错误 6、12306乘客身份信息核验失败
				paramMap.put("order_id", orderId);
				paramMap.put("buy_money", buyMoney);
				paramMap.put("out_ticket_billno", billNo);
				paramMap.put("order_status", Consts.ELONG_ORDER_FAIL);//出票失败
				paramMap.put("passenger_reason", passenger_reason);
				paramMap.put("out_fail_reason", errorinfo);
				
				if(Consts.OUT_FAIL_REASON_3.equals(errorinfo)){
					this.detailDataPacking(seatTrains, cpMapList, response);
					elongOrderService.updateOrderInfo(paramMap,cpMapList,true);
				}else{
					elongOrderService.updateOrderInfo(paramMap,null,true);
				}
				/**更新订单表*/
				write2Response(response, "success");
				//明细数据处理
			}else{//异常
				logger.info("exception：订单" + orderId + "，接口返回未知状态码！");
				write2Response(response, "failed");
			}
		} catch (Exception e) {
			logger.info("获取出票结果通知 异常"+e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 明细数据组合
	 */
	private void detailDataPacking(String seatTrains, List<Map<String, String>> cpMapList,
			HttpServletResponse response){
		//CP0120212|133|12|058号#CP0120213|133|12|059号
		logger.info("明细拆分内容"+seatTrains);
		String[] seatMsgs = seatTrains.split("#");
		Map<String, String> cpMap = null;
		for (String seatMsg : seatMsgs) {//CP0120212|133|12|058号
			String[] str = seatMsg.split("\\|");
			if(str == null ){
				//参数为空
				logger.info("exception：参数拆分失败！");
				write2Response(response, "failed");
				return;
			}
			
				cpMap = new HashMap<String, String>(5);
				cpMap.put("cp_id", str[0]);
				cpMap.put("buy_money", str[1]);//成本价格
				cpMap.put("train_box", str[2]);//车厢
				cpMap.put("seat_no", str[3]);//座位号
				if(str.length == 5){
					cpMap.put("seat_type", str[4]);//座位类型
				}
				cpMapList.add(cpMap);
			
		}
	}
	public JSONObject returnJson(String retcode,String retdesc){
		JSONObject json = new JSONObject();
		json.put("retcode", retcode);
		json.put("retdesc", retdesc);
		return json;
	}
	
	private String toString(Object obj){
		return obj==null?"":obj.toString();
	}
}
