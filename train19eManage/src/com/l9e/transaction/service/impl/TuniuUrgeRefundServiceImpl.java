package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.TuniuUrgeRefundDao;
import com.l9e.transaction.service.TuniuUrgeRefundService;
import com.l9e.transaction.vo.TuniuUrgeRefund;

@Service("tuniuUrgeRefundService")
public class TuniuUrgeRefundServiceImpl implements TuniuUrgeRefundService {
	@Resource
	private TuniuUrgeRefundDao tuniuUrgeRefundDao;

	@Override
	public int queryUrgeRefundCount(Map<String, Object> param) {
		return tuniuUrgeRefundDao.queryUrgeRefundCount(param);
	}

	@Override
	public List<TuniuUrgeRefund> queryUrgeRefundList(Map<String, Object> param) {
		return tuniuUrgeRefundDao.queryUrgeRefundList(param);
	}

	@Override
	public TuniuUrgeRefund queryUrgeRefundInfo(Map<String, Object> param) {
		return tuniuUrgeRefundDao.queryUrgeRefundInfo(param);
	}

	@Override
	public int updateUrgeRefundInfo(Map<String, Object> param) {
		return tuniuUrgeRefundDao.updateUrgeRefundInfo(param);
	}

	@Override
	public int updateUrgeRefundByOrderId(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return tuniuUrgeRefundDao.updateUrgeRefundByOrderId(param);
	}

	@Override
	public List<TuniuUrgeRefund> queryUrgeRefundListForExcel(
			Map<String, Object> param) {
		return tuniuUrgeRefundDao.queryUrgeRefundListForExcel(param);
	}

	@Override
	public int updateUrgeRefund(TuniuUrgeRefund tuniuUrgeRefund) {
		return tuniuUrgeRefundDao.updateUrgeRefund(tuniuUrgeRefund);
	}

}
