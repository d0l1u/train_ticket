package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.JobDao;
import com.l9e.transaction.service.JobService;

@Service("jobService")
public class JobServiceImpl implements JobService {
	@Resource
	private JobDao jobDao;

	public List<Map<String, String>> queryScanedOrderList() {
		return jobDao.queryScanedOrderList();
	}

	public List<Map<String, String>> queryCpInfoList(String order_id) {
		return jobDao.queryCpInfoList(order_id);
	}

	public int updateScanInfoById(Map<String, String> map) {
		return jobDao.updateScanInfoById(map);
	}

}
