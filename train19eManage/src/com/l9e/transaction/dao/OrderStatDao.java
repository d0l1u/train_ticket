package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface OrderStatDao {

	int queryOrderStatListCount(Map<String, Object> paramMap);

	List<Map<String, Object>> queryOrderStatList(Map<String, Object> paramMap);

	List<Map<String, String>> queryPictureLineParam();

	List<Map<String, String>> queryAllHour();

	List<Map<String, String>> queryThisDayHour(String create_time);

	List<Map<String, String>> queryDateTimeDetail(String create_time);

	List<Map<String, Object>> queryProvinceCount(Map<String, Object> paramMap);

	int queryProvinceCountPagein(Map<String, Object> paramMap);

	int queryProvinceTicket(Map<String, String> ticket_map);

	List<String> queryEstateCount(String area_no);

	int queryProvinceActiveAgent(Map<String, String> query_map);

	List<Map<String, String>> showProvinceSellChart(
			Map<String, Object> query_Map);

	int queryActiveUser(String create_time);

	List<String> queryJoinList(String create_time);

	List<Map<String, Object>> queryHc_statInfo_provinceList(String create_time);

	List<String> queryOrderCount(Map<String, String> query_Map);

	int queryTicketCount(String area_no);

	Map<String, String> querySupervise_nameToArea_no(String area_name);

	int querySuperviseAreaCount(Map<String, Object> paramMap);

	List<Map<String, Object>> querySuperviseAreaList(
			Map<String, Object> paramMap);




}
