package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.OrderStatDao;
import com.l9e.transaction.service.OrderStatService;
@Service("orderStatService")
public class OrderStatServiceImpl implements OrderStatService{
	@Resource 
	private OrderStatDao orderStatDao;
	
	public int queryOrderStatListCount(Map<String, Object> paramMap) {
		return orderStatDao.queryOrderStatListCount(paramMap);
	}

	public List<Map<String, Object>> queryOrderStatList(
			Map<String, Object> paramMap) {
		return orderStatDao.queryOrderStatList(paramMap);
	}

	public List<Map<String, String>> queryPictureLineParam() {
		return orderStatDao.queryPictureLineParam();
	}

	public List<Map<String, String>> queryAllHour() {
		return orderStatDao.queryAllHour();
	}

	public List<Map<String, String>> queryThisDayHour(String create_time) {
		return orderStatDao.queryThisDayHour(create_time);
	}

	public List<Map<String, String>> queryDateTimeDetail(String create_time) {
		return orderStatDao.queryDateTimeDetail(create_time);
	}



	public List<Map<String, Object>> queryProvinceCount(
			Map<String, Object> paramMap) {
		return orderStatDao.queryProvinceCount(paramMap);
	}

	public int queryProvinceCountPagein(Map<String, Object> paramMap) {
		return orderStatDao.queryProvinceCountPagein(paramMap);
	}

	public int queryProvinceTicket(Map<String, String> ticket_map) {
		return orderStatDao.queryProvinceTicket(ticket_map);
	}

	public List<String> queryEstateCount(String area_no) {
		return orderStatDao.queryEstateCount(area_no);
	}

	public int queryProvinceActiveAgent(Map<String, String> query_map) {
		return orderStatDao.queryProvinceActiveAgent(query_map);
	}

	public List<Map<String, String>> showProvinceSellChart(
			Map<String, Object> query_Map) {
		return orderStatDao.showProvinceSellChart(query_Map);
	}

	public int queryActiveUser(String create_time) {
		return orderStatDao.queryActiveUser(create_time);
	}

	public List<String> queryJoinList(String create_time) {
		return orderStatDao.queryJoinList(create_time);
	}

	public List<Map<String, Object>> queryHc_statInfo_provinceList(
			String create_time) {
		return orderStatDao.queryHc_statInfo_provinceList(create_time);
	}

	public List<String> queryOrderCount(Map<String, String> query_Map) {
		return orderStatDao.queryOrderCount(query_Map);
	}

	public int queryTicketCount(String area_no) {
		return orderStatDao.queryTicketCount(area_no);
	}

	public Map<String, String> querySupervise_nameToArea_no(String area_name) {
		return orderStatDao.querySupervise_nameToArea_no(area_name);
	}

	public int querySuperviseAreaCount(Map<String, Object> paramMap) {
		return orderStatDao.querySuperviseAreaCount(paramMap);
	}

	public List<Map<String, Object>> querySuperviseAreaList(
			Map<String, Object> paramMap) {
		return orderStatDao.querySuperviseAreaList(paramMap);
	}


}
