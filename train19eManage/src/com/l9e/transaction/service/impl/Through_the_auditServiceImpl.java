package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.Through_the_auditDao;
import com.l9e.transaction.service.Through_the_auditService;
@Service("through_the_auditService")
public class Through_the_auditServiceImpl implements Through_the_auditService{
	@Resource
	private Through_the_auditDao  through_the_auditDao;

	public List<Map<String, String>> queryDoes_not_pass_the_examination(
			Map<String, String> queryList) {
		return through_the_auditDao.queryDoes_not_pass_the_examination(queryList);
	}

	public void updateWaitPassToPass(Map<String,String>update_Map) {
		through_the_auditDao.updateWaitPassToPass(update_Map);
	}
}
