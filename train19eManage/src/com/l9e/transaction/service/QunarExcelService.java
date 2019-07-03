package com.l9e.transaction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface QunarExcelService {
	//查询预订订单信息
	public List<Map<String, String>> queryQunarBook(HashMap<String, Object> map);
	
	//查询联程预订订单信息
	public List<Map<String, String>> queryLianChengQunarBook(HashMap<String, Object> map);
	
	//查询退款订单信息
	public List<Map<String, String>> queryRefundTicket(HashMap<String, Object> map);
}
