package com.l9ea.train.service;

import com.l9e.train.po.IpInfo;




/**
 * IP业务接口
 * @author wangsf
 *
 */
public interface IpInfoService {

	
	/**
	 * 根据指定类型获取一个队列中IP
	 * @param type IP类型 
	 * @return
	 */
	IpInfo getIpInfoByType(Integer type);
	
}
