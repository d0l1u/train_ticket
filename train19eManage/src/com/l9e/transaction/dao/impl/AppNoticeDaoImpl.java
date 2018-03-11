package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.AppNoticeDao;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.AppNoticeVo;
@Repository("appNoticeDao")
public class AppNoticeDaoImpl extends BaseDao implements AppNoticeDao {

	public List<Map<String, Object>> queryNoticeList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("appNotice.queryNoticeList", paramMap);
	}

	@SuppressWarnings("unchecked")
	public int queryNoticeListCount(Map<String, Object> paramMap) {
		return getTotalRows("appNotice.queryNoticeListCount", paramMap);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryNotice(String notice_id) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("appNotice.queryNotice", notice_id);
	}

	@SuppressWarnings("unchecked")
	public void updateNotice(AppNoticeVo  Notice) {
		this.getSqlMapClientTemplate().update("appNotice.updateNotice", Notice);
	}

	@SuppressWarnings("unchecked")
	public void insertNotice(AppNoticeVo  Notice) {
		this.getSqlMapClientTemplate().insert("appNotice.insertNotice", Notice);
	}

	@SuppressWarnings("unchecked")
	public void deleteNotice(AppNoticeVo Notice) {
		this.getSqlMapClientTemplate().delete("appNotice.deleteNotice", Notice);
	}

	@SuppressWarnings("unchecked")
	public List<AreaVo> getProvince() {
		return this.getSqlMapClientTemplate().queryForList("appNotice.getProvince");
	}

	@SuppressWarnings("unchecked")
	public List<AreaVo> getCity(String provinceid) {
		return this.getSqlMapClientTemplate().queryForList("appNotice.getCity", provinceid);
	}

	@SuppressWarnings("unchecked")
	public List<AreaVo> getArea(String cityid) {
		return this.getSqlMapClientTemplate().queryForList("appNotice.getArea", cityid);
	}

	@Override
	public void addNoticeUser(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().insert("appNotice.addNoticeUser", paramMap);
	}

	@Override
	public String queryNoticeId(AppNoticeVo notice) {
		return (String) this.getSqlMapClientTemplate().queryForObject("appNotice.queryNoticeId", notice);
	}

	@Override
	public List<String> queryUserPhone(String noticeId) {
		return this.getSqlMapClientTemplate().queryForList("appNotice.queryUserPhone", noticeId);
	}


}
