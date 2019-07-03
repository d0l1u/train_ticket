package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.CtripAcc;

public interface CtripAccountService {
	
	/**
	 * 查询携程账号列表
	 * @param paramMap
	 * @return
	 */
	List<CtripAcc> queryCtripAccountList(Map<String, Object> paramMap);
	
	
	/**
	 * 更新携程账号信息
	 * @param paramMap
	 * @return
	 */
	int modifyCtripAccount(Map<String, Object> paramMap);

}
