package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface FailtjDao {
	// 获取按条件查询数 
	public int queryFailtjCounts(Map<String, Object> paramMap);
	//获取按条件查询所有
	public List<Map<String, String>> queryFailtjList(Map<String, Object> paramMap);
	//导出excel
	public List<Map<String, String>> queryFailtjExcel(Map<String, Object> paramMap);
}
