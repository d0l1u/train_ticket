package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.IpInfoRelease;

public interface IpInfoReleaseService {
	
	/**
	 * 查询待释放IP列表
	 * @param paramMap
	 * @return
	 */
	List<IpInfoRelease> queryIpInfoReleaseList(Map<String, Object> paramMap);
	
	
	/**
	 * 更新待释放IP表的信息
	 * @param paramMap
	 * @return
	 */
	int modifyIpInfoRelease(Map<String, Object> paramMap);

}
