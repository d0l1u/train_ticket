package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.TuniuWhitePassDao;
import com.l9e.transaction.vo.TuniuWhitePass;
@Repository("tuniuWhitePassDao")
public class TuniuWhitePassDaoImpl extends BaseDao implements TuniuWhitePassDao{
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TuniuWhitePass> getWhitePassList(Map<String,Object> param) {
		return this.getSqlMapClientTemplate().queryForList("whitePass.getWhitePassList", param);
	}

}
