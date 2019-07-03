package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface TuniuBookService {
	// 查询预订订单列表
	public List<Map<String, String>> queryTuniuBookList(Map<String, Object> map);

	// 查询预订订单条数
	public int queryTuniuBookListCount(Map<String, Object> map);

	// 查询预订订单车票信息
	public List<Map<String, String>> queryTuniuBookOrderInfoCp(String order_id);

	// 查询预订订单保险信息
	public List<Map<String, String>> queryTuniuBookOrderInfoBx(String order_id);

	// 查询预订订单历史记录
	public List<Map<String, Object>> queryTuniuHistroyByOrderId(String order_id);

	// 查询预订订单信息
	public Map<String, String> queryTuniuBookOrderInfo(String order_id);

	// 查询退款明细
	public List<Map<String, Object>> queryTuniuOutTicketInfo(String order_id);

	// 切换无视截止时间
	void updateTuniuSwitch_ignore(Map<String, String> map);

	// 增加操作日志
	void addTuniuUserAccount(Map<String, String> map);

	public Map<String, String> queryBookOrderInfo(String orderId);
	
}
