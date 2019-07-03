package com.l9e.transaction.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

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
import com.l9e.transaction.service.JoinUsService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.ReceiveNotifyService;
import com.l9e.transaction.service.UserInfoService;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.ProductVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.MobileMsgUtil;

@Controller
@RequestMapping("/receiveNotify")
public class ReceiveNotifyController extends BaseController{
	
	private static final Logger logger = Logger.getLogger(ReceiveNotifyController.class);
	
	@Resource
	private OrderService orderService;
	
	@Resource
	private JoinUsService joinUsService;
	
	@Resource
	private ReceiveNotifyService receiveNotifyService;
	
	@Resource
	private UserInfoService userInfoService;
	
	@Resource
	private MobileMsgUtil mobileMsgUtil;
	
	public static final Object LOCK = new Object();
	
	
	
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
		String is_pay = this.getParam(request, "is_pay");//乘客审核信息
		//CP1403021140261036|西红柿|110101198406079315|0#CP1403021140261038|海龟派|110101198406079235|1#CP1403021140261040|想不通|110101198810014951|0
		
		
		logger.info("[接收出票结果通知接口]参数orderId=" + orderId 
				+ "，result=" + result + "，billNo=" + billNo
				+ "，buyMoney=" + buyMoney + "，seatTrains=" + seatTrains + "，status=" + status+",level="+level);
		
		logger.info("order_id="+orderId+"&passengers="+passengers);
		
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
			
			
			if("00".equals(status)){//出票成功
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
				this.detailDataPacking(seatTrains, billNo, cpMapList, response);

				int count = receiveNotifyService.updateOrderWithCpNotify(paramMap, cpMapList, null);
				if (count == 1){
					write2Response(response, "success");
					
					if(StringUtils.isEmpty(level) || "0".equals(level)){
						//发送出票成功短信
						this.sendCpSuccMsn(orderId, billNo);
					}
					
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
			if("00".equals(is_pay)){
				failRefundMap.put("order_id", orderId);
				failRefundMap.put("refund_seq", CreateIDUtil.createID("TK"));//ASP退款请求流水号
				failRefundMap.put("refund_type", TrainConsts.REFUND_TYPE_3);//出票失败退款
				failRefundMap.put("refund_money", orderInfo.getPay_money());
				failRefundMap.put("user_remark", errorinfo);
				failRefundMap.put("refund_status", TrainConsts.REFUND_STREAM_WAIT_REFUND);//准备退款
			}else{
				failRefundMap = null;
			}
			int count = receiveNotifyService.updateOrderWithCpNotify(paramMap, null, failRefundMap);
			if (count == 1){
				write2Response(response, "success");
				if("00".equals(is_pay)){
					//发送出票失败短信
					//this.sendCpFailMsn(orderId);
				}
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
			userInfoService.ProcessingUserCheckData(passengers);
		}
	}
	
	
	/**
	 * 明细数据组合
	 */
	private void detailDataPacking(String seatTrains, String billNo, List<Map<String, String>> cpMapList,
			HttpServletResponse response){
		//CP0120212|133|12|058号#CP0120213|133|12|059号
		String[] seatMsgs = seatTrains.split("#");
		Map<String, String> cpMap = null;
		for (String seatMsg : seatMsgs) {//CP0120212|133|12|058号
			String[] str = seatMsg.split("\\|");
			if(str == null || str.length != 4){
				//参数为空
				logger.info("exception：参数拆分失败！");
				write2Response(response, "failed");
				return;
			}
			for (int i = 0; i < str.length; i++) {
				cpMap = new HashMap<String, String>(4);
				cpMap.put("cp_id", str[0]);
				cpMap.put("buy_money", str[1]);//成本价格
				cpMap.put("train_box", str[2]);//车厢
				cpMap.put("seat_no", str[3]);//座位号
				cpMap.put("out_ticket_billno", billNo);//12306单号
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
			//【19旅行】尊敬的用户，您好！我们非常荣幸帮助您成功订购编号为：EXXXXXXX的1月16日09：30（武威-武威南）7520次，王东霓XX车XX号席位的车票，请您携身份证到车站售票点或者自助机领取纸质车票，祝您旅途愉快！
			StringBuffer content = new StringBuffer();
			
			content.append("【19旅行】尊敬的用户，您好！我们非常荣幸帮助您成功订购编号为：");
			content.append(contact.get("out_ticket_billno")+"的"+contact.get("travel_time")+contact.get("from_time"));
			content.append("（"+contact.get("from_station")+"-"+contact.get("arrive_station")+"）"+contact.get("train_no")+"次，");
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
			mobileMsgUtil.send(contact.get("link_phone"), content.toString(), "22");
		}
	}
	
	


}
