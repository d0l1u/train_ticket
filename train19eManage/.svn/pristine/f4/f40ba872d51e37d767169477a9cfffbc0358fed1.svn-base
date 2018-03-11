package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.CpStatDao;
import com.l9e.transaction.service.CpStatService;

@Service("cpStatService")
public class CpStatServiceImpl implements CpStatService{
	@Resource
	private CpStatDao cpStatDao;

	public int querycpStatServiceListCount(Map<String, Object> paramMap) {
		return cpStatDao.querycpStatServiceListCount(paramMap);
	}
	
	public List<Map<String, Object>> querycpStatServiceList(
			Map<String, Object> paramMap) {
		return cpStatDao.querycpStatServiceList(paramMap);
	}

	public List<Map<String, String>> queryPictureLineParam() {
		return cpStatDao.queryPictureLineParam();
	}
}
