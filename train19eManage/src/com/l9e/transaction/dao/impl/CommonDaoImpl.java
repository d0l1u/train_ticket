package com.l9e.transaction.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.CommonDao;
import com.l9e.transaction.vo.AreaVo;

@Repository("commonDao")
public class CommonDaoImpl extends BaseDao implements CommonDao {
	
	@Override
	public String query() {
		return (String) this.getSqlMapClientTemplate().queryForObject("common.getUser", "127.0.0.1");
	}
	
	@SuppressWarnings("unchecked")
	public List<AreaVo> getProvince() {
		return this.getSqlMapClientTemplate().queryForList("common.getProvince");
	}

	@SuppressWarnings("unchecked")
	public List<AreaVo> getCity(String provinceid) {
		return this.getSqlMapClientTemplate().queryForList("common.getCity", provinceid);
	}

	@SuppressWarnings("unchecked")
	public List<AreaVo> getArea(String cityid) {
		return this.getSqlMapClientTemplate().queryForList("common.getArea", cityid);
	}

}
