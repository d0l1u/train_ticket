package com.l9e.train.notify.request;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.l9e.train.po.OrderBill;
import com.l9e.train.util.PostRequestUtil;
import com.l9e.train.util.UrlFormatUtil;

public class NotifyRequest {
	
	private Logger logger=Logger.getLogger(this.getClass());
	public static String SUCCESS = "success";
	public static String TIMEOUT = "timeout";
	public static String FAILURE = "failure";
	public static String EXCEPTION = "exception";
	
	
	public String sending(OrderBill orderbill) {
		Map<String, String> maps = new HashMap<String,String>();
		try{
			maps.put("orderid", orderbill.getOrderId());//订单号
			maps.put("cpid", orderbill.getCpId());//车票单号
			maps.put("refundmoney", orderbill.getRefundMoney());//退订金额	
			maps.put("refund12306money", orderbill.getRefund12306Money());//12306退款金额	
			maps.put("status",orderbill.getNotifyType());
			maps.put("refund12306seq",orderbill.getRefund12306Seq());
			if("0".equals(orderbill.getNotifyType())){//通知类别：0、改签成功通知 1、退票成功通知
				maps.put("alterbuymoney", orderbill.getAlterBuyMoney());
				maps.put("altertraveltime", orderbill.getAlterTravelTime());
				maps.put("alterseattype", orderbill.getAlterSeatType());	
				maps.put("altertrainbox", orderbill.getAlterTrainBox());
				maps.put("alterseatno", orderbill.getAlterSeatNo());
			}
			if("22".equals(orderbill.getOrderStatus())){//22：拒绝退票
				maps.put("result","FAILURE");
			}else if("11".equals(orderbill.getOrderStatus())){//11：退票完成 
				maps.put("result","SUCCESS");
			}
			maps.put("alterdiffmoney", orderbill.getAlterDiffMoney());//改签差价
			
			maps.put("our_remark",orderbill.getOurRemark());
			maps.put("refuse_reason",orderbill.getRefuseReason());
		}catch(Exception e){
			logger.error("拼接数据异常", e);
		}
		
		
		String param=null;
		try {
			param = UrlFormatUtil.CreateUrl("", maps, "", "UTF-8");
		} catch (Exception e1) {
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
