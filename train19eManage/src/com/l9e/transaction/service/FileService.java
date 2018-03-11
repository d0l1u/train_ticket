package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface FileService {

	void insertFile(Map<String, String> paramMap);

	int queryFileCount(Map<String, Object> paramMap);

	List<Map<String, Object>> queryFileList(Map<String, Object> paramMap);

	String queryFilepath(String id);

	void deleteFile(String id);


}
