package com.l9e.transaction.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.ProxyIpDao;
import com.l9e.transaction.service.ProxyIpService;

/**
 * <p>
 * Title: ProxyIpServiceImpl.java
 * </p>
 * <p>
 * Description: TODO
 * </p>
 * 
 * @author taokai
 * @date 2017年3月2日
 */
@Service("proxyIpService")
public class ProxyIpServiceImpl implements ProxyIpService{

	@Resource
	private ProxyIpDao  proxyIpDao;

	@Override
	public Integer updateDisable() {
		return proxyIpDao.updateDisable();
	}
	


}
