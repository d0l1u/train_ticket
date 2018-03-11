package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.TuniuNoticeDao;
import com.l9e.transaction.service.TuniuNoticeService;
import com.l9e.transaction.vo.Notice;
import com.l9e.transaction.vo.TuniuQueueOrder;

@Service("tuniuNoticeService")
public class TuniuNoticeServiceImpl implements TuniuNoticeService {

//	private static final Logger logger = Logger
//			.getLogger(TuniuNoticeServiceImpl.class);

	@Resource
	private TuniuNoticeDao tuniuNoticeDao;

	@Override
	public List<Notice> findBookWaitOutTicketNotifies(int amount) {
		amount = amount == 0 ? 20 : amount;
		Map<String, Object> queryParam = new HashMap<String, Object>();
		queryParam.put("limit", amount);
		return tuniuNoticeDao.selectBookPreparedNotices(queryParam);
	}

	@Override
	public List<Notice> findOutWaitOutTicketNotifies(int amount) {
		amount = amount == 0 ? 20 : amount;
		Map<String, Object> queryParam = new HashMap<String, Object>();
		queryParam.put("limit", amount);
		return tuniuNoticeDao.selectOutPreparedNotices(queryParam);
	}

	@Override
	public List<Notice> findRefundWaitOutTicketNotifies(int amount) {
		amount = amount == 0 ? 20 : amount;
		Map<String, Object> queryParam = new HashMap<String, Object>();
		queryParam.put("limit", amount);
		return tuniuNoticeDao.selectRefundPreparedNotices(queryParam);
	}

	@Override
	public void updateBookNotice(Notice notice) {
		tuniuNoticeDao.updateBookNotice(notice);
	}

	@Override
	public void updateOutNotice(Notice notice) {
		tuniuNoticeDao.updateOutNotice(notice);
	}

	@Override
	public void updateRefundNotice(Notice notice) {
		tuniuNoticeDao.updateRefundNotice(notice);
	}

	@Override
	public Notice getBookNoticeByOrderId(String orderId) {
		Map<String, Object> queryParam = new HashMap<String, Object>();
		queryParam.put("orderId", orderId);
		return tuniuNoticeDao.selectBookOneNotice(queryParam);
	}

	@Override
	public Notice getOutNoticeByOrderId(String orderId) {
		Map<String, Object> queryParam = new HashMap<String, Object>();
		queryParam.put("orderId", orderId);
		return tuniuNoticeDao.selectOutOneNotice(queryParam);
	}

	@Override
	public Notice getRefundNoticeByOrderIdAndCpId(String orderId, String cpId) {
		Map<String, Object> queryParam = new HashMap<String, Object>();
		queryParam.put("orderId", orderId);
		queryParam.put("cpId", cpId);
		return tuniuNoticeDao.selectRefundOneNotice(queryParam);
	}

	@Override
	public List<Notice> findBookWaitCallbackNotifies(int amount) {
		amount = amount == 0 ? 20 : amount;
		Map<String, Object> queryParam = new HashMap<String, Object>();
		queryParam.put("limit", amount);
		return tuniuNoticeDao.selectBookCallbackNotices(queryParam);
	}

	@Override
	public List<Notice> findOutWaitCallbackNotifies(int amount) {
		amount = amount == 0 ? 20 : amount;
		Map<String, Object> queryParam = new HashMap<String, Object>();
		queryParam.put("limit", amount);
		return tuniuNoticeDao.selectOutCallbackNotices(queryParam);
	}

	@Override
	public List<Notice> findRefundWaitCallbackNotifies(int amount) {
		amount = amount == 0 ? 20 : amount;
		Map<String, Object> queryParam = new HashMap<String, Object>();
		queryParam.put("limit", amount);
		return tuniuNoticeDao.selectRefundCallbackNotices(queryParam);
	}
	@Override
	public List<TuniuQueueOrder> getQueueOrder() {
		
		return tuniuNoticeDao.getQueueOrder();
	}

	@Override
	public void updateQueueNotice(TuniuQueueOrder queueOrder) {
		tuniuNoticeDao.updateQueueNotice(queueOrder);
	} 
}
