package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface TuniuStationService {
	
	// 获取按条件查询数 
	public int queryTuniuStationCounts(Map<String, Object> paramMap);
	//获取按条件查询所有
	public List<Map<String, String>> queryTuniuStationList(Map<String, Object> paramMap);
	//导出excel
	public List<Map<String, String>> queryTuniuStationInfoExcel(Map<String, Object> paramMap);
	// 获取按条件查询数 
	public int queryTuniuStationInfoCounts(Map<String, Object> paramMap);
	
	public int queryCounts(Map<String, Object> paramMap);
	
	//获取按条件查询所有
	public List<Map<String, String>> queryTuniuStationInfoList(Map<String, Object> paramMap);
	//插入addTuniuStation
	public void addTuniuStation(Map<String, Object> paramMap);
	//插入addTuniuStationTj
	public void addTuniuStationTj(Map<String, Object> paramMap);
	// 按订单查询退款完成数
	public int queryRefundFinishCount(String orderId);


}
