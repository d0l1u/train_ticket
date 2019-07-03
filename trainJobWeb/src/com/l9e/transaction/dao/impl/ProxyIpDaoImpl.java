package com.l9e.transaction.dao.impl;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ProxyIpDao;

/**
 * <p>
 * Title: ProxyIpDaoImpl.java
 * </p>
 * <p>
 * Description: TODO
 * </p>
 * 
 * @author taokai
 * @date 2017年3月2日
 */
@Repository("proxyIpDao")
@SuppressWarnings("all")
public class ProxyIpDaoImpl extends BaseDao implements ProxyIpDao {

	@Override
	public Integer updateDisable() {
		return this.getSqlMapClientTemplate().update("proxyIp.updateDisable");
	}

	
}
