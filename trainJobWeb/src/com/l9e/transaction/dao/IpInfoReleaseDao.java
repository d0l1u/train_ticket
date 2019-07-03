package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.IpInfoRelease;

public interface IpInfoReleaseDao {
	
	/**
	 * 查询待释放IP列表
	 * @param paramMap
	 * @return
	 */
	List<IpInfoRelease> selectIpInfoReleaseList(Map<String, Object> paramMap);
	
	
	/**
	 * 更新待释放IP表的信息
	 * @param paramMap
	 * @return
	 */
	int updateIpInfoRelease(Map<String, Object> paramMap);
	
}
