package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface XcBookService {
	// 查询预订订单列表
	public List<Map<String, String>> queryXcBookList(Map<String, Object> map);

	// 查询预订订单条数
	public int queryXcBookListCount(Map<String, Object> map);

	// 查询预订订单车票信息
	public List<Map<String, String>> queryXcBookOrderInfoCp(String order_id);

	// 查询预订订单保险信息
	public List<Map<String, String>> queryXcBookOrderInfoBx(String order_id);

	// 查询预订订单历史记录
	public List<Map<String, Object>> queryXcHistroyByOrderId(String order_id);

	// 查询预订订单信息
	public Map<String, String> queryXcBookOrderInfo(String order_id);

	// 查询退款明细
	public List<Map<String, Object>> queryXcOutTicketInfo(String order_id);

	// 切换无视截止时间
	void updateXcSwitch_ignore(Map<String, String> map);

	// 增加操作日志
	void addXcUserAccount(Map<String, String> map);

	public Map<String, String> queryBookOrderInfo(String orderId);
	
}
