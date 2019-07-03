package com.l9e.transaction.controller;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiexun.iface.sign.PayResultSign;
import com.jiexun.iface.sign.RefundResultSign;
import com.jiexun.iface.sign.SignService;
import com.jiexun.iface.util.ASPUtil;
import com.l9e.common.BaseController;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.ReceiveNotifyService;
import com.l9e.transaction.service.RefundService;
import com.l9e.transaction.vo.ExternalLogsVo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.util.MobileMsgUtil;

@Controller
@RequestMapping("/receiveNotify")
public class ReceiveNotifyController extends BaseController{
	@Value("#{propertiesReader[pay_sign_key]}")
	protected String pay_sign_key;		//平台密钥
	
	private static final Logger logger = Logger.getLogger(ReceiveNotifyController.class);
	
	@Resource
	private OrderService orderService;
	
	@Resource
	private RefundService refundService;
	
	@Resource
	private ReceiveNotifyService receiveNotifyService;
	
	@Resource
	private MobileMsgUtil mobileMsgUtil;
	
	/**
	 * 接收支付结果通知
	 * @param request
	 * @param response
	 */
	@RequestMapping("/payNotify_no.jhtml")
	public void payNotify(HttpServletRequest request,
			HttpServletResponse response){
		SignService signService = new PayResultSign();
		try{
			ASPUtil.sign(request, pay_sign_key, signService); //signService为支付结果
			String asp_order_id = ((PayResultSign) signService).getAsp_order_id();
			logger.info("接收平台支付结果通知！订单号："+asp_order_id);
			String eop_order_id = ((PayResultSign) signService).getEop_order_id();
			String eop_refund_url = ((PayResultSign) signService).getEop_refund_url();
			String eop_pay_number = ((PayResultSign) signService).getEop_pay_number();
			String send_notify_url = ((PayResultSign) signService).getSend_notify_url();
			ExternalLogsVo logs=new ExternalLogsVo();
			logs.setOrder_id(asp_order_id);
			if("SUCCESS".equals(signService.getSignResult())){
				if("SUCCESS".equals(((PayResultSign) signService).getPay_status())){
					logger.info("平台支付成功！订单号："+asp_order_id);
					Map<String,String> paramMap = new HashMap<String,String>();
					paramMap.put("eop_pay_number", eop_pay_number);
					paramMap.put("eop_refund_url", eop_refund_url);
					paramMap.put("send_notify_url", send_notify_url);
					paramMap.put("order_id", asp_order_id);
					paramMap.put("eop_order_id", eop_order_id);
					paramMap.put("order_status","11");
					orderService.updateOrderEopInfo(paramMap);
					
					receiveNotifyService.updateEopAndPayNotifyInfo(paramMap);
					
					receiveNotifyService.insertPayReturnNotify(paramMap);
					logs.setOrder_optlog("eop平台支付完成");
					logs.setOpter("xc_app");
					
				}else{
					logger.info("eop平台支付失败，订单号："+asp_order_id);
					Map<String,String> paramMap = new HashMap<String,String>();
					paramMap.put("eop_pay_number", eop_pay_number);
					paramMap.put("eop_refund_url", eop_refund_url);
					paramMap.put("order_id", asp_order_id);
					paramMap.put("eop_order_id", eop_order_id);
					orderService.updateOrderEopInfo(paramMap);
					
					paramMap.put("order_status", "00");
					receiveNotifyService.insertPayReturnNotify(paramMap);
					logs.setOrder_optlog("eop平台支付失败");
					logs.setOpter("xc_app");
				}
				orderService.insertOrderLogs(logs);
				write2Response(response, "{\"result_code\":\"SUCCESS\",\"result_desc\":\"成功\"}"); 
			}else{
				throw new RuntimeException("接收支付结果验签失败！");
			}
		}catch(Exception e){
			logger.error("反馈平台支付结果时系统异常！", e);
			write2Response(response, "{\"result_code\":\"FAIL\",\"result_desc\":\"失败\"}");
		}
		
	}
	
	/**
	 * 接收平台eop退票结果异步通知
	 * @param request
	 * @param response
	 */
	@RequestMapping("/refundNotify_no.jhtml")
	public void refundNotify(HttpServletRequest request,
			HttpServletResponse response){
		SignService signService = new RefundResultSign();
		try{
			ASPUtil.sign(request, pay_sign_key, signService); //signService为退票结果
			String asp_refund_number = ((RefundResultSign) signService).getAsp_refund_number();
			String eop_order_id = ((RefundResultSign) signService).getEop_order_id();
			logger.info("接收平台退款结果通知！订单号："+eop_order_id);
			String eop_refund_time = ((RefundResultSign) signService).getEop_refund_time();
			String eop_refund_seq = ((RefundResultSign) signService).getEop_refund_seq();
			String refund_money = ((RefundResultSign) signService).getRefund_money();
			Map<String,String> paramMap = new HashMap<String,String>();
			String order_id = orderService.queryOrderIdByEop(eop_order_id);
			ExternalLogsVo logs=new ExternalLogsVo();
			logs.setOrder_id(order_id);
			logs.setOpter("xc_app");
			if("SUCCESS".equals(signService.getSignResult())){
				if("SUCCESS".equals(((RefundResultSign) signService).getRefund_status())){
					logger.info("平台退款成功！退款流水号："+asp_refund_number);
					paramMap.put("asp_refund_number", asp_refund_number);
					NumberFormat objFormat =new DecimalFormat("#.00");
					paramMap.put("eop_refund_money", objFormat.format(Double.valueOf(refund_money)).toString());
					paramMap.put("eop_refund_time", eop_refund_time);
					paramMap.put("eop_refund_seq", eop_refund_seq);
					paramMap.put("eop_order_id", eop_order_id);
					paramMap.put("eop_refund_status","22");
					paramMap.put("refund_status",TrainConsts.REFUND_STATUS_SUCCESS);
					
					try{
						refundService.updateEopRefundStreamInfo(paramMap);
					}catch (Exception e) {
						logger.error("平台退款成功，改退款表-->>【退款完成】失败！");
					}
					logs.setOrder_optlog("平台退款成功！退款流水号："+asp_refund_number);
				}else{
					logger.info("平台退款失败，退款流水号："+asp_refund_number);
					paramMap.put("asp_refund_number", asp_refund_number);
//					paramMap.put("eop_refund_money", refund_money);
					paramMap.put("eop_refund_time", eop_refund_time);
					paramMap.put("eop_refund_seq", eop_refund_seq);
					paramMap.put("eop_order_id", eop_order_id);
					paramMap.put("eop_refund_status","11");
					paramMap.put("refund_status",TrainConsts.REFUND_STATUS_FAIL);
					
					try{
						refundService.updateEopRefundStreamInfo(paramMap);
					}catch (Exception e) {
						logger.error("平台退款失败，改退款表-->>【退款失败】失败！");
					}
					logs.setOrder_optlog("平台退款失败，退款流水号："+asp_refund_number);
				}
				orderService.insertOrderLogs(logs);
				write2Response(response, "{\"result_code\":\"SUCCESS\",\"result_desc\":\"成功\"}"); 
			}else{
				write2Response(response, "{\"result_code\":\"FAIL\",\"result_desc\":\"失败\"}");
				throw new RuntimeException("接收平台退款结果验签失败！");
			}
		}catch(Exception e){
			logger.error("反馈平台退款结果时系统异常！", e);
			write2Response(response, "{\"result_code\":\"FAIL\",\"result_desc\":\"失败\"}");
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
		String level = this.getParam(request, "level");//vip用户不为空
		
		
		logger.info("[接收出票结果通知接口]参数orderId=" + orderId 
				+ "，result=" + result + "，billNo=" + billNo
				+ "，buyMoney=" + buyMoney + "，seatTrains=" + seatTrains + "，status=" + status+",level="+level);
		
		Map<String, String> paramMap = new HashMap<String, String>(3);//主订单参数
		List<Map<String, String>> cpMapList = new ArrayList<Map<String, String>>();//车票订单参数
		
		if(StringUtils.isEmpty(result) || StringUtils.isEmpty(orderId)){
			//参数为空
			logger.info("exception：参数为空！");
			write2Response(response, "failed");
			
		}else if(TrainConsts.SUCCESS.equalsIgnoreCase(result)){//成功
			if(StringUtils.isEmpty(billNo) || StringUtils.isEmpty(buyMoney) 
					|| StringUtils.isEmpty(status) || StringUtils.isEmpty(seatTrains)){
				//参数为空
				logger.info("exception：明细参数为空！");
				write2Response(response, "failed");
				return;
			}else if(!"11".equals(status) && !"00".equals(status)){
				logger.info("exception：状态有误！status="+status);
				write2Response(response, "failed");
				return;
			}
			
			//查询订单信息
			OrderInfo orderInfo = orderService.queryOrderInfo(orderId);
			if(StringUtils.isEmpty(orderInfo.getOrder_status())){
				logger.info("exception：订单查询异常，订单状态为空！");
				write2Response(response, "failed");
				return;
			}
			Map<String, String> merchantSetting = commonService.queryMerchantInfo(orderInfo.getMerchant_id());
			if("11".equals(status)){//预订成功
				//预订成功通知重复通知
				if(!TrainConsts.BOOKING_TICKET.equals(orderInfo.getOrder_status())){
					logger.info("[接收出票结果通知接口本次预订成功通知为重复通知，orderId=" + orderId);
					write2Response(response, "success");
					return;
				}
				paramMap.put("order_id", orderId);
				paramMap.put("book_flow", merchantSetting.get("merchantSetting")); //购票流程 00:先支付后出票；11：先预定后支付
				paramMap.put("buy_money", buyMoney);
				paramMap.put("out_ticket_billno", billNo);
				paramMap.put("order_status", TrainConsts.BOOK_SUCCESS);//预订成功
				
				//明细数据处理
				this.detailDataPacking(seatTrains, cpMapList, response);

				int count = receiveNotifyService.updateOrderWithCpNotify(paramMap, cpMapList, null);
				if (count == 1){
					write2Response(response, "success");
//					if("1".equals(orderInfo.getSms_notify()) && "1".equals(orderInfo.getOrder_level())){
//						//vip发送预订成功短信
//						this.sendVipSuccMsn(orderId, billNo);
//					}
					ExternalLogsVo logs=new ExternalLogsVo();
					logs.setOrder_id(orderId);
					logs.setOrder_optlog("预定成功");
					logs.setOpter("xc_app");
					orderService.insertOrderLogs(logs);
				}else{
					logger.info("failed：修改订单" + orderId + "失败！");
					write2Response(response, "failed");
				}
				
			}else if("00".equals(status)){//出票成功
				
				if(!TrainConsts.BOOKING_TICKET.equals(orderInfo.getOrder_status())
						&& !TrainConsts.BOOK_SUCCESS.equals(orderInfo.getOrder_status())){
					logger.info("[接收出票结果通知接口]本次出票成功通知为重复通知，orderId=" + orderId);
					write2Response(response, "success");
					return;
				}
				paramMap.put("order_id", orderId);
				paramMap.put("buy_money", buyMoney);
				paramMap.put("out_ticket_time", "now");//保证出票时间值非空，数据库进行处理
				paramMap.put("out_ticket_billno", billNo);
				paramMap.put("order_status", TrainConsts.OUT_SUCCESS);//出票成功
				
				//明细数据处理
				this.detailDataPacking(seatTrains, cpMapList, response);
				
				int count = receiveNotifyService.updateOrderWithCpNotify(paramMap, cpMapList, null);
				if (count == 1 &&"1".equals(orderInfo.getSms_notify())){
					write2Response(response, "success");
					String channel = orderService.queryMsgChannel(orderId);
					if(!"88".equals(channel)){
						//发送出票成功短信
						this.sendCpSuccMsn(orderId, billNo,channel);
					}
					ExternalLogsVo logs=new ExternalLogsVo();
					logs.setOrder_id(orderId);
					logs.setOrder_optlog("出票成功");
					logs.setOpter("xc_app");
					orderService.insertOrderLogs(logs);
					
				}else{
					logger.info("failed：修改订单" + orderId + "失败！");
					write2Response(response, "failed");
				}
			}
			
		}else if(TrainConsts.FAILURE.equalsIgnoreCase(result)){//失败
			//阻止重复出票系统通知请求
			OrderInfo orderInfo = orderService.queryOrderInfo(orderId);
			if(!StringUtils.isEmpty(orderInfo.getOrder_status())
					&& TrainConsts.OUT_FAIL.equals(orderInfo.getOrder_status())){
				logger.info("[接收出票结果通知接口]本次出票失败通知为重复请求，orderId=" + orderId);
				write2Response(response, "success");
				return;
			}
//			//如果是先预定后支付 则 判断是否为支付成功订单
//			Map<String, String> merchantSetting = commonService.queryMerchantInfo(orderInfo.getMerchant_id());
//			if("11".equals(merchantSetting.get("book_flow"))){//针对先预定后支付的订单
//				if(StringUtils.isEmpty(orderInfo.getEop_pay_number())){//前台代扣支付流水号空
//					logger.info("[接收出票结果通知接口]本次出票失败不是支付成功订单不添加出票失败退款信息，orderId=" + orderId);
//					ExternalLogsVo logs=new ExternalLogsVo();
//					logs.setOrder_id(orderId);
//					logs.setOrder_optlog("[接收出票结果通知接口]本次出票失败不是支付成功订单不添加出票失败退款信息，orderId=" + orderId);
//					logs.setOpter("xc_app");
//					orderService.insertOrderLogs(logs);
//					write2Response(response, "success");
//					return;
//				}else if(!StringUtils.isEmpty(orderInfo.getEop_pay_number())){//支付流水号不为空	 
//					int count = orderService.queryHistoryByOrderId(orderId);//判断是否有支付成功日志（余额不足等原因会造成有支付流水号但没有支付成功）
//					if(count == 0){
//						ExternalLogsVo logs=new ExternalLogsVo();
//						logs.setOrder_id(orderId);
//						logs.setOrder_optlog("[接收出票结果通知接口]本次出票失败不是支付成功订单不添加出票失败退款信息，orderId=" + orderId);
//						logs.setOpter("xc_app");
//						orderService.insertOrderLogs(logs);
//						logger.info("[接收出票结果通知接口]本次出票失败不是支付成功订单不添加出票失败退款信息，orderId=" + orderId);
//						write2Response(response, "success");
//						return;
//					}
//				}
//			}
			paramMap.put("order_id", orderId);
			paramMap.put("buy_money", buyMoney);
			paramMap.put("out_ticket_billno", billNo);
			paramMap.put("order_status", TrainConsts.OUT_FAIL);//出票失败
			
			String errorinfo = this.getParam(request, "errorinfo");//错误信息
			Map<String, String> failRefundMap = new HashMap<String, String>();//出票失败退款map
			failRefundMap.put("order_id", orderId);
			failRefundMap.put("refund_type", TrainConsts.ORDER_RETURN_TYPE_2);//出票失败退款
			failRefundMap.put("eop_pay_number", orderInfo.getEop_pay_number());//出票失败退款
			failRefundMap.put("refund_amount", orderInfo.getPay_money_show());//退给用户的金额
			failRefundMap.put("refund_money", orderInfo.getPay_money());	  //退给商户的金额
			failRefundMap.put("fail_reason", errorinfo);
			int count = receiveNotifyService.updateOrderWithCpNotify(paramMap, null, failRefundMap);
			if (count == 1){
				write2Response(response, "success");
				//发送出票失败短信
//				this.sendCpFailMsn(orderId);

			}else{
				logger.info("failed：修改订单" + orderId + "失败！");
				write2Response(response, "failed");
			}
			
		}else{//异常
			logger.info("exception：订单" + orderId + "，接口返回未知状态码！");
			write2Response(response, "failed");
		}
	}
	/**
	 * 明细数据组合
	 */
	private void detailDataPacking(String seatTrains, List<Map<String, String>> cpMapList,
			HttpServletResponse response){
		//CP0120212|133|12|058号#CP0120213|133|12|059号
		String[] seatMsgs = seatTrains.split("#");
		Map<String, String> cpMap = null;
		for (String seatMsg : seatMsgs) {//CP0120212|133|12|058号
			String[] str = seatMsg.split("\\|");
			if(str == null){
				//参数为空
				logger.info("exception：参数拆分失败！");
				write2Response(response, "failed");
				return;
			}
			for (int i = 0; i < str.length; i++) {
				cpMap = new HashMap<String, String>();
				cpMap.put("cp_id", str[0]);
				cpMap.put("buy_money", str[1]);//成本价格
				cpMap.put("train_box", str[2]);//车厢
				cpMap.put("seat_no", str[3]);//座位号
				if(str.length == 5){
					cpMap.put("seat_type", str[4]);//座位号
				}
				cpMapList.add(cpMap);
			}
		}
	}
	


	//【步步高超市】尊贵的VIP用户，欢迎您使用19e提供的火车票订购业务，我们会竭诚为您服务！如果您在订购中出现问题，请拨打专线400-698-6666转5进行咨询。

	
	/**
	 * 发送VIP出票短信（预订成功则发短信）
	 */
	private void sendVipSuccMsn(String orderId, String billNo){
		//发送出票成功短信
		Map<String, String> contact = orderService.queryOrderContactInfo(orderId);
		List<Map<String, String>> conList = orderService.queryCpContactInfo(orderId);
		if(contact != null && conList != null && conList.size() > 0){
			StringBuffer content = new StringBuffer();
			
			//尊贵的VIP用户，我们非常荣幸的帮助您预定成功编号为E297492020的11月12日20:55（北京西-武昌）Z37次张睿的12车17号下铺、
			//陈迪12车18号上铺、严志有12车20号上铺席位的车票，请您携身份证到车站售票点或者自助机领取纸质车票，祝你旅途愉快！
			content.append("尊贵的VIP用户，我们非常荣幸的帮助您成功订购编号为")
				   .append(billNo)
				   .append("的")
				   .append(contact.get("travel_time")).append(" ").append(contact.get("from_time"))
				   .append("（").append(contact.get("from_station")).append("-").append(contact.get("arrive_station")).append("）")
				   .append(contact.get("train_no")).append("次");
			for(int i=0; i<conList.size(); i++){
				Map<String, String> conDetail = conList.get(i);
				if(i > 0){
					content.append("、");
				}
				content.append(conDetail.get("user_name"))
				       .append(conDetail.get("train_box")).append("车").append(conDetail.get("seat_no"));
			}
			content.append("席位的车票，请您携身份证到车站售票点或者自助机领取纸质车票，祝您旅途愉快！");
			
			/*
			 * 发送短信通知车票预订成功
			 */
			mobileMsgUtil.send(contact.get("link_phone"), content.toString(),null);

		}
	}
	
	/**
	 * 发送预订成功短信
	 */
	private void sendYdSuccMsn(String orderId, String billNo){
		//发送预订成功短信
		Map<String, String> contact = orderService.queryOrderContactInfo(orderId);
		List<Map<String, String>> conList = orderService.queryCpContactInfo(orderId);
		if(contact != null && conList != null && conList.size() > 0){
			//提醒您成功订购07月26日13:50（福州—福州南）D6237次2车14D号席位，张XX、李XX请持二代身份证乘车或换票乘车。
			StringBuffer content = new StringBuffer();
			if(!StringUtils.isEmpty(contact.get("link_name"))){
				content.append("尊敬的"+contact.get("link_name")+"先生/女士，");
			}
			content.append("我们非常荣幸的帮助您成功预订编号为")
				   .append(billNo)//12306订单号
				   .append("的")
				   .append(contact.get("travel_time")).append(" ").append(contact.get("from_time"))
				   .append("（").append(contact.get("from_station")).append("-").append(contact.get("arrive_station")).append("）")
				   .append(contact.get("train_no")).append("次");
			for(int i=0; i<conList.size(); i++){
				Map<String, String> conDetail = conList.get(i);
				if(i > 0){
					content.append("、");
				}
				content.append(conDetail.get("train_box")).append("车").append(conDetail.get("seat_no"));
			}
			content.append("席位。");
			content.append("请您等待最终出票短信通知。");	
			/*
			 * 发送短信通知车票预订成功
			 */
			mobileMsgUtil.send(contact.get("link_phone"), content.toString(),null);
		}
	}
	
	/**
	 * 发送出票成功短信
	 */
	private void sendCpSuccMsn(String orderId, String billNo,String channel){
		//发送出票成功短信
		Map<String, String> contact = orderService.queryOrderContactInfo(orderId);
		List<Map<String, String>> conList = orderService.queryCpContactInfo(orderId);
		if(contact != null && conList != null && conList.size() > 0){
			StringBuffer content = new StringBuffer();
			
			//尊敬的xxx先生/女士，我们非常荣幸的帮助您预定成功编号为E297492020的11月12日20:55（北京西-武昌）Z37次张睿的12车17号下铺、
			//陈迪12车18号上铺、严志有12车20号上铺席位的车票，请您携身份证到车站售票点或者自助机领取纸质车票，祝你旅途愉快！
			if(StringUtils.isEmpty(contact.get("link_name"))){
				content.append("尊敬的");
				content.append(contact.get("link_name")+"先生/女士，");
			}
			content.append("我们非常荣幸的帮助您成功订购编号为")
				   .append(billNo)
				   .append("的")
				   .append(contact.get("travel_time")).append(" ").append(contact.get("from_time"))
				   .append("（").append(contact.get("from_station")).append("-").append(contact.get("arrive_station")).append("）")
				   .append(contact.get("train_no")).append("次");
			for(int i=0; i<conList.size(); i++){
				Map<String, String> conDetail = conList.get(i);
				if(i > 0){
					content.append("、");
				}
				content.append(conDetail.get("user_name"))
				       .append(conDetail.get("train_box")).append("车").append(conDetail.get("seat_no"));
			}
			content.append("席位的车票，请您携身份证到车站售票点或者自助机领取纸质车票，祝您旅途愉快！");
			
			/*
			 * 发送短信通知车票预订成功
			 */
			mobileMsgUtil.send(contact.get("link_phone"), content.toString(),channel);
		}
	}
	
	
	/**
	 * 发送出票失败短信
	 */
	private void sendCpFailMsn(String orderId){
		//发送出票失败短信
		Map<String, String> contact = orderService.queryOrderContactInfo(orderId);
		//尊敬的xxx先生/女士，您订购的07月26日13:50（福州—福州南）T101次的火车票出票失败，为您带来不便请谅解，谢谢！
		StringBuffer content = new StringBuffer();
		if(contact != null){
			if(StringUtils.isEmpty(contact.get("link_name"))){
				content.append("尊敬的");
				content.append(contact.get("link_name")+"先生/女士，");
			}
			content.append("您订购的")
				   .append(contact.get("travel_time")).append(" ").append(contact.get("from_time"))
				   .append("（").append(contact.get("from_station")).append("-").append(contact.get("arrive_station")).append("）")
				   .append(contact.get("train_no")).append("次")
				   .append("的火车票出票失败，请及时联系");
			content.append("为您带来不便请谅解，谢谢！");
			/*
			 * 发送短信通知出票失败
			 */
			mobileMsgUtil.send(contact.get("link_phone"), content.toString(),null);
		}
	}

	/**
	 * 接收退票结果通知
	 * @param request
	 * @param response
	 */
	@RequestMapping("/refundSendNotify_no.jhtml")
	public void refundSendNotify(HttpServletRequest request,
			HttpServletResponse response){
		String result = this.getParam(request, "result");//成功
		String orderId = this.getParam(request, "orderid");
		String cpid = this.getParam(request, "cpid");
		String alterdiffmoney = this.getParam(request, "alterdiffmoney");
		String refundmoney = this.getParam(request, "refundmoney");
		String refund12306money = this.getParam(request, "refund12306money");
		String refund12306seq = this.getParam(request, "refund12306seq");
		String status = this.getParam(request, "status");//0、改签通知 1、退票通知
		String our_remark = this.getParam(request, "our_remark");//备注
		String refuse_reason = this.getParam(request, "refuse_reason");//拒绝退票原因
		
		logger.info("【接收退票系统通知】参数orderId=" + orderId + "，cpid=" + cpid + 
				"，alterdiffmoney=" + alterdiffmoney + "，refund12306money=" + refund12306money + 
				"，refund12306seq=" + refund12306seq + "，status=" + status + "，result=" + result + 
				"，our_remark=" + our_remark + "，refuse_reason=" + refuse_reason);
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,String> paramMap = new HashMap<String,String>();
		map.put("order_id", orderId);
		map.put("cp_id", cpid);
		map.put("our_remark", refuse_reason);//出票方备注
		map.put("refuse_reason", refuse_reason);
		paramMap.put("order_id", orderId);
		paramMap.put("cp_id", cpid);
		try{
			//00、等待退票 01、02正在改签（01：重新机器改签;02：开始机器改签;）03：人工改签;04、05、06正在退票（04：等待机器退票；05：重新机器退票；06：开始机器退票；）07：人工退票；
			//11、同意退票 22、拒绝退票 33、退票完成 44、退票失败 55、审核退款；99、搁置订单
			if("0".equals(status) && TrainConsts.SUCCESS.equalsIgnoreCase(result)){
				map.put("alter_money", alterdiffmoney);
				orderService.updateCPAlterInfo(map);
				logger.info("更改xc_orderinfo_cp表的alter_money="+alterdiffmoney);
				write2Response(response, "success");
			}else if("1".equals(status) && TrainConsts.SUCCESS.equalsIgnoreCase(result)){
				map.put("alter_tickets_money", Double.valueOf(alterdiffmoney));//改签差价
				map.put("actual_refund_money", Double.valueOf(refund12306money));//12306实际退款金额
				map.put("refund_12306_seq", refund12306seq); //12306退款流水单号
				map.put("refund_status", "11");
				if(Double.valueOf(refundmoney) < 0){
					map.put("refund_money", Double.valueOf("0"));
				}else{
					map.put("refund_money", Double.valueOf(refundmoney));
				}
				map.put("refund_time", "NOW()");
				map.put("refund_type", "1");
//				orderService.updateRefundInfo(map);
				orderService.updateRefundAgree(map, paramMap);//同意退款
				paramMap.put("notify_status","11");
				refundService.updateRefundNotifyRestart(paramMap);
				logger.info("更改xc_orderinfo_refundstream表的refund_status=11同意退款");
				write2Response(response, "success");
			}else if("1".equals(status) && TrainConsts.FAILURE.equalsIgnoreCase(result)){
				map.put("refund_status", "22");//22、拒绝退款
				map.put("refund_type", "1");
				orderService.updateRefundRefuse(map, paramMap);//拒绝退款
//				orderService.updateRefundInfo(map);
				logger.info("更改xc_orderinfo_refundstream表的refund_status=22拒绝退款");
				paramMap.put("notify_status","11");
				refundService.updateRefundNotifyRestart(paramMap);
				write2Response(response, "success");
			}else if("2".equals(status)){
				map.put("refund_status", "99");//99、搁置订单
				map.put("refund_type", "1");
				orderService.updateRefundInfo(map);
				logger.info("更改xc_orderinfo_refundstream表的refund_status=99搁置订单");
				write2Response(response, "success");
			}
		}catch(Exception e){
			logger.error("exception",e);
			write2Response(response, "failed");
		}
	}
	
}
