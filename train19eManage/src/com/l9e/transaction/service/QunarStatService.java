package com.l9e.transaction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface QunarStatService {

	public List<HashMap<String, String>> getStatInfo();
	
	public int getStatInfoCount(HashMap<String, Object> map);
	
	//查询某一时间段内的每天订单统计
	public List<HashMap<String, String>> getDaysStatInfo(HashMap<String, Object> map);
	

	//15日内交易报表	
	public List<Map<String, String>> queryPictureLineParam();
	
	
	//总体小时报表
	public List<Map<String, String>> queryAllHour();
	
	//当日小时报表
	public List<Map<String, String>> queryThisDayHour(String create_time);
	
	//查询日小时报表
	public List<Map<String, String>> queryDateTimeDetail(String create_time);
}
