package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.IpInfo;

/**
 * IP持久层接口
 * @author wangsf
 *
 */
public interface IpInfoDao {
	
	/**
	 * 查询某一个IP
	 * @param params
	 * @return
	 */
	IpInfo selectOneIp(Map<String, Object> params);

	/**
	 * 查询多个IP
	 * @param params
	 * @return
	 */
	List<IpInfo> selectIpList(Map<String, Object> params);
	
	/**
	 * 更新IP
	 * @param ipInfo
	 */
	int updateIpInfo(IpInfo ipInfo);
	
	/**
	 * 往cp_ipinfo_log表插入一条日志记录
	 * @param paramMap
	 */
	void insertIpInfoLog(Map<String, Object> paramMap);
	
	/**
	 * 往cp_ipinfo_release表插入一条待释放IP记录
	 * @param paramMap
	 */
	void insertIpInfoRelease(Map<String, Object> paramMap);
		
}
