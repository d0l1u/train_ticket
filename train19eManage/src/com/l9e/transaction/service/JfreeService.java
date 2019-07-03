package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface JfreeService {

	List<Map<String, String>> queryPictureLineParam();

	int queryActiveUser(String create_time);

	//List<Map<String, String>> queryDateTimeDetail(String dateNow);

	List<Map<String, String>> queryQunar15DayPic();

	List<Map<String, String>> queryThisDayHour(String create_time,String channel);

	List<Map<String, String>> showProvinceSellChart(
			Map<String, Object> query_Map);
	
	List<Map<String, String>> queryDateTimeBefore(String dateNow);
	
	List<Map<String, String>> queryDateTimeAfter(String dateNow);

	List<Map<String, String>> query15DaysActive(String provinceId);

	List<Map<String, String>> queryDayTimeBefore(String createTime);

	List<Map<String, String>> queryOutTicketSbl(String createTime);

	//查询本省十五日销售统计
	List<Map<String, String>> query15DaysActiveInfo(String province_id);

}
