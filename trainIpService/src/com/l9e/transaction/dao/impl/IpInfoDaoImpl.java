package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.IpInfoDao;
import com.l9e.transaction.vo.IpInfo;

@Repository("ipInfoDao")
public class IpInfoDaoImpl extends BaseDao implements IpInfoDao{

	
	/**
	 * 查询某个具体的IP地址
	 * params：ipId
	 */
	@Override
	public IpInfo selectOneIp(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return (IpInfo)this.getSqlMapClientTemplate().queryForObject("ipInfo.selectIpInfo", params);
	}
	
	/**
	 * 查询多个具体的IP地址
	 * params：ipStatus，ipType
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<IpInfo> selectIpList(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().queryForList("ipInfo.selectIpInfo",params);
	}

	/**
	 * 更新某个具体的IP
	 */
	@Override
	public int updateIpInfo(IpInfo ipInfo) {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().update("ipInfo.updateIpInfo", ipInfo);
	}

	/**
	 * 往cp_ipinfo_log表插入一条日志记录
	 * @param paramMap
	 */
	@Override
	public void insertIpInfoLog(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().insert("ipInfo.insertIpInfoLog", paramMap);

	}

	/**
	 * 往cp_ipinfo_release表插入一条待释放IP记录
	 * @param paramMap
	 */
	@Override
	public void insertIpInfoRelease(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().insert("ipInfo.insertIpInfoRelease", paramMap);
	}



}
