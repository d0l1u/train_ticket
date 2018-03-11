package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ProxyIpDao;
import com.l9e.transaction.qo.ProxyIpQO;
import com.l9e.transaction.vo.ProxyIpVo;

/**   
 * @ClassName: ProxyIpDaoImpl   
 * @Description: TODO  
 * @author: taokai
 * @date: 2017年6月27日 下午3:30:36
 * @Copyright: 2017 www.19e.cn Inc. All rights reserved. 
 */
@Repository
public class ProxyIpDaoImpl  extends BaseDao  implements ProxyIpDao {

	@Override
	public Integer addProxyIps(ProxyIpVo ipvo) {
		Object insert = this.getSqlMapClientTemplate().insert("proxyIp.addProxyIps", ipvo);
		return 1;
	}

	@Override
	public List<ProxyIpVo> queryListProxyIp(ProxyIpQO proxyIpQo) {
		
		return this.getSqlMapClientTemplate().queryForList("proxyIp.queryListProxyIp",proxyIpQo);
	}

	@Override
	public Integer queryTotalCount(ProxyIpQO proxyIpQo) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("proxyIp.queryTotalCount",proxyIpQo);
	}

	@Override
	public Integer deleteListByIds(List<Integer> linkedList) {
		return (Integer) this.getSqlMapClientTemplate().delete("proxyIp.deleteListByIds",linkedList);
	}

	@Override
	public Integer updateListByIds(List<Integer> idList, ProxyIpVo ipVo) {
		HashMap<String,	 Object> map = new HashMap<String, Object>();
		map.put("idList", idList);
		map.put("ipVo", ipVo);
		return (Integer) this.getSqlMapClientTemplate().update("proxyIp.updateListByIds", map);
	}

}
