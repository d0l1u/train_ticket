package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.OrderInfoToOrder_provinceDao;
import com.l9e.transaction.service.OrderInfoToOrder_provinceService;
@Service("orderInfoToOrder_provinceService")
public class OrderInfoToOrder_provinceServiceImpl implements OrderInfoToOrder_provinceService{
	@Resource
	private OrderInfoToOrder_provinceDao orderInfoToOrder_provinceDao;  

	public List<Map<String, Object>> queryProvinceCount(
			Map<String, Object> paramMap) {
		return orderInfoToOrder_provinceDao.queryProvinceCount(paramMap) ;
	}

	public Map<String, Object> queryProvinceTicket(Map<String, String> query_map) {
		return orderInfoToOrder_provinceDao.queryProvinceTicket(query_map);
	}

	public List<String> queryEstateCount(String area_no) {
		return orderInfoToOrder_provinceDao.queryEstateCount(area_no);
	}

	public int queryProvinceActiveAgent(Map<String, String> query_map) {
		return orderInfoToOrder_provinceDao.queryProvinceActiveAgent(query_map);
	}

	public void addOrder_province(Map insert_Map) {
		orderInfoToOrder_provinceDao.addOrder_province(insert_Map);
	}

	public int queryHc_statInfo_provinceCount() {
		return orderInfoToOrder_provinceDao.queryHc_statInfo_provinceCount();
	}

	public List<Map<String, Object>> queryDate() {
		return orderInfoToOrder_provinceDao.queryDate();
	}

	public double query_Province_Bx_Money_Sum(Map<String, String> query_map) {
		return orderInfoToOrder_provinceDao.query_Province_Bx_Money_Sum(query_map);
	}

	public int query_Province_Bx_count(Map<String, String> query_map) {
		return orderInfoToOrder_provinceDao.query_Province_Bx_count(query_map);
	}

	public double query_Province_Succeed_money(Map<String, String> query_map) {
		return orderInfoToOrder_provinceDao.query_Province_Succeed_money(query_map);
	}

	public double query_Province_defeated_money(Map<String, String> query_map) {
		return orderInfoToOrder_provinceDao.query_Province_defeated_money(query_map);
	}

	public List<String> query_Province_Succeed_count(
			Map<String, String> query_map) {
		return orderInfoToOrder_provinceDao.query_Province_Succeed_count(query_map);
	}

	public long query_Province_Last_mount_count(Map<String, String> query_map) {
		return orderInfoToOrder_provinceDao.query_Province_Last_mount_count(query_map);
	}

	public long query_Province_Mount_count(Map<String, String> query_map) {
		return orderInfoToOrder_provinceDao.query_Province_Mount_count(query_map);
	}
}
