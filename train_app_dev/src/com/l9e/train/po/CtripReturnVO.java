package com.l9e.train.po;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class CtripReturnVO {
	@JsonProperty("ErrorCode")  
	Integer errorCode;
	
	@JsonProperty("ErrorInfo") 
	List<CtripOrderVo> errorInfo;
	

	/*ctrip_id ="",携程订单号
		 orderId   ="", --订单号
		 outTicketBillno ="", --12306订单号
		 retValue  ="", --结果状态
		 retInfo     ="", --错误信息
		 robotNum   =0,
		 payMoney = 0，支付金额
		 
		 【{"ErrorCode":0,"ErrorInfo":[{"payMoney":0,"arrivetime":"","robotNum":8,"outTicketBillno":"","retInfo":"没有进入携程会员信息页面","orderId":"HC1506182151557086","ctrip_id":"","retValue":"failure"}]}
		 
      */

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public List<CtripOrderVo> getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(List<CtripOrderVo> errorInfo) {
		this.errorInfo = errorInfo;
	}

	
	

	
	
	
	
	
	

}
