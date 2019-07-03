package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface FileDao {

	void insertFile(Map<String, String> paramMap);

	int queryFileCount(Map<String, Object> paramMap);

	List<Map<String, Object>> queryFileList(Map<String, Object> paramMap);

	String queryFilepath(String id);

	void deleteFile(String id);

}
