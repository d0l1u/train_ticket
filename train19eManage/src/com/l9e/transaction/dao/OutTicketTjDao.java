package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface OutTicketTjDao {
	// 获取按条件查询数 
	public int queryOutTicketTjCounts(Map<String, Object> paramMap);
	//获取按条件查询所有
	public List<Map<String, String>> queryOutTicketTjList(Map<String, Object> paramMap);
	
}
