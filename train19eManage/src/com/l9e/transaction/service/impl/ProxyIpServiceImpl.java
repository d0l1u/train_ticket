package com.l9e.transaction.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.ProxyIpDao;
import com.l9e.transaction.qo.ProxyIpQO;
import com.l9e.transaction.service.ProxyIpService;
import com.l9e.transaction.vo.ProxyIpVo;

/**   
 * @ClassName: ProxyIpServiceImpl   
 * @Description: TODO  
 * @author: taokai
 * @date: 2017年6月27日 下午3:34:29
 * @Copyright: 2017 www.19e.cn Inc. All rights reserved. 
 */
@Service
public class ProxyIpServiceImpl implements ProxyIpService {

	@Resource
	private ProxyIpDao proxyIpDao;

	@Override
	public Integer addProxyIps(ProxyIpVo ipvo) {
		return proxyIpDao.addProxyIps(ipvo);
	}

	@Override
	public List<ProxyIpVo> queryListProxyIp(ProxyIpQO proxyIpQo) {
		return proxyIpDao.queryListProxyIp(proxyIpQo);
	}

	@Override
	public Integer queryTotalCount(ProxyIpQO proxyIpQo) {
		return proxyIpDao.queryTotalCount(proxyIpQo);
	}

	@Override
	public Integer deleteListByIds(List<Integer> linkedList) {
		return proxyIpDao.deleteListByIds(linkedList);
	}

	@Override
	public Integer updateListByIds(List<Integer> idList, ProxyIpVo ipVo) {
		return proxyIpDao.updateListByIds(idList,ipVo);
	}
}
