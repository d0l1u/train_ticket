package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface GtStationService {
	
	// 获取按条件查询数 
	public int queryGtStationCounts(Map<String, Object> paramMap);
	//获取按条件查询所有
	public List<Map<String, String>> queryGtStationList(Map<String, Object> paramMap);
	//导出excel
	public List<Map<String, String>> queryGtStationInfoExcel(Map<String, Object> paramMap);
	// 获取按条件查询数 
	public int queryGtStationInfoCounts(Map<String, Object> paramMap);
	
	public int queryCounts(Map<String, Object> paramMap);
	
	//获取按条件查询所有
	public List<Map<String, String>> queryGtStationInfoList(Map<String, Object> paramMap);
	//插入addGtStation
	public void addGtStation(Map<String, Object> paramMap);
	//插入addGtStationTj
	public void addGtStationTj(Map<String, Object> paramMap);
	// 按订单查询退款完成数
	public int queryRefundFinishCount(String orderId);


}
