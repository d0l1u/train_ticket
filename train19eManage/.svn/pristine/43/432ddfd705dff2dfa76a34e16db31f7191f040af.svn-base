package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.InnerBookDao;
import com.l9e.transaction.service.InnerBookService;

@Service("innerBookService")
public class InnerBookServiceImpl implements InnerBookService {

	@Resource
	private InnerBookDao innerBookDao;

	// 查询预订订单列表
	public List<Map<String, String>> queryInnerBookList(Map<String, Object> map) {
		return innerBookDao.queryInnerBookList(map);
	}

	// 查询预订订单条数
	public int queryInnerBookListCount(Map<String, Object> map) {
		return innerBookDao.queryInnerBookListCount(map);
	}

	// 查询预订订单车票信息
	public List<Map<String, String>> queryInnerBookOrderInfoCp(String order_id) {
		return innerBookDao.queryInnerBookOrderInfoCp(order_id);
	}

	// 查询预订订单保险信息
	public List<Map<String, String>> queryInnerBookOrderInfoBx(String order_id) {
		return innerBookDao.queryInnerBookOrderInfoBx(order_id);
	}

	// 查询预订订单历史记录
	public List<Map<String, Object>> queryInnerHistroyByOrderId(String order_id) {
		return innerBookDao.queryInnerHistroyByOrderId(order_id);
	}

	// 查询预订订单信息
	public Map<String, String> queryInnerBookOrderInfo(String order_id) {
		return innerBookDao.queryInnerBookOrderInfo(order_id).get(0);
	}

	// 查询退款明细
	public List<Map<String, Object>> queryInnerOutTicketInfo(String order_id) {
		return innerBookDao.queryInnerOutTicketInfo(order_id);
	}

	// 切换无视截止时间
	public void updateInnerSwitch_ignore(Map<String, String> map) {
		innerBookDao.updateInnerSwitch_ignore(map);
	}

	// 增加操作日志
	public void addInnerUserAccount(Map<String, String> map) {
		innerBookDao.addInnerUserAccount(map);
	}
}
