package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.QunarStatDao;
import com.l9e.transaction.service.QunarStatService;

@Service("qunarStatService")
public class QunarStatServiceImpl implements QunarStatService {

	@Resource
	private QunarStatDao qunarStatDao;
	public List<HashMap<String, String>> getStatInfo() {
		return qunarStatDao.getStatInfo();
	}
	
	public int getStatInfoCount(HashMap<String, Object> map) {
		return qunarStatDao.getStatInfoCount(map);
	}
	
	//查询某一时间段内的每天订单统计
	public List<HashMap<String, String>> getDaysStatInfo(HashMap<String, Object> map){
		return qunarStatDao.getDaysStatInfo(map);
	}

	public List<Map<String, String>> queryAllHour() {
		// TODO Auto-generated method stub
		return qunarStatDao.queryAllHour();
	}

	public List<Map<String, String>> queryDateTimeDetail(String create_time) {
		// TODO Auto-generated method stub
		return qunarStatDao.queryDateTimeDetail(create_time);
	}

	public List<Map<String, String>> queryPictureLineParam() {
		// TODO Auto-generated method stub
		return qunarStatDao.queryPictureLineParam();
	}

	public List<Map<String, String>> queryThisDayHour(String create_time) {
		// TODO Auto-generated method stub
		return qunarStatDao.queryThisDayHour(create_time);
	}

}
