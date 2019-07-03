package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.CtripAcc;

public interface CtripAccountDao {
	
	/**
	 * 查询携程账号列表
	 * @param paramMap
	 * @return
	 */
	List<CtripAcc> selectCtripAccountList(Map<String, Object> paramMap);
	
	
	/**
	 * 更新携程账号信息
	 * @param paramMap
	 * @return
	 */
	int updateCtripAccount(Map<String, Object> paramMap);
	
}
