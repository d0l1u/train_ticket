package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface ChanneltjDao {
	// 获取按条件查询数 
	public int queryChanneltjCounts(Map<String, Object> paramMap);
	//获取按条件查询所有
	public List<Map<String, String>> queryChanneltjList(Map<String, Object> paramMap);
	//导出excel
	public List<Map<String, String>> queryChanneltjExcel(Map<String, Object> paramMap);
}
