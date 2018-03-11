package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface AppBookService {

	// 查询预订订单列表
	public List<Map<String, String>> queryAppBookList(Map<String, Object> map);

	// 查询预订订单条数
	public int queryAppBookListCount(Map<String, Object> map);

	// 查询预订订单车票信息
	public List<Map<String, String>> queryAppBookOrderInfoCp(String order_id);

	// 查询预订订单保险信息
	public List<Map<String, String>> queryAppBookOrderInfoBx(String order_id);

	// 查询预订订单历史记录
	public List<Map<String, Object>> queryAppHistroyByOrderId(String order_id);

	// 查询预订订单信息
	public Map<String, String> queryAppBookOrderInfo(String order_id);

	// 查询退款明细
	public List<Map<String, Object>> queryAppOutTicketInfo(String order_id);

	// 切换无视截止时间
	void updateAppSwitch_ignore(Map<String, String> map);

	// 增加操作日志
	void addAppUserAccount(Map<String, String> map);

}
