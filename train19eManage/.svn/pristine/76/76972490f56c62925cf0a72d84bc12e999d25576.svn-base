package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface JfreeDao {

	int queryActiveUser(String create_time);

	List<Map<String, String>> queryPictureLineParam();

	//List<Map<String, String>> queryDateTimeDetail(String dateNow);

	List<Map<String, String>> queryQunar15DayPic();

	List<Map<String, String>> queryThisDayHour(String create_time);

	List<Map<String, String>> queryThisDayHourQunar(String create_time);

	List<Map<String, String>> showProvinceSellChart(
			Map<String, Object> query_Map);
	
	List<Map<String, String>> queryDateTimeBefore(String create_time);
	
	List<Map<String, String>> queryDateTimeAfter(String create_time);

	List<Map<String, String>> query15DaysActive(String provinceId);

	List<Map<String, String>> queryDayTimeBefore(String createTime);

	List<Map<String, String>> queryThisDayHour19pay(String createTime);

	List<Map<String, String>> queryThisDayHourCmpay(String createTime);

	List<Map<String, String>> queryThisDayHourApp(String createTime);

	List<Map<String, String>> queryThisDayHourCBB(String createTime);

	List<Map<String, String>> queryThisDayHourWeixin(String createTime);

	List<Map<String, String>> queryOutTicketSbl(String createTime);

	List<Map<String, String>> queryThisDayHourExt(Map<String, String> map);

	//查询本省十五日销售统计
	List<Map<String, String>> query15DaysActiveInfo(String province_id);

	List<Map<String, String>> queryThisDayHourElong(String createTime);
}
