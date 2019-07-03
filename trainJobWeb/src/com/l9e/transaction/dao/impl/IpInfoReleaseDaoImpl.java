package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.IpInfoReleaseDao;
import com.l9e.transaction.vo.CtripAcc;
import com.l9e.transaction.vo.IpInfoRelease;

@Repository("ipInfoReleaseDao")
public class IpInfoReleaseDaoImpl extends BaseDao implements IpInfoReleaseDao {

	@Override
	public List<IpInfoRelease> selectIpInfoReleaseList(
			Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().queryForList("ipInfoRelease.selectReleaseIpInfoList", paramMap);
	}

	@Override
	public int updateIpInfoRelease(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().update("ipInfoRelease.updateIpInfoRelease", paramMap);
	}




}
