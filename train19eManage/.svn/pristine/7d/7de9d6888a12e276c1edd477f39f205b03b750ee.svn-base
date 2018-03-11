package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.AppBookDao;
import com.l9e.transaction.service.AppBookService;
@Service("appBookService")
public class AppBookServiceImpl implements AppBookService {

	@Resource
	private AppBookDao appBookDao;

	// 查询预订订单列表
	public List<Map<String, String>> queryAppBookList(Map<String, Object> map) {
		return appBookDao.queryAppBookList(map);
	}

	// 查询预订订单条数
	public int queryAppBookListCount(Map<String, Object> map) {
		return appBookDao.queryAppBookListCount(map);
	}

	// 查询预订订单车票信息
	public List<Map<String, String>> queryAppBookOrderInfoCp(String order_id) {
		return appBookDao.queryAppBookOrderInfoCp(order_id);
	}

	// 查询预订订单保险信息
	public List<Map<String, String>> queryAppBookOrderInfoBx(String order_id) {
		return appBookDao.queryAppBookOrderInfoBx(order_id);
	}

	// 查询预订订单历史记录
	public List<Map<String, Object>> queryAppHistroyByOrderId(String order_id) {
		return appBookDao.queryAppHistroyByOrderId(order_id);
	}

	// 查询预订订单信息
	public Map<String, String> queryAppBookOrderInfo(String order_id) {
		return appBookDao.queryAppBookOrderInfo(order_id).get(0);
	}

	// 查询退款明细
	public List<Map<String, Object>> queryAppOutTicketInfo(String order_id) {
		return appBookDao.queryAppOutTicketInfo(order_id);
	}

	// 切换无视截止时间
	public void updateAppSwitch_ignore(Map<String, String> map) {
		appBookDao.updateAppSwitch_ignore(map);
	}

	// 增加操作日志
	public void addAppUserAccount(Map<String, String> map) {
		appBookDao.addAppUserAccount(map);
	}
}
