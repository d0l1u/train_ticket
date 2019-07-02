package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.NoticeDao;
import com.l9e.transaction.vo.DBNoticeVo;
import com.l9e.transaction.vo.DBRemedyNoticeVo;

@Repository("noticeDao")
public class NoticeDaoImpl extends BaseDao implements NoticeDao{
	@Override
	public void insertNotice(DBNoticeVo notice) {
		this.getSqlMapClientTemplate().insert("allNotice.insertNotice",notice);
	}

	@Override
	public void updateNotice(DBNoticeVo notice) {
		this.getSqlMapClientTemplate().update("allNotice.updateNotice",notice);
	}

	@Override
	public List<DBNoticeVo> selectBookResultList(Map<String, String> param) {
		return this.getSqlMapClientTemplate().queryForList("allNotice.selectBookResultList",param);
	}

	@Override
	public List<DBNoticeVo> selectOrderResultList(Map<String, String> param) {
		return this.getSqlMapClientTemplate().queryForList("allNotice.selectOrderResultList",param);
	}

	@Override
	public List<DBNoticeVo> selectWaitNoticeList(Map<String, String> param) {
		return this.getSqlMapClientTemplate().queryForList("allNotice.selectWaitNoticeList",param);
	}

	@Override
	public DBNoticeVo queryNoticeInfoById(String order_id) {
		return (DBNoticeVo)this.getSqlMapClientTemplate().queryForObject("allNotice.queryNoticeInfoById",order_id);
	}

	@Override
	public String selectBookNoticeStatus(String order_id) {
		return (String)this.getSqlMapClientTemplate().queryForObject("allNotice.selectBookNoticeStatus",order_id);
	}



	@Override
	public int updateStartWaitNoticeList(DBNoticeVo vo) {
		return this.getSqlMapClientTemplate().update("allNotice.updateStartWaitNoticeList",vo);
	}

	@Override
	public int updateStartOrderResultNotice(DBNoticeVo vo) {
		return this.getSqlMapClientTemplate().update("allNotice.updateStartOrderResultNotice",vo);
	}

	@Override
	public int updateStartBookResultNotice(DBNoticeVo vo) {
		return this.getSqlMapClientTemplate().update("allNotice.updateStartBookResultNotice",vo);
	}


	@Override
	public List<DBNoticeVo> selectWaitNoticeListsx(Map<String, String> param) {
		return this.getSqlMapClientTemplate().queryForList("allNotice.selectWaitNoticeListsx",param);
	}

}
