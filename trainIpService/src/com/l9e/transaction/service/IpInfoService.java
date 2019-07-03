package com.l9e.transaction.service;

import com.l9e.transaction.vo.IpInfo;




/**
 * IP业务接口
 * @author wangsf
 *
 */
public interface IpInfoService {

	/**
	 * 准备IP
	 * @param type IP类型 
	 */
	void setIpInfo(Integer type);
	
	/**
	 * 根据指定类型获取一个队列中IP
	 * @param type IP类型 
	 * @return
	 */
	IpInfo getIpInfo(Integer type);
	
	/**
	 * 根据ID从库中查询某个具体IP地址
	 * @param ipId IP-id
	 * @return
	 */
	IpInfo queryIpInfoById(Integer ipId);
	
	/**
	 * 修改IP信息
	 * @param ipInfo IP实体
	 */
	void updateIpInfo(IpInfo ipInfo);
	
	/**
	 * 切换美团云IP，把旧IP换成新购IP
	 * @param ip_name 浮动IP的自定义名称  唯一(以字母开头，仅包含字母、数字或中划线的3-40个字符)
	 * @param oldIP 旧IP
	 * @return 新购IP
	 */
	String changeMtyunIp(String ip_name,String oldIP);
	
	/**
	 * 12306-IP授权核验接口
	 * @param proxyIP 12306代理IP
	 * @return 授权核验返回的结果
	 */
	String verifyProxyIp(String proxyIP);
	
	/**
	 * 携程-IP授权核验接口
	 * @param proxyIP 携程代理IP
	 * @return 授权核验返回的结果
	 */
	String verifyCtripProxyIp(String proxyIP);
	
}
