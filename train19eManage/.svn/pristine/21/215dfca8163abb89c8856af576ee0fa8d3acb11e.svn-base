package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.CpStatDao;
@Repository("cpStatDao")
public class CpStatDaoImpl extends BaseDao implements CpStatDao{
	
	public int querycpStatServiceListCount(Map<String, Object> paramMap) {
		return this.getTotalRows("cpStat.querycpStatServiceListCount", paramMap);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> querycpStatServiceList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("cpStat.querycpStatServiceList",paramMap);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryPictureLineParam() {
		return this.getSqlMapClientTemplate().queryForList("cpStat.queryPictureLineParam");
	}
}
