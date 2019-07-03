package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface CpStatDao {

	int querycpStatServiceListCount(Map<String, Object> paramMap);

	List<Map<String, Object>> querycpStatServiceList(
			Map<String, Object> paramMap);

	List<Map<String, String>> queryPictureLineParam();

}
