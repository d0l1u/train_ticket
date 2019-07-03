package com.l9e.transaction.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hispeed.encryption.KeyedDigestMD5;
import com.l9e.common.BaseController;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.ReceiveNotifyService;
import com.l9e.transaction.service.UserInfoService;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.MobileMsgUtil;

@Controller
@RequestMapping("/receiveNotify")
public class ReceiveNotifyController extends BaseController{
	
	private static final Logger logger = Logger.getLogger(ReceiveNotifyController.class);
	
	@Resource
	private OrderService orderService;
	
	@Resource
	private ReceiveNotifyService receiveNotifyService;
	
	@Resource
	private MobileMsgUtil mobileMsgUtil;
	
	@Resource
	private UserInfoService userInfoService;
	
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
		String passengers = this.getParam(request, "passengers");//乘客审核信息
		//CP1403021140261036|西红柿|110101198406079315|0#CP1403021140261038|海龟派|110101198406079235|1#CP1403021140261040|想不通|110101198810014951|0
		
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
			
			if("11".equals(status)){//预订成功
				//预订成功通知重复通知
				if(!TrainConsts.BOOKING_TICKET.equals(orderInfo.getOrder_status())){
					logger.info("[接收出票结果通知接口]本次预订成功通知为重复通知，orderId=" + orderId);
					write2Response(response, "success");
					return;
				}
				paramMap.put("order_id", orderId);
				paramMap.put("buy_money", buyMoney);
				paramMap.put("out_ticket_billno", billNo);
				paramMap.put("order_status", TrainConsts.BOOK_SUCCESS);//预订成功
				
				//明细数据处理
				this.detailDataPacking(seatTrains, cpMapList, response);

				int count = receiveNotifyService.updateOrderWithCpNotify(paramMap, cpMapList, null);
				if (count == 1){
					write2Response(response, "success");
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
				paramMap.put("out_ticket_billno", billNo);
				paramMap.put("order_status", TrainConsts.OUT_SUCCESS);//出票成功
				
				//明细数据处理
				this.detailDataPacking(seatTrains, cpMapList, response);

				int count = receiveNotifyService.updateOrderWithCpNotify(paramMap, cpMapList, null);
				if (count == 1){
					//发送出票成功短信
					this.sendCpSuccMsn(orderId, billNo);
					write2Response(response, "success");
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
			
			paramMap.put("order_id", orderId);
			paramMap.put("buy_money", buyMoney);
			paramMap.put("out_ticket_billno", billNo);
			paramMap.put("order_status", TrainConsts.OUT_FAIL);//出票失败
			
			String errorinfo = this.getParam(request, "errorinfo");//错误信息
			Map<String, String> failRefundMap = new HashMap<String, String>();//出票失败退款map
			failRefundMap.put("order_id", orderId);
			failRefundMap.put("refund_seq", CreateIDUtil.createID("TK"));//ASP退款请求流水号
			failRefundMap.put("refund_type", TrainConsts.REFUND_TYPE_3);//出票失败退款
			failRefundMap.put("refund_money", orderInfo.getPay_money());
			failRefundMap.put("user_remark", errorinfo);
			failRefundMap.put("refund_status", TrainConsts.REFUND_STREAM_PRE_REFUND);//准备退款
			int count = receiveNotifyService.updateOrderWithCpNotify(paramMap, null, failRefundMap);
			if (count == 1){
				write2Response(response, "success");
			}else{
				logger.info("failed：修改订单" + orderId + "失败！");
				write2Response(response, "failed");
			}
			
		}else{//异常
			logger.info("exception：订单" + orderId + "，接口返回未知状态码！");
			write2Response(response, "failed");
		}
		
		/**
		 * 开始添加用户身份证信息
		 */
		if(!StringUtils.isEmpty(passengers)){
			logger.info("接收到的身份证信息为:"+passengers);
			userInfoService.ProcessingUserCheckData(passengers);
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
			if(str == null ){
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
				cpMapList.add(cpMap);
			}
		}
	}
	
	/**
	 * 发送出票成功短信
	 */
	private void sendCpSuccMsn(String orderId, String billNo){
		//发送出票成功短信
		Map<String, String> contact = orderService.queryOrderContactInfo(orderId);
		List<Map<String, String>> conList = orderService.queryCpContactInfo(orderId);
		if(contact != null && conList != null && conList.size() > 0){
			StringBuffer content = new StringBuffer();
			
			content.append("温馨提示：我们非常荣幸的帮助您成功订购编号为：")
				   .append(billNo)
				   .append("的")
				   .append(contact.get("from_time"))
				   .append("（").append(contact.get("from_city")).append("-").append(contact.get("to_city")).append("）")
				   .append(contact.get("train_no")).append("次，");
			for(int i=0; i<conList.size(); i++){
				Map<String, String> conDetail = conList.get(i);
				if(i > 0){
					content.append("、");
				}
				content.append(conDetail.get("user_name")).append(" ")
				       .append(conDetail.get("train_box")).append("车").append(conDetail.get("seat_no"));
			}
			content.append("席位的车票，请您携身份证到车站售票点或者自助机领取纸质车票，祝您旅途愉快！");
			
			/*
			 * 发送短信通知车票预订成功
			 */
			mobileMsgUtil.send(contact.get("link_phone"), content.toString());
		}
	}
	
	/**
	 * 发送出票失败短信
	 */
	private void sendCpFailMsn(String orderId){
		//发送出票失败短信
		Map<String, String> contact = orderService.queryOrderContactInfo(orderId);
		StringBuffer content = new StringBuffer();
		if(contact != null){
			content.append("很抱歉，您");
			content.append("订购的")
				   .append(contact.get("from_time"))
				   .append("（").append(contact.get("from_city")).append("-").append(contact.get("to_city")).append("）")
				   .append(contact.get("train_no")).append("次")
				   .append("的火车票出票失败，我们会尽快将票款退还到您的账户，请您耐心等待！");
			/*
			 * 发送短信通知出票失败
			 */
			mobileMsgUtil.send(contact.get("link_phone"), content.toString());
		}
	}
	
	/**
	 * @param request
	 * @param response
	 */
	public  String  getAllRequestParams(HttpServletRequest request,
			HttpServletResponse response){
		StringBuffer buffer=new StringBuffer();
		Map map=request.getParameterMap();
	    Set keSet=map.entrySet();  
	    for(Iterator itr=keSet.iterator();itr.hasNext();){
	        Map.Entry me=(Map.Entry)itr.next();
	        Object ok=me.getKey();
	        Object ov=me.getValue();
	        String[] value=new String[1];
	        if(ov instanceof String[]){
	            value=(String[])ov;  
	        }else{  
	            value[0]=ov.toString();  
	        }  
	  
	        for(int k=0;k<value.length;k++){  
	            logger.info(ok+"="+value[k]);
	            buffer.append(ok+"="+value[k]+"&");
	        } 
	        
	      }
	    
	    return buffer.toString();
	}

	/**
	 * 接收退票结果通知
	 * @param request
	 * @param response
	 */
	@RequestMapping("/refundNotify_no.jhtml")
	public void refundNotify(HttpServletRequest request,
			HttpServletResponse response){
		logger.info("订单号："+this.getParam(request, "oriPayOrderId")+"调用异步退票结果反馈");
		logger.info("request参数："+this.getAllRequestParams(request, response));
		String version = this.getParam(request, "version");
		String mxresq = this.getParam(request, "mxResq");
		String status = this.getParam(request, "status");
		String code = this.getParam(request, "retCode");
		String verify = this.getParam(request, "verifyString");
		String orderid = this.getParam(request, "oriPayOrderId");
		String sysseq = this.getParam(request, "refOrderId");
		String sutime = this.getParam(request, "finishTime");
		String amount = this.getParam(request, "amount");
		
		StringBuffer buffer = new StringBuffer();
	       
	    buffer.append("version=").append(version);
	    buffer.append("&mxResq=").append(mxresq);
        buffer.append("&status=").append(status);
        buffer.append("&retCode=").append(code);
	    buffer.append("&oriPayOrderId=").append(orderid);
	    buffer.append("&finishTime=").append(sutime);
	    buffer.append("&amount=").append(amount);
	    buffer.append("&refOrderId=").append(sysseq);// 退款流水
	    buffer.append("&fee=").append("0.00");
	    
	    String verifyString = KeyedDigestMD5.getKeyedDigestUTF8(buffer.toString(), merchant_key);
		logger.info("加密串："+buffer.toString());
		
		String now_time = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3);
		//添加日志
		Map<String,String> logMap = new HashMap<String,String>();
		logMap.put("order_id", orderid);
		logMap.put("opter", "ccb");
		logMap.put("order_time", now_time);
		// -- 验证签名
		if(!verifyString.equals(verify)){
			StringBuffer err_log = new StringBuffer();
			err_log.append("【退款流水接口】对退款返回数据验签失败，orderId=").append(orderid)
					.append("本地加密：").append(verifyString).append("；19pay加密：").append(verify);
			logger.info(err_log.toString());
			logMap.put("order_optlog", err_log.toString());
			
			//return;
		}
		
		if("0".equals(status)){
			logger.info("【退款流水接口】退款成功，orderId="+ orderid);
			 /*
		     * 申请成功更新退款状态信息
		     */
			Map<String, String> map = new HashMap<String, String>();
			//根据退款流水号修改退款状态
			map.put("refund_seq", mxresq);//ASP退款请求流水号
			map.put("plat_refund_seq", sysseq);
		    map.put("refund_status", TrainConsts.REFUND_STREAM_REFUND_FINISH);//退款完成
		    map.put("old_refund_status", TrainConsts.REFUND_STREAM_EOP_REFUNDING);//开始退款
		    orderService.updateOrderStreamResultStatus(map);
		    logger.info("【退款流水接口】退款状态修改成功，orderId="+ orderid);
		    logMap.put("order_optlog", "【退款流水接口】退款成功,退款状态修改成功");
		    write2Response(response,"Y");
		}else if("1".equals(status)){
			logger.info("【退款流水接口】接口返回退款状态为退款失败，orderId="+ orderid);
			logMap.put("order_optlog", "【退款流水接口】接口返回退款状态为退款失败");
			write2Response(response,"N");
		}else{
			logger.info("【退款流水接口】接口返回退款状态未知，orderId="+ orderid+"status="+status);
			logMap.put("order_optlog", "【退款流水接口】接口返回退款状态未知");
			write2Response(response,"N");
		}
		
		orderService.addOrderinfoHistory(logMap);
	}
}
