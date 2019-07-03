package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface OrderInfoToOrder_provinceService {

	List<Map<String, Object>> queryProvinceCount(Map<String, Object> paramMap);

	Map<String, Object> queryProvinceTicket(Map<String, String> query_map);  
	
	int queryProvinceActiveAgent(Map<String, String> query_map);

	List<String> queryEstateCount(String area_no);

	void addOrder_province(Map insert_Map);

	int queryHc_statInfo_provinceCount();

	List<Map<String, Object>> queryDate();

	int query_Province_Bx_count(Map<String, String> query_map);

	double query_Province_Bx_Money_Sum(Map<String, String> query_map);

	double query_Province_Succeed_money(Map<String, String> query_map);

	double query_Province_defeated_money(Map<String, String> query_map);

	List<String> query_Province_Succeed_count(Map<String, String> query_map);
	
	long query_Province_Mount_count(Map<String, String> query_map);
	
	long query_Province_Last_mount_count(Map<String, String> query_map);

}