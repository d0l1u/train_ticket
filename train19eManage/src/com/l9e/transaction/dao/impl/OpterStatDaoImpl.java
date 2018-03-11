package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.OpterStatDao;
@Repository("opterStatDao")
public class OpterStatDaoImpl extends BaseDao implements OpterStatDao{

	public int queryOpterStatCount(Map<String, Object> query_Map) {
		return this.getTotalRows("opterStat.queryOpterStatCount", query_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryOpterStatList(
			Map<String, Object> query_Map) {
		return this.getSqlMapClientTemplate().queryForList("opterStat.queryOpterStatList",query_Map);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> queryOpterInfo(String tj_id) { 
		return (HashMap<String, Object>) this.getSqlMapClientTemplate().queryForObject("opterStat.queryOpterInfo", tj_id); 
	}

}
