package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ComplainDao;
import com.l9e.transaction.dao.NoticeDao;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.NoticeVo;
import com.l9e.transaction.vo.NoticeVo;

@Repository("noticeDao")
public class NoticeDaoImpl extends BaseDao
implements NoticeDao{

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryNoticeList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("notice.queryNoticeList", paramMap);
	}

	@SuppressWarnings("unchecked")
	public int queryNoticeListCount(Map<String, Object> paramMap) {
		return getTotalRows("notice.queryNoticeListCount", paramMap);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryNotice(String notice_id) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("notice.queryNotice", notice_id);
	}

	@SuppressWarnings("unchecked")
	public void updateNotice(NoticeVo  Notice) {
		this.getSqlMapClientTemplate().update("notice.updateNotice", Notice);
	}

	@SuppressWarnings("unchecked")
	public void insertNotice(NoticeVo  Notice) {
		this.getSqlMapClientTemplate().insert("notice.insertNotice", Notice);
	}

	@SuppressWarnings("unchecked")
	public void deleteNotice(NoticeVo Notice) {
		this.getSqlMapClientTemplate().delete("notice.deleteNotice", Notice);
	}

	@SuppressWarnings("unchecked")
	public List<AreaVo> getProvince() {
		return this.getSqlMapClientTemplate().queryForList("notice.getProvince");
	}

	@SuppressWarnings("unchecked")
	public List<AreaVo> getCity(String provinceid) {
		return this.getSqlMapClientTemplate().queryForList("notice.getCity", provinceid);
	}

	@SuppressWarnings("unchecked")
	public List<AreaVo> getArea(String cityid) {
		return this.getSqlMapClientTemplate().queryForList("notice.getArea", cityid);
	}


}
