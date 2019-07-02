package com.l9e.train.notify.request;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.l9e.train.notify.service.impl.NotifyServiceImpl;
import com.l9e.train.po.OrderBill;
import com.l9e.train.util.DateUtil;
import com.l9e.train.util.PostRequestUtil;
import com.l9e.train.util.UrlFormatUtil;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

public class NotifyRequest {
	
	private Logger logger=Logger.getLogger(this.getClass());
	public static String SUCCESS = "success";
	public static String TIMEOUT = "timeout";
	public static String FAILURE = "failure";
	public static String EXCEPTION = "exception";
	
	
	public String sending(OrderBill orderbill) {

		Map<String, String> maps = new HashMap<String,String>();
		
		maps.put("orderid", orderbill.getOrderId());//子订单号
		maps.put("billno", orderbill.getOutTicketBillno());//主订单号
		maps.put("buymoney", orderbill.getBuyMoney());//供货商价格
		maps.put("seattrains", orderbill.getSeattrains());//坐席信息		
		maps.put("username", orderbill.getAccUsername());
		maps.put("password", orderbill.getAccPassword());
		maps.put("result", "success");	
		maps.put("errorinfo", orderbill.getErrorInfo());
		maps.put("extseattype", orderbill.getExtSeattype());
		maps.put("level", orderbill.getLevel());
		maps.put("passengers", orderbill.getPassengers());
		maps.put("from_time", orderbill.getFromTime());
		maps.put("to_time", orderbill.getToTime());
		maps.put("returnoptlog", orderbill.getReturnOptlog());
		
		if("77,83,10".contains(orderbill.getOrderStatus())){//订单发送失败
			maps.put("result", "failure");
		}else{
			if(orderbill.getOrderStatus().equals(OrderBill.BILL_SUCCESS)){
				maps.put("status", "00");
			}else{
				maps.put("status", "11");
			}
			maps.put("result", "success");	
		}
		maps.put("is_pay", orderbill.getIsPay());
		maps.put("out_ticket_time", orderbill.getOutTicketTime());
		if(null == orderbill.getOutTicketTime() || "".equals(orderbill.getOutTicketTime())){
			maps.put("pay_limit_time", "");
		}else if(null == orderbill.getPayLimitTime() || "".equals(orderbill.getPayLimitTime())){
			NotifyServiceImpl nsi = new NotifyServiceImpl();
			Integer time_num = 45;
			try {
				time_num = nsi.queryWaitPayTime();
			} catch (Exception e) {
				e.printStackTrace();
			}
			String pay_limit_time = DateUtil.dateToString(DateUtil.dateAddMin(DateUtil.stringToDate(orderbill.getOutTicketTime(), DateUtil.DATE_FMT3),time_num),DateUtil.DATE_FMT3);
			maps.put("pay_limit_time", pay_limit_time);
		}else{
			maps.put("pay_limit_time", orderbill.getPayLimitTime());
		}
		logger.info("Seattrains："+orderbill.getSeattrains()+" is_pay:"+orderbill.getIsPay());
		String param=null;
		try {
			param = UrlFormatUtil.CreateUrl("", maps, "", "UTF-8");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		logger.info("start post request params:"+param);
		String str=PostRequestUtil.getPostRes("UTF-8", orderbill.getNotifyUrl(), param);
		logger.info("end post request retValue:"+str);
		if(str.equals(PostRequestUtil.TIME_OUT) ){//超时处理
			//进行修改订单状态,插入超时插入超时重发表
			logger.info("Connect timeout!");
			return TIMEOUT;
		}else if(str.equals(PostRequestUtil.URL_ERROR) || str.equals(PostRequestUtil.CONNECT_ERROR)){//URL错误处理
			//进行修改订单状态,并且进行退款
			logger.info("Connect Exception!");
			return EXCEPTION;
		}else if(str.equals("success") || str.equals("repeat")){//如果回复成功、重复，表示发送成功
			logger.info("Post Success!");
			return SUCCESS;
		}else if(str.equals("failed")){//提交请求失败
			logger.info("Post Failure!");
			return FAILURE;
		}else{//如果没有任何回复或者报错，确定发送失败，得重新发送，直到发送成功
			return EXCEPTION;
		}
	}

}
