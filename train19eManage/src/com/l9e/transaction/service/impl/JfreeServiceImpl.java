package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.JfreeDao;
import com.l9e.transaction.service.JfreeService;

@Service("jfreeService")
public class JfreeServiceImpl implements JfreeService{
	@Resource
	private JfreeDao jfreeDao ;

	public int queryActiveUser(String create_time) {
		return jfreeDao.queryActiveUser(create_time);
	}

	public List<Map<String, String>> queryPictureLineParam() {
		return jfreeDao.queryPictureLineParam();
	}

//	public List<Map<String, String>> queryDateTimeDetail(String dateNow) {
//		return jfreeDao.queryDateTimeDetail(dateNow);
//	}

	public List<Map<String, String>> queryQunar15DayPic() {
		return jfreeDao.queryQunar15DayPic();
	}

	public List<Map<String, String>> queryThisDayHour(String create_time,String channel) {
		if("19e".equals(channel)){
			return jfreeDao.queryThisDayHour(create_time);
		}else if("qunar".equals(channel)){
			return jfreeDao.queryThisDayHourQunar(create_time);
		}else if("19pay".equals(channel)){
			return jfreeDao.queryThisDayHour19pay(create_time);
		}else if("cmpay".equals(channel)){
			return jfreeDao.queryThisDayHourCmpay(create_time);
		}else if("app".equals(channel)){
			return jfreeDao.queryThisDayHourApp(create_time);
		}else if("ccb".equals(channel)){   
			return jfreeDao.queryThisDayHourCBB(create_time);
		}else if("weixin".equals(channel)){   
			return jfreeDao.queryThisDayHourWeixin(create_time);
		}else if("elong".equals(channel)){   
			return jfreeDao.queryThisDayHourElong(create_time);
		}else{//对外商户
			Map<String, String> map = new HashMap<String, String>();
			map.put("create_time", create_time);
			map.put("channel", channel);
			return jfreeDao.queryThisDayHourExt(map);
		}
	}

	public List<Map<String, String>> showProvinceSellChart(
			Map<String, Object> query_Map) {
		return jfreeDao.showProvinceSellChart(query_Map);
	}

	public List<Map<String, String>> queryDateTimeBefore(String dateNow) {
		return jfreeDao.queryDateTimeBefore(dateNow);
	}
	
	public List<Map<String, String>> queryDateTimeAfter(String dateNow) {
		return jfreeDao.queryDateTimeAfter(dateNow);
	}

	public List<Map<String, String>> query15DaysActive(String provinceId) {
		return jfreeDao.query15DaysActive(provinceId);
	}


	public List<Map<String, String>> queryDayTimeBefore(String createTime) {
		return jfreeDao.queryDayTimeBefore(createTime);
	}

	@Override
	public List<Map<String, String>> queryOutTicketSbl(String createTime) {
		return jfreeDao.queryOutTicketSbl(createTime);
	}

	@Override
	//查询本省十五日销售统计
	public List<Map<String, String>> query15DaysActiveInfo(String province_id) {
		return jfreeDao.query15DaysActiveInfo(province_id);
	}

	
}
