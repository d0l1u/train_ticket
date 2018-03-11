package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface GtBookService {
	// 查询预订订单列表
	public List<Map<String, String>> queryGtBookList(Map<String, Object> map);

	// 查询预订订单条数
	public int queryGtBookListCount(Map<String, Object> map);

	// 查询预订订单车票信息
	public List<Map<String, String>> queryGtBookOrderInfoCp(String order_id);

	// 查询预订订单保险信息
	public List<Map<String, String>> queryGtBookOrderInfoBx(String order_id);

	// 查询预订订单历史记录
	public List<Map<String, Object>> queryGtHistroyByOrderId(String order_id);

	// 查询预订订单信息
	public Map<String, String> queryGtBookOrderInfo(String order_id);

	// 查询退款明细
	public List<Map<String, Object>> queryGtOutTicketInfo(String order_id);

	// 切换无视截止时间
	void updateGtSwitch_ignore(Map<String, String> map);

	// 增加操作日志
	void addGtUserAccount(Map<String, String> map);

	public Map<String, String> queryBookOrderInfo(String orderId);
	/**
	 * 出票系统重新通知订单系统
	 * @param order_id
	 */
	public void cpNoticeAgain(String order_id);
}
