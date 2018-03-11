package com.l9e.transaction.vo;

import java.util.List;
import java.util.Map;

public class MobileOrderInfo {
	private OrderInfo orderInfo;
	private OrderInfoPs orderInfoPs;
	private List<Map<String, String>> detailList;
	private Map<String, String> eopInfo;
	private int wz_ext;
	public OrderInfo getOrderInfo() {
		return orderInfo;
	}
	public void setOrderInfo(OrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}
	public OrderInfoPs getOrderInfoPs() {
		return orderInfoPs;
	}
	public void setOrderInfoPs(OrderInfoPs orderInfoPs) {
		this.orderInfoPs = orderInfoPs;
	}
	public List<Map<String, String>> getDetailList() {
		return detailList;
	}
	public void setDetailList(List<Map<String, String>> detailList) {
		this.detailList = detailList;
	}
	public Map<String, String> getEopInfo() {
		return eopInfo;
	}
	public void setEopInfo(Map<String, String> eopInfo) {
		this.eopInfo = eopInfo;
	}
	public int getWz_ext() {
		return wz_ext;
	}
	public void setWz_ext(int wzExt) {
		wz_ext = wzExt;
	}
	
}
