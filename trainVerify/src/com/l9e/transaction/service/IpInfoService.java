package com.l9e.transaction.service;

import java.util.List;

import com.l9e.transaction.vo.IpInfo;




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
	
	/**
	 * 根据ID从库中查询某个具体IP地址
	 * @param ipId IP-id
	 * @return
	 */
	IpInfo queryIpInfoById(Integer ipId);
	

	
}
