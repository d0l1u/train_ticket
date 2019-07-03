package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.TuniuNoticeDao;
import com.l9e.transaction.vo.Notice;
import com.l9e.transaction.vo.TuniuQueueOrder;

@Repository("tuniuNoticeDao")
public class TuniuNoticeDaoImpl extends BaseDao implements TuniuNoticeDao {

	@Override
	public void insertBookNotice(Notice notice) {
		getSqlMapClientTemplate().insert("notice.insertBookNotice", notice);
	}

	@Override
	public void insertOutNotice(Notice notice) {
		getSqlMapClientTemplate().insert("notice.insertOutNotice", notice);
	}

	@Override
	public void insertRefundNotice(Notice notice) {
		getSqlMapClientTemplate().insert("notice.insertRefundNotice", notice);
	}

	@Override
	public void updateBookNotice(Notice notice) {
		getSqlMapClientTemplate().update("notice.updateBookNotice", notice);
	}

	@Override
	public void updateOutNotice(Notice notice) {
		getSqlMapClientTemplate().update("notice.updateOutNotice", notice);
	}

	@Override
	public void updateRefundNotice(Notice notice) {
		getSqlMapClientTemplate().update("notice.updateRefundNotice", notice);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Notice> selectBookPreparedNotices(Map<String, Object> queryParam) {
		return getSqlMapClientTemplate().queryForList("notice.selectBookPreparedNotice", queryParam);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Notice> selectOutPreparedNotices(Map<String, Object> queryParam) {
		return getSqlMapClientTemplate().queryForList("notice.selectOutPreparedNotice", queryParam);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Notice> selectRefundPreparedNotices(
			Map<String, Object> queryParam) {
		return getSqlMapClientTemplate().queryForList("notice.selectRefundPreparedNotice", queryParam);
	}

	@Override
	public Notice selectBookOneNotice(Map<String, Object> queryParam) {
		return (Notice) getSqlMapClientTemplate().queryForObject("notice.selectBookNotice", queryParam);
	}

	@Override
	public Notice selectOutOneNotice(Map<String, Object> queryParam) {
		return (Notice) getSqlMapClientTemplate().queryForObject("notice.selectOutNotice", queryParam);
	}

	@Override
	public Notice selectRefundOneNotice(Map<String, Object> queryParam) {
		return (Notice) getSqlMapClientTemplate().queryForObject("notice.selectRefundNotice", queryParam);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Notice> selectBookCallbackNotices(Map<String, Object> queryParam) {
		return getSqlMapClientTemplate().queryForList("notice.selectBookCallbackNotice", queryParam);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Notice> selectOutCallbackNotices(Map<String, Object> queryParam) {
		return getSqlMapClientTemplate().queryForList("notice.selectOutCallbackNotice", queryParam);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Notice> selectRefundCallbackNotices(
			Map<String, Object> queryParam) {
		return getSqlMapClientTemplate().queryForList("notice.selectRefundCallbackNotice", queryParam);
	}

	@Override
	public void deleteNotice(Notice notice) {
		getSqlMapClientTemplate().delete("notice.deleteRefundNotice",notice);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TuniuQueueOrder> getQueueOrder() {
		
		return this.getSqlMapClientTemplate().queryForList("notice.getQueueOrder");
	}

	@Override
	public void updateQueueNotice(TuniuQueueOrder queueOrder) {
		this.getSqlMapClientTemplate().update("notice.updateQueueNotice", queueOrder);
	}


}
