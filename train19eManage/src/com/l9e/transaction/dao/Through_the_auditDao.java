package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface Through_the_auditDao {

	List<Map<String, String>> queryDoes_not_pass_the_examination(
			Map<String, String> queryList);

	void updateWaitPassToPass(Map<String,String>update_Map);

}
