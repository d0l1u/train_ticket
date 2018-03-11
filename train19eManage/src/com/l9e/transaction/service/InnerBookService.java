package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface InnerBookService {
	// 查询预订订单列表
	public List<Map<String, String>> queryInnerBookList(Map<String, Object> map);

	// 查询预订订单条数
	public int queryInnerBookListCount(Map<String, Object> map);

	// 查询预订订单车票信息
	public List<Map<String, String>> queryInnerBookOrderInfoCp(String order_id);

	// 查询预订订单保险信息
	public List<Map<String, String>> queryInnerBookOrderInfoBx(String order_id);

	// 查询预订订单历史记录
	public List<Map<String, Object>> queryInnerHistroyByOrderId(String order_id);

	// 查询预订订单信息
	public Map<String, String> queryInnerBookOrderInfo(String order_id);

	// 查询退款明细
	public List<Map<String, Object>> queryInnerOutTicketInfo(String order_id);

	// 切换无视截止时间
	void updateInnerSwitch_ignore(Map<String, String> map);

	// 增加操作日志
	void addInnerUserAccount(Map<String, String> map);
}
