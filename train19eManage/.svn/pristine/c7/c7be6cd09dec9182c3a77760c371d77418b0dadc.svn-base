package com.l9e.transaction.dao;

import java.util.List;

import com.l9e.transaction.qo.ProxyIpQO;
import com.l9e.transaction.vo.ProxyIpVo;

/**   
 * @ClassName: ProxyIpDao   
 * @Description: TODO  
 * @author: taokai
 * @date: 2017年6月27日 下午3:30:07
 * @Copyright: 2017 www.19e.cn Inc. All rights reserved. 
 */
public interface ProxyIpDao {

	Integer addProxyIps(ProxyIpVo ipvo);

	 /**   
	 * @Title: queryListProxyIp   
	 * @Description: 根据条件查询代理IP列表  
	 * @author: taokai
	 * @date: 2017年6月27日 下午7:09:44
	 * @param proxyIpQo
	 * @return List<ProxyIpVo>
	 */
	List<ProxyIpVo> queryListProxyIp(ProxyIpQO proxyIpQo);

	 /**   
	 * @Title: queryTotalCount   
	 * @Description: TODO  
	 * @author: taokai
	 * @date: 2017年6月27日 下午7:43:29
	 * @param proxyIpQo
	 * @return Integer
	 */
	Integer queryTotalCount(ProxyIpQO proxyIpQo);

	 /**   
	 * @Title: deleteListByIds   
	 * @Description: TODO  
	 * @author: taokai
	 * @date: 2017年7月10日 下午6:03:31
	 * @param linkedList
	 * @return Integer
	 */
	Integer deleteListByIds(List<Integer> linkedList);

	 /**   
	 * @Title: updateListByIds   
	 * @Description: TODO  
	 * @author: taokai
	 * @date: 2017年7月10日 下午7:38:35
	 * @param idList
	 * @param ipVo
	 * @return Integer
	 */
	Integer updateListByIds(List<Integer> idList, ProxyIpVo ipVo);
}
