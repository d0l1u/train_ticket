package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.Tj_hc_outTicketSblDao;
import com.l9e.transaction.service.Tj_hc_outTicketSblService;
@Service("tj_hc_outTicketSblService")
public class Tj_hc_outTicketSblServiceImpl implements Tj_hc_outTicketSblService {
	@Resource
	private Tj_hc_outTicketSblDao tj_hc_outTicketSblDao;

	public void addToTj_hc_outTicketSbl(Map paramMap) {
		tj_hc_outTicketSblDao.addToTj_hc_outTicketSbl(paramMap);
	}

	public int queryCount(Map paramMap) {
		return tj_hc_outTicketSblDao.queryCount(paramMap);
	}

	public List<Map<String, String>> queryOutTicketSblAfter(String createTime) {
		return tj_hc_outTicketSblDao.queryOutTicketSblAfter(createTime);
	}

	public List<Map<String, String>> queryOutTicketSblBefore(String createTime) {
		return tj_hc_outTicketSblDao.queryOutTicketSblBefore(createTime);
	}

	public void updateTj_hc_outTicketSbl(Map paramMap) {
		tj_hc_outTicketSblDao.updateTj_hc_outTicketSbl(paramMap);
	}
	
	
}
