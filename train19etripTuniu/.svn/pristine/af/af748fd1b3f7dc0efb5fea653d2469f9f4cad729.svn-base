package com.l9e.transaction.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.TuniuCommonService;
import com.l9e.transaction.service.TuniuNoticeService;
import com.l9e.transaction.service.TuniuOrderService;
import com.l9e.transaction.service.TuniuRefundService;
import com.l9e.transaction.vo.Notice;
import com.l9e.transaction.vo.TuniuOrder;
import com.l9e.transaction.vo.TuniuPassenger;
import com.l9e.transaction.vo.TuniuRefund;
import com.l9e.util.DateUtil;

/**
 * 出票系统回调接口
 * 
 * @author licheng
 * 
 */
@Controller
@RequestMapping("/outTicket")
public class OutTicketController extends BaseController {

	private static final Logger logger = Logger.getLogger(OutTicketController.class);
	
	@Resource
	private TuniuOrderService tuniuOrderService;
	
	@Resource
	private TuniuNoticeService tuniuNoticeService;
	
	@Resource
	private TuniuRefundService tuniuRefundService;

	/**
	 * 接收出票系统回调
	 * @param request
	 * @param response
	 */
	@RequestMapping("/orderCallback")
	public void orderCallBack(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("----orderCallBack----"+getFullURL(request));
		try {
			String status = getParam(request, "status");//00：出票 11预定
			String result = getParam(request, "result");
			String orderId = getParam(request, "orderid");
			logger.info("接受出票系统结果，orderId："+orderId+",result："+result+",status："+status);
			if(isEmpty(result) || isEmpty(orderId)) {
				logger.info("出票系统返回结果参数或状态异常，接受失败!");
				write2Response(response, "failed");
			} else if("failure".equalsIgnoreCase(result)) {//失败
				recieveFailure(request, response);
			} else if("success".equalsIgnoreCase(result)) {//成功
				if("00".equals(status)) {
					recieveOut(request, response);
				} else if("11".equals(status)) {
					recieveBook(request, response);
				} else {
					logger.info("订单:" + orderId + "，接口返回未知状态码！");
					write2Response(response, "failed");
				}
			} else {//异常
				logger.info("订单:" + orderId + "，接口返回未知参数值！");
				write2Response(response, "failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("接口系统异常！");
			write2Response(response, "failed");
		}
	}
	
	/**
	 * 接收退票系统回调
	 * @param request
	 * @param response
	 */
	@RequestMapping("/refundCallback")
	public void refundCallBack(HttpServletRequest request,
			HttpServletResponse response) {
		String result = getParam(request, "result");//成功
		String orderId = getParam(request, "orderid");
		String cpId = getParam(request, "cpid");
		String alterDiffMoney = getParam(request, "alterdiffmoney");
		String refundMoney = getParam(request, "refundmoney");
		String refund12306Money = getParam(request, "refund12306money");
		String refund12306Seq = getParam(request, "refund12306seq");
		String status = getParam(request, "status");//0、改签通知 1、退票通知
		String ourRemark = getParam(request, "our_remark");//备注
		String refuseReason = getParam(request, "refuse_reason");//拒绝退票原因

		logger.info("【接收退票系统通知】参数orderId=" + orderId + "，cpid=" + cpId + 
				"，alterdiffmoney=" + alterDiffMoney + "，refund12306money=" + refund12306Money +
				"，refundmoney=" + refundMoney +
				"，refund12306seq=" + refund12306Seq + "，status=" + status + "，result=" + result + 
				"，our_remark=" + ourRemark + "，refuse_reason=" + refuseReason);
		
		try {
			TuniuRefund refund = tuniuRefundService.getRefundByOrderIdAndCpId(orderId, cpId);
			Notice notice = tuniuNoticeService.getRefundNoticeByOrderIdAndCpId(orderId, cpId);
			
			Double alterDiff = 0.0;
			if(!StringUtils.isEmpty(alterDiffMoney)) {
				alterDiff = Double.valueOf(alterDiffMoney);
			}
			Double refund12306 = 0.0;
			if(!StringUtils.isEmpty(refund12306Money)) {
				refund12306 = Double.valueOf(refund12306Money);
			}
			Double refundM = 0.0;
			if(Double.valueOf(refundMoney) < 0){
				refundM = 0.0;
			}else{
				refundM = Double.valueOf(refundMoney);
			}
			
			refund.setRefund12306Seq(refund12306Seq);
			refund.setOurRemark(ourRemark);
			refund.setFailReason(refuseReason);
			refund.setRefundMoney(refundM);
			refund.setActualRefundMoney(refund12306);
			refund.setDetailAlter(alterDiff);
			refund.setDetailRefund(refund12306);
			
			if("0".equals(status)) {//改签
				// TODO:
			} else if("1".equals(status)) {//退款
				//11：退票完成   22：拒绝退票 
				if("success".equalsIgnoreCase(result)) {//退款成功
					refund.setRefundStatus(TuniuRefundService.STATUS_REFUND_SUCCESS);
					refund.setVerifyTime(new Date());
				} else if("failure".equalsIgnoreCase(result)) {//退款失败
					refund.setRefundStatus(TuniuRefundService.STATUS_REFUND_FAILURE);
				}
				
				tuniuRefundService.updateRefund(refund);
				/*更新通知*/
				notice.setNotifyStatus(TuniuCommonService.NOTIFY_PREPARED);
				tuniuNoticeService.updateRefundNotice(notice);
			}
			write2Response(response, "success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("接口系统异常！");
			write2Response(response, "failed");
		}
	}
	
	/**
	 * 预订结果
	 * @param request
	 * @param response
	 */
	private void recieveBook(HttpServletRequest request,
			HttpServletResponse response) {
		String orderId = getParam(request, "orderid");
		TuniuOrder order = tuniuOrderService.getOrderById(orderId, false);
		Notice notice = tuniuNoticeService.getBookNoticeByOrderId(orderId);
		if(order == null || isEmpty(order.getOrderStatus())) {
			logger.info("[占座成功]途牛订单状态异常，接受出票系统回调结果失败!orderId : " + orderId);
			write2Response(response, "failed");
			return;
		}
		
		/*剔除重复通知*/
		if(TuniuOrderService.STATUS_BOOK_SUCCESS.equals(order.getOrderStatus())) {
			logger.info("[占座成功]]本次预订成功通知为重复通知,orderId=" + orderId);
			write2Response(response, "success");
			return;
		} else if(TuniuOrderService.STATUS_TIME_OUT.equals(order.getOrderStatus())) {
			logger.info("[占座成功]订单已经超时,不再更新订单内容以及结果,orderId=" + orderId);
			write2Response(response, "success");
			return;
		} else if(TuniuOrderService.STATUS_CANCEL_SUCCESS.equals(order.getOrderStatus())) {
			logger.info("[占座成功]订单已经取消,不再更新订单内容以及结果,orderId=" + orderId);
			write2Response(response, "success");
			return;
		}
		
		/*订单参数*/
		String billNo = getParam(request, "billno");
		String buyMoney = getParam(request, "buymoney");
		String seatTrains = getParam(request, "seattrains");
		String fromTime= getParam(request, "from_time");
		String toTime= getParam(request, "to_time");
		String outTicketTime= getParam(request, "out_ticket_time");
		String examines = getParam(request, "passengers");
		String errorInfo = getParam(request, "errorinfo");
		String isPay = getParam(request, "is_pay");
		String clearTime=getParam(request,"pay_limit_time");//清位时间
		logger.info("[接收占座结果]参数orderId=" + orderId 
				+ ",billno=" + billNo + ",buyMoney=" + buyMoney
				+ ",seatTrains=" + seatTrains + ",fromTime=" + fromTime
				+ ",toTime=" + toTime + ",outTicketTime=" + outTicketTime
				+ ",examines=" + examines + ",errorInfo=" + errorInfo+",pay_limit_time="+clearTime);
		if(isEmpty(billNo) || isEmpty(buyMoney) || isEmpty(seatTrains)){
			logger.info("[接收占座结果]预订回调参数异常!orderId: " + orderId);
			write2Response(response, "failed");
			return;
		}
		
		Double money = null;
		try {
			money = Double.valueOf(buyMoney);
		} catch (NumberFormatException e) {
			logger.info("[接收占座结果]预订回调金额异常!orderId : " + orderId);
			write2Response(response, "failed");
			return;
		}
		if(StringUtils.isEmpty(fromTime)) {
			fromTime = null;
		}
		if(StringUtils.isEmpty(toTime)) {
			toTime = null;
		}
		order.setBuyMoney(money);
		order.setOutTicketBillno(billNo);
		order.setOutTicketTime(DateUtil.stringToDate(outTicketTime, DateUtil.DATE_FMT3));
		order.setFromTime(fromTime == null ? null : DateUtil.stringToDate(fromTime, DateUtil.DATE_FMT3));
		order.setToTime(toTime == null ? null : DateUtil.stringToDate(toTime, DateUtil.DATE_FMT3));
		order.setOutFailReason(errorInfo);
		order.setPayLimitTime(clearTime==null?null: DateUtil.stringToDate(clearTime, DateUtil.DATE_FMT3));
		if(isPay != null && isPay.equals("00")) {
			order.setOrderStatus(TuniuOrderService.STATUS_WAIT_PAY);
		} else {
			order.setOrderStatus(TuniuOrderService.STATUS_BOOK_SUCCESS);
		}
		
		/*乘客及车票参数*/
		List<TuniuPassenger> passengers = seatTrains2Passenger(seatTrains);
		if(passengers == null || passengers.size() == 0){
			logger.info("[接收占座结果]乘客车票信息异常!orderId : " + orderId);
			write2Response(response, "failed");
			return;
		}
		examinePassenger(examines, passengers);
		order.setPassengers(passengers);
		
		/*更新订单信息以及乘客车票信息*/
		tuniuOrderService.updateOrder(order, true);
		
		/*通知状态修改*/
		notice.setNotifyStatus(TuniuCommonService.NOTIFY_PREPARED);
		tuniuNoticeService.updateBookNotice(notice);//修改notifyStatus 准备通知
		write2Response(response, "success");
		
	}
	
	/**
	 * 出票结果
	 * @param request
	 * @param response
	 */
	private void recieveOut(HttpServletRequest request,
			HttpServletResponse response) {
		String orderId = getParam(request, "orderid");
		TuniuOrder order = tuniuOrderService.getOrderById(orderId, false);
		Notice notice = tuniuNoticeService.getOutNoticeByOrderId(orderId);
		if(order == null || isEmpty(order.getOrderStatus())) {
			logger.info("途牛订单状态异常，接受出票系统回调结果失败!orderId : " + orderId);
			write2Response(response, "failed");
			return;
		}
		
		/*剔除重复通知*/
		if(TuniuOrderService.STATUS_OUT_SUCCESS.equals(order.getOrderStatus())) {
			logger.info("[接收出票结果通知接口]本次出票成功通知为重复通知,orderId=" + orderId);
			write2Response(response, "success");
			return;
		} else if(TuniuOrderService.STATUS_TIME_OUT.equals(order.getOrderStatus())) {
			logger.info("订单已经超时,不再更新订单内容以及结果,orderId=" + orderId);
			write2Response(response, "success");
			return;
		} else if(TuniuOrderService.STATUS_CANCEL_SUCCESS.equals(order.getOrderStatus())) {
			logger.info("订单已经取消,不再更新订单内容以及结果,orderId=" + orderId);
			write2Response(response, "success");
			return;
		}
		
		/*订单参数*/
		String billNo = getParam(request, "billno");
		String buyMoney = getParam(request, "buymoney");
		String seatTrains = getParam(request, "seattrains");
		try {
			seatTrains = URLDecoder.decode(seatTrains, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String fromTime= getParam(request, "from_time");
		String toTime= getParam(request, "to_time");
		String examines = getParam(request, "passengers");
		try {
			examines = URLDecoder.decode(examines, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String outTicketTime= getParam(request, "out_ticket_time");
		logger.info("[接收出票结果通知接口]参数orderId=" + orderId 
				+ ",billno=" + billNo + ",buyMoney=" + buyMoney
				+ ",seatTrains=" + seatTrains + ",fromTime=" + fromTime
				+ ",toTime=" + toTime + ",outTicketTime=" + outTicketTime
				+ ",examines=" + examines);
		
		if(isEmpty(billNo) || isEmpty(buyMoney) || isEmpty(seatTrains)){
			logger.info("预订回调参数异常!orderId: " + orderId);
			write2Response(response, "failed");
			return;
		}
		Double money = null;
		try {
			money = Double.valueOf(buyMoney);
		} catch (NumberFormatException e) {
			logger.info("预订回调金额异常!orderId : " + orderId);
			write2Response(response, "failed");
			return;
		}
		if(StringUtils.isEmpty(fromTime)) {
			fromTime = null;
		}
		if(StringUtils.isEmpty(toTime)) {
			toTime = null;
		}
		
		order.setBuyMoney(money);
		order.setOutTicketBillno(billNo);
		order.setOutTicketTime(DateUtil.stringToDate(outTicketTime, DateUtil.DATE_FMT3));
		order.setFromTime(fromTime == null ? null : DateUtil.stringToDate(fromTime, DateUtil.DATE_FMT3));
		order.setToTime(toTime == null ? null : DateUtil.stringToDate(toTime, DateUtil.DATE_FMT3));
		if(order.getOrderStatus().equals(TuniuOrderService.STATUS_CANCEL_ING)){
			order.setOrderStatus(TuniuOrderService.STATUS_CANCEL_SUCCESS);
		}else{
			order.setOrderStatus(TuniuOrderService.STATUS_OUT_SUCCESS);
		}
		/*乘客及车票参数*/
		List<TuniuPassenger> passengers = seatTrains2Passenger(seatTrains);
		if(passengers == null || passengers.size() == 0){
			logger.info("乘客车票信息异常!orderId : " + orderId);
			write2Response(response, "failed");
			return;
		}
		examinePassenger(examines, passengers);
		order.setPassengers(passengers);
		
		/*更新订单信息以及乘客车票信息*/
		tuniuOrderService.updateOrder(order, true);
		
		/*通知状态修改*/
		notice.setNotifyStatus(TuniuCommonService.NOTIFY_PREPARED);
		tuniuNoticeService.updateOutNotice(notice);
		write2Response(response, "success");
		
	}
	
	/**
	 * 失败结果
	 * @param request
	 * @param response
	 */
	private void recieveFailure(HttpServletRequest request, 
			HttpServletResponse response) {
		
		String orderId = getParam(request, "orderid");
		TuniuOrder order = tuniuOrderService.getOrderById(orderId, true);
		
		/*剔除重复通知*/
		if(order == null || isEmpty(order.getOrderStatus())
				|| TuniuOrderService.STATUS_OUT_FAILURE.equals(order.getOrderStatus())) {
			logger.info("[出票返回失败接口]本次出票失败通知为重复请求，orderId=" + orderId);
			write2Response(response, "success");
			return;
		} else if(TuniuOrderService.STATUS_TIME_OUT.equals(order.getOrderStatus())) {
			logger.info("[出票返回失败接口]订单已经超时,不再更新订单内容以及结果,orderId=" + orderId);
			write2Response(response, "success");
			return;
		} else if(TuniuOrderService.STATUS_CANCEL_SUCCESS.equals(order.getOrderStatus())) {
			logger.info("[出票返回失败接口]订单已经取消,不再更新订单内容以及结果,orderId=" + orderId);
			write2Response(response, "success");
			return;
		}
		
		/*订单参数*/
		String billNo = getParam(request, "billno");
		String buyMoney = getParam(request, "buymoney");
		String fromTime= getParam(request, "from_time");
		String toTime= getParam(request, "to_time");
		String outTicketTime= getParam(request, "out_ticket_time");
		String passengerStr = getParam(request, "passengers");//乘客审核信息
		String errorinfo = getParam(request, "errorinfo");//错误信息
		logger.info("[出票返回失败接口]乘客审核信息：passengers=" + passengerStr + ", order_id : " + orderId);
		Double money = null;
		try {
			if(!isEmpty(buyMoney)) {
				money = Double.valueOf(buyMoney);
			}
		} catch (NumberFormatException e) {
			logger.info("[出票返回失败接口]预订回调金额异常!orderId : " + orderId);
			write2Response(response, "failed");
			return;
		}
		if(StringUtils.isEmpty(fromTime)) {
			fromTime = null;
		}
		if(StringUtils.isEmpty(toTime)) {
			toTime = null;
		}
		if(StringUtils.isEmpty(outTicketTime)) {
			outTicketTime = null;
		}
		
		order.setBuyMoney(money);
		order.setOutTicketBillno(billNo);
		order.setOutTicketTime(outTicketTime == null ? null : DateUtil.stringToDate(outTicketTime, DateUtil.DATE_FMT3));
		order.setFromTime(fromTime == null ? null : DateUtil.stringToDate(fromTime, DateUtil.DATE_FMT3));
		order.setToTime(toTime == null ? null : DateUtil.stringToDate(toTime, DateUtil.DATE_FMT3));
		order.setOutFailReason(errorinfo);
		
		/*乘客审核信息*/
		List<TuniuPassenger> passengers = order.getPassengers();
		examinePassenger(passengerStr, passengers);
		
		/*通知状态修改*/
		Notice notice = null;
		if(TuniuOrderService.STATUS_NOTIFY_SUCCESS.equals(order.getOrderStatus())) {
			notice = tuniuNoticeService.getBookNoticeByOrderId(orderId);
			logger.info("[出票返回失败接口]修改[占座通知]:准备通知【占座失败】！："+orderId + ", status : " + order.getOrderStatus());
			notice.setNotifyStatus(TuniuCommonService.NOTIFY_PREPARED);
			logger.info("book : " + orderId + ", " + notice.getNotifyStatus() + ", " + notice.getNotifyUrl());
			tuniuNoticeService.updateBookNotice(notice);
		} else {
			notice = tuniuNoticeService.getOutNoticeByOrderId(orderId);
			if(notice != null) {
				logger.info("[出票返回失败接口]修改[出票通知]:准备通知【出票失败】！："+orderId + ", status : " + order.getOrderStatus());
				notice.setNotifyStatus(TuniuCommonService.NOTIFY_PREPARED);
				logger.info("out : " + orderId + ", " + notice.getNotifyStatus() + ", " + notice.getNotifyUrl());
				tuniuNoticeService.updateOutNotice(notice);
			}
		}
		/*更新订单信息以及乘客车票信息*/
		if(order.getOrderStatus().equals(TuniuOrderService.STATUS_CANCEL_ING)){
			//取消
			order.setOrderStatus(TuniuOrderService.STATUS_CANCEL_FAILURE);
		}else{
			//出票
			order.setOrderStatus(TuniuOrderService.STATUS_OUT_FAILURE);
		}
		
		tuniuOrderService.updateOrder(order, true);
		
		if(errorinfo.equals("11")) {
			logger.info("超时订单不予主动回调，orderid : " + orderId);
			return;
		}
		
		write2Response(response, "success");
	
	}
	
	/**
	 * 解析出票系统回调的乘客车票信息
	 * @param seatTrains
	 * @return
	 */
	private List<TuniuPassenger> seatTrains2Passenger(String seatTrains) {
		if(isEmpty(seatTrains))
			return null;
		String[] seats = seatTrains.split("#");
		if(seats == null || seats.length == 0)
			return null;
		List<TuniuPassenger> passengers = new ArrayList<TuniuPassenger>();
		for(String seat : seats) {
			String[] seatMeta = seat.split("\\|");
			if(seatMeta == null || seatMeta.length == 0)
				return null;
			
			TuniuPassenger passenger = new TuniuPassenger();
			passenger.setCpId(seatMeta[0]);//车票号
			Double money = Double.valueOf(seatMeta[1]);
			passenger.setBuyMoney(money);//成本价格
			passenger.setTrainBox(seatMeta[2]);//车厢
			passenger.setSeatNo(seatMeta[3]);//座位号
			if(seatMeta.length == 5) {
				passenger.setSeatType(seatMeta[4]);//座位类型
			}
			passengers.add(passenger);
		}
		return passengers;
	}
	
	/**
	 * 解析出票系统回调的乘客审核信息
	 * @param examinePassengers
	 * @return
	 */
	private void examinePassenger(String examinePassengers, List<TuniuPassenger> passengers) {
		if(isEmpty(examinePassengers))
			return;
		String[] examines = examinePassengers.split("#");
		if(examines == null || examines.length == 0)
			return;
		
		for(String examine : examines) {
			String[] examineMeta = examine.split("\\|");
			if(examineMeta == null || examineMeta.length != 4)
				return;
			
			String cpId = examineMeta[0];
			String reasonStr = examineMeta[3];
			if(isEmpty(reasonStr))
				continue;
			Integer reason = Integer.valueOf(reasonStr);
			for(TuniuPassenger passenger : passengers) {
				if(passenger.getCpId().equals(cpId)) {
					passenger.setReason(reason);
					break;
				}
			}
		}
		
	}
	
	/**
	  * 获取所有请求参数
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  String getFullURL(HttpServletRequest request) {
	    	
	    	StringBuffer params = new StringBuffer();
	    	 Map<String,String[]> map1 = (Map<String,String[]>)request.getParameterMap();  
	         for(String name:map1.keySet()){  
	             String[] values = map1.get(name);
	             StringBuffer sBuffer = new StringBuffer();
	             for (int i = 0; i < values.length; i++) {
	            	 sBuffer.append(values[i]).append("&");
				 }
	             params.append(name).append("=").append(sBuffer.toString());
	         }

	    	 StringBuffer url = request.getRequestURL();
	    	 
	    	 if (request.getQueryString() != null) {
	    	     url.append('?');
	    	     url.append(request.getQueryString());
	    	 }
	    	 url.append("##").append(params.toString());
	    	 
	    	 return url.toString();
	    }
	
}
