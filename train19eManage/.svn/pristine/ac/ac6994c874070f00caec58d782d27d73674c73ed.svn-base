package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.OutTicketTjDao;
import com.l9e.transaction.service.OutTicketTjService;


@Service("outTicketTjService")
public class OutTicketTjServiceImpl implements OutTicketTjService{
	@Resource
	private OutTicketTjDao outTicketTjDao ;

	@Override
	public int queryOutTicketTjCounts(Map<String, Object> paramMap) {
		return outTicketTjDao.queryOutTicketTjCounts(paramMap);
	}

	@Override
	public List<Map<String, String>> queryOutTicketTjList(
			Map<String, Object> paramMap) {
		return outTicketTjDao.queryOutTicketTjList(paramMap);
	}

	
}
