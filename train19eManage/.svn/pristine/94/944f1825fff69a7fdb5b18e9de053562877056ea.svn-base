package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface ExtBookDao {
	//查询预订订单列表
	public List<Map<String, String>> queryExtBookList(Map<String, Object> map);
	
	//查询预订订单条数
	public int queryExtBookListCount(Map<String, Object> map);
	
	//查询预订订单车票信息
	public List<Map<String, String>> queryExtBookOrderInfoCp(String order_id); 
	
	//查询预订订单保险信息
	public List<Map<String, String>> queryExtBookOrderInfoBx(String order_id);
	
	//查询预订订单历史记录
	public List<Map<String, Object>> queryExtHistroyByOrderId(String order_id);
	
	//查询预订订单信息
	public List<Map<String, String>> queryExtBookOrderInfo(String order_id);

	//查询退款明细
	public List<Map<String, Object>> queryExtOutTicketInfo(String order_id);
	
	//切换无视截止时间
	void updateExtSwitch_ignore(Map<String, String> map);
	
	//增加操作日志
	void addExtUserAccount(Map<String, String> map);

	public Map<String, String> queryBookOrderInfo(String orderId);
}
